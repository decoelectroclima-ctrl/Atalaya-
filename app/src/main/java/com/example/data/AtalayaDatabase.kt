package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        CheckinEntity::class,
        UrgeEpisodeEntity::class,
        ThoughtEntity::class,
        RelationshipAuditEntity::class,
        IdealizationEntity::class,
        UnsentLetterEntity::class,
        MemoryBankEntity::class,
        ExperimentEntity::class,
        IdentityGoalEntity::class,
        RelapseEntity::class,
        AiMessageEntity::class,
        SoltarSettingsEntity::class,
        FactEntity::class,
        CryptoVaultEntity::class,
        JournalEntryEntity::class,
        UserProfileEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AtalayaDatabase : RoomDatabase() {
    abstract fun checkinDao(): CheckinDao
    abstract fun urgeEpisodeDao(): UrgeEpisodeDao
    abstract fun thoughtDao(): ThoughtDao
    abstract fun relationshipAuditDao(): RelationshipAuditDao
    abstract fun idealizationDao(): IdealizationDao
    abstract fun unsentLetterDao(): UnsentLetterDao
    abstract fun memoryBankDao(): MemoryBankDao
    abstract fun experimentDao(): ExperimentDao
    abstract fun identityGoalDao(): IdentityGoalDao
    abstract fun relapseDao(): RelapseDao
    abstract fun aiMessageDao(): AiMessageDao
    abstract fun soltarSettingsDao(): SoltarSettingsDao

    // Legacy DAOs
    abstract fun factDao(): FactDao
    abstract fun cryptoVaultDao(): CryptoVaultDao
    abstract fun journalDao(): JournalDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AtalayaDatabase? = null

        fun getDatabase(context: Context): AtalayaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AtalayaDatabase::class.java,
                    "soltar_database"
                )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .addCallback(DatabasePrepopulationCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabasePrepopulationCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                populateInitialData(getDatabase(context))
            }
        }

        private suspend fun populateInitialData(database: AtalayaDatabase) {
            // Initial Check-in
            database.checkinDao().insertOrUpdateCheckin(
                CheckinEntity(
                    dateKey = "2026-08-25",
                    pain = 4f,
                    anxiety = 3f,
                    nostalgia = 5f,
                    anger = 2f,
                    loneliness = 4f,
                    rumination = 3f,
                    urgeToContact = 2f,
                    autonomy = 7f,
                    focusBodyAction = "Caminar 30 min al aire libre sin mirar el móvil",
                    focusBodyDone = true,
                    focusSelfAction = "Dedicar 45 min a mi proyecto de diseño",
                    focusSelfDone = false,
                    focusSocialAction = "Llamar a Carlos para tomar un café",
                    focusSocialDone = false,
                    note = "Día tranquilo. Tuve un momento de nostalgia al pasar por la cafetería, pero apliqué la respiración."
                )
            )

            // Initial Thoughts in Laboratory
            database.thoughtDao().insertThought(
                ThoughtEntity(
                    originalThought = "No me responde el mensaje de logística porque ya no le importo nada y me borró de su vida.",
                    fact = "Envié un mensaje sobre las llaves hace 3 horas y no hay confirmación de lectura.",
                    interpretation = "Asumo que su silencio significa desprecio intencional hacia mi persona.",
                    hypothesis = "Podría estar ocupado/a, sin cobertura, o simplemente gestionando su propio espacio.",
                    evidenceFor = "En el pasado a veces tardaba en responder cuando estaba distante.",
                    evidenceAgainst = "Muchas veces tardó horas por reuniones de trabajo o falta de batería.",
                    cannotKnow = "Qué está sintiendo en este instante exacto o qué está haciendo.",
                    dependsOnMe = "Dejar el teléfono en otra habitación y continuar con mi informe de trabajo.",
                    concreteAction = "Poner el teléfono en modo avión y dar una caminata de 15 minutos.",
                    isClosed = true
                )
            )

            // Initial Relationship Audits (3 responsibility columns)
            database.relationshipAuditDao().insertAudit(
                RelationshipAuditEntity(
                    title = "Discusión sobre planes de fin de semana y convivencia",
                    category = "Comunicación y Límites",
                    myResponsibility = "Esperar que adivinara mis necesidades sin expresarlas con claridad; reaccionar con silencio pasivo-agresivo.",
                    otherResponsibility = "Descartar mis propuestas considerándolas una exigencia; cancelar planes a última hora sin avisar.",
                    sharedResponsibility = "No haber establecido acuerdos explícitos sobre tiempo compartido y espacio individual.",
                    patternIdentified = "Dinámica persecución-distanciamiento (cuanto más pedía confirmación, más se alejaba)."
                )
            )
            database.relationshipAuditDao().insertAudit(
                RelationshipAuditEntity(
                    title = "Gestión de desacuerdos económicos y proyectos de futuro",
                    category = "Compatibilidad y Proyecto",
                    myResponsibility = "Ceder en mis metas financieras prioritarias para evitar generar tensión o conflicto.",
                    otherResponsibility = "Priorizar gastos impulsivos sin consultar metas de ahorro comunes previamente acordadas.",
                    sharedResponsibility = "Diferencia fundamental en valores de estabilidad vs espontaneidad que no quisimos abordar a tiempo.",
                    patternIdentified = "Evitación sistemática de conversaciones incómodas sobre el futuro."
                )
            )

            // Initial Idealization Antidote pairs
            database.idealizationDao().insertIdealizationEntry(
                IdealizationEntity(
                    whatIMiss = "Extraño cómo nos reíamos juntos viendo películas los domingos por la tarde.",
                    whatIActuallyExperienced = "La mayoría de domingos había una tensión no resuelta y una sensación constante de caminar sobre cáscaras de huevo."
                )
            )
            database.idealizationDao().insertIdealizationEntry(
                IdealizationEntity(
                    whatIMiss = "Extraño sentir que tenía a alguien incondicional a quien contarle mi día.",
                    whatIActuallyExperienced = "Cuando le contaba mis problemas, con frecuencia recibía respuestas cortantes o críticas sobre cómo debería sentirme."
                )
            )

            // Initial Unsent Letter
            database.unsentLetterDao().insertLetter(
                UnsentLetterEntity(
                    title = "Cosas que no te dije el día que nos despedimos",
                    category = "Despedida",
                    content = "Te agradezco lo que aprendí a tu lado, pero también reconozco que nuestro vínculo ya no era un lugar seguro para mi bienestar. No te guardo rencor, pero elijo no continuar buscándote. Hoy decido quedarme conmigo.",
                    isClosed = true,
                    closedAtTimestamp = System.currentTimeMillis() - 86400000L
                )
            )

            // Initial Behavioral Experiments
            database.experimentDao().insertExperiment(
                ExperimentEntity(
                    title = "24 horas completas sin entrar a sus perfiles en redes sociales",
                    durationHours = 24,
                    status = "Completado",
                    actualOutcome = "Las primeras 4 horas sentí ansiedad por verificar, pero a la tarde me sentí mucho más sereno y descansado mentalmente.",
                    learning = "Mirar sus redes no me alivia: solo reabre el ciclo de búsqueda de pistas y eleva mi cortisol.",
                    completedAt = System.currentTimeMillis() - 172800000L
                )
            )
            database.experimentDao().insertExperiment(
                ExperimentEntity(
                    title = "48 horas sin comprobar su última hora de conexión",
                    durationHours = 48,
                    status = "Activo",
                    actualOutcome = "En progreso...",
                    learning = ""
                )
            )

            // Initial Identity Areas & Goals
            database.identityGoalDao().insertIdentityGoal(
                IdentityGoalEntity(
                    area = "Cuerpo y Salud",
                    whoIWas = "Abandoné mis entrenamientos y comía con desgana según el estado de la relación.",
                    whoIAm = "Retomando rutinas físicas con paciencia y respeto hacia mi cuerpo.",
                    whoIWantToBe = "Una persona con vitalidad, fuerza física y energía estable.",
                    goalTitle = "Entrenar 3 veces por semana y caminar 8.000 pasos diarios",
                    goalFrequency = "Diario",
                    isCompleted = true,
                    streakDays = 5
                )
            )
            database.identityGoalDao().insertIdentityGoal(
                IdentityGoalEntity(
                    area = "Vida Social y Amistades",
                    whoIWas = "Me aislé de mis amigos más cercanos para estar siempre disponible para mi pareja.",
                    whoIAm = "Reconectando con personas que me nutren y escuchan de verdad.",
                    whoIWantToBe = "Un amigo presente, que cultiva vínculos recíprocos y seguros.",
                    goalTitle = "Organizar una cena o llamada con un amigo cada semana",
                    goalFrequency = "Semanal",
                    isCompleted = false,
                    streakDays = 2
                )
            )
            database.identityGoalDao().insertIdentityGoal(
                IdentityGoalEntity(
                    area = "Proyectos y Vocación",
                    whoIWas = "Pospuse mis cursos y proyectos creativos por falta de enfoque.",
                    whoIAm = "Dedicando bloques diarios de concentración a mi aprendizaje.",
                    whoIWantToBe = "Un profesional autónomo y apasionado por su desarrollo.",
                    goalTitle = "Avanzar 45 minutos diarios en mi proyecto profesional",
                    goalFrequency = "Diario",
                    isCompleted = false,
                    streakDays = 3
                )
            )

            // Initial Memory with Conscious Rule
            database.memoryBankDao().insertMemory(
                MemoryBankEntity(
                    title = "Viaje a la costa (Primavera 2024)",
                    description = "Fue una experiencia bonita que formó parte de mi historia. Guardo el recuerdo con serenidad, entendiendo que el pasado ya cumplió su ciclo.",
                    category = "Experiencia",
                    ruleAcknowledged = true
                )
            )

            // Initial AI Messages
            database.aiMessageDao().insertMessage(
                AiMessageEntity(
                    sender = "soltar_ai",
                    content = "Bienvenido/a a **SOLTAR**. Este no es un espacio para alimentar la rumiación ni buscar culpables. Es una herramienta de precisión para regular tu sistema nervioso, comprender lo vivido con rigor y reconstruir tu autonomía.\n\n*«Puedes seguir queriendo a alguien y dejar de organizar tu vida alrededor de esa persona.»*\n\n¿Qué está ocurriendo hoy en tu día?",
                    detectedRumination = false
                )
            )

            // Initial Settings
            database.soltarSettingsDao().saveSettings(
                SoltarSettingsEntity(
                    id = 1,
                    memoryEnabled = true,
                    userName = "Viajero",
                    breakupDateTimestamp = System.currentTimeMillis() - (21L * 24 * 3600 * 1000),
                    biometricLockEnabled = false
                )
            )
        }
    }
}
