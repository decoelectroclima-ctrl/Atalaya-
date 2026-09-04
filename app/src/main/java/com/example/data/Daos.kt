package com.example.data

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("SELECT * FROM daily_checkins ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestCheckin(): CheckinEntity?

    @Query("SELECT * FROM daily_checkins ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentCheckins(limit: Int): List<CheckinEntity>

    @Query("SELECT COUNT(*) FROM daily_checkins")
    suspend fun getCheckinCount(): Int

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

    @Query("SELECT * FROM unsent_letters ORDER BY timestamp DESC")
    suspend fun getAllLettersOnce(): List<UnsentLetterEntity>

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

    @Query("SELECT * FROM relapse_logs ORDER BY timestamp DESC")
    suspend fun getAllRelapsesOnce(): List<RelapseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelapse(relapse: RelapseEntity): Long
}

@Dao
interface TriggerEventDao {
    @Query("SELECT * FROM trigger_events ORDER BY timestamp DESC")
    fun getAllTriggerEvents(): Flow<List<TriggerEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTriggerEvent(triggerEvent: TriggerEventEntity): Long
}

@Dao
interface RedFlagDao {
    @Query("SELECT * FROM red_flags ORDER BY timestamp DESC")
    fun getAllRedFlags(): Flow<List<RedFlagEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRedFlag(redFlag: RedFlagEntity): Long

    @Delete
    suspend fun deleteRedFlag(redFlag: RedFlagEntity)
}

@Dao
interface PeerSupportDao {
    @Query("SELECT * FROM peer_support_posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<PeerSupportPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PeerSupportPostEntity): Long

    @Query("UPDATE peer_support_posts SET likes = likes + 1 WHERE id = :id")
    suspend fun likePost(id: Long)
}

@Dao
interface ThoughtLabDao {
    @Query("SELECT * FROM thought_lab_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<ThoughtLabEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: ThoughtLabEntity): Long
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
interface JournalDao {
    @Query("SELECT * FROM personal_journal ORDER BY timestamp DESC")
    fun getAllJournalEntries(): Flow<List<JournalEntryEntity>>

    @Query("SELECT * FROM personal_journal WHERE id = :id LIMIT 1")
    fun getJournalEntryById(id: Long): Flow<JournalEntryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntry(entry: JournalEntryEntity): Long

    @Query("UPDATE personal_journal SET aiFeedback = :feedback, aiCorePrinciple = :corePrinciple, aiSocraticQuestion = :socraticQuestion, aiConcreteAction = :concreteAction, philosophicalFramework = :framework WHERE id = :id")
    suspend fun updateJournalFeedback(
        id: Long,
        feedback: String,
        corePrinciple: String,
        socraticQuestion: String,
        concreteAction: String,
        framework: String
    )

    @Query("DELETE FROM personal_journal WHERE id = :id")
    suspend fun deleteJournalEntry(id: Long)
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
interface TimeCapsuleDao {
    @Query("SELECT * FROM time_capsules ORDER BY createdAt DESC")
    fun getAllCapsules(): Flow<List<TimeCapsuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCapsule(capsule: TimeCapsuleEntity): Long

    @Query("UPDATE time_capsules SET isUnlocked = 1 WHERE id = :id")
    suspend fun unlockCapsule(id: Long)
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

@Dao
interface WisdomContributionDao {
    @Query("SELECT * FROM wisdom_contributions ORDER BY timestamp DESC")
    fun getAllContributions(): Flow<List<WisdomContributionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContribution(contribution: WisdomContributionEntity): Long
}

@Dao
interface RiskDateDao {
    @Query("SELECT * FROM risk_dates ORDER BY month ASC, day ASC")
    fun getAllRiskDates(): Flow<List<RiskDateEntity>>

    @Query("SELECT * FROM risk_dates")
    suspend fun getAllRiskDatesOnce(): List<RiskDateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRiskDate(entity: RiskDateEntity): Long

    @Query("DELETE FROM risk_dates WHERE id = :id")
    suspend fun deleteRiskDate(id: Long)
}

@Dao
interface CoachGoalDao {
    @Query("SELECT * FROM coach_goals ORDER BY timestamp DESC")
    fun getAllGoals(): Flow<List<CoachGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: CoachGoalEntity): Long

    @Query("UPDATE coach_goals SET isCompleted = :completed WHERE id = :id")
    suspend fun toggleGoal(id: Long, completed: Boolean)

    @Query("DELETE FROM coach_goals WHERE id = :id")
    suspend fun deleteGoal(id: Long)
}

@Dao
interface BodyMetricDao {
    @Query("SELECT * FROM body_metrics ORDER BY timestamp DESC")
    fun getAllMetrics(): Flow<List<BodyMetricRecordEntity>>

    @Query("SELECT * FROM body_metrics ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMetric(): BodyMetricRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetric(metric: BodyMetricRecordEntity): Long

    @Query("DELETE FROM body_metrics WHERE id = :id")
    suspend fun deleteMetric(id: Long)
}

@Dao
interface CoachDailyCheckinDao {
    @Query("SELECT * FROM coach_daily_checkins ORDER BY timestamp DESC")
    fun getAllCheckins(): Flow<List<CoachDailyCheckinEntity>>

    @Query("SELECT * FROM coach_daily_checkins WHERE dateKey = :dateKey LIMIT 1")
    fun getCheckinByDate(dateKey: String): Flow<CoachDailyCheckinEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckin(checkin: CoachDailyCheckinEntity): Long
}

@Dao
interface CoachPlanDao {
    @Query("SELECT * FROM coach_plans ORDER BY timestamp DESC")
    fun getAllPlans(): Flow<List<CoachPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: CoachPlanEntity): Long

    @Query("DELETE FROM coach_plans WHERE id = :id")
    suspend fun deletePlan(id: Long)
}





