package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

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
        TriggerEventEntity::class,
        RedFlagEntity::class,
        PeerSupportPostEntity::class,
        ThoughtLabEntity::class,
        AiMessageEntity::class,
        JournalEntryEntity::class,
        SoltarSettingsEntity::class,
        TimeCapsuleEntity::class
    ],
    version = 18,
    exportSchema = false
)
abstract class AdrianaDatabase : RoomDatabase() {
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
    abstract fun triggerEventDao(): TriggerEventDao
    abstract fun redFlagDao(): RedFlagDao
    abstract fun peerSupportDao(): PeerSupportDao
    abstract fun thoughtLabDao(): ThoughtLabDao
    abstract fun aiMessageDao(): AiMessageDao
    abstract fun journalDao(): JournalDao
    abstract fun soltarSettingsDao(): SoltarSettingsDao
    abstract fun timeCapsuleDao(): TimeCapsuleDao

    companion object {
        @Volatile
        private var INSTANCE: AdrianaDatabase? = null

        fun getDatabase(context: Context): AdrianaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AdrianaDatabase::class.java,
                    "adriana_database"
                )
                .addMigrations(object : Migration(9, 10) {
                    override fun migrate(db: SupportSQLiteDatabase) {
                        db.execSQL("ALTER TABLE soltar_settings ADD COLUMN isLoggedIn INTEGER NOT NULL DEFAULT 0")
                        db.execSQL("ALTER TABLE soltar_settings ADD COLUMN userEmail TEXT NOT NULL DEFAULT ''")
                        db.execSQL("ALTER TABLE soltar_settings ADD COLUMN userPasswordHash TEXT NOT NULL DEFAULT ''")
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateInitialDataIfEmpty(database: AdrianaDatabase) {
            try {
                val settings = database.soltarSettingsDao().getSettingsOnce()
                if (settings == null) {
                    populateCleanData(database)
                }
            } catch (_: Exception) {
            }
        }

        suspend fun populateCleanData(database: AdrianaDatabase) {
            database.soltarSettingsDao().saveSettings(
                SoltarSettingsEntity(
                    id = 1,
                    memoryEnabled = true,
                    userName = "",
                    pinHash = "",
                    isLoggedIn = false,
                    breakupDateTimestamp = System.currentTimeMillis(),
                    biometricLockEnabled = false,
                    soundEnabled = true,
                    onboardingCompleted = false,
                    preferredFramework = "PSICOLOGIA_MODERNA",
                    subscriptionTier = "FREE",
                    isTrialActive = false
                )
            )
        }
    }
}
