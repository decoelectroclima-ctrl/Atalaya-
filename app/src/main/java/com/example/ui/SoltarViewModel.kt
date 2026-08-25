package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.SoltarAiEngine
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
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
    
    // Contextual Modals & Dialogs
    val isThoughtModalVisible: Boolean = false,
    val isAuditModalVisible: Boolean = false,
    val isIdealizationModalVisible: Boolean = false,
    val isLetterModalVisible: Boolean = false,
    val isRelapseModalVisible: Boolean = false,
    val isIdentityGoalModalVisible: Boolean = false,
    val isAiCompanionSheetVisible: Boolean = false,
    val isNoThinkingSheetVisible: Boolean = false,
    val isMemoryModalVisible: Boolean = false,
    
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
    
    // AI Chat Inputs
    val aiInputMessage: String = "",
    val isAiTyping: Boolean = false
)

class SoltarViewModel(application: Application) : AndroidViewModel(application) {

    val repository: SoltarRepository = SoltarRepository(AtalayaDatabase.getDatabase(application))

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

    val relapses: StateFlow<List<RelapseEntity>> = repository.allRelapses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val aiMessages: StateFlow<List<AiMessageEntity>> = repository.allAiMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<SoltarSettingsEntity?> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private var urgeTimerJob: Job? = null

    init {
        loadTodayCheckin()
    }

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

    fun saveRelapseLog() {
        val s = _uiState.value
        if (s.relapseWhatHappenedInput.isBlank()) return
        viewModelScope.launch {
            repository.saveRelapse(
                RelapseEntity(
                    whatHappened = s.relapseWhatHappenedInput,
                    trigger = s.relapseTriggerInput,
                    emotion = s.relapseEmotionInput,
                    thought = s.relapseThoughtInput,
                    behavior = s.relapseBehaviorInput,
                    consequence = s.relapseConsequenceInput,
                    learning = s.relapseLearningInput.ifBlank { "Una recaída no borra mi progreso, me da información sobre mis detonantes." }
                )
            )
            _uiState.update {
                it.copy(
                    relapseWhatHappenedInput = "",
                    relapseTriggerInput = "",
                    relapseEmotionInput = "",
                    relapseThoughtInput = "",
                    relapseBehaviorInput = "",
                    relapseConsequenceInput = "",
                    relapseLearningInput = ""
                )
            }
            showNotification("🤝 Información registrada sin juicios. Volvemos al presente.")
        }
    }

    // ==========================================
    // "NO QUIERO PENSAR MÁS" QUICK ACTIONS
    // ==========================================
    fun openNoThinkingSheet() = _uiState.update { it.copy(isNoThinkingSheetVisible = true) }
    fun closeNoThinkingSheet() = _uiState.update { it.copy(isNoThinkingSheetVisible = false) }

    // ==========================================
    // AI CHAT & MEMORY CONTROLS
    // ==========================================
    fun setAiInputMessage(t: String) = _uiState.update { it.copy(aiInputMessage = t) }
    fun toggleMemoryModal(visible: Boolean) = _uiState.update { it.copy(isMemoryModalVisible = visible) }

    fun sendAiMessage() {
        val text = _uiState.value.aiInputMessage.trim()
        if (text.isBlank() || _uiState.value.isAiTyping) return

        _uiState.update { it.copy(aiInputMessage = "", isAiTyping = true) }

        viewModelScope.launch {
            repository.saveAiMessage(
                AiMessageEntity(
                    sender = "user",
                    content = text
                )
            )

            val currentMessages = aiMessages.value.map { it.sender to it.content }
            val response = SoltarAiEngine.generateResponse(text, currentMessages)

            repository.saveAiMessage(
                AiMessageEntity(
                    sender = "soltar_ai",
                    content = response.replyText,
                    detectedRumination = response.isRuminationDetected,
                    suggestedAction = response.suggestedAction
                )
            )

            _uiState.update { it.copy(isAiTyping = false) }
        }
    }

    fun setSelectedFeeling(f: String) = _uiState.update { it.copy(selectedFeeling = f) }
    
    fun toggleThoughtModal(visible: Boolean) = _uiState.update { it.copy(isThoughtModalVisible = visible) }
    fun toggleAuditModal(visible: Boolean) = _uiState.update { it.copy(isAuditModalVisible = visible) }
    fun toggleIdealizationModal(visible: Boolean) = _uiState.update { it.copy(isIdealizationModalVisible = visible) }
    fun toggleLetterModal(visible: Boolean) = _uiState.update { it.copy(isLetterModalVisible = visible) }
    fun toggleRelapseModal(visible: Boolean) = _uiState.update { it.copy(isRelapseModalVisible = visible) }
    fun toggleIdentityGoalModal(visible: Boolean) = _uiState.update { it.copy(isIdentityGoalModalVisible = visible) }
    fun toggleAiCompanionSheet(visible: Boolean) = _uiState.update { it.copy(isAiCompanionSheetVisible = visible) }

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
        viewModelScope.launch {
            repository.clearAiMemory()
            showNotification("🧹 Todos los registros locales han sido reiniciados.")
        }
    }
}
