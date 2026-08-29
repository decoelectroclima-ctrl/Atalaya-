package com.example.data

import kotlinx.coroutines.flow.Flow

class SoltarRepository(private val database: AdrianaDatabase) {

    // Daily Checkins
    val allCheckins: Flow<List<CheckinEntity>> = database.checkinDao().getAllCheckins()

    fun getCheckinByDate(dateKey: String): Flow<CheckinEntity?> {
        return database.checkinDao().getCheckinByDate(dateKey)
    }

    suspend fun getLatestCheckin(): CheckinEntity? {
        return database.checkinDao().getLatestCheckin()
    }

    suspend fun getCheckinCount(): Int {
        return database.checkinDao().getCheckinCount()
    }

    suspend fun saveCheckin(checkin: CheckinEntity): Long {
        return database.checkinDao().insertOrUpdateCheckin(checkin)
    }

    suspend fun updateFocusActionDone(id: Long, type: String, done: Boolean) {
        when (type) {
            "body" -> database.checkinDao().updateBodyDone(id, done)
            "self" -> database.checkinDao().updateSelfDone(id, done)
            "social" -> database.checkinDao().updateSocialDone(id, done)
        }
    }

    // Urge Episodes
    val allUrgeEpisodes: Flow<List<UrgeEpisodeEntity>> = database.urgeEpisodeDao().getAllUrgeEpisodes()

    suspend fun saveUrgeEpisode(episode: UrgeEpisodeEntity): Long {
        return database.urgeEpisodeDao().insertUrgeEpisode(episode)
    }

    suspend fun deleteUrgeEpisode(id: Long) {
        database.urgeEpisodeDao().deleteUrgeEpisode(id)
    }

    // Thought Laboratory
    val allThoughts: Flow<List<ThoughtEntity>> = database.thoughtDao().getAllThoughts()

    suspend fun saveThought(thought: ThoughtEntity): Long {
        return database.thoughtDao().insertThought(thought)
    }

    suspend fun closeThoughtLoop(id: Long) {
        database.thoughtDao().closeThoughtLoop(id)
    }

    suspend fun deleteThought(id: Long) {
        database.thoughtDao().deleteThought(id)
    }

    // Relationship Audit
    val allAudits: Flow<List<RelationshipAuditEntity>> = database.relationshipAuditDao().getAllAudits()

    suspend fun saveAudit(audit: RelationshipAuditEntity): Long {
        return database.relationshipAuditDao().insertAudit(audit)
    }

    suspend fun deleteAudit(id: Long) {
        database.relationshipAuditDao().deleteAudit(id)
    }

    // Idealization vs Reality
    val allIdealizationEntries: Flow<List<IdealizationEntity>> = database.idealizationDao().getAllIdealizationEntries()

    suspend fun saveIdealizationEntry(entry: IdealizationEntity): Long {
        return database.idealizationDao().insertIdealizationEntry(entry)
    }

    suspend fun deleteIdealizationEntry(id: Long) {
        database.idealizationDao().deleteIdealizationEntry(id)
    }

    // Unsent Letters
    val allLetters: Flow<List<UnsentLetterEntity>> = database.unsentLetterDao().getAllLetters()

    suspend fun saveLetter(letter: UnsentLetterEntity): Long {
        return database.unsentLetterDao().insertLetter(letter)
    }

    suspend fun performClosingCeremony(id: Long) {
        database.unsentLetterDao().performClosingCeremony(id)
    }

    suspend fun deleteLetter(id: Long) {
        database.unsentLetterDao().deleteLetter(id)
    }

    // Memory Bank
    val allMemories: Flow<List<MemoryBankEntity>> = database.memoryBankDao().getAllMemories()

    suspend fun saveMemory(memory: MemoryBankEntity): Long {
        return database.memoryBankDao().insertMemory(memory)
    }

    suspend fun deleteMemory(id: Long) {
        database.memoryBankDao().deleteMemory(id)
    }

    // Behavioral Experiments
    val allExperiments: Flow<List<ExperimentEntity>> = database.experimentDao().getAllExperiments()

    suspend fun saveExperiment(experiment: ExperimentEntity): Long {
        return database.experimentDao().insertExperiment(experiment)
    }

    suspend fun completeExperiment(id: Long, outcome: String, learning: String) {
        database.experimentDao().completeExperiment(id, "Completado", outcome, learning)
    }

    suspend fun deleteExperiment(id: Long) {
        database.experimentDao().deleteExperiment(id)
    }

    // Identity & Goals
    val allIdentityGoals: Flow<List<IdentityGoalEntity>> = database.identityGoalDao().getAllIdentityGoals()

    suspend fun saveIdentityGoal(goal: IdentityGoalEntity): Long {
        return database.identityGoalDao().insertIdentityGoal(goal)
    }

    suspend fun toggleGoalCompleted(id: Long, completed: Boolean) {
        database.identityGoalDao().toggleGoalCompleted(id, completed)
    }

    suspend fun deleteIdentityGoal(id: Long) {
        database.identityGoalDao().deleteIdentityGoal(id)
    }

    // Relapse Log
    val allRelapses: Flow<List<RelapseEntity>> = database.relapseDao().getAllRelapses()

    suspend fun saveRelapse(relapse: RelapseEntity): Long {
        return database.relapseDao().insertRelapse(relapse)
    }

    // Trigger Events (B5)
    val allTriggerEvents: Flow<List<TriggerEventEntity>> = database.triggerEventDao().getAllTriggerEvents()

    suspend fun saveTriggerEvent(triggerEvent: TriggerEventEntity): Long {
        return database.triggerEventDao().insertTriggerEvent(triggerEvent)
    }

    // Red Flags (B2)
    val allRedFlags: Flow<List<RedFlagEntity>> = database.redFlagDao().getAllRedFlags()

    suspend fun saveRedFlag(redFlag: RedFlagEntity): Long {
        return database.redFlagDao().insertRedFlag(redFlag)
    }

    suspend fun deleteRedFlag(redFlag: RedFlagEntity) {
        database.redFlagDao().deleteRedFlag(redFlag)
    }

    // Peer Support (B3)
    val allPeerSupportPosts: Flow<List<PeerSupportPostEntity>> = database.peerSupportDao().getAllPosts()

    suspend fun savePeerSupportPost(post: PeerSupportPostEntity): Long {
        return database.peerSupportDao().insertPost(post)
    }

    suspend fun likePeerSupportPost(id: Long) {
        database.peerSupportDao().likePost(id)
    }

    // Relationship Audit (D)
    val allRelationshipAudits: Flow<List<RelationshipAuditEntity>> = database.relationshipAuditDao().getAllAudits()

    suspend fun saveRelationshipAudit(audit: RelationshipAuditEntity): Long {
        return database.relationshipAuditDao().insertAudit(audit)
    }

    suspend fun deleteRelationshipAudit(id: Long) {
        database.relationshipAuditDao().deleteAudit(id)
    }

    // Personal Journal & Philosophical Mentorship
    val allJournalEntries: Flow<List<JournalEntryEntity>> = database.journalDao().getAllJournalEntries()

    // Thought Lab (C)
    val allThoughtLabEntries: Flow<List<ThoughtLabEntity>> = database.thoughtLabDao().getAllEntries()

    suspend fun saveThoughtLabEntry(entry: ThoughtLabEntity): Long {
        return database.thoughtLabDao().insertEntry(entry)
    }

    fun getJournalEntryById(id: Long): Flow<JournalEntryEntity?> {
        return database.journalDao().getJournalEntryById(id)
    }

    suspend fun saveJournalEntry(entry: JournalEntryEntity): Long {
        return database.journalDao().insertJournalEntry(entry)
    }

    suspend fun updateJournalFeedback(
        id: Long,
        feedback: String,
        corePrinciple: String,
        socraticQuestion: String,
        concreteAction: String,
        framework: String
    ) {
        database.journalDao().updateJournalFeedback(
            id, feedback, corePrinciple, socraticQuestion, concreteAction, framework
        )
    }

    suspend fun deleteJournalEntry(id: Long) {
        database.journalDao().deleteJournalEntry(id)
    }

    // AI Messages
    val allAiMessages: Flow<List<AiMessageEntity>> = database.aiMessageDao().getAllMessages()

    suspend fun saveAiMessage(message: AiMessageEntity): Long {
        return database.aiMessageDao().insertMessage(message)
    }

    suspend fun clearAiMemory() {
        database.aiMessageDao().clearAllMessages()
    }

    // Settings
    val settings: Flow<SoltarSettingsEntity?> = database.soltarSettingsDao().getSettings()

    suspend fun getSettingsOnce(): SoltarSettingsEntity? {
        return database.soltarSettingsDao().getSettingsOnce()
    }

    suspend fun saveSettings(settings: SoltarSettingsEntity) {
        database.soltarSettingsDao().saveSettings(settings)
    }

    // Time Capsule (C1)
    val allTimeCapsules: Flow<List<TimeCapsuleEntity>> = database.timeCapsuleDao().getAllCapsules()

    suspend fun saveTimeCapsule(capsule: TimeCapsuleEntity): Long {
        return database.timeCapsuleDao().insertCapsule(capsule)
    }

    suspend fun unlockTimeCapsule(id: Long) {
        database.timeCapsuleDao().unlockCapsule(id)
    }

    // Wisdom Contributions (C4)
    val allWisdomContributions: Flow<List<WisdomContributionEntity>> = database.wisdomContributionDao().getAllContributions()

    suspend fun saveWisdomContribution(contribution: WisdomContributionEntity): Long {
        return database.wisdomContributionDao().insertContribution(contribution)
    }
}
