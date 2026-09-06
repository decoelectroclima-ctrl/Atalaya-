package com.example.ai

import com.example.data.SoltarRepository
import kotlinx.coroutines.flow.first

class ConversationAnalyzerManager(
    private val aiEngine: SoltarAiEngine,
    private val repository: SoltarRepository
) {
    // Analiza un texto de conversación buscando patrones de manipulación, gaslighting, etc.
    // Utiliza el engine existente para la razonamiento clínico local.
    suspend fun analyzeConversation(text: String): AnalysisResult {
        val analysis = aiEngine.analyzeConversationText(text)
        return AnalysisResult(
            rawAnalysis = analysis,
            timestamp = System.currentTimeMillis()
        )
    }
}

data class AnalysisResult(
    val rawAnalysis: String,
    val timestamp: Long
)
