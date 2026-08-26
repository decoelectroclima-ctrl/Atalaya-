package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_checkins")
data class CheckinEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val dateKey: String, // e.g. "2026-08-25"
    val pain: Float = 0f, // 0 - 10
    val anxiety: Float = 0f,
    val nostalgia: Float = 0f,
    val anger: Float = 0f,
    val loneliness: Float = 0f,
    val rumination: Float = 0f,
    val urgeToContact: Float = 0f,
    val autonomy: Float = 5f,
    val focusBodyAction: String = "",
    val focusBodyDone: Boolean = false,
    val focusSelfAction: String = "",
    val focusSelfDone: Boolean = false,
    val focusSocialAction: String = "",
    val focusSocialDone: Boolean = false,
    val note: String = ""
)

@Entity(tableName = "urge_episodes")
data class UrgeEpisodeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val initialIntensity: Int = 8, // 0 - 10
    val finalIntensity: Int = 3, // 0 - 10
    val emotion: String = "nostalgia", // nostalgia, ansiedad, soledad, miedo, rabia, necesidad de respuesta, etc.
    val desiredAction: String = "escribir", // escribir, llamar, mirar redes, buscar info, comprobar conexion, etc.
    val expectedOutcome: String = "alivio", // alivio, respuesta, saber qué siente, reconciliación, etc.
    val fact: String = "",
    val interpretation: String = "",
    val cannotKnow: String = "",
    val dependsOnMe: String = "",
    val trigger: String = "",
    val actualBehavior: String = "resistido", // resistido, aplazado, sustituido, contactado
    val timerCompletedMinutes: Int = 20,
    val learning: String = ""
)

@Entity(tableName = "thought_laboratory")
data class ThoughtEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val originalThought: String,
    val fact: String,
    val interpretation: String,
    val hypothesis: String,
    val evidenceFor: String,
    val evidenceAgainst: String,
    val cannotKnow: String,
    val dependsOnMe: String,
    val concreteAction: String, // caminar, ducharse, entrenar, trabajar, llamar a un amigo, etc.
    val isClosed: Boolean = true
)

@Entity(tableName = "relationship_audits")
data class RelationshipAuditEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // Momento positivo, Conflicto, Ruptura, Reconciliación, Límites, Comunicación, etc.
    val myResponsibility: String,
    val otherResponsibility: String,
    val sharedResponsibility: String,
    val patternIdentified: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "idealization_antidotes")
data class IdealizationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val whatIMiss: String, // "Lo que extraño"
    val whatIActuallyExperienced: String, // "Lo que realmente viví"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "unsent_letters")
data class UnsentLetterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // Amor, Rabia, Culpa, Agradecimiento, Dolor, Despedida, Cosas que nunca dije, Perdonarme
    val content: String,
    val isClosed: Boolean = false,
    val closedAtTimestamp: Long? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "memory_bank")
data class MemoryBankEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: String, // Foto, Recuerdo, Lugar, Experiencia, Frase, Momento
    val ruleAcknowledged: Boolean = true, // "Guardar un recuerdo no significa necesitar contactar"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "behavioral_experiments")
data class ExperimentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String, // "24 horas sin mirar redes", "48 horas sin comprobar conexión", etc.
    val durationHours: Int = 24,
    val status: String = "Activo", // Activo, Completado, Pausado
    val actualOutcome: String = "",
    val learning: String = "",
    val startedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

@Entity(tableName = "identity_goals")
data class IdentityGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val area: String, // Cuerpo, Amistades, Familia, Trabajo, Dinero, Proyectos, Aprendizaje, Ocio, Sexualidad, Autoestima, Propósito
    val whoIWas: String = "",
    val whoIAm: String = "",
    val whoIWantToBe: String = "",
    val goalTitle: String = "",
    val goalFrequency: String = "Diario", // Diario, Semanal, Mensual
    val isCompleted: Boolean = false,
    val streakDays: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "relapse_logs")
data class RelapseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val whatHappened: String,
    val trigger: String,
    val emotion: String,
    val thought: String,
    val behavior: String,
    val consequence: String,
    val learning: String
)

@Entity(tableName = "ai_messages")
data class AiMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "user" | "soltar_ai"
    val content: String,
    val detectedRumination: Boolean = false,
    val suggestedAction: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "soltar_settings")
data class SoltarSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val memoryEnabled: Boolean = true,
    val userName: String = "Viajero",
    val userEmail: String = "",
    val isLoggedIn: Boolean = false,
    val authProvider: String = "guest", // "email" | "google" | "guest"
    val accountCreatedAt: Long = System.currentTimeMillis(),
    val breakupDateTimestamp: Long = System.currentTimeMillis() - (14L * 24 * 3600 * 1000), // Default 2 weeks ago
    val biometricLockEnabled: Boolean = false,
    val soundEnabled: Boolean = true,
    val onboardingCompleted: Boolean = false,
    val preferredFramework: String = "PSICOLOGIA_MODERNA",
    val recentCardIds: String = "",
    // Support Network (Red de Apoyo - up to 3 contacts)
    val contact1Name: String = "",
    val contact1Phone: String = "",
    val contact1Relationship: String = "",
    val contact2Name: String = "",
    val contact2Phone: String = "",
    val contact2Relationship: String = "",
    val contact3Name: String = "",
    val contact3Phone: String = "",
    val contact3Relationship: String = "",
    // Subscription & Monetization
    val subscriptionTier: String = "FREE", // "FREE" | "PREMIUM_MONTHLY" | "PREMIUM_ANNUAL"
    val isTrialActive: Boolean = false,
    val subscriptionExpiryTimestamp: Long = 0L,
    // Perspectives
    val faithPerspectiveActive: Boolean = false,
    val stoicPerspectiveActive: Boolean = false,
    val modernPsychologyPerspectiveActive: Boolean = true
)

// Legacy entity backwards compatibility
@Entity(tableName = "facts")
data class FactEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val factText: String,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "crypto_vault")
data class CryptoVaultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val messageText: String,
    val recipient: String,
    val createdTimestamp: Long = System.currentTimeMillis(),
    val scheduledDestructionTimestamp: Long = System.currentTimeMillis() + (60 * 1000),
    val isDestroyed: Boolean = false
)

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userQuery: String,
    val aiResponse: String,
    val stateDetected: String,
    val actionTaken: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSosIntervention: Boolean = false
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val demographics: String = "Adulto (18+), En reconstrucción de identidad",
    val hasAdhd: Boolean = true,
    val hasAsd: Boolean = false,
    val hrvLevel: Int = 38,
    val restingHeartRate: Int = 85,
    val isLateLutealPhase: Boolean = false
)
