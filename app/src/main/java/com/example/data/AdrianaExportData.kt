package com.example.data

import kotlinx.serialization.Serializable

@Serializable
data class AdrianaExportData(
    val version: Int = 1,
    val checkins: List<CheckinEntity>,
    val journalEntries: List<JournalEntryEntity>,
    val unsentLetters: List<UnsentLetterEntity>,
    val relationshipAudits: List<RelationshipAuditEntity>,
    val aiMessages: List<AiMessageEntity>,
    val redFlags: List<RedFlagEntity>,
    val triggerEvents: List<TriggerEventEntity>,
    val thoughtLabEntries: List<ThoughtLabEntity>,
    val settings: SoltarSettingsEntity?,
    val clinicalProgressSummary: String? = null
)
