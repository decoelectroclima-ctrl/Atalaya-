package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "trigger_events")
data class TriggerEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val context: String, // "trabajo", "social", "solo", "redes", etc.
    val trigger: String,
    val emotion: String,
    val note: String = ""
)

@Serializable
@Entity(tableName = "red_flags")
data class RedFlagEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reason: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
@Entity(tableName = "peer_support_posts")
data class PeerSupportPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val likes: Int = 0
)

@Serializable
@Entity(tableName = "thought_lab_entries")
data class ThoughtLabEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalThought: String,
    val distortionType: String,
    val reframedThought: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
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

@Serializable
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

@Serializable
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

@Serializable
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

@Serializable
@Entity(tableName = "idealization_antidotes")
data class IdealizationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val whatIMiss: String, // "Lo que extraño"
    val whatIActuallyExperienced: String, // "Lo que realmente viví"
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
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

@Serializable
@Entity(tableName = "memory_bank")
data class MemoryBankEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: String, // Foto, Recuerdo, Lugar, Experiencia, Frase, Momento
    val ruleAcknowledged: Boolean = true, // "Guardar un recuerdo no significa necesitar contactar"
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
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

@Serializable
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

@Serializable
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

@Serializable
@Entity(tableName = "personal_journal")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val content: String,
    val moodTag: String = "Reflexión", // Calma, Nostalgia, Ansiedad, Claridad, Duelo, Gratitud, Valentía, Confusión
    val philosophicalFramework: String = "ESTOICO", // ESTOICO, PSICOLOGIA_MODERNA, CATOLICO, SOCRATICO
    val aiFeedback: String = "", // Retroalimentación reflexiva / mentoría filosófica
    val aiCorePrinciple: String = "", // Máxima / Principio rector citado
    val aiSocraticQuestion: String = "", // Pregunta socrática de autoindagación
    val aiConcreteAction: String = "", // Micro-acción sugerida
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
@Entity(tableName = "ai_messages")
data class AiMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "user" | "soltar_ai"
    val content: String,
    val detectedRumination: Boolean = false,
    val suggestedAction: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
@Entity(tableName = "time_capsules")
data class TimeCapsuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val unlockAtTimestamp: Long, // Fecha prevista de desbloqueo
    val createdAt: Long = System.currentTimeMillis(),
    val isUnlocked: Boolean = false
)

@Serializable
@Entity(tableName = "soltar_settings")
data class SoltarSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val memoryEnabled: Boolean = true,
    val userName: String = "Viajero",
    val breakupDateTimestamp: Long = System.currentTimeMillis() - (14L * 24 * 3600 * 1000), // Default 2 weeks ago
    val biometricLockEnabled: Boolean = false,
    val soundEnabled: Boolean = true,
    val themeMode: String = "LIGHT", // "LIGHT" | "DARK" | "SYSTEM"
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
    val subscriptionTier: String = "FREE", // "FREE" | "PREMIUM_ONE_TIME"
    val isTrialActive: Boolean = false,
    val subscriptionExpiryTimestamp: Long = 0L,
    // Authentication
    val isLoggedIn: Boolean = false,
    val userEmail: String = "",
    val userPasswordHash: String = "",
    // PIN Hash (Local Lockdown)
    val pinHash: String = "",
    // Perspectives
    val faithPerspectiveActive: Boolean = false,
    val stoicPerspectiveActive: Boolean = false,
    val modernPsychologyPerspectiveActive: Boolean = true,
    // Scheduled Notifications & Empathetic Reminders
    val notificationsEnabled: Boolean = true,
    val reminderHour: Int = 21,
    val reminderMinute: Int = 0,
    val lastMilestoneCelebrated: Int = 0,
    val inactivityAlertsEnabled: Boolean = true,
    val lastInactivityNoticeSentTimestamp: Long = 0L
)
