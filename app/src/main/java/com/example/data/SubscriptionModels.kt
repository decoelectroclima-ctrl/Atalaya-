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
        title = "Plan Mensual",
        priceDisplay = "10,99 €",
        periodLabel = "por mes",
        billingDetail = "Acceso completo ilimitado, renovable mensualmente. Cancela en Google Play.",
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
    val isTrial: Boolean,
    val maxDailyCoachMessages: Int, // e.g. 5 for free, Int.MAX_VALUE for premium
    val canAccessAllLabs: Boolean,
    val canAccessDeepMemory: Boolean,
    val canExportDataReport: Boolean,
    val canAccessEmergencySpeedDial: Boolean,
    val canUseCustomSoundscapes: Boolean
) {
    companion object {
        fun fromSettings(settings: SoltarSettingsEntity?): UserEntitlements {
            val tierKey = settings?.subscriptionTier ?: "FREE"
            val isTrial = settings?.isTrialActive == true

            val isPrem = tierKey != "FREE" || isTrial

            val plan = when (tierKey) {
                "premium_weekly" -> SubscriptionPlan.WEEKLY
                "atalaya_pro_monthly" -> SubscriptionPlan.MONTHLY
                "premium_annual" -> SubscriptionPlan.ANNUAL
                "program_6_months" -> SubscriptionPlan.PROGRAM_6_MONTHS
                "lifetime_access" -> SubscriptionPlan.LIFETIME
                else -> if (isPrem) SubscriptionPlan.MONTHLY else SubscriptionPlan.FREE
            }

            return UserEntitlements(
                isPremium = isPrem,
                tier = plan,
                isTrial = isTrial,
                maxDailyCoachMessages = if (isPrem) Int.MAX_VALUE else 5,
                canAccessAllLabs = isPrem,
                canAccessDeepMemory = isPrem,
                canExportDataReport = isPrem,
                canAccessEmergencySpeedDial = true, // Emergency safety is always open for human dignity
                canUseCustomSoundscapes = isPrem
            )
        }
    }
}

