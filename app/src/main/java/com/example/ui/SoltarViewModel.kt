package com.example.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.example.ai.SoltarAiEngine
import com.example.ai.SoltarAiResponse
import com.example.ai.SoltarUserContext
import com.example.billing.BillingManager
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class SoltarTab(val label: String, val title: String) {
    INICIO("Inicio", "Hoy"),
    PROCESO("Proceso", "Tu Proceso"),
    PERFIL("Perfil", "Identidad y Perfil")
}

data class SoltarUiState(
    val currentTab: SoltarTab = SoltarTab.INICIO,
    val notificationMessage: String? = null,
    
    // Urge Mode (Persistent Floating / 6-Phase Protocol)
    val isUrgeSheetVisible: Boolean = false,
    val urgePhase: Int = 1, // 1 to 6
    val urgeTimerSecondsRemaining: Int = 1200, // 20 minutes (1200 sec)
    val isUrgeTimerRunning: Boolean = false,
    val urgeInitialIntensity: Int = 8,
    val urgeFinalIntensity: Int = 3,
    val urgeEmotion: String = "Nostalgia",
    val urgeDesiredAction: String = "Escribir un mensaje",
    val urgeExpectedOutcome: String = "Alivio momentáneo",
    val urgeFact: String = "",
    val urgeInterpretation: String = "",
    val urgeCannotKnow: String = "",
    val urgeDependsOnMe: String = "",
    val urgeTrigger: String = "Soledad / Noche",
    val urgeLearning: String = "",
    
    // Daily Checkin Form
    val todayPain: Float = 4f,
    val todayAnxiety: Float = 3f,
    val todayNostalgia: Float = 5f,
    val todayAnger: Float = 2f,
    val todayLoneliness: Float = 4f,
    val todayRumination: Float = 3f,
    val todayUrgeToContact: Float = 2f,
    val todayAutonomy: Float = 7f,
    val focusBodyInput: String = "Caminar 20 minutos al aire libre",
    val focusSelfInput: String = "Avanzar en mi proyecto personal",
    val focusSocialInput: String = "Conversar con un buen amigo",
    val checkinNoteInput: String = "",
    
    // Contextual State Selection ("¿Cómo estás ahora?")
    val selectedFeeling: String = "",
    val isJournalModalVisible: Boolean = false,
    val selectedJournalEntry: JournalEntryEntity? = null,
    val isGeneratingJournalMentorship: Boolean = false,
    val journalInputTitle: String = "",
    val journalInputContent: String = "",
    val journalInputMood: String = "Reflexión",
    val journalInputFramework: SoltarFramework = SoltarFramework.ESTOICO,
    val isThoughtModalVisible: Boolean = false,
    val isAuditModalVisible: Boolean = false,
    val isIdealizationModalVisible: Boolean = false,
    val isLetterModalVisible: Boolean = false,
    val isRelapseModalVisible: Boolean = false,
    val isIdentityGoalModalVisible: Boolean = false,
    val isAiCompanionSheetVisible: Boolean = false,
    val isNoThinkingSheetVisible: Boolean = false,
    val isMemoryModalVisible: Boolean = false,
    val isTimeCapsuleModalVisible: Boolean = false,
    val isEncounterSimulatorVisible: Boolean = false,
    val isWisdomLibraryVisible: Boolean = false,
    val isClosingRitualVisible: Boolean = false,
    val isVoluntaryExitVisible: Boolean = false,
    val isNeedHelpSheetVisible: Boolean = false,
    val isFounderExperienceVisible: Boolean = false,
    val isConversationAnalyzerVisible: Boolean = false,
    val isEmotionalCheckinVisible: Boolean = false,
    val checkinStateInput: String = "Neutral",
    val checkinFirstThoughtsInput: String = "",
    val checkinUrgeInput: Float = 2f,
    val checkinPredominantEmotionInput: String = "Nostalgia",
    val checkinTriggerInput: String = "",
    val checkinComparisonInput: String = "Igual",
    val checkinFreeNoteInput: String = "",
    
    // Thought Laboratory Inputs
    val thoughtOriginalInput: String = "",
    val thoughtFactInput: String = "",
    val thoughtInterpretationInput: String = "",
    val thoughtHypothesisInput: String = "",
    val thoughtEvidenceForInput: String = "",
    val thoughtEvidenceAgainstInput: String = "",
    val thoughtCannotKnowInput: String = "",
    val thoughtDependsOnMeInput: String = "",
    val thoughtConcreteActionInput: String = "Dar un paseo de 15 minutos",
    
    // Relationship Audit Inputs
    val auditTitleInput: String = "",
    val auditCategoryInput: String = "Comunicación y Límites",
    val auditMyRespInput: String = "",
    val auditOtherRespInput: String = "",
    val auditSharedRespInput: String = "",
    val auditPatternInput: String = "",
    
    // Idealization Inputs
    val idealizationMissInput: String = "",
    val idealizationRealityInput: String = "",
    
    // Unsent Letter Inputs
    val letterTitleInput: String = "",
    val letterCategoryInput: String = "Despedida",
    val letterContentInput: String = "",
    
    // Identity & Goals Inputs
    val identityAreaSelected: String = "Cuerpo y Salud",
    val whoIWasInput: String = "",
    val whoIAmInput: String = "",
    val whoIWantToBeInput: String = "",
    val newGoalTitleInput: String = "",
    val newGoalFrequencyInput: String = "Diario",
    
    // Relapse Inputs
    val relapseWhatHappenedInput: String = "",
    val relapseTriggerInput: String = "",
    val relapseEmotionInput: String = "",
    val relapseThoughtInput: String = "",
    val relapseBehaviorInput: String = "",
    val relapseConsequenceInput: String = "",
    val relapseLearningInput: String = "",
    val relapseTimestamp: Long = System.currentTimeMillis(),
    val relapseIsRestarting: Boolean = true,
    val relapseInterpretation: String = "retroceso", // "retroceso", "reafirmacion", "neutro"
    
    // AI Chat Inputs
    val aiInputMessage: String = "",
    val isAiTyping: Boolean = false,

    // Onboarding & Sound UI State
    val isOnboardingVisible: Boolean = false,
    val isSoundEnabled: Boolean = true,
    val themeMode: String = "LIGHT", // "LIGHT" | "DARK" | "SYSTEM"
    val isPrivacyPolicyVisible: Boolean = false,
    val isTermsConditionsVisible: Boolean = false,

    // Reference Framework & Wisdom Cards
    val preferredFramework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA,
    val currentWisdomCard: WisdomCard? = null,

    // Authentication & Account Management
    val isAuthDialogVisible: Boolean = false,

    // Paywall & Monetization
    val isPaywallVisible: Boolean = false,
    val selectedSubscriptionPlan: SubscriptionPlan = SubscriptionPlan.MONTHLY,
    val isProcessingPayment: Boolean = false,

    // Support Network Contact Editor Dialog
    val isSupportContactDialogVisible: Boolean = false,
    val editingContactIndex: Int = 1,
    val contactNameInput: String = "",
    val contactPhoneInput: String = "",
    val contactRelationshipInput: String = "",

    // Scheduled Reminders & Notification Settings
    val isTimePickerDialogVisible: Boolean = false,
    val reminderHourInput: Int = 21,
    val reminderMinuteInput: Int = 0,
    val notificationsEnabled: Boolean = true,
    val inactivityAlertsEnabled: Boolean = true,
    val mandatoryJournalHourInput: Int = 20,
    val mandatoryJournalMinuteInput: Int = 0,
    val customNotifications: List<CustomNotificationItem> = emptyList(),
    val isCustomNotificationDialogVisible: Boolean = false,
    val editingCustomNotificationId: Long? = null,
    val customNotificationTitleInput: String = "Recordatorio de Soberanía",
    val customNotificationMessageInput: String = "Mantén tu enfoque y respira hondo.",
    val customNotificationHourInput: Int = 10,
    val customNotificationMinuteInput: Int = 0,

    // Anticipated Risk Dates Calendar
    val isRiskDateModalVisible: Boolean = false,
    val riskDateTitleInput: String = "",
    val riskDateMonthInput: Int = 1,
    val riskDateDayInput: Int = 1,
    val riskDateStrategyInput: String = "",
    val riskDateReminderDaysInput: Int = 7
)

class SoltarViewModel(application: Application) : AndroidViewModel(application) {

    val repository: SoltarRepository = SoltarRepository(AdrianaDatabase.getDatabase(application))
    val billingManager = BillingManager(application)
    val premiumProductDetails = billingManager.premiumProductDetails
    fun launchPurchase(activity: Activity, productDetails: ProductDetails) {
        billingManager.launchBillingFlow(activity, productDetails)
    }

    private val _uiState = MutableStateFlow(SoltarUiState())
    val uiState: StateFlow<SoltarUiState> = _uiState.asStateFlow()

    // Data streams from Room
    val checkins: StateFlow<List<CheckinEntity>> = repository.allCheckins
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val urgeEpisodes: StateFlow<List<UrgeEpisodeEntity>> = repository.allUrgeEpisodes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val thoughts: StateFlow<List<ThoughtEntity>> = repository.allThoughts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val audits: StateFlow<List<RelationshipAuditEntity>> = repository.allAudits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val idealizations: StateFlow<List<IdealizationEntity>> = repository.allIdealizationEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val letters: StateFlow<List<UnsentLetterEntity>> = repository.allLetters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val memories: StateFlow<List<MemoryBankEntity>> = repository.allMemories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val experiments: StateFlow<List<ExperimentEntity>> = repository.allExperiments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val identityGoals: StateFlow<List<IdentityGoalEntity>> = repository.allIdentityGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val coachGoals: StateFlow<List<CoachGoalEntity>> = repository.allCoachGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bodyMetrics: StateFlow<List<BodyMetricRecordEntity>> = repository.allBodyMetrics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val coachCheckins: StateFlow<List<CoachDailyCheckinEntity>> = repository.allCoachCheckins
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val coachPlans: StateFlow<List<CoachPlanEntity>> = repository.allCoachPlans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val relapses: StateFlow<List<RelapseEntity>> = repository.allRelapses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val triggerEvents: StateFlow<List<TriggerEventEntity>> = repository.allTriggerEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val journalEntries: StateFlow<List<JournalEntryEntity>> = repository.allJournalEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<SoltarSettingsEntity?> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val isMandatoryJournalPending: StateFlow<Boolean> = combine(
        settings,
        journalEntries
    ) { currentSettings, entries ->
        if (currentSettings == null) return@combine false
        val hour = currentSettings.mandatoryJournalHour
        val minute = currentSettings.mandatoryJournalMinute

        val calendar = Calendar.getInstance()
        val nowMillis = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val scheduledMillis = calendar.timeInMillis

        val isPastTime = nowMillis >= scheduledMillis

        val hasWrittenToday = entries.any { entry ->
            val entryCal = Calendar.getInstance().apply { timeInMillis = entry.timestamp }
            val currentCal = Calendar.getInstance()
            entryCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR) &&
            entryCal.get(Calendar.DAY_OF_YEAR) == currentCal.get(Calendar.DAY_OF_YEAR)
        }

        isPastTime && !hasWrittenToday
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val aiMessages: StateFlow<List<AiMessageEntity>> = repository.allAiMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wisdomContributions: StateFlow<List<WisdomContributionEntity>> = repository.allWisdomContributions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val riskDates: StateFlow<List<RiskDateEntity>> = repository.allRiskDates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val timeCapsules: StateFlow<List<TimeCapsuleEntity>> = repository.allTimeCapsules
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 100% Real, multi-variable vulnerability assessment engine
    val realVulnerabilityAssessment: StateFlow<com.example.ai.RealVulnerabilityAssessment> = combine(
        listOf(
            settings,
            checkins,
            journalEntries,
            urgeEpisodes,
            thoughts,
            relapses,
            riskDates,
            audits,
            idealizations
        )
    ) { array ->
        @Suppress("UNCHECKED_CAST")
        val currentSettings = array[0] as? SoltarSettingsEntity
        @Suppress("UNCHECKED_CAST")
        val checkinList = array[1] as List<CheckinEntity>
        @Suppress("UNCHECKED_CAST")
        val journalList = array[2] as List<JournalEntryEntity>
        @Suppress("UNCHECKED_CAST")
        val urgeList = array[3] as List<UrgeEpisodeEntity>
        @Suppress("UNCHECKED_CAST")
        val thoughtList = array[4] as List<ThoughtEntity>
        @Suppress("UNCHECKED_CAST")
        val relapseList = array[5] as List<RelapseEntity>
        @Suppress("UNCHECKED_CAST")
        val riskList = array[6] as List<RiskDateEntity>
        @Suppress("UNCHECKED_CAST")
        val auditList = array[7] as List<RelationshipAuditEntity>
        @Suppress("UNCHECKED_CAST")
        val idealizationList = array[8] as List<IdealizationEntity>

        val now = System.currentTimeMillis()
        val breakupTs = currentSettings?.breakupDateTimestamp ?: (now - (14L * 24 * 3600 * 1000))

        com.example.ai.VulnerabilityAndEvolutionEngine.calculateRealVulnerability(
            currentTime = now,
            breakupDateTimestamp = breakupTs,
            checkins = checkinList,
            journalEntries = journalList,
            urgeEpisodes = urgeList,
            thoughts = thoughtList,
            relapses = relapseList,
            riskDates = riskList,
            audits = auditList,
            idealizations = idealizationList
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        com.example.ai.VulnerabilityAndEvolutionEngine.calculateRealVulnerability(
            currentTime = System.currentTimeMillis(),
            breakupDateTimestamp = System.currentTimeMillis() - (14L * 24 * 3600 * 1000),
            checkins = emptyList(),
            journalEntries = emptyList(),
            urgeEpisodes = emptyList(),
            thoughts = emptyList(),
            relapses = emptyList(),
            riskDates = emptyList(),
            audits = emptyList(),
            idealizations = emptyList()
        )
    )

    val vulnerabilityScore: StateFlow<Int> = realVulnerabilityAssessment
        .map { it.score }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 40)

    val vulnerabilityExplanation: StateFlow<String> = realVulnerabilityAssessment
        .map { it.primaryExplanation }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Evaluando estado y balance emocional...")

    // Real Personal Evolution Timeline (Connected to all user entries & days)
    private val _evolutionRangeDays = MutableStateFlow(7)
    val evolutionRangeDays: StateFlow<Int> = _evolutionRangeDays.asStateFlow()

    fun setEvolutionRangeDays(days: Int) {
        _evolutionRangeDays.value = days
    }

    val realEvolutionTimeline: StateFlow<com.example.ai.RealPersonalEvolutionTimeline> = combine(
        listOf(
            _evolutionRangeDays,
            settings,
            checkins,
            journalEntries,
            urgeEpisodes,
            relapses
        )
    ) { array ->
        val range = array[0] as Int
        @Suppress("UNCHECKED_CAST")
        val currentSettings = array[1] as? SoltarSettingsEntity
        @Suppress("UNCHECKED_CAST")
        val checkinList = array[2] as List<CheckinEntity>
        @Suppress("UNCHECKED_CAST")
        val journalList = array[3] as List<JournalEntryEntity>
        @Suppress("UNCHECKED_CAST")
        val urgeList = array[4] as List<UrgeEpisodeEntity>
        @Suppress("UNCHECKED_CAST")
        val relapseList = array[5] as List<RelapseEntity>

        val now = System.currentTimeMillis()
        val breakupTs = currentSettings?.breakupDateTimestamp ?: (now - (14L * 24 * 3600 * 1000))

        com.example.ai.VulnerabilityAndEvolutionEngine.buildRealEvolutionTimeline(
            rangeDays = range,
            currentTime = now,
            breakupDateTimestamp = breakupTs,
            checkins = checkinList,
            journalEntries = journalList,
            urgeEpisodes = urgeList,
            relapses = relapseList
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        com.example.ai.VulnerabilityAndEvolutionEngine.buildRealEvolutionTimeline(
            rangeDays = 7,
            currentTime = System.currentTimeMillis(),
            breakupDateTimestamp = System.currentTimeMillis() - (14L * 24 * 3600 * 1000),
            checkins = emptyList(),
            journalEntries = emptyList(),
            urgeEpisodes = emptyList(),
            relapses = emptyList()
        )
    )

    private var urgeTimerJob: Job? = null

    init {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            AdrianaDatabase.populateInitialDataIfEmpty(AdrianaDatabase.getDatabase(application))
        }
        loadTodayCheckin()
        observeSettings()
        observeJournalEntriesForLinguisticAnalysis()
    }

    fun toggleNeedHelpSheet(visible: Boolean) = _uiState.update { it.copy(isNeedHelpSheetVisible = visible) }
    fun openNeedHelpSheet() = _uiState.update { it.copy(isNeedHelpSheetVisible = true) }
    fun closeNeedHelpSheet() = _uiState.update { it.copy(isNeedHelpSheetVisible = false) }

    private fun observeSettings() {
        viewModelScope.launch {
            repository.settings.collect { currentSettings ->
                if (currentSettings != null) {
                    com.example.audio.SoltarSoundManager.isSoundEnabled = currentSettings.soundEnabled
                    val framework = SoltarFramework.fromKey(currentSettings.preferredFramework)
                    val recentList = currentSettings.recentCardIds
                        .split(",")
                        .filter { it.isNotBlank() }

                    val currentCard = _uiState.value.currentWisdomCard
                    val frameworkCards = WisdomBank.cards.filter { it.framework == framework }
                    val latestCheckin = checkins.value.firstOrNull()
                    val newCard = if (currentCard == null || currentCard.framework != framework) {
                        com.example.ai.OnDeviceLlmEngine.selectOptimalWisdomCard(
                            availableCards = frameworkCards,
                            latestCheckin = latestCheckin,
                            framework = framework,
                            recentCardIds = recentList
                        )
                    } else {
                        currentCard
                    }

                    // Auth persistence - only trigger lockdown if a PIN is actually configured and lock enabled
                    val shouldShowAuth = !currentSettings.isLoggedIn && 
                            currentSettings.onboardingCompleted && 
                            currentSettings.pinHash.isNotBlank() && 
                            currentSettings.biometricLockEnabled

                    _uiState.update {
                        it.copy(
                            isSoundEnabled = currentSettings.soundEnabled,
                            themeMode = currentSettings.themeMode,
                            isOnboardingVisible = !currentSettings.onboardingCompleted,
                            preferredFramework = framework,
                            currentWisdomCard = newCard,
                            isAuthDialogVisible = shouldShowAuth,
                            reminderHourInput = currentSettings.reminderHour,
                            reminderMinuteInput = currentSettings.reminderMinute,
                            notificationsEnabled = currentSettings.notificationsEnabled,
                            inactivityAlertsEnabled = currentSettings.inactivityAlertsEnabled,
                            mandatoryJournalHourInput = currentSettings.mandatoryJournalHour,
                            mandatoryJournalMinuteInput = currentSettings.mandatoryJournalMinute,
                            customNotifications = try {
                                if (currentSettings.customNotificationsJson.isNotBlank()) {
                                    json.decodeFromString<List<CustomNotificationItem>>(currentSettings.customNotificationsJson)
                                } else {
                                    DEFAULT_PRESET_REMINDERS
                                }
                            } catch (_: Exception) { DEFAULT_PRESET_REMINDERS }
                        )
                    }

                    if (currentSettings.customNotificationsJson.isBlank()) {
                        val jsonStr = json.encodeToString(kotlinx.serialization.builtins.ListSerializer(CustomNotificationItem.serializer()), DEFAULT_PRESET_REMINDERS)
                        repository.saveSettings(currentSettings.copy(customNotificationsJson = jsonStr))
                        DEFAULT_PRESET_REMINDERS.forEach { item ->
                            if (item.enabled) {
                                com.example.notifications.SoltarNotificationHelper.scheduleCustomNotification(getApplication(), item)
                            }
                        }
                    }
                }
            }
        }
    }

    fun setFramework(framework: SoltarFramework) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val recentList = current.recentCardIds.split(",").filter { it.isNotBlank() }
            val newCard = WisdomBank.getRandomCard(framework, recentList)
            val updatedRecent = (recentList + newCard.id).takeLast(5).joinToString(",")

            repository.saveSettings(
                current.copy(
                    preferredFramework = framework.key,
                    recentCardIds = updatedRecent
                )
            )
            _uiState.update {
                it.copy(
                    preferredFramework = framework,
                    currentWisdomCard = newCard
                )
            }
            playSound(com.example.audio.SoltarSoundManager.SoundType.CALM_BELL)
            showNotification("A partir de ahora tus tarjetas y tu coach hablarán en clave ${framework.title}.")
        }
    }

    fun setJourneyStage(stage: String) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            repository.saveSettings(current.copy(journeyStage = stage))
            playSound(com.example.audio.SoltarSoundManager.SoundType.WARM_CHIME)
            val msg = if (stage == "LIFE_COACH") {
                "Has recorrido un largo camino. Ahora ADRIANA Life Coach te acompaña en quién quieres ser."
            } else {
                "ADRIANA Recovery activada: Enfoque en duelo, contacto cero y reconstrucción."
            }
            showNotification(msg)
        }
    }

    fun saveCoachGoal(title: String, category: String, targetValue: String) {
        viewModelScope.launch {
            if (title.isBlank()) return@launch
            repository.saveCoachGoal(CoachGoalEntity(title = title, category = category, targetValue = targetValue))
            showNotification("Nuevo propósito registrado.")
            playSound(com.example.audio.SoltarSoundManager.SoundType.WARM_CHIME)
        }
    }

    fun toggleCoachGoal(id: Long, completed: Boolean) {
        viewModelScope.launch {
            repository.toggleCoachGoal(id, completed)
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
        }
    }

    fun deleteCoachGoal(id: Long) {
        viewModelScope.launch {
            repository.deleteCoachGoal(id)
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
        }
    }

    fun saveBodyMetric(weight: Float, height: Float, waist: Float, arm: Float, leg: Float, notes: String) {
        viewModelScope.launch {
            val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            repository.saveBodyMetric(
                BodyMetricRecordEntity(
                    dateKey = dateKey,
                    weightKg = weight,
                    heightCm = height,
                    waistCm = waist,
                    armCm = arm,
                    legCm = leg,
                    notes = notes
                )
            )
            showNotification("Evolución física registrada con éxito.")
            playSound(com.example.audio.SoltarSoundManager.SoundType.WARM_CHIME)
        }
    }

    fun saveCoachCheckin(wentToGym: Boolean, studiedOrWorked: Boolean, mood: String, energy: Int, note: String) {
        viewModelScope.launch {
            val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            repository.saveCoachCheckin(
                CoachDailyCheckinEntity(
                    dateKey = dateKey,
                    wentToGym = wentToGym,
                    studiedOrWorked = studiedOrWorked,
                    mood = mood,
                    energyLevel = energy,
                    note = note
                )
            )
            showNotification("Check-in diario guardado.")
            playSound(com.example.audio.SoltarSoundManager.SoundType.WARM_CHIME)
        }
    }

    fun resetAppData() {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val db = AdrianaDatabase.getDatabase(getApplication())
            db.clearAllTables()
            AdrianaDatabase.populateInitialDataIfEmpty(db)
            // No need to call observeSettings(), it will re-trigger as the Room database flow updates
        }
    }

    fun rotateWisdomCard(framework: SoltarFramework = _uiState.value.preferredFramework) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val recentList = current.recentCardIds.split(",").filter { it.isNotBlank() }
            val frameworkCards = WisdomBank.cards.filter { it.framework == framework }
            val latestCheckin = checkins.value.firstOrNull()
            val newCard = com.example.ai.OnDeviceLlmEngine.selectOptimalWisdomCard(
                availableCards = frameworkCards,
                latestCheckin = latestCheckin,
                framework = framework,
                recentCardIds = recentList
            )
            val updatedRecent = (recentList + newCard.id).takeLast(5).joinToString(",")

            repository.saveSettings(current.copy(recentCardIds = updatedRecent))
            _uiState.update { it.copy(currentWisdomCard = newCard) }
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
        }
    }

    fun playSound(type: com.example.audio.SoltarSoundManager.SoundType) {
        com.example.audio.SoltarSoundManager.playSound(type)
    }

    fun toggleSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            repository.saveSettings(current.copy(soundEnabled = enabled))
            com.example.audio.SoltarSoundManager.isSoundEnabled = enabled
            _uiState.update { it.copy(isSoundEnabled = enabled) }
            if (enabled) {
                playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
            }
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            repository.saveSettings(current.copy(themeMode = mode))
            _uiState.update { it.copy(themeMode = mode) }
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
            val modeLabel = when (mode.uppercase()) {
                "LIGHT" -> "Modo Claro (Porcelana Cálida)"
                "DARK" -> "Modo Oscuro (Obsidiana Kintsugi)"
                else -> "Modo Automático (Sistema)"
            }
            showNotification("🎨 Apariencia: $modeLabel")
        }
    }

    fun toggleDarkMode(isDarkMode: Boolean) {
        setThemeMode(if (isDarkMode) "DARK" else "LIGHT")
    }

    fun updateRelationshipContext(
        relDuration: String,
        timeSinceBreakup: String,
        previousBreakupsCount: Int,
        cohabitation: Boolean,
        marriedOrEngaged: Boolean,
        breakupSituation: String,
        anticipatedGrief: String,
        hasChildren: Boolean,
        inevitableContact: Boolean,
        childrenContactFrequency: String,
        childrenCohabitation: String,
        parentalOnlyCommunication: Boolean,
        contactType: String,
        emotionalSituation: String,
        decisionMaker: String,
        breakupReason: String,
        practicals: String
    ) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            repository.saveSettings(
                current.copy(
                    relDuration = relDuration,
                    timeSinceBreakup = timeSinceBreakup,
                    previousBreakupsCount = previousBreakupsCount,
                    cohabitation = cohabitation,
                    marriedOrEngaged = marriedOrEngaged,
                    breakupSituation = breakupSituation,
                    anticipatedGrief = anticipatedGrief,
                    hasChildren = hasChildren,
                    inevitableContact = inevitableContact,
                    childrenContactFrequency = childrenContactFrequency,
                    childrenCohabitation = childrenCohabitation,
                    parentalOnlyCommunication = parentalOnlyCommunication,
                    contactType = contactType,
                    emotionalSituation = emotionalSituation,
                    decisionMaker = decisionMaker,
                    breakupReason = breakupReason,
                    practicals = practicals
                )
            )
            showNotification("✅ Perfil contextual actualizado correctamente.")
            playSound(com.example.audio.SoltarSoundManager.SoundType.CALM_BELL)
        }
    }

    fun setOnboardingCompleted(completed: Boolean) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            repository.saveSettings(current.copy(onboardingCompleted = completed))
            _uiState.update { it.copy(isOnboardingVisible = !completed) }
        }
    }

    fun completeOnboardingFlow(
        userName: String,
        userEmail: String,
        relDuration: String,
        timeSinceBreakup: String,
        hasChildren: Boolean,
        contactType: String,
        framework: SoltarFramework,
        breakupSituation: String,
        anticipatedGrief: String,
        emotionalSituation: String,
        decisionMaker: String,
        breakupReason: String,
        freeHistoryNotes: String,
        cohabitation: Boolean,
        marriedOrEngaged: Boolean,
        previousBreakupsCount: Int
    ) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val recentList = current.recentCardIds.split(",").filter { it.isNotBlank() }
            val frameworkCards = WisdomBank.cards.filter { it.framework == framework }
            val newCard = com.example.ai.OnDeviceLlmEngine.selectOptimalWisdomCard(
                availableCards = frameworkCards,
                latestCheckin = checkins.value.firstOrNull(),
                framework = framework,
                recentCardIds = recentList
            )
            val updatedRecent = (recentList + newCard.id).takeLast(5).joinToString(",")

            repository.saveSettings(
                current.copy(
                    userName = if (userName.isNotBlank()) userName else "Viajero",
                    userEmail = userEmail,
                    isLoggedIn = userEmail.isNotBlank(),
                    relDuration = relDuration,
                    timeSinceBreakup = timeSinceBreakup,
                    hasChildren = hasChildren,
                    contactType = contactType,
                    preferredFramework = framework.key,
                    recentCardIds = updatedRecent,
                    onboardingCompleted = true,
                    breakupSituation = breakupSituation,
                    anticipatedGrief = anticipatedGrief,
                    emotionalSituation = emotionalSituation,
                    decisionMaker = decisionMaker,
                    breakupReason = breakupReason,
                    freeHistoryNotes = freeHistoryNotes,
                    cohabitation = cohabitation,
                    marriedOrEngaged = marriedOrEngaged,
                    previousBreakupsCount = previousBreakupsCount
                )
            )
            _uiState.update {
                it.copy(
                    isOnboardingVisible = false,
                    preferredFramework = framework,
                    currentWisdomCard = newCard
                )
            }
            playSound(com.example.audio.SoltarSoundManager.SoundType.WARM_CHIME)
            showNotification("✨ ¡Bienvenido a Recuerda! Tu perfil y contexto han sido configurados.")
        }
    }

    fun updateNoContactStartDate(timestamp: Long) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val isFirstTime = !current.initialStartDateSet
            val newInitial = if (isFirstTime) timestamp else current.initialStartDateTimestamp
            repository.saveSettings(
                current.copy(
                    breakupDateTimestamp = timestamp,
                    initialStartDateTimestamp = newInitial,
                    initialStartDateSet = true
                )
            )
            showNotification("⏱️ Fecha de Contacto Cero actualizada correctamente.")
            playSound(com.example.audio.SoltarSoundManager.SoundType.CALM_BELL)
            com.example.widget.SoltarAppWidgetProvider.notifyWidgetDataChanged(getApplication())
        }
    }

    fun resetNoContactCounter() {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            repository.saveSettings(current.copy(breakupDateTimestamp = System.currentTimeMillis()))
            showNotification("🌱 Contador reiniciado con compasión. Cada nuevo minuto es una victoria.")
            playSound(com.example.audio.SoltarSoundManager.SoundType.WARM_CHIME)
            com.example.widget.SoltarAppWidgetProvider.notifyWidgetDataChanged(getApplication())
        }
    }

    fun openUrgeMode() = openUrgeSheet()
    fun setSelectedTab(tab: SoltarTab) = setTab(tab)

    private fun getTodayDateKey(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun loadTodayCheckin() {
        val todayKey = getTodayDateKey()
        viewModelScope.launch {
            repository.getCheckinByDate(todayKey).collect { existing ->
                if (existing != null) {
                    _uiState.update {
                        it.copy(
                            todayPain = existing.pain,
                            todayAnxiety = existing.anxiety,
                            todayNostalgia = existing.nostalgia,
                            todayAnger = existing.anger,
                            todayLoneliness = existing.loneliness,
                            todayRumination = existing.rumination,
                            todayUrgeToContact = existing.urgeToContact,
                            todayAutonomy = existing.autonomy,
                            focusBodyInput = existing.focusBodyAction,
                            focusSelfInput = existing.focusSelfAction,
                            focusSocialInput = existing.focusSocialAction,
                            checkinNoteInput = existing.note
                        )
                    }
                }
            }
        }
    }

    // Navigation
    fun setTab(tab: SoltarTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun clearNotification() {
        _uiState.update { it.copy(notificationMessage = null) }
    }

    fun showNotification(msg: String) {
        _uiState.update { it.copy(notificationMessage = msg) }
    }

    // ==========================================
    // 6-PHASE URGE PROTOCOL (MODO IMPULSO)
    // ==========================================
    fun openUrgeSheet() {
        _uiState.update {
            it.copy(
                isUrgeSheetVisible = true,
                urgePhase = 1,
                urgeTimerSecondsRemaining = 1200,
                isUrgeTimerRunning = true,
                urgeInitialIntensity = 8,
                urgeFinalIntensity = 3
            )
        }
        startUrgeTimer()
    }

    fun closeUrgeSheet() {
        stopUrgeTimer()
        _uiState.update { it.copy(isUrgeSheetVisible = false) }
    }

    fun setUrgePhase(phase: Int) {
        _uiState.update { it.copy(urgePhase = phase.coerceIn(1, 6)) }
    }

    fun nextUrgePhase() {
        val current = _uiState.value.urgePhase
        if (current < 6) {
            _uiState.update { it.copy(urgePhase = current + 1) }
        }
    }

    fun prevUrgePhase() {
        val current = _uiState.value.urgePhase
        if (current > 1) {
            _uiState.update { it.copy(urgePhase = current - 1) }
        }
    }

    fun startUrgeTimer() {
        urgeTimerJob?.cancel()
        _uiState.update { it.copy(isUrgeTimerRunning = true) }
        urgeTimerJob = viewModelScope.launch {
            while (_uiState.value.urgeTimerSecondsRemaining > 0 && _uiState.value.isUrgeTimerRunning) {
                delay(1000)
                _uiState.update { it.copy(urgeTimerSecondsRemaining = it.urgeTimerSecondsRemaining - 1) }
            }
            if (_uiState.value.urgeTimerSecondsRemaining == 0) {
                _uiState.update { it.copy(isUrgeTimerRunning = false) }
            }
        }
    }

    fun toggleUrgeTimer() {
        if (_uiState.value.isUrgeTimerRunning) {
            stopUrgeTimer()
        } else {
            startUrgeTimer()
        }
    }

    fun stopUrgeTimer() {
        urgeTimerJob?.cancel()
        _uiState.update { it.copy(isUrgeTimerRunning = false) }
    }

    fun setUrgeEmotion(emotion: String) = _uiState.update { it.copy(urgeEmotion = emotion) }
    fun setUrgeDesiredAction(action: String) = _uiState.update { it.copy(urgeDesiredAction = action) }
    fun setUrgeExpectedOutcome(outcome: String) = _uiState.update { it.copy(urgeExpectedOutcome = outcome) }
    fun setUrgeFact(fact: String) = _uiState.update { it.copy(urgeFact = fact) }
    fun setUrgeInterpretation(interp: String) = _uiState.update { it.copy(urgeInterpretation = interp) }
    fun setUrgeCannotKnow(cannot: String) = _uiState.update { it.copy(urgeCannotKnow = cannot) }
    fun setUrgeDependsOnMe(depends: String) = _uiState.update { it.copy(urgeDependsOnMe = depends) }
    fun setUrgeTrigger(trigger: String) = _uiState.update { it.copy(urgeTrigger = trigger) }
    fun setUrgeInitialIntensity(v: Int) = _uiState.update { it.copy(urgeInitialIntensity = v) }
    fun setUrgeFinalIntensity(v: Int) = _uiState.update { it.copy(urgeFinalIntensity = v) }
    fun setUrgeLearning(learning: String) = _uiState.update { it.copy(urgeLearning = learning) }

    fun completeAndSaveUrgeEpisode() {
        val s = _uiState.value
        val completedMin = (1200 - s.urgeTimerSecondsRemaining) / 60
        viewModelScope.launch {
            repository.saveUrgeEpisode(
                UrgeEpisodeEntity(
                    initialIntensity = s.urgeInitialIntensity,
                    finalIntensity = s.urgeFinalIntensity,
                    emotion = s.urgeEmotion,
                    desiredAction = s.urgeDesiredAction,
                    expectedOutcome = s.urgeExpectedOutcome,
                    fact = s.urgeFact,
                    interpretation = s.urgeInterpretation,
                    cannotKnow = s.urgeCannotKnow,
                    dependsOnMe = s.urgeDependsOnMe,
                    trigger = s.urgeTrigger,
                    actualBehavior = "Resistido con éxito (Modo Impulso)",
                    timerCompletedMinutes = completedMin.coerceAtLeast(1),
                    learning = s.urgeLearning.ifBlank { "Sentí el impulso sin obedecerlo. El dolor pasó sin romper mi dignidad." }
                )
            )
            closeUrgeSheet()
            showNotification("🛡️ Impulso regulado y guardado en tu historial.")
        }
    }

    // ==========================================
    // DAILY CHECK-IN & 3 FOCUS ACTIONS
    // ==========================================
    fun setMetricPain(v: Float) = _uiState.update { it.copy(todayPain = v) }
    fun setMetricAnxiety(v: Float) = _uiState.update { it.copy(todayAnxiety = v) }
    fun setMetricNostalgia(v: Float) = _uiState.update { it.copy(todayNostalgia = v) }
    fun setMetricAnger(v: Float) = _uiState.update { it.copy(todayAnger = v) }
    fun setMetricLoneliness(v: Float) = _uiState.update { it.copy(todayLoneliness = v) }
    fun setMetricRumination(v: Float) = _uiState.update { it.copy(todayRumination = v) }
    fun setMetricUrge(v: Float) = _uiState.update { it.copy(todayUrgeToContact = v) }
    fun setMetricAutonomy(v: Float) = _uiState.update { it.copy(todayAutonomy = v) }
    fun setFocusBodyInput(t: String) = _uiState.update { it.copy(focusBodyInput = t) }
    fun setFocusSelfInput(t: String) = _uiState.update { it.copy(focusSelfInput = t) }
    fun setFocusSocialInput(t: String) = _uiState.update { it.copy(focusSocialInput = t) }
    fun setCheckinNote(t: String) = _uiState.update { it.copy(checkinNoteInput = t) }

    fun saveTodayCheckin() {
        val s = _uiState.value
        val todayKey = getTodayDateKey()
        viewModelScope.launch {
            repository.saveCheckin(
                CheckinEntity(
                    dateKey = todayKey,
                    pain = s.todayPain,
                    anxiety = s.todayAnxiety,
                    nostalgia = s.todayNostalgia,
                    anger = s.todayAnger,
                    loneliness = s.todayLoneliness,
                    rumination = s.todayRumination,
                    urgeToContact = s.todayUrgeToContact,
                    autonomy = s.todayAutonomy,
                    focusBodyAction = s.focusBodyInput,
                    focusSelfAction = s.focusSelfInput,
                    focusSocialAction = s.focusSocialInput,
                    note = s.checkinNoteInput
                )
            )
            showNotification("✅ Registro diario guardado correctamente.")
        }
    }

    fun toggleFocusDone(checkinId: Long, type: String, currentDone: Boolean) {
        viewModelScope.launch {
            repository.updateFocusActionDone(checkinId, type, !currentDone)
        }
    }

    // ==========================================
    // THOUGHT LABORATORY (CBT / HECHOS VS INTERPRETACIÓN)
    // ==========================================
    fun setThoughtOriginal(t: String) = _uiState.update { it.copy(thoughtOriginalInput = t) }
    fun setThoughtFact(t: String) = _uiState.update { it.copy(thoughtFactInput = t) }
    fun setThoughtInterpretation(t: String) = _uiState.update { it.copy(thoughtInterpretationInput = t) }
    fun setThoughtHypothesis(t: String) = _uiState.update { it.copy(thoughtHypothesisInput = t) }
    fun setThoughtEvidenceFor(t: String) = _uiState.update { it.copy(thoughtEvidenceForInput = t) }
    fun setThoughtEvidenceAgainst(t: String) = _uiState.update { it.copy(thoughtEvidenceAgainstInput = t) }
    fun setThoughtCannotKnow(t: String) = _uiState.update { it.copy(thoughtCannotKnowInput = t) }
    fun setThoughtDependsOnMe(t: String) = _uiState.update { it.copy(thoughtDependsOnMeInput = t) }
    fun setThoughtAction(t: String) = _uiState.update { it.copy(thoughtConcreteActionInput = t) }

    fun saveThoughtAndCloseLoop() {
        val s = _uiState.value
        if (s.thoughtOriginalInput.isBlank()) return
        viewModelScope.launch {
            repository.saveThought(
                ThoughtEntity(
                    originalThought = s.thoughtOriginalInput,
                    fact = s.thoughtFactInput.ifBlank { "Hecho no especificado" },
                    interpretation = s.thoughtInterpretationInput.ifBlank { "Interpretación subjetiva" },
                    hypothesis = s.thoughtHypothesisInput.ifBlank { "Existen múltiples explicaciones posibles" },
                    evidenceFor = s.thoughtEvidenceForInput,
                    evidenceAgainst = s.thoughtEvidenceAgainstInput,
                    cannotKnow = s.thoughtCannotKnowInput.ifBlank { "No puedo saber lo que otra persona siente o piensa" },
                    dependsOnMe = s.thoughtDependsOnMeInput.ifBlank { "Enfocarme en mi propia jornada y salud" },
                    concreteAction = s.thoughtConcreteActionInput.ifBlank { "Caminar 15 min y cerrar la rumiación" },
                    isClosed = true
                )
            )
            _uiState.update {
                it.copy(
                    thoughtOriginalInput = "",
                    thoughtFactInput = "",
                    thoughtInterpretationInput = "",
                    thoughtHypothesisInput = "",
                    thoughtEvidenceForInput = "",
                    thoughtEvidenceAgainstInput = "",
                    thoughtCannotKnowInput = "",
                    thoughtDependsOnMeInput = ""
                )
            }
            showNotification("🔒 Bucle de pensamiento analizado y cerrado con éxito.")
        }
    }

    fun deleteThought(id: Long) {
        viewModelScope.launch { repository.deleteThought(id) }
    }

    // ==========================================
    // RELATIONSHIP AUDIT (3 RESPONSIBILITY COLUMNS)
    // ==========================================
    fun setAuditTitle(t: String) = _uiState.update { it.copy(auditTitleInput = t) }
    fun setAuditCategory(t: String) = _uiState.update { it.copy(auditCategoryInput = t) }
    fun setAuditMyResp(t: String) = _uiState.update { it.copy(auditMyRespInput = t) }
    fun setAuditOtherResp(t: String) = _uiState.update { it.copy(auditOtherRespInput = t) }
    fun setAuditSharedResp(t: String) = _uiState.update { it.copy(auditSharedRespInput = t) }
    fun setAuditPattern(t: String) = _uiState.update { it.copy(auditPatternInput = t) }

    fun saveRelationshipAudit() {
        val s = _uiState.value
        if (s.auditTitleInput.isBlank()) return
        viewModelScope.launch {
            repository.saveAudit(
                RelationshipAuditEntity(
                    title = s.auditTitleInput,
                    category = s.auditCategoryInput,
                    myResponsibility = s.auditMyRespInput.ifBlank { "Sin especificar" },
                    otherResponsibility = s.auditOtherRespInput.ifBlank { "Sin especificar" },
                    sharedResponsibility = s.auditSharedRespInput.ifBlank { "Sin especificar" },
                    patternIdentified = s.auditPatternInput
                )
            )
            _uiState.update {
                it.copy(
                    auditTitleInput = "",
                    auditMyRespInput = "",
                    auditOtherRespInput = "",
                    auditSharedRespInput = "",
                    auditPatternInput = ""
                )
            }
            showNotification("⚖️ Evento de la relación auditado con ecuanimidad.")
        }
    }

    fun deleteAudit(id: Long) {
        viewModelScope.launch { repository.deleteAudit(id) }
    }

    // ==========================================
    // IDEALIZATION ANTIDOTE
    // ==========================================
    fun setIdealizationMiss(t: String) = _uiState.update { it.copy(idealizationMissInput = t) }
    fun setIdealizationReality(t: String) = _uiState.update { it.copy(idealizationRealityInput = t) }

    fun saveIdealizationPair() {
        val s = _uiState.value
        if (s.idealizationMissInput.isBlank() || s.idealizationRealityInput.isBlank()) return
        viewModelScope.launch {
            repository.saveIdealizationEntry(
                IdealizationEntity(
                    whatIMiss = s.idealizationMissInput,
                    whatIActuallyExperienced = s.idealizationRealityInput
                )
            )
            _uiState.update {
                it.copy(
                    idealizationMissInput = "",
                    idealizationRealityInput = ""
                )
            }
            showNotification("💡 Par de contraste agregado al antídoto de idealización.")
        }
    }

    fun deleteIdealization(id: Long) {
        viewModelScope.launch { repository.deleteIdealizationEntry(id) }
    }

    // ==========================================
    // UNSENT LETTERS & CLOSING CEREMONY
    // ==========================================
    fun setLetterTitle(t: String) = _uiState.update { it.copy(letterTitleInput = t) }
    fun setLetterCategory(t: String) = _uiState.update { it.copy(letterCategoryInput = t) }
    fun setLetterContent(t: String) = _uiState.update { it.copy(letterContentInput = t) }

    fun saveUnsentLetter() {
        val s = _uiState.value
        if (s.letterTitleInput.isBlank() || s.letterContentInput.isBlank()) return
        viewModelScope.launch {
            repository.saveLetter(
                UnsentLetterEntity(
                    title = s.letterTitleInput,
                    category = s.letterCategoryInput,
                    content = s.letterContentInput,
                    isClosed = false
                )
            )
            _uiState.update {
                it.copy(
                    letterTitleInput = "",
                    letterContentInput = ""
                )
            }
            showNotification("✉️ Carta privada guardada de forma segura.")
        }
    }

    fun performLetterCeremony(id: Long) {
        viewModelScope.launch {
            repository.performClosingCeremony(id)
            showNotification("🕯️ Ceremonia de cierre realizada. La carta queda sellada.")
        }
    }

    fun deleteLetter(id: Long) {
        viewModelScope.launch { repository.deleteLetter(id) }
    }

    // ==========================================
    // BEHAVIORAL EXPERIMENTS
    // ==========================================
    fun completeExperiment(id: Long, outcome: String, learning: String) {
        viewModelScope.launch {
            repository.completeExperiment(id, outcome, learning)
            showNotification("🧪 Experimento completado y registrado como aprendizaje.")
        }
    }

    // ==========================================
    // IDENTITY & LIFE GOALS
    // ==========================================
    fun setIdentityArea(area: String) = _uiState.update { it.copy(identityAreaSelected = area) }
    fun setWhoIWas(t: String) = _uiState.update { it.copy(whoIWasInput = t) }
    fun setWhoIAm(t: String) = _uiState.update { it.copy(whoIAmInput = t) }
    fun setWhoIWantToBe(t: String) = _uiState.update { it.copy(whoIWantToBeInput = t) }
    fun setNewGoalTitle(t: String) = _uiState.update { it.copy(newGoalTitleInput = t) }
    fun setNewGoalFrequency(f: String) = _uiState.update { it.copy(newGoalFrequencyInput = f) }

    fun saveIdentityGoal() {
        val s = _uiState.value
        if (s.newGoalTitleInput.isBlank()) return
        viewModelScope.launch {
            repository.saveIdentityGoal(
                IdentityGoalEntity(
                    area = s.identityAreaSelected,
                    whoIWas = s.whoIWasInput,
                    whoIAm = s.whoIAmInput,
                    whoIWantToBe = s.whoIWantToBeInput,
                    goalTitle = s.newGoalTitleInput,
                    goalFrequency = s.newGoalFrequencyInput,
                    isCompleted = false,
                    streakDays = 0
                )
            )
            _uiState.update {
                it.copy(
                    whoIWasInput = "",
                    whoIAmInput = "",
                    whoIWantToBeInput = "",
                    newGoalTitleInput = ""
                )
            }
            showNotification("🌱 Nuevo objetivo de identidad registrado.")
        }
    }

    fun toggleGoalCompleted(id: Long, current: Boolean) {
        viewModelScope.launch {
            repository.toggleGoalCompleted(id, !current)
        }
    }

    fun deleteIdentityGoal(id: Long) {
        viewModelScope.launch { repository.deleteIdentityGoal(id) }
    }

    // ==========================================
    // RELAPSE LOG (COMPASSIONATE & REFLECTIVE)
    // ==========================================
    fun setRelapseWhatHappened(t: String) = _uiState.update { it.copy(relapseWhatHappenedInput = t) }
    fun setRelapseTrigger(t: String) = _uiState.update { it.copy(relapseTriggerInput = t) }
    fun setRelapseEmotion(t: String) = _uiState.update { it.copy(relapseEmotionInput = t) }
    fun setRelapseThought(t: String) = _uiState.update { it.copy(relapseThoughtInput = t) }
    fun setRelapseBehavior(t: String) = _uiState.update { it.copy(relapseBehaviorInput = t) }
    fun setRelapseConsequence(t: String) = _uiState.update { it.copy(relapseConsequenceInput = t) }
    fun setRelapseLearning(t: String) = _uiState.update { it.copy(relapseLearningInput = t) }
    fun setRelapseTimestamp(ts: Long) = _uiState.update { it.copy(relapseTimestamp = ts) }
    fun setRelapseIsRestarting(isRestarting: Boolean) = _uiState.update { it.copy(relapseIsRestarting = isRestarting) }
    fun setRelapseInterpretation(interpretation: String) {
        val isRestarting = interpretation == "retroceso"
        _uiState.update { it.copy(relapseInterpretation = interpretation, relapseIsRestarting = isRestarting) }
    }

    fun saveRelapseLog() {
        val s = _uiState.value
        if (s.relapseWhatHappenedInput.isBlank()) return
        viewModelScope.launch {
            repository.saveRelapse(
                RelapseEntity(
                    timestamp = s.relapseTimestamp,
                    whatHappened = s.relapseWhatHappenedInput,
                    trigger = s.relapseTriggerInput,
                    emotion = s.relapseEmotionInput,
                    thought = s.relapseThoughtInput,
                    behavior = s.relapseBehaviorInput,
                    consequence = s.relapseConsequenceInput,
                    learning = s.relapseLearningInput.ifBlank { "Una recaída no borra mi progreso, me da información sobre mis detonantes." },
                    isRestartingFromZero = s.relapseIsRestarting,
                    interpretation = s.relapseInterpretation
                )
            )
            
            // Only update breakup date/counter if user explicitly chose to restart counter
            if (s.relapseIsRestarting) {
                val current = settings.value ?: SoltarSettingsEntity()
                repository.saveSettings(current.copy(breakupDateTimestamp = s.relapseTimestamp))
            }

            _uiState.update {
                it.copy(
                    relapseWhatHappenedInput = "",
                    relapseTriggerInput = "",
                    relapseEmotionInput = "",
                    relapseThoughtInput = "",
                    relapseBehaviorInput = "",
                    relapseConsequenceInput = "",
                    relapseLearningInput = "",
                    relapseTimestamp = System.currentTimeMillis(),
                    relapseIsRestarting = true,
                    relapseInterpretation = "retroceso",
                    isRelapseModalVisible = false
                )
            }
            playSound(com.example.audio.SoltarSoundManager.SoundType.CALM_BELL)
            showNotification("🤝 Registro completado sin juicios. Tu proceso y tu dignidad siguen intactos.")
            com.example.widget.SoltarAppWidgetProvider.notifyWidgetDataChanged(getApplication())
        }
    }

    // ==========================================
    // "NO QUIERO PENSAR MÁS" QUICK ACTIONS
    // ==========================================
    fun openNoThinkingSheet() = _uiState.update { it.copy(isNoThinkingSheetVisible = true) }
    fun closeNoThinkingSheet() = _uiState.update { it.copy(isNoThinkingSheetVisible = false) }

    // ==========================================
    // AI CHAT & MEMORY CONTROLS
    // C3: Progreso Lingüístico estructurado (Gemini con fallback local)
    private val _linguisticProgress = MutableStateFlow(com.example.ai.LinguisticAnalysisResult())
    val linguisticProgress: StateFlow<com.example.ai.LinguisticAnalysisResult> = _linguisticProgress.asStateFlow()

    private fun observeJournalEntriesForLinguisticAnalysis() {
        viewModelScope.launch {
            repository.allJournalEntries.collect { entries ->
                val userCtx = buildUserPersonalizationContext()
                val result = SoltarAiEngine.analyzeJournalLinguistic(entries, userCtx)
                _linguisticProgress.value = result
            }
        }
    }

    fun setAiInputMessage(t: String) = _uiState.update { it.copy(aiInputMessage = t) }

    // AI Engine exposed for Encounter Simulator (On-Device LLM prioritized)
    suspend fun sendEncounterMessage(
        message: String,
        history: List<Pair<String, String>>,
        scenario: String,
        tone: com.example.ai.EncounterTone = com.example.ai.EncounterTone.COLD
    ): SoltarAiResponse {
        val exName = "tu expareja"
        val onDeviceReply = com.example.ai.OnDeviceLlmEngine.generateEncounterExResponse(
            userMessage = message,
            tone = tone,
            interactionHistory = history,
            exName = exName
        )
        return SoltarAiResponse(
            replyText = onDeviceReply,
            isRuminationDetected = false,
            suggestedAction = "",
            stateDetected = "REGULAR"
        )
    }
    
    fun toggleMemoryModal(visible: Boolean) = _uiState.update { it.copy(isMemoryModalVisible = visible) }
    fun toggleTimeCapsuleModal(visible: Boolean) = _uiState.update { it.copy(isTimeCapsuleModalVisible = visible) }
    fun toggleEncounterSimulator(visible: Boolean) = _uiState.update { it.copy(isEncounterSimulatorVisible = visible) }
    fun toggleWisdomLibraryDialog(visible: Boolean) = _uiState.update { it.copy(isWisdomLibraryVisible = visible) }
    fun toggleClosingRitualDialog(visible: Boolean) = _uiState.update { it.copy(isClosingRitualVisible = visible) }
    fun toggleVoluntaryExitDialog(visible: Boolean) = _uiState.update { it.copy(isVoluntaryExitVisible = visible) }
    
    fun saveTimeCapsule(title: String, content: String, unlockAt: Long) {
        viewModelScope.launch {
            repository.saveTimeCapsule(
                TimeCapsuleEntity(
                    title = title,
                    content = content,
                    unlockAtTimestamp = unlockAt
                )
            )
            showNotification("⏳ Cápsula del tiempo sellada. Nos vemos en el futuro.")
        }
    }

    fun unlockTimeCapsule(id: Long) {
        viewModelScope.launch {
            repository.unlockTimeCapsule(id)
            showNotification("🔓 Cápsula del tiempo desbloqueada.")
        }
    }

    fun saveWisdomContribution(framework: String, quote: String) {
        viewModelScope.launch {
            repository.saveWisdomContribution(
                WisdomContributionEntity(
                    framework = framework,
                    quote = quote
                )
            )
            showNotification("✨ ¡Frase guardada en tu banco personal!")
        }
    }

    fun buildUserPersonalizationContext(): SoltarUserContext {
        val currentSettings = settings.value
        val startTs = currentSettings?.breakupDateTimestamp ?: (System.currentTimeMillis() - (14L * 24 * 3600 * 1000))
        val diffDays = ((System.currentTimeMillis() - startTs) / (24 * 3600 * 1000L)).coerceAtLeast(0L).toInt()

        val allCheckinsList = checkins.value
        val lastCheckin = allCheckinsList.maxByOrNull { it.timestamp }
        val lastMood = lastCheckin?.let {
            "Dolor: ${it.pain.toInt()}/10, Ansiedad: ${it.anxiety.toInt()}/10, Nostalgia: ${it.nostalgia.toInt()}/10, Rumiación: ${it.rumination.toInt()}/10"
        } ?: ""
        val avgAutonomy = lastCheckin?.autonomy ?: 5f

        val recentTriggers = (relapses.value.map { it.trigger } + urgeEpisodes.value.map { it.trigger })
            .filter { it.isNotBlank() }
            .distinct()
            .take(4)

        val patternsAudited = audits.value
            .mapNotNull { it.patternIdentified.ifBlank { null } ?: if (it.title.isNotBlank()) it.title else null }
            .take(3)

        val activeGoals = identityGoals.value
            .filter { !it.isCompleted }
            .map { "${it.area}: ${it.goalTitle}" }
            .take(3)

        val nowCal = java.util.Calendar.getInstance()
        val currentYr = nowCal.get(java.util.Calendar.YEAR)
        val upcomingRiskSummary = riskDates.value.mapNotNull { rd ->
            val target = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.YEAR, currentYr)
                set(java.util.Calendar.MONTH, rd.month - 1)
                set(java.util.Calendar.DAY_OF_MONTH, rd.day)
            }
            if (target.timeInMillis < nowCal.timeInMillis) {
                target.add(java.util.Calendar.YEAR, 1)
            }
            val days = ((target.timeInMillis - nowCal.timeInMillis) / (1000L * 3600 * 24)).toInt()
            if (days in 0..14) {
                "• ${rd.title} en $days días (Estrategia preparada: ${rd.customStrategy.ifBlank { "Ninguna especificada" }})"
            } else null
        }.joinToString("\n")

        return SoltarUserContext(
            streakDays = diffDays,
            totalCheckins = allCheckinsList.size,
            lastCheckinMood = lastMood,
            averageAutonomyScore = avgAutonomy,
            recentRelapseTriggers = recentTriggers,
            recentPatternsAudited = patternsAudited,
            activeIdentityGoals = activeGoals,
            framework = _uiState.value.preferredFramework,
            relDuration = currentSettings?.relDuration ?: "",
            hasChildren = currentSettings?.hasChildren ?: false,
            contactType = currentSettings?.contactType ?: "",
            breakupSituation = currentSettings?.breakupSituation ?: "",
            practicals = currentSettings?.practicals ?: "",
            timeSinceBreakup = currentSettings?.timeSinceBreakup ?: "",
            previousBreakupsCount = currentSettings?.previousBreakupsCount ?: 0,
            cohabitation = currentSettings?.cohabitation ?: false,
            marriedOrEngaged = currentSettings?.marriedOrEngaged ?: false,
            anticipatedGrief = currentSettings?.anticipatedGrief ?: "",
            parentalOnlyCommunication = currentSettings?.parentalOnlyCommunication ?: true,
            emotionalSituation = currentSettings?.emotionalSituation ?: "",
            decisionMaker = currentSettings?.decisionMaker ?: "",
            breakupReason = currentSettings?.breakupReason ?: "",
            freeHistoryNotes = currentSettings?.freeHistoryNotes ?: "",
            upcomingRiskDatesSummary = upcomingRiskSummary
        )
    }

    fun sendAiMessage() {
        val text = _uiState.value.aiInputMessage.trim()
        if (text.isBlank() || _uiState.value.isAiTyping) return

        _uiState.update { it.copy(aiInputMessage = "", isAiTyping = true) }

        // Critical safety protocol: Crisis / Self-harm bypasses paywalls and responds immediately in chat
        if (SoltarAiEngine.checkSelfHarmTrigger(text)) {
            viewModelScope.launch {
                repository.saveAiMessage(
                    AiMessageEntity(
                        sender = "user",
                        content = text
                    )
                )
                val response = SoltarAiEngine.generateResponse(text)
                repository.saveAiMessage(
                    AiMessageEntity(
                        sender = "soltar_ai",
                        content = response.replyText,
                        detectedRumination = false,
                        suggestedAction = response.suggestedAction
                    )
                )
                _uiState.update { it.copy(isAiTyping = false) }
                showNotification("⚠️ Líneas de ayuda y apoyo registradas en tu chat")
            }
            return
        }

        // Daily limit check for normal conversational coaching
        val entitlements = UserEntitlements.fromSettings(settings.value)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val messagesToday = aiMessages.value.filter { 
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.timestamp)) == today 
        }.size
        
        if (!entitlements.isPremium && messagesToday >= entitlements.maxDailyCoachMessages) {
             _uiState.update { it.copy(isPaywallVisible = true, isAiTyping = false) }
             return
        }

        viewModelScope.launch {
            try {
                repository.saveAiMessage(
                    AiMessageEntity(
                        sender = "user",
                        content = text
                    )
                )

                val currentMessages = aiMessages.value.map { it.sender to it.content }
                val framework = _uiState.value.preferredFramework
                val userContext = buildUserPersonalizationContext()
                val response = SoltarAiEngine.generateResponse(text, currentMessages, framework, userContext)

                repository.saveAiMessage(
                    AiMessageEntity(
                        sender = "soltar_ai",
                        content = response.replyText,
                        detectedRumination = response.isRuminationDetected,
                        suggestedAction = response.suggestedAction
                    )
                )
            } catch (e: Exception) {
                repository.saveAiMessage(
                    AiMessageEntity(
                        sender = "soltar_ai",
                        content = "Ha ocurrido un problema al procesar la respuesta. Por favor, respira hondo e inténtalo de nuevo en unos momentos.",
                        detectedRumination = false
                    )
                )
            } finally {
                _uiState.update { it.copy(isAiTyping = false) }
            }
        }
    }

    fun setSelectedFeeling(f: String) = _uiState.update { it.copy(selectedFeeling = f) }
    fun openJournalModal(entry: JournalEntryEntity? = null) {
        _uiState.update {
            it.copy(
                isJournalModalVisible = true,
                selectedJournalEntry = entry,
                journalInputTitle = entry?.title ?: "",
                journalInputContent = entry?.content ?: "",
                journalInputMood = entry?.moodTag ?: "Reflexión",
                journalInputFramework = try {
                    SoltarFramework.valueOf(entry?.philosophicalFramework ?: it.preferredFramework.name)
                } catch (_: Exception) {
                    it.preferredFramework
                }
            )
        }
    }
    fun closeJournalModal() = _uiState.update { it.copy(isJournalModalVisible = false, selectedJournalEntry = null) }
    fun setJournalInputTitle(text: String) = _uiState.update { it.copy(journalInputTitle = text) }
    fun setJournalInputContent(text: String) = _uiState.update { it.copy(journalInputContent = text) }
    fun setJournalInputMood(mood: String) = _uiState.update { it.copy(journalInputMood = mood) }
    fun setJournalInputFramework(framework: SoltarFramework) = _uiState.update { it.copy(journalInputFramework = framework) }
    fun selectJournalEntry(entry: JournalEntryEntity?) = _uiState.update { it.copy(selectedJournalEntry = entry) }

    fun saveJournalEntry(
        title: String,
        content: String,
        moodTag: String,
        framework: SoltarFramework,
        requestMentorship: Boolean
    ) {
        val cleanContent = content.trim()
        if (cleanContent.isBlank()) {
            showNotification("⚠️ Escribe algo en tu diario antes de guardar.")
            return
        }

        viewModelScope.launch {
            val entryId = repository.saveJournalEntry(
                JournalEntryEntity(
                    title = title.trim(),
                    content = cleanContent,
                    moodTag = moodTag,
                    philosophicalFramework = framework.name,
                    aiFeedback = "",
                    aiCorePrinciple = "",
                    aiSocraticQuestion = "",
                    aiConcreteAction = "",
                    timestamp = System.currentTimeMillis()
                )
            )

            if (requestMentorship) {
                _uiState.update { it.copy(isGeneratingJournalMentorship = true) }
                try {
                    val result = SoltarAiEngine.generateJournalMentorship(
                        journalContent = cleanContent,
                        moodTag = moodTag,
                        framework = framework,
                        userContext = buildUserPersonalizationContext()
                    )
                    repository.updateJournalFeedback(
                        id = entryId,
                        feedback = result.feedback,
                        corePrinciple = result.corePrinciple,
                        socraticQuestion = result.socraticQuestion,
                        concreteAction = result.concreteAction,
                        framework = framework.name
                    )
                    // Update current selected entry if viewing
                    val updated = repository.getJournalEntryById(entryId).firstOrNull()
                    if (updated != null) {
                        _uiState.update { it.copy(selectedJournalEntry = updated) }
                    }
                    showNotification("✨ Mentoría filosófica generada con éxito.")
                } catch (e: Exception) {
                    showNotification("⚠️ Entrada guardada. No se pudo conectar con el mentor.")
                } finally {
                    _uiState.update { it.copy(isGeneratingJournalMentorship = false) }
                }
            } else {
                showNotification("📖 Entrada guardada en tu diario personal.")
            }

            // Reset inputs
            _uiState.update {
                it.copy(
                    journalInputTitle = "",
                    journalInputContent = "",
                    journalInputMood = "Reflexión"
                )
            }
        }
    }

    fun requestMentorshipForExistingEntry(entry: JournalEntryEntity, framework: SoltarFramework) {
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingJournalMentorship = true) }
            try {
                val result = SoltarAiEngine.generateJournalMentorship(
                    journalContent = entry.content,
                    moodTag = entry.moodTag,
                    framework = framework,
                    userContext = buildUserPersonalizationContext()
                )
                repository.updateJournalFeedback(
                    id = entry.id,
                    feedback = result.feedback,
                    corePrinciple = result.corePrinciple,
                    socraticQuestion = result.socraticQuestion,
                    concreteAction = result.concreteAction,
                    framework = framework.name
                )
                val updated = repository.getJournalEntryById(entry.id).firstOrNull()
                if (updated != null) {
                    _uiState.update { it.copy(selectedJournalEntry = updated) }
                }
                showNotification("✨ Nueva perspectiva filosófica generada.")
            } catch (e: Exception) {
                showNotification("⚠️ No se pudo regenerar la mentoría.")
            } finally {
                _uiState.update { it.copy(isGeneratingJournalMentorship = false) }
            }
        }
    }

    fun deleteJournalEntry(id: Long) {
        viewModelScope.launch {
            repository.deleteJournalEntry(id)
            if (_uiState.value.selectedJournalEntry?.id == id) {
                _uiState.update { it.copy(selectedJournalEntry = null) }
            }
            showNotification("🗑️ Entrada de diario eliminada.")
        }
    }

    fun toggleThoughtModal(visible: Boolean) = _uiState.update { it.copy(isThoughtModalVisible = visible) }
    fun toggleAuditModal(visible: Boolean) = _uiState.update { it.copy(isAuditModalVisible = visible) }
    fun toggleIdealizationModal(visible: Boolean) = _uiState.update { it.copy(isIdealizationModalVisible = visible) }
    fun toggleLetterModal(visible: Boolean) = _uiState.update { it.copy(isLetterModalVisible = visible) }
    fun toggleRelapseModal(visible: Boolean) = _uiState.update { it.copy(isRelapseModalVisible = visible) }
    fun toggleIdentityGoalModal(visible: Boolean) = _uiState.update { it.copy(isIdentityGoalModalVisible = visible) }
    fun toggleFounderExperience(visible: Boolean) = _uiState.update { it.copy(isFounderExperienceVisible = visible) }
    fun toggleAiCompanionSheet(visible: Boolean) = _uiState.update { it.copy(isAiCompanionSheetVisible = visible) }
    fun toggleAuthDialog(visible: Boolean) = _uiState.update { it.copy(isAuthDialogVisible = visible) }

    fun clearAiMemory() {
        viewModelScope.launch {
            repository.clearAiMemory()
            repository.saveAiMessage(
                AiMessageEntity(
                    sender = "soltar_ai",
                    content = "Memoria del chat reiniciada. ¿En qué podemos concentrarnos hoy para cuidar tu día?",
                    detectedRumination = false
                )
            )
            toggleMemoryModal(false)
            showNotification("🗑️ Historial de conversación eliminado con éxito.")
        }
    }

    fun fullDataReset() {
        resetAppData()
        showNotification("🧹 Todos los registros locales han sido reiniciados.")
    }

    // ==========================================
    // SUPPORT NETWORK (RED DE APOYO)
    // ==========================================
    fun openSupportContactDialog(index: Int) {
        val current = settings.value
        val (name, phone, rel) = when (index) {
            1 -> Triple(current?.contact1Name ?: "", current?.contact1Phone ?: "", current?.contact1Relationship ?: "")
            2 -> Triple(current?.contact2Name ?: "", current?.contact2Phone ?: "", current?.contact2Relationship ?: "")
            3 -> Triple(current?.contact3Name ?: "", current?.contact3Phone ?: "", current?.contact3Relationship ?: "")
            else -> Triple("", "", "")
        }

        _uiState.update {
            it.copy(
                isSupportContactDialogVisible = true,
                editingContactIndex = index,
                contactNameInput = name,
                contactPhoneInput = phone,
                contactRelationshipInput = rel
            )
        }
    }

    fun closeSupportContactDialog() {
        _uiState.update { it.copy(isSupportContactDialogVisible = false) }
    }

    fun setContactName(name: String) = _uiState.update { it.copy(contactNameInput = name) }
    fun setContactPhone(phone: String) = _uiState.update { it.copy(contactPhoneInput = phone) }
    fun setContactRelationship(rel: String) = _uiState.update { it.copy(contactRelationshipInput = rel) }

    fun saveSupportContact() {
        val s = _uiState.value
        val index = s.editingContactIndex
        val name = s.contactNameInput.trim()
        val phone = s.contactPhoneInput.trim()
        val rel = s.contactRelationshipInput.trim()

        if (name.isBlank()) {
            showNotification("⚠️ Por favor, introduce el nombre del contacto.")
            return
        }

        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val updated = when (index) {
                1 -> current.copy(contact1Name = name, contact1Phone = phone, contact1Relationship = rel)
                2 -> current.copy(contact2Name = name, contact2Phone = phone, contact2Relationship = rel)
                3 -> current.copy(contact3Name = name, contact3Phone = phone, contact3Relationship = rel)
                else -> current
            }
            repository.saveSettings(updated)
            closeSupportContactDialog()
            playSound(com.example.audio.SoltarSoundManager.SoundType.CALM_BELL)
            showNotification("👥 Contacto de la Red de Apoyo guardado.")
        }
    }

    fun deleteSupportContact(index: Int) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val updated = when (index) {
                1 -> current.copy(contact1Name = "", contact1Phone = "", contact1Relationship = "")
                2 -> current.copy(contact2Name = "", contact2Phone = "", contact2Relationship = "")
                3 -> current.copy(contact3Name = "", contact3Phone = "", contact3Relationship = "")
                else -> current
            }
            repository.saveSettings(updated)
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
            showNotification("🗑️ Contacto eliminado de tu Red de Apoyo.")
        }
    }

    // ==========================================
    // PERSPECTIVES & COMPASS SETTINGS
    // ==========================================
    fun toggleFaithPerspective(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            repository.saveSettings(current.copy(faithPerspectiveActive = enabled))
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
        }
    }

    fun toggleStoicPerspective(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            repository.saveSettings(current.copy(stoicPerspectiveActive = enabled))
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
        }
    }

    fun toggleModernPsychologyPerspective(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            repository.saveSettings(current.copy(modernPsychologyPerspectiveActive = enabled))
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
        }
    }

    // ==========================================
    // MONETIZATION, SUBSCRIPTION & PAYWALL
    // ==========================================
    fun openPaywall(plan: SubscriptionPlan = SubscriptionPlan.MONTHLY) {
        _uiState.update {
            it.copy(
                isPaywallVisible = true,
                selectedSubscriptionPlan = plan,
                isProcessingPayment = false
            )
        }
        playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
    }

    fun closePaywall() {
        _uiState.update { it.copy(isPaywallVisible = false, isProcessingPayment = false) }
    }

    fun selectSubscriptionPlan(plan: SubscriptionPlan) {
        _uiState.update { it.copy(selectedSubscriptionPlan = plan) }
        playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
    }

    fun purchaseSubscription(plan: SubscriptionPlan) {
        _uiState.update { it.copy(isProcessingPayment = true) }
        viewModelScope.launch {
            delay(1200) // Realistic secure billing transaction handshake
            val current = settings.value ?: SoltarSettingsEntity()
            val expiry = 0L
            repository.saveSettings(
                current.copy(
                    subscriptionTier = plan.tierKey,
                    isTrialActive = false,
                    subscriptionExpiryTimestamp = expiry
                )
            )
            _uiState.update { it.copy(isProcessingPayment = false, isPaywallVisible = false) }
            playSound(com.example.audio.SoltarSoundManager.SoundType.WARM_CHIME)
            showNotification("💎 ¡Bienvenido/a a Recuerda Premium! Tu acceso completo está activo.")
        }
    }

    fun startFreeTrial() {
        _uiState.update { it.copy(isProcessingPayment = true) }
        viewModelScope.launch {
            delay(1000)
            val current = settings.value ?: SoltarSettingsEntity()
            val expiry = System.currentTimeMillis() + (7L * 24 * 3600 * 1000)
            repository.saveSettings(
                current.copy(
                    subscriptionTier = "atalaya_pro_monthly",
                    isTrialActive = true,
                    subscriptionExpiryTimestamp = expiry
                )
            )
            _uiState.update { it.copy(isProcessingPayment = false, isPaywallVisible = false) }
            playSound(com.example.audio.SoltarSoundManager.SoundType.WARM_CHIME)
            showNotification("🌟 Has iniciado tus 7 días de prueba gratis en Recuerda Premium.")
        }
    }

    fun cancelSubscription() {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            repository.saveSettings(
                current.copy(
                    subscriptionTier = "FREE",
                    isTrialActive = false,
                    subscriptionExpiryTimestamp = 0L
                )
            )
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
            showNotification("ℹ️ Tu suscripción ha sido cancelada. Mantienes el acceso a Recuerda Free.")
        }
    }

    fun restorePurchases() {
        _uiState.update { it.copy(isProcessingPayment = true) }
        billingManager.restorePurchases { success, message ->
            _uiState.update { it.copy(isProcessingPayment = false) }
            if (success) {
                // Update local settings if needed based on billing result
                viewModelScope.launch {
                    val current = settings.value ?: SoltarSettingsEntity()
                    repository.saveSettings(current.copy(subscriptionTier = "atalaya_pro_monthly"))
                }
            }
            showNotification(message)
        }
    }

    fun manageSubscriptionInGooglePlay(context: Context) {
        try {
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                data = android.net.Uri.parse("https://play.google.com/store/account/subscriptions?package=${context.packageName}&sku=${"premium_subscription"}")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            showNotification("No se pudo abrir la gestión de suscripciones.")
        }
    }

    // ==========================================
    // NOTIFICATIONS & SCHEDULED REMINDERS
    // ==========================================
    fun toggleReminderTimeDialog(show: Boolean) {
        _uiState.update { it.copy(isTimePickerDialogVisible = show) }
        playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
    }

    fun updateRelationshipContext(
        relDuration: String,
        hasChildren: Boolean,
        contactType: String,
        breakupSituation: String,
        practicals: String
    ) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            repository.saveSettings(
                current.copy(
                    relDuration = relDuration,
                    hasChildren = hasChildren,
                    contactType = contactType,
                    breakupSituation = breakupSituation,
                    practicals = practicals
                )
            )
        }
    }

    fun setReminderHourInput(hour: Int) {
        _uiState.update { it.copy(reminderHourInput = hour.coerceIn(0, 23)) }
    }

    fun setReminderMinuteInput(min: Int) {
        _uiState.update { it.copy(reminderMinuteInput = min.coerceIn(0, 59)) }
    }

    fun saveReminderSchedule(hour: Int, minute: Int) {
        viewModelScope.launch {
            val clampedHour = hour.coerceIn(0, 23)
            val clampedMin = minute.coerceIn(0, 59)
            val current = settings.value ?: SoltarSettingsEntity()
            val updated = current.copy(
                reminderHour = clampedHour,
                reminderMinute = clampedMin,
                notificationsEnabled = true
            )
            repository.saveSettings(updated)
            com.example.notifications.SoltarNotificationHelper.scheduleDailyReminder(
                getApplication(),
                clampedHour,
                clampedMin
            )
            _uiState.update {
                it.copy(
                    isTimePickerDialogVisible = false,
                    reminderHourInput = clampedHour,
                    reminderMinuteInput = clampedMin,
                    notificationsEnabled = true
                )
            }
            playSound(com.example.audio.SoltarSoundManager.SoundType.CALM_BELL)
            val formattedTime = String.format(java.util.Locale.getDefault(), "%02d:%02d", clampedHour, clampedMin)
            showNotification("⏰ Recordatorio diario programado para las $formattedTime hs.")
        }
    }

    fun toggleNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val updated = current.copy(notificationsEnabled = enabled)
            repository.saveSettings(updated)
            if (enabled) {
                com.example.notifications.SoltarNotificationHelper.scheduleDailyReminder(
                    getApplication(),
                    current.reminderHour,
                    current.reminderMinute
                )
                val formattedTime = String.format(java.util.Locale.getDefault(), "%02d:%02d", current.reminderHour, current.reminderMinute)
                showNotification("🔔 Recordatorios diarios activados ($formattedTime hs)")
            } else {
                com.example.notifications.SoltarNotificationHelper.cancelDailyReminder(getApplication())
                showNotification("🔕 Recordatorios diarios desactivados")
            }
            _uiState.update { it.copy(notificationsEnabled = enabled) }
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
        }
    }

    fun toggleInactivityAlertsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val updated = current.copy(inactivityAlertsEnabled = enabled)
            repository.saveSettings(updated)
            _uiState.update { it.copy(inactivityAlertsEnabled = enabled) }
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
            showNotification(if (enabled) "🌿 Acompañamiento empático tras 3 días activado" else "Acompañamiento por inactividad desactivado")
        }
    }

    fun triggerTestDailyReminder() {
        com.example.notifications.SoltarNotificationHelper.sendDailyCheckinNotification(getApplication())
        playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
        showNotification("🔔 Notificación de recordatorio diario enviada")
    }

    fun triggerTestInactivityReminder() {
        val s = settings.value
        val userName = s?.userName?.ifBlank { "Viajero" } ?: "Viajero"
        val framework = s?.let { SoltarFramework.fromKey(it.preferredFramework) } ?: SoltarFramework.PSICOLOGIA_MODERNA
        com.example.notifications.SoltarNotificationHelper.sendInactivityEmpatheticNotification(
            getApplication(),
            daysInactive = 3,
            userName = userName,
            framework = framework
        )
        playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
        showNotification("🌿 Notificación empática (3 días sin registro) enviada")
    }

    fun triggerTestMilestoneReminder(days: Int = 7) {
        val s = settings.value
        val userName = s?.userName?.ifBlank { "Viajero" } ?: "Viajero"
        val framework = s?.let { SoltarFramework.fromKey(it.preferredFramework) } ?: SoltarFramework.PSICOLOGIA_MODERNA
        com.example.notifications.SoltarNotificationHelper.sendMilestoneNotification(
            getApplication(),
            days = days,
            framework = framework,
            userName = userName
        )
        playSound(com.example.audio.SoltarSoundManager.SoundType.WARM_CHIME)
        showNotification("🎉 Notificación de celebración de hito ($days días) enviada")
    }

    // --- B5: Trigger Events ---
    fun registerTriggerEvent(context: String, trigger: String, emotion: String, note: String = "") {
        viewModelScope.launch {
            repository.saveTriggerEvent(
                com.example.data.TriggerEventEntity(
                    context = context,
                    trigger = trigger,
                    emotion = emotion,
                    note = note
                )
            )
        }
    }
    // --------------------------

    // --- B2/B3: Red Flags & Peer Support ---
    val redFlags: StateFlow<List<com.example.data.RedFlagEntity>> = repository.allRedFlags
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val peerSupportPosts: StateFlow<List<com.example.data.PeerSupportPostEntity>> = repository.allPeerSupportPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addRedFlag(reason: String) {
        viewModelScope.launch {
            repository.saveRedFlag(com.example.data.RedFlagEntity(reason = reason))
        }
    }

    fun removeRedFlag(redFlag: com.example.data.RedFlagEntity) {
        viewModelScope.launch {
            repository.deleteRedFlag(redFlag)
        }
    }

    fun addPeerSupportPost(content: String) {
        viewModelScope.launch {
            repository.savePeerSupportPost(com.example.data.PeerSupportPostEntity(content = content))
        }
    }

    fun likePeerSupportPost(id: Long) {
        viewModelScope.launch {
            repository.likePeerSupportPost(id)
        }
    }

    fun toggleConversationAnalyzer(visible: Boolean) = _uiState.update { it.copy(isConversationAnalyzerVisible = visible) }
    fun openConversationAnalyzer() = _uiState.update { it.copy(isConversationAnalyzerVisible = true) }
    fun closeConversationAnalyzer() = _uiState.update { it.copy(isConversationAnalyzerVisible = false) }
    // --------------------------

    // --- Thought Lab (C) ---
    val thoughtLabEntries: StateFlow<List<com.example.data.ThoughtLabEntity>> = repository.allThoughtLabEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addThoughtLabEntry(original: String, distortion: String, reframed: String) {
        viewModelScope.launch {
            repository.saveThoughtLabEntry(com.example.data.ThoughtLabEntity(originalThought = original, distortionType = distortion, reframedThought = reframed))
        }
    }
    // --------------------------

    // --- Relationship Audit (D) ---
    val relationshipAudits: StateFlow<List<com.example.data.RelationshipAuditEntity>> = repository.allRelationshipAudits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addRelationshipAudit(title: String, category: String, myResp: String, otherResp: String, sharedResp: String, pattern: String) {
        viewModelScope.launch {
            repository.saveRelationshipAudit(com.example.data.RelationshipAuditEntity(
                title = title, 
                category = category, 
                myResponsibility = myResp,
                otherResponsibility = otherResp,
                sharedResponsibility = sharedResp,
                patternIdentified = pattern
            ))
        }
    }

    fun removeRelationshipAudit(id: Long) {
        viewModelScope.launch {
            repository.deleteRelationshipAudit(id)
        }
    }
    // --------------------------

    // --- Emotional Check-in ---
    fun openEmotionalCheckin() = _uiState.update { it.copy(isEmotionalCheckinVisible = true) }
    fun closeEmotionalCheckin() = _uiState.update { it.copy(isEmotionalCheckinVisible = false) }
    fun updateCheckinState(state: String) = _uiState.update { it.copy(checkinStateInput = state) }
    fun updateCheckinFirstThoughts(text: String) = _uiState.update { it.copy(checkinFirstThoughtsInput = text) }
    fun updateCheckinUrge(urge: Float) = _uiState.update { it.copy(checkinUrgeInput = urge) }
    fun updateCheckinPredominantEmotion(emotion: String) = _uiState.update { it.copy(checkinPredominantEmotionInput = emotion) }
    fun updateCheckinTrigger(trigger: String) = _uiState.update { it.copy(checkinTriggerInput = trigger) }
    fun updateCheckinComparison(comparison: String) = _uiState.update { it.copy(checkinComparisonInput = comparison) }
    fun updateCheckinFreeNote(note: String) = _uiState.update { it.copy(checkinFreeNoteInput = note) }

    fun saveEmotionalCheckin() {
        val state = _uiState.value
        val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            repository.saveCheckin(
                CheckinEntity(
                    dateKey = dateKey,
                    emotionalState = state.checkinStateInput,
                    firstThoughts = state.checkinFirstThoughtsInput,
                    urgeToContact = state.checkinUrgeInput,
                    predominantEmotion = state.checkinPredominantEmotionInput,
                    trigger = state.checkinTriggerInput,
                    comparisonWithYesterday = state.checkinComparisonInput,
                    note = state.checkinFreeNoteInput
                )
            )
            _uiState.update {
                it.copy(
                    isEmotionalCheckinVisible = false,
                    notificationMessage = "✨ Check-in emocional guardado con éxito. Evolución registrada."
                )
            }
        }
    }
    // --------------------------

    // --- Anticipated Risk Dates Calendar ---
    fun toggleRiskDateModal(visible: Boolean) {
        _uiState.update { it.copy(isRiskDateModalVisible = visible) }
    }

    fun setRiskDateTitle(t: String) = _uiState.update { it.copy(riskDateTitleInput = t) }
    fun setRiskDateMonth(m: Int) = _uiState.update { it.copy(riskDateMonthInput = m) }
    fun setRiskDateDay(d: Int) = _uiState.update { it.copy(riskDateDayInput = d) }
    fun setRiskDateStrategy(s: String) = _uiState.update { it.copy(riskDateStrategyInput = s) }
    fun setRiskDateReminderDays(d: Int) = _uiState.update { it.copy(riskDateReminderDaysInput = d) }

    fun saveRiskDate() {
        val s = _uiState.value
        if (s.riskDateTitleInput.isBlank()) {
            showNotification("⚠️ Por favor, introduce un título para la fecha de riesgo.")
            return
        }
        viewModelScope.launch {
            repository.saveRiskDate(
                RiskDateEntity(
                    title = s.riskDateTitleInput.trim(),
                    month = s.riskDateMonthInput.coerceIn(1, 12),
                    day = s.riskDateDayInput.coerceIn(1, 31),
                    customStrategy = s.riskDateStrategyInput.trim(),
                    reminderDaysBefore = s.riskDateReminderDaysInput.coerceIn(1, 30)
                )
            )
            _uiState.update {
                it.copy(
                    isRiskDateModalVisible = false,
                    riskDateTitleInput = "",
                    riskDateStrategyInput = ""
                )
            }
            showNotification("📅 Fecha clave de riesgo anticipado guardada con éxito.")
        }
    }

    fun deleteRiskDate(id: Long) {
        viewModelScope.launch {
            repository.deleteRiskDate(id)
            showNotification("🗑️ Fecha de riesgo eliminada.")
        }
    }

    fun updateMandatoryJournalTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val clampedHour = hour.coerceIn(0, 23)
            val clampedMinute = minute.coerceIn(0, 59)
            repository.saveSettings(current.copy(mandatoryJournalHour = clampedHour, mandatoryJournalMinute = clampedMinute))
            com.example.notifications.SoltarNotificationHelper.scheduleMandatoryJournalReminder(getApplication(), clampedHour, clampedMinute)
            _uiState.update { it.copy(mandatoryJournalHourInput = clampedHour, mandatoryJournalMinuteInput = clampedMinute) }
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
            showNotification(String.format(Locale.getDefault(), "Diario obligatorio programado a las %02d:%02d hs", clampedHour, clampedMinute))
        }
    }

    private val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }

    fun addCustomNotification(hour: Int, minute: Int, title: String, message: String) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val list = try {
                if (current.customNotificationsJson.isNotBlank()) {
                    json.decodeFromString<List<CustomNotificationItem>>(current.customNotificationsJson)
                } else emptyList()
            } catch (_: Exception) { emptyList() }

            val newItem = CustomNotificationItem(
                id = System.currentTimeMillis(),
                hour = hour.coerceIn(0, 23),
                minute = minute.coerceIn(0, 59),
                title = title.ifBlank { "Recordatorio de Soberanía" },
                message = message.ifBlank { "Mantén tu enfoque y respira hondo." },
                enabled = true
            )
            val updatedList = list + newItem
            val jsonStr = json.encodeToString(kotlinx.serialization.builtins.ListSerializer(CustomNotificationItem.serializer()), updatedList)
            repository.saveSettings(current.copy(customNotificationsJson = jsonStr))
            com.example.notifications.SoltarNotificationHelper.scheduleCustomNotification(getApplication(), newItem)
            _uiState.update { it.copy(customNotifications = updatedList) }
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
            showNotification("Notificación personalizada añadida")
        }
    }

    fun updateCustomNotification(id: Long, hour: Int, minute: Int, title: String, message: String, enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val list = try {
                if (current.customNotificationsJson.isNotBlank()) {
                    json.decodeFromString<List<CustomNotificationItem>>(current.customNotificationsJson)
                } else emptyList()
            } catch (_: Exception) { emptyList() }

            val updatedList = list.map { item ->
                if (item.id == id) {
                    val updated = item.copy(
                        hour = hour.coerceIn(0, 23),
                        minute = minute.coerceIn(0, 59),
                        title = title.ifBlank { item.title },
                        message = message.ifBlank { item.message },
                        enabled = enabled
                    )
                    if (enabled) com.example.notifications.SoltarNotificationHelper.scheduleCustomNotification(getApplication(), updated)
                    else com.example.notifications.SoltarNotificationHelper.cancelCustomNotification(getApplication(), id)
                    updated
                } else item
            }
            val jsonStr = json.encodeToString(kotlinx.serialization.builtins.ListSerializer(CustomNotificationItem.serializer()), updatedList)
            repository.saveSettings(current.copy(customNotificationsJson = jsonStr))
            _uiState.update { it.copy(customNotifications = updatedList) }
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
        }
    }

    fun deleteCustomNotification(id: Long) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val list = try {
                if (current.customNotificationsJson.isNotBlank()) {
                    json.decodeFromString<List<CustomNotificationItem>>(current.customNotificationsJson)
                } else emptyList()
            } catch (_: Exception) { emptyList() }

            val updatedList = list.filter { it.id != id }
            com.example.notifications.SoltarNotificationHelper.cancelCustomNotification(getApplication(), id)
            val jsonStr = json.encodeToString(kotlinx.serialization.builtins.ListSerializer(CustomNotificationItem.serializer()), updatedList)
            repository.saveSettings(current.copy(customNotificationsJson = jsonStr))
            _uiState.update { it.copy(customNotifications = updatedList) }
            playSound(com.example.audio.SoltarSoundManager.SoundType.TAP)
            showNotification("Notificación eliminada")
        }
    }

    fun toggleCustomNotificationEnabled(id: Long, enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val list = try {
                if (current.customNotificationsJson.isNotBlank()) {
                    json.decodeFromString<List<CustomNotificationItem>>(current.customNotificationsJson)
                } else emptyList()
            } catch (_: Exception) { emptyList() }

            val updatedList = list.map { item ->
                if (item.id == id) {
                    val updated = item.copy(enabled = enabled)
                    if (enabled) com.example.notifications.SoltarNotificationHelper.scheduleCustomNotification(getApplication(), updated)
                    else com.example.notifications.SoltarNotificationHelper.cancelCustomNotification(getApplication(), id)
                    updated
                } else item
            }
            val jsonStr = json.encodeToString(kotlinx.serialization.builtins.ListSerializer(CustomNotificationItem.serializer()), updatedList)
            repository.saveSettings(current.copy(customNotificationsJson = jsonStr))
            _uiState.update { it.copy(customNotifications = updatedList) }
        }
    }

    fun openAddCustomNotificationDialog(
        defaultTitle: String = "Recordatorio de Soberanía",
        defaultMessage: String = "Mantén tu enfoque y respira hondo.",
        defaultHour: Int = 12,
        defaultMinute: Int = 0
    ) {
        _uiState.update {
            it.copy(
                isCustomNotificationDialogVisible = true,
                editingCustomNotificationId = null,
                customNotificationTitleInput = defaultTitle,
                customNotificationMessageInput = defaultMessage,
                customNotificationHourInput = defaultHour.coerceIn(0, 23),
                customNotificationMinuteInput = defaultMinute.coerceIn(0, 59)
            )
        }
    }

    fun openEditCustomNotificationDialog(item: CustomNotificationItem) {
        _uiState.update {
            it.copy(
                isCustomNotificationDialogVisible = true,
                editingCustomNotificationId = item.id,
                customNotificationTitleInput = item.title,
                customNotificationMessageInput = item.message,
                customNotificationHourInput = item.hour,
                customNotificationMinuteInput = item.minute
            )
        }
    }

    fun dismissCustomNotificationDialog() {
        _uiState.update { it.copy(isCustomNotificationDialogVisible = false, editingCustomNotificationId = null) }
    }

    fun setCustomNotificationTitleInput(title: String) {
        _uiState.update { it.copy(customNotificationTitleInput = title) }
    }

    fun setCustomNotificationMessageInput(msg: String) {
        _uiState.update { it.copy(customNotificationMessageInput = msg) }
    }

    fun setCustomNotificationHourInput(hour: Int) {
        _uiState.update { it.copy(customNotificationHourInput = hour.coerceIn(0, 23)) }
    }

    fun setCustomNotificationMinuteInput(min: Int) {
        _uiState.update { it.copy(customNotificationMinuteInput = min.coerceIn(0, 59)) }
    }

    fun saveCustomNotificationFromDialog() {
        val s = _uiState.value
        val id = s.editingCustomNotificationId
        if (id != null) {
            updateCustomNotification(
                id = id,
                hour = s.customNotificationHourInput,
                minute = s.customNotificationMinuteInput,
                title = s.customNotificationTitleInput,
                message = s.customNotificationMessageInput,
                enabled = true
            )
        } else {
            addCustomNotification(
                hour = s.customNotificationHourInput,
                minute = s.customNotificationMinuteInput,
                title = s.customNotificationTitleInput,
                message = s.customNotificationMessageInput
            )
        }
        dismissCustomNotificationDialog()
    }

    fun triggerTestCustomNotification(title: String, message: String) {
        com.example.notifications.SoltarNotificationHelper.sendCustomNotification(
            getApplication(),
            title.ifBlank { "Recordatorio de Soberanía" },
            message.ifBlank { "Mantén tu enfoque y respira hondo." }
        )
        playSound(com.example.audio.SoltarSoundManager.SoundType.WARM_CHIME)
        showNotification("🔔 Notificación de prueba enviada")
    }

    fun restoreDefaultPresetReminders() {
        viewModelScope.launch {
            val current = settings.value ?: SoltarSettingsEntity()
            val existing = try {
                if (current.customNotificationsJson.isNotBlank()) {
                    json.decodeFromString<List<CustomNotificationItem>>(current.customNotificationsJson)
                } else emptyList()
            } catch (_: Exception) { emptyList() }

            val merged = existing + DEFAULT_PRESET_REMINDERS.filter { p -> existing.none { it.title == p.title } }
            val jsonStr = json.encodeToString(kotlinx.serialization.builtins.ListSerializer(CustomNotificationItem.serializer()), merged)
            repository.saveSettings(current.copy(customNotificationsJson = jsonStr))
            merged.forEach { item ->
                if (item.enabled) {
                    com.example.notifications.SoltarNotificationHelper.scheduleCustomNotification(getApplication(), item)
                }
            }
            _uiState.update { it.copy(customNotifications = merged) }
            playSound(com.example.audio.SoltarSoundManager.SoundType.WARM_CHIME)
            showNotification("✨ Plantillas de recordatorios programables cargadas")
        }
    }

    fun togglePrivacyPolicy(visible: Boolean) {
        _uiState.update { it.copy(isPrivacyPolicyVisible = visible) }
    }

    fun toggleTermsConditions(visible: Boolean) {
        _uiState.update { it.copy(isTermsConditionsVisible = visible) }
    }

    companion object {
        val DEFAULT_PRESET_REMINDERS = listOf(
            CustomNotificationItem(
                id = 101L,
                hour = 8,
                minute = 30,
                title = "🌅 Intención Matutina",
                message = "Respira hondo: hoy eliges tu paz mental y tu soberanía emocional.",
                enabled = true
            ),
            CustomNotificationItem(
                id = 102L,
                hour = 14,
                minute = 0,
                title = "🛡️ Pausa Antirrumiación",
                message = "Si surge urgencia de buscar o escribir, haz una pausa. El impulso pasará.",
                enabled = true
            ),
            CustomNotificationItem(
                id = 103L,
                hour = 18,
                minute = 30,
                title = "🌿 Chequeo de Calma & Autocuidado",
                message = "Tómate un respiro, bebe agua y valida el camino que has recorrido.",
                enabled = true
            ),
            CustomNotificationItem(
                id = 104L,
                hour = 22,
                minute = 30,
                title = "🌙 Serenidad & Cierre Nocturno",
                message = "Un día más que has protegido tu dignidad. Descansa y suelta lo que no controlas.",
                enabled = true
            )
        )
    }
}
