package com.example.data

enum class SubscriptionPlan(
    val tierKey: String,
    val title: String,
    val priceDisplay: String,
    val periodLabel: String,
    val billingDetail: String,
    val savingsBadge: String? = null,
    val trialDays: Int = 0
) {
    FREE(
        tierKey = "FREE",
        title = "Recuerda Free",
        priceDisplay = "0 €",
        periodLabel = "Para siempre",
        billingDetail = "Acceso esencial a herramientas de contención y contador diario"
    ),
    WEEKLY(
        tierKey = "premium_weekly",
        title = "Plan Semanal",
        priceDisplay = "6,99 €",
        periodLabel = "por semana",
        billingDetail = "Acceso completo ilimitado, renovable semanalmente. Cancela cuando quieras."
    ),
    MONTHLY(
        tierKey = "atalaya_pro_monthly",
        title = "Plan Mensual (ADRIANA Life Coach)",
        priceDisplay = "9,99 €",
        periodLabel = "por mes",
        billingDetail = "Suscripción mensual recurrente sin anuncios. Acceso completo a Recuperación y Life Coach.",
        trialDays = 7
    ),
    ANNUAL(
        tierKey = "premium_annual",
        title = "Plan Anual",
        priceDisplay = "39,99 €",
        periodLabel = "por año",
        billingDetail = "Equivalente a 3,33 €/mes. El compromiso más sólido con tu recuperación.",
        savingsBadge = "AHORRO 70%"
    ),
    PROGRAM_6_MONTHS(
        tierKey = "program_6_months",
        title = "Programa 6 Meses",
        priceDisplay = "34,99 €",
        periodLabel = "pago único",
        billingDetail = "Acceso guiado especial de 6 meses sin renovaciones automáticas."
    ),
    LIFETIME(
        tierKey = "lifetime_access",
        title = "Acceso de Por Vida",
        priceDisplay = "59,99 €",
        periodLabel = "pago único",
        billingDetail = "Acceso definitivo e ilimitado para toda la vida a todas las actualizaciones futuras."
    )
}

data class UserEntitlements(
    val isPremium: Boolean,
    val tier: SubscriptionPlan,
    val canAccessConversationAnalyzer: Boolean,
    val canExportClinicalReport: Boolean,
    val canAccessFullWisdomLibrary: Boolean,
    val canAccessTimeCapsule: Boolean,
    val canAccessEncounterSimulator: Boolean,
    val canAccessClosingRitual: Boolean,
    val canAccessAdvancedCharts: Boolean,
    val canAccessEmergencySpeedDial: Boolean // SIEMPRE true, nunca depende de isPrem
) {
    companion object {
        fun fromSettings(settings: SoltarSettingsEntity?): UserEntitlements {
            val tierKey = settings?.subscriptionTier ?: "FREE"
            val isPrem = tierKey != "FREE"
            val plan = when (tierKey) {
                "premium_weekly" -> SubscriptionPlan.WEEKLY
                "atalaya_pro_monthly" -> SubscriptionPlan.MONTHLY
                "premium_annual" -> SubscriptionPlan.ANNUAL
                "program_6_months" -> SubscriptionPlan.PROGRAM_6_MONTHS
                "lifetime_access" -> SubscriptionPlan.LIFETIME
                else -> SubscriptionPlan.FREE
            }
            return UserEntitlements(
                isPremium = isPrem,
                tier = plan,
                canAccessConversationAnalyzer = isPrem,
                canExportClinicalReport = isPrem,
                canAccessFullWisdomLibrary = isPrem,
                canAccessTimeCapsule = isPrem,
                canAccessEncounterSimulator = isPrem,
                canAccessClosingRitual = isPrem,
                canAccessAdvancedCharts = isPrem,
                canAccessEmergencySpeedDial = true
            )
        }
    }
}

