package com.example.ai

import com.example.data.JournalEntryEntity
import com.example.data.SoltarFramework
import com.example.data.ThoughtEntity
import com.example.data.UrgeEpisodeEntity
import java.util.Calendar

data class DailySummaryData(
    val journalCount: Int,
    val thoughtCount: Int,
    val urgeCount: Int,
    val resistedUrgesCount: Int,
    val totalActivities: Int,
    val predominantMood: String?,
    val corePrinciple: String?,
    val keyLearning: String?,
    val concreteAction: String?,
    val briefMotivationalNote: String,
    val detailedSummaryText: String,
    val hasActivityToday: Boolean,
    val generatedAt: Long = System.currentTimeMillis()
)

object DailySummaryEngine {

    fun getStartOfTodayMillis(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    fun generateDailySummary(
        journals: List<JournalEntryEntity>,
        thoughts: List<ThoughtEntity>,
        urges: List<UrgeEpisodeEntity>,
        framework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA,
        userName: String = "Viajero"
    ): DailySummaryData {
        val journalCount = journals.size
        val thoughtCount = thoughts.size
        val urgeCount = urges.size
        val totalActivities = journalCount + thoughtCount + urgeCount
        val hasActivity = totalActivities > 0

        val resistedUrges = urges.count {
            val b = it.actualBehavior.lowercase()
            b.contains("resist") || b.contains("aplaz") || b.contains("sustitu") || b.contains("éxito")
        }

        val predominantMood = journals
            .map { it.moodTag.trim() }
            .filter { it.isNotBlank() }
            .groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key

        val corePrinciple = journals
            .firstOrNull { it.aiCorePrinciple.isNotBlank() }
            ?.aiCorePrinciple

        val keyLearning = urges
            .firstOrNull { it.learning.isNotBlank() }
            ?.learning
            ?: thoughts.firstOrNull { it.dependsOnMe.isNotBlank() }?.let { "Enfoque en lo que depende de mí: ${it.dependsOnMe}" }

        val concreteAction = thoughts
            .firstOrNull { it.concreteAction.isNotBlank() }
            ?.concreteAction
            ?: journals.firstOrNull { it.aiConcreteAction.isNotBlank() }?.aiConcreteAction

        // Build brief motivational note for home screen widget
        val briefNote = when {
            urgeCount > 0 && thoughtCount > 0 && journalCount > 0 -> {
                "✨ Resumen: Venciste $resistedUrges impulso(s), aclaraste pensamientos y escribiste en tu diario. Tu soberanía hoy está blindada."
            }
            urgeCount > 0 && thoughtCount > 0 -> {
                "✨ Resumen: Frenaste a tiempo la urgencia y desactivaste pensamientos trampa. Hoy tus decisiones mandan sobre tu dolor."
            }
            urgeCount > 0 && journalCount > 0 -> {
                val moodText = if (!predominantMood.isNullOrBlank()) " ($predominantMood)" else ""
                "✨ Resumen: Transformaste la tentación en desahogo consciente en tu diario$moodText. Elegiste dignidad sobre impulso."
            }
            thoughtCount > 0 && journalCount > 0 -> {
                if (!corePrinciple.isNullOrBlank()) {
                    "✨ Resumen: «$corePrinciple». Desmontaste rumiaciones y cultivaste lucidez en tu diario."
                } else {
                    "✨ Resumen: Separaste hechos de conjeturas y volcaste tu sentir en el diario. Gran avance en tu claridad mental."
                }
            }
            urgeCount > 0 -> {
                val lastUrge = urges.firstOrNull()
                val desiredAction = lastUrge?.desiredAction?.lowercase()?.takeIf { it.isNotBlank() } ?: "contacto"
                if (resistedUrges > 0) {
                    "✨ Resumen: Superaste con éxito el impulso de $desiredAction. Cada segundo de pausa consolida tu paz y libertad."
                } else {
                    "✨ Resumen: Registraste un momento difícil de $desiredAction. Aprender de la caída es avanzar hacia la firmeza."
                }
            }
            thoughtCount > 0 -> {
                val lastThought = thoughts.firstOrNull()
                if (!lastThought?.dependsOnMe.isNullOrBlank()) {
                    "✨ Resumen: Pusiste foco en lo que depende de ti. Tu mente recupera su centro frente a la rumiación."
                } else {
                    "✨ Resumen: Cuestionaste tus pensamientos automáticos en el laboratorio. Menos ficción mental, más serenidad."
                }
            }
            journalCount > 0 -> {
                if (!corePrinciple.isNullOrBlank()) {
                    "✨ Resumen: «$corePrinciple». Escribir sana lo que el silencio acumula; tu proceso avanza con verdad."
                } else {
                    val moodStr = if (!predominantMood.isNullOrBlank()) " de $predominantMood" else ""
                    "✨ Resumen: Procesaste tu sentir$moodStr en el diario. Cuidar tu mundo interior es tu mayor acto de amor propio."
                }
            }
            else -> {
                when (framework) {
                    SoltarFramework.ESTOICO ->
                        "🏛️ Resumen: Día sereno y en calma. Mantén tu soberanía intacta; tu tranquilidad solo depende de ti."
                    SoltarFramework.CATOLICO ->
                        "✝️ Resumen: Día en paz. Guarda tu corazón con paciencia; cada jornada de contacto cero renueva tu espíritu."
                    SoltarFramework.PSICOLOGIA_MODERNA ->
                        "🧠 Resumen: Día en calma y sin retrocesos. Tu mente continúa desinflamando el apego y ganando autonomía."
                }
            }
        }

        // Build detailed multi-line text for in-app display
        val detailedSb = StringBuilder()
        if (hasActivity) {
            detailedSb.append("Hoy has consolidado $totalActivities momento(s) de consciencia activa:\n")
            if (urgeCount > 0) {
                detailedSb.append("• 🛡️ $urgeCount episodio(s) de urgencia procesados ($resistedUrges regulados con éxito).\n")
            }
            if (thoughtCount > 0) {
                detailedSb.append("• 🔬 $thoughtCount pensamiento(s) reestructurados en el Laboratorio Mental.\n")
            }
            if (journalCount > 0) {
                val moodInfo = if (!predominantMood.isNullOrBlank()) " con tono emocional predominante: $predominantMood" else ""
                detailedSb.append("• 📖 $journalCount entrada(s) en tu diario personal$moodInfo.\n")
            }
            if (!keyLearning.isNullOrBlank()) {
                detailedSb.append("\n💡 Aprendizaje clave del día: \"$keyLearning\"\n")
            }
            if (!concreteAction.isNullOrBlank()) {
                detailedSb.append("⚡ Acción concreta activada: $concreteAction\n")
            }
        } else {
            detailedSb.append("Aún no has registrado eventos hoy. Tu día transcurre en estabilidad y contacto cero.")
        }

        return DailySummaryData(
            journalCount = journalCount,
            thoughtCount = thoughtCount,
            urgeCount = urgeCount,
            resistedUrgesCount = resistedUrges,
            totalActivities = totalActivities,
            predominantMood = predominantMood,
            corePrinciple = corePrinciple,
            keyLearning = keyLearning,
            concreteAction = concreteAction,
            briefMotivationalNote = briefNote,
            detailedSummaryText = detailedSb.toString().trim(),
            hasActivityToday = hasActivity
        )
    }
}
