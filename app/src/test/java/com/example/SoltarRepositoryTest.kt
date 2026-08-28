package com.example

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SoltarRepositoryTest {

    private lateinit var database: AdrianaDatabase
    private lateinit var repository: SoltarRepository

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AdrianaDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = SoltarRepository(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testSettingsLifecycleAndEntitlements() = runBlocking {
        val initialSettings = repository.settings.first()
        assertNull(initialSettings)

        val newSettings = SoltarSettingsEntity(
            userName = "Alejandro",
            subscriptionTier = "PLUS",
            isLoggedIn = true
        )
        repository.saveSettings(newSettings)

        val loaded = repository.settings.first()
        assertNotNull(loaded)
        assertEquals("Alejandro", loaded?.userName)
        assertEquals("PLUS", loaded?.subscriptionTier)
    }

    @Test
    fun testCheckinAndHistory() = runBlocking {
        val checkin = CheckinEntity(
            dateKey = "2026-08-27",
            pain = 4f,
            anxiety = 3f,
            nostalgia = 6f,
            rumination = 2f,
            autonomy = 8f,
            note = "Hoy pude concentrarme en mi trabajo.",
            timestamp = System.currentTimeMillis()
        )
        repository.saveCheckin(checkin)

        val list = repository.allCheckins.first()
        assertEquals(1, list.size)
        assertEquals(4f, list[0].pain)
        assertEquals("2026-08-27", list[0].dateKey)
    }

    @Test
    fun testJournalEntryWithMentorshipUpdate() = runBlocking {
        val entryId = repository.saveJournalEntry(
            JournalEntryEntity(
                title = "Reflexión del atardecer",
                content = "Hoy sentí el impulso pero me detuve a respirar.",
                moodTag = "Calma",
                philosophicalFramework = SoltarFramework.ESTOICO.name,
                timestamp = System.currentTimeMillis()
            )
        )

        repository.updateJournalFeedback(
            id = entryId,
            feedback = "Excelente aplicación de la pausa reflexiva.",
            corePrinciple = "Dicotomía del control",
            socraticQuestion = "¿Qué ganaste al no ceder al impulso?",
            concreteAction = "Anota tres cosas bajo tu control.",
            framework = SoltarFramework.ESTOICO.name
        )

        val retrieved = repository.getJournalEntryById(entryId).first()
        assertNotNull(retrieved)
        assertEquals("Excelente aplicación de la pausa reflexiva.", retrieved?.aiFeedback)
        assertEquals("Dicotomía del control", retrieved?.aiCorePrinciple)
    }

    @Test
    fun testUrgeEpisodePersistence() = runBlocking {
        repository.saveUrgeEpisode(
            UrgeEpisodeEntity(
                trigger = "Foto en redes",
                emotion = "Angustia",
                desiredAction = "escribir",
                expectedOutcome = "alivio",
                actualBehavior = "resistido",
                learning = "Esperé a que la curva de dopamina bajara."
            )
        )

        val episodes = repository.allUrgeEpisodes.first()
        assertEquals(1, episodes.size)
        assertEquals("Foto en redes", episodes[0].trigger)
    }
}
