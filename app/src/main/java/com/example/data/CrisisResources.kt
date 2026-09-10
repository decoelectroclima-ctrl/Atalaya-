package com.example.data

data class CrisisLine(val label: String, val phone: String, val displayPhone: String)

data class CountryCrisisResources(
    val countryName: String,
    val lines: List<CrisisLine>
)

object CrisisResources {
    val ESPANA = CountryCrisisResources(
        countryName = "España",
        lines = listOf(
            CrisisLine("Línea 024 (Conducta Suicida)", "024", "024"),
            CrisisLine("Teléfono de la Esperanza", "717003717", "717 003 717"),
            CrisisLine("Emergencias", "112", "112")
        )
    )
    val MEXICO = CountryCrisisResources(
        countryName = "México",
        lines = listOf(
            CrisisLine("Línea de la Vida", "8009112000", "800 911 2000"),
            CrisisLine("Emergencias", "911", "911")
        )
    )
    val COLOMBIA = CountryCrisisResources(
        countryName = "Colombia",
        lines = listOf(
            CrisisLine("Línea de Salud Mental", "106", "106"),
            CrisisLine("Emergencias", "192", "192")
        )
    )
    val ARGENTINA = CountryCrisisResources(
        countryName = "Argentina",
        lines = listOf(
            CrisisLine("Centro de Asistencia al Suicida", "135", "135"),
            CrisisLine("Centro de Asistencia al Suicida (fijo)", "01152751135", "(011) 5275-1135")
        )
    )
    val ESTADOS_UNIDOS = CountryCrisisResources(
        countryName = "Estados Unidos",
        lines = listOf(
            CrisisLine("988 Suicide & Crisis Lifeline (en español, opción 2)", "988", "988"),
            CrisisLine("Emergencias", "911", "911")
        )
    )
    val GENERICO_LATAM = CountryCrisisResources(
        countryName = "tu país",
        lines = listOf(
            CrisisLine("Línea 988 (disponible en español en varios países)", "988", "988"),
            CrisisLine("Emergencias", "911", "911")
        )
    )

    private val byCountryCode = mapOf(
        "ES" to ESPANA,
        "MX" to MEXICO,
        "CO" to COLOMBIA,
        "AR" to ARGENTINA,
        "US" to ESTADOS_UNIDOS
    )

    fun forCountryCode(isoCode: String?): CountryCrisisResources {
        if (isoCode.isNullOrBlank()) return GENERICO_LATAM
        return byCountryCode[isoCode.uppercase()] ?: GENERICO_LATAM
    }
}
