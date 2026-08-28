package com.example.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.security.SecureRandom

@RunWith(RobolectricTestRunner::class)
class DataExportManagerTest {

    private lateinit var db: AdrianaDatabase
    private lateinit var exportManager: DataExportManager
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AdrianaDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        exportManager = DataExportManager(db)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun testExportImportHappyPath() = runBlocking {
        // 1. Prepare data
        val checkin = CheckinEntity(
            dateKey = "2026-08-25",
            pain = 5f,
            anxiety = 5f,
            nostalgia = 5f,
            anger = 5f,
            loneliness = 5f,
            rumination = 5f,
            urgeToContact = 5f,
            autonomy = 5f
        )
        db.checkinDao().insertOrUpdateCheckin(checkin)
        
        val pin = "1234"
        val exportFile = File(context.cacheDir, "test_export.enc")
        
        // 2. Export
        exportManager.exportData(pin, exportFile)
        assertTrue(exportFile.exists())
        
        // 3. Clear DB
        db.clearAllTables()
        
        // 4. Import
        val success = exportManager.importData(pin, exportFile)
        assertTrue(success)
        
        // 5. Verify
        val checkins = db.checkinDao().getAllCheckins().first()
        assertEquals(1, checkins.size)
    }

    @Test
    fun testExportImportWrongPin() = runBlocking {
        val pin = "1234"
        val exportFile = File(context.cacheDir, "test_wrong_pin.enc")
        exportManager.exportData(pin, exportFile)
        
        val success = exportManager.importData("0000", exportFile)
        assertFalse(success)
    }

    @Test
    fun testExportImportTamperedFile() = runBlocking {
        val pin = "1234"
        val exportFile = File(context.cacheDir, "test_tampered.enc")
        exportManager.exportData(pin, exportFile)
        
        // Tamper with bytes
        val bytes = exportFile.readBytes()
        bytes[bytes.size - 1] = bytes[bytes.size - 1].inc()
        exportFile.writeBytes(bytes)
        
        val success = exportManager.importData(pin, exportFile)
        assertFalse(success)
    }
}
