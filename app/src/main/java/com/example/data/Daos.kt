package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckinDao {
    @Query("SELECT * FROM daily_checkins ORDER BY timestamp DESC")
    fun getAllCheckins(): Flow<List<CheckinEntity>>

    @Query("SELECT * FROM daily_checkins WHERE dateKey = :dateKey LIMIT 1")
    fun getCheckinByDate(dateKey: String): Flow<CheckinEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCheckin(checkin: CheckinEntity): Long

    @Query("UPDATE daily_checkins SET focusBodyDone = :done WHERE id = :id")
    suspend fun updateBodyDone(id: Long, done: Boolean)

    @Query("UPDATE daily_checkins SET focusSelfDone = :done WHERE id = :id")
    suspend fun updateSelfDone(id: Long, done: Boolean)

    @Query("UPDATE daily_checkins SET focusSocialDone = :done WHERE id = :id")
    suspend fun updateSocialDone(id: Long, done: Boolean)
}

@Dao
interface UrgeEpisodeDao {
    @Query("SELECT * FROM urge_episodes ORDER BY timestamp DESC")
    fun getAllUrgeEpisodes(): Flow<List<UrgeEpisodeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUrgeEpisode(episode: UrgeEpisodeEntity): Long

    @Query("DELETE FROM urge_episodes WHERE id = :id")
    suspend fun deleteUrgeEpisode(id: Long)
}

@Dao
interface ThoughtDao {
    @Query("SELECT * FROM thought_laboratory ORDER BY timestamp DESC")
    fun getAllThoughts(): Flow<List<ThoughtEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThought(thought: ThoughtEntity): Long

    @Query("UPDATE thought_laboratory SET isClosed = 1 WHERE id = :id")
    suspend fun closeThoughtLoop(id: Long)

    @Query("DELETE FROM thought_laboratory WHERE id = :id")
    suspend fun deleteThought(id: Long)
}

@Dao
interface RelationshipAuditDao {
    @Query("SELECT * FROM relationship_audits ORDER BY timestamp DESC")
    fun getAllAudits(): Flow<List<RelationshipAuditEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudit(audit: RelationshipAuditEntity): Long

    @Query("DELETE FROM relationship_audits WHERE id = :id")
    suspend fun deleteAudit(id: Long)
}

@Dao
interface IdealizationDao {
    @Query("SELECT * FROM idealization_antidotes ORDER BY timestamp DESC")
    fun getAllIdealizationEntries(): Flow<List<IdealizationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdealizationEntry(entry: IdealizationEntity): Long

    @Query("DELETE FROM idealization_antidotes WHERE id = :id")
    suspend fun deleteIdealizationEntry(id: Long)
}

@Dao
interface UnsentLetterDao {
    @Query("SELECT * FROM unsent_letters ORDER BY timestamp DESC")
    fun getAllLetters(): Flow<List<UnsentLetterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLetter(letter: UnsentLetterEntity): Long

    @Query("UPDATE unsent_letters SET isClosed = 1, closedAtTimestamp = :timestamp WHERE id = :id")
    suspend fun performClosingCeremony(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM unsent_letters WHERE id = :id")
    suspend fun deleteLetter(id: Long)
}

@Dao
interface MemoryBankDao {
    @Query("SELECT * FROM memory_bank ORDER BY timestamp DESC")
    fun getAllMemories(): Flow<List<MemoryBankEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryBankEntity): Long

    @Query("DELETE FROM memory_bank WHERE id = :id")
    suspend fun deleteMemory(id: Long)
}

@Dao
interface ExperimentDao {
    @Query("SELECT * FROM behavioral_experiments ORDER BY startedAt DESC")
    fun getAllExperiments(): Flow<List<ExperimentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExperiment(experiment: ExperimentEntity): Long

    @Query("UPDATE behavioral_experiments SET status = :status, actualOutcome = :outcome, learning = :learning, completedAt = :completedAt WHERE id = :id")
    suspend fun completeExperiment(id: Long, status: String, outcome: String, learning: String, completedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM behavioral_experiments WHERE id = :id")
    suspend fun deleteExperiment(id: Long)
}

@Dao
interface IdentityGoalDao {
    @Query("SELECT * FROM identity_goals ORDER BY area ASC, timestamp DESC")
    fun getAllIdentityGoals(): Flow<List<IdentityGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdentityGoal(goal: IdentityGoalEntity): Long

    @Query("UPDATE identity_goals SET isCompleted = :completed WHERE id = :id")
    suspend fun toggleGoalCompleted(id: Long, completed: Boolean)

    @Query("DELETE FROM identity_goals WHERE id = :id")
    suspend fun deleteIdentityGoal(id: Long)
}

@Dao
interface RelapseDao {
    @Query("SELECT * FROM relapse_logs ORDER BY timestamp DESC")
    fun getAllRelapses(): Flow<List<RelapseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelapse(relapse: RelapseEntity): Long
}

@Dao
interface AiMessageDao {
    @Query("SELECT * FROM ai_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<AiMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: AiMessageEntity): Long

    @Query("DELETE FROM ai_messages")
    suspend fun clearAllMessages()
}

@Dao
interface SoltarSettingsDao {
    @Query("SELECT * FROM soltar_settings WHERE id = 1")
    fun getSettings(): Flow<SoltarSettingsEntity?>

    @Query("SELECT * FROM soltar_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettingsOnce(): SoltarSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: SoltarSettingsEntity)
}

// Legacy DAOs for backwards compatibility
@Dao
interface FactDao {
    @Query("SELECT * FROM facts ORDER BY timestamp DESC")
    fun getAllFacts(): Flow<List<FactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFact(fact: FactEntity)

    @Query("DELETE FROM facts WHERE id = :id")
    suspend fun deleteFact(id: Long)
}

@Dao
interface CryptoVaultDao {
    @Query("SELECT * FROM crypto_vault WHERE isDestroyed = 0 ORDER BY createdTimestamp DESC")
    fun getActiveVaultMessages(): Flow<List<CryptoVaultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaultMessage(message: CryptoVaultEntity): Long

    @Query("UPDATE crypto_vault SET isDestroyed = 1 WHERE id = :id")
    suspend fun markDestroyed(id: Long)

    @Query("DELETE FROM crypto_vault WHERE isDestroyed = 1 OR scheduledDestructionTimestamp <= :currentTime")
    suspend fun purgeExpiredMessages(currentTime: Long)
}

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY timestamp DESC")
    fun getAllJournalEntries(): Flow<List<JournalEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntryEntity)

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteEntry(id: Long)
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)
}
