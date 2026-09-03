package com.example.data

import android.content.Context
import com.example.data.AdrianaDatabase
import com.example.data.AdrianaExportData
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.FileInputStream
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import java.nio.ByteBuffer

class DataExportManager(private val database: AdrianaDatabase) {
    private val json = Json { ignoreUnknownKeys = true }
    private val GCM_IV_LENGTH = 12
    private val GCM_TAG_LENGTH = 128
    private val KEY_LENGTH = 256
    private val ITERATIONS = 150000 // Higher iterations
    private val MAGIC = "ADRIA".toByteArray()
    private val FORMAT_VERSION: Byte = 1

    fun exportData(pin: String, outputFile: File) {
        val data = runBlocking {
            val checkins = database.checkinDao().getAllCheckins().first()
            val journals = database.journalDao().getAllJournalEntries().first()
            val letters = database.unsentLetterDao().getAllLetters().first()
            val settings = database.soltarSettingsDao().getSettingsOnce()
            val breakupDays = settings?.let { s ->
                val start = if (s.initialStartDateTimestamp > 0) s.initialStartDateTimestamp else s.breakupDateTimestamp
                ((System.currentTimeMillis() - start) / (1000L * 3600 * 24)).toInt().coerceAtLeast(0)
            } ?: checkins.size

            val clinicalSummary = com.example.ai.OnDeviceLlmEngine.generateClinicalProgressSummary(
                checkins = checkins,
                journals = journals,
                letters = letters,
                breakupDays = breakupDays,
                userName = settings?.userName ?: "Usuario"
            )

            AdrianaExportData(
                checkins = checkins,
                journalEntries = journals,
                unsentLetters = letters,
                relationshipAudits = database.relationshipAuditDao().getAllAudits().first(),
                aiMessages = database.aiMessageDao().getAllMessages().first(),
                redFlags = database.redFlagDao().getAllRedFlags().first(),
                triggerEvents = database.triggerEventDao().getAllTriggerEvents().first(),
                thoughtLabEntries = database.thoughtLabDao().getAllEntries().first(),
                settings = settings,
                clinicalProgressSummary = clinicalSummary
            )
        }
        val jsonData = json.encodeToString(data)
        
        val salt = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val iv = ByteArray(GCM_IV_LENGTH).apply { SecureRandom().nextBytes(this) }
        
        val spec = PBEKeySpec(pin.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, "AES")

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_LENGTH, iv))
        val encryptedData = cipher.doFinal(jsonData.toByteArray())

        FileOutputStream(outputFile).use {
            it.write(MAGIC)
            it.write(FORMAT_VERSION.toInt())
            it.write(salt)
            it.write(iv)
            it.write(encryptedData)
        }
    }

    fun importData(pin: String, file: File): Boolean {
        return try {
            val bytes = FileInputStream(file).use { it.readBytes() }
            val buffer = ByteBuffer.wrap(bytes)
            
            val magic = ByteArray(5)
            buffer.get(magic)
            if (!magic.contentEquals(MAGIC)) return false

            val version = buffer.get()
            val salt = ByteArray(16); buffer.get(salt)
            val iv = ByteArray(GCM_IV_LENGTH); buffer.get(iv)
            val encryptedData = ByteArray(buffer.remaining()); buffer.get(encryptedData)

            val spec = PBEKeySpec(pin.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, "AES")

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_LENGTH, iv))
            val jsonData = String(cipher.doFinal(encryptedData))
            
            val data = json.decodeFromString<AdrianaExportData>(jsonData)
            
            runBlocking {
                // Restore data (ensure INSERT OR REPLACE)
                data.checkins.forEach { database.checkinDao().insertOrUpdateCheckin(it) }
                data.journalEntries.forEach { database.journalDao().insertJournalEntry(it) }
                data.unsentLetters.forEach { database.unsentLetterDao().insertLetter(it) }
                data.relationshipAudits.forEach { database.relationshipAuditDao().insertAudit(it) }
                data.aiMessages.forEach { database.aiMessageDao().insertMessage(it) }
                data.redFlags.forEach { database.redFlagDao().insertRedFlag(it) }
                data.triggerEvents.forEach { database.triggerEventDao().insertTriggerEvent(it) }
                data.thoughtLabEntries.forEach { database.thoughtLabDao().insertEntry(it) }
                data.settings?.let { database.soltarSettingsDao().saveSettings(it) }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun generateClinicalNarrativeReport(): String {
        val checkins = database.checkinDao().getAllCheckins().first()
        val journals = database.journalDao().getAllJournalEntries().first()
        val letters = database.unsentLetterDao().getAllLetters().first()
        val settings = database.soltarSettingsDao().getSettingsOnce()
        val breakupDays = settings?.let { s ->
            val start = if (s.initialStartDateTimestamp > 0) s.initialStartDateTimestamp else s.breakupDateTimestamp
            ((System.currentTimeMillis() - start) / (1000L * 3600 * 24)).toInt().coerceAtLeast(0)
        } ?: checkins.size
        val userName = settings?.userName ?: "Usuario"

        return com.example.ai.OnDeviceLlmEngine.generateClinicalProgressSummary(
            checkins = checkins,
            journals = journals,
            letters = letters,
            breakupDays = breakupDays,
            userName = userName
        )
    }

    suspend fun exportClinicalNarrativeToFile(outputFile: File) {
        val report = generateClinicalNarrativeReport()
        FileOutputStream(outputFile).use {
            it.write(report.toByteArray(Charsets.UTF_8))
        }
    }
}
