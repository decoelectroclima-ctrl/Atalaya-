package com.example.data

enum class SoltarFramework(
    val key: String,
    val title: String,
    val description: String,
    val badge: String,
    val focusLabel: String
) {
    ESTOICO(
        key = "ESTOICO",
        title = "Estoico",
        description = "Claridad y disciplina interior. Marco Aurelio, Epicteto, Séneca.",
        badge = "FILOSOFÍA ESTOICA",
        focusLabel = "Enfoque Estoico"
    ),
    PSICOLOGIA_MODERNA(
        key = "PSICOLOGIA_MODERNA",
        title = "Psicología moderna",
        description = "Comprensión emocional y vínculos. Enfoque terapéutico contemporáneo.",
        badge = "PSICOLOGÍA Y APEGO",
        focusLabel = "Enfoque Psicológico"
    ),
    CATOLICO(
        key = "CATOLICO",
        title = "Católico",
        description = "Fe, esperanza y sabiduría bíblica como acompañamiento.",
        badge = "FE Y ESPERANZA",
        focusLabel = "Enfoque de Fe y Sabiduría"
    );

    companion object {
        fun fromKey(key: String?): SoltarFramework {
            return entries.firstOrNull { it.key.equals(key, ignoreCase = true) } ?: PSICOLOGIA_MODERNA
        }
    }
}

enum class UserGender(
    val key: String,
    val title: String,
    val emoji: String,
    val subtitle: String,
    val clinicalGuidance: String,
    val description: String = subtitle
) {
    MAN(
        key = "MAN",
        title = "Hombre",
        emoji = "👨",
        subtitle = "Enfoque adaptado a la psicología, dinámicas y desapego masculino.",
        clinicalGuidance = "El usuario es hombre. Utiliza concordancia gramatical masculina en español (ej. 'fuerte, centrado, enfocado, preparado, soberano, dueño de tus decisiones'). Aborda los patrones de rumiación, orgullo herido, impulsos de rescate/proveedor y canalización de energía (Modo Guerra, disciplina física y mental, respeto inquebrantable a su propio valor, cero mendicidad)."
    ),
    WOMAN(
        key = "WOMAN",
        title = "Mujer",
        emoji = "👩",
        subtitle = "Enfoque adaptado a la psicología, dinámicas y desapego femenino.",
        clinicalGuidance = "La usuaria es mujer. Utiliza concordancia gramatical femenina en español (ej. 'fuerte, centrada, enfocada, preparada, soberana, dueña de tus decisiones'). Aborda los patrones de hiperresponsabilidad afectiva, culpa internalizada, idealización del potencial del otro, desmantelamiento de la sumisión y recuperación de su trono y autonomía emocional."
    ),
    NOT_SPECIFIED(
        key = "NOT_SPECIFIED",
        title = "Sin especificar",
        emoji = "⚪",
        subtitle = "Acompañamiento neutral y universal.",
        clinicalGuidance = "El usuario no ha especificado su género. Emplea un lenguaje respetuoso, equilibrado y adaptable."
    );

    companion object {
        fun fromKey(key: String?): UserGender {
            return entries.firstOrNull { it.key.equals(key, ignoreCase = true) } ?: NOT_SPECIFIED
        }
    }
}

data class WisdomCard(
    val id: String,
    val framework: SoltarFramework,
    val title: String,
    val quote: String,
    val author: String,
    val reflection: String
)

object WisdomBank {

    val cards: List<WisdomCard> = estoicoCards + psicologiaCards + catolicoCards

    fun getRandomCard(framework: SoltarFramework, recentIds: List<String>): WisdomCard {
        val frameworkCards = cards.filter { it.framework == framework }
        if (frameworkCards.isEmpty()) return cards.first()

        val candidateCards = frameworkCards.filterNot { recentIds.contains(it.id) }
        val pool = if (candidateCards.isNotEmpty()) candidateCards else frameworkCards
        return pool.random()
    }

    fun getCardById(id: String): WisdomCard? {
        return cards.firstOrNull { it.id == id }
    }
}
