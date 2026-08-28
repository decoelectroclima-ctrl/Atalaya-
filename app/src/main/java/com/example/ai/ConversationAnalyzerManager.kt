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
        val prompt = """
            Analiza el siguiente texto de conversación buscando patrones como:
            - Manipulación
            - Gaslighting
            - Comportamiento hot-and-cold
            - Luz intermitente
            - Control coercitivo
            - Contradicciones
            - Invalidación
            
            Diferencia claramente entre HECHOS OBSERVABLES e INTERPRETACIONES POSIBLES.
            No diagnostiques a la persona ausente. Usa un lenguaje cauteloso.
            
            Texto:
            $text
        """.trimIndent()

        // Usamos valores por defecto seguros ya que este análisis es independiente
        val response = aiEngine.executeAdvancedLocalClinicalReasoning(
            input = text,
            isRumination = false,
            framework = com.example.data.SoltarFramework.PSICOLOGIA_MODERNA,
            userContext = com.example.ai.SoltarUserContext()
        )
        
        return AnalysisResult(
            rawAnalysis = response.replyText,
            timestamp = System.currentTimeMillis()
        )
    }
}

data class AnalysisResult(
    val rawAnalysis: String,
    val timestamp: Long
)
