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
        title = "ADRIANA Free",
        priceDisplay = "0 €",
        periodLabel = "Para siempre",
        billingDetail = "Acceso esencial a herramientas de contención y contador diario"
    ),
    PREMIUM_ONE_TIME(
        tierKey = "PREMIUM_ONE_TIME",
        title = "ADRIANA Premium",
        priceDisplay = "19,99 €",
        periodLabel = "pago único",
        billingDetail = "Acceso completo de por vida a todas las herramientas, sonidos y chat avanzado."
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
            val isPrem = tierKey.startsWith("PREMIUM") || isTrial

            val plan = when (tierKey) {
                "PREMIUM_ONE_TIME" -> SubscriptionPlan.PREMIUM_ONE_TIME
                else -> SubscriptionPlan.FREE
            }

            return UserEntitlements(
                isPremium = isPrem,
                tier = plan,
                isTrial = isTrial,
                maxDailyCoachMessages = if (isPrem) 9999 else 5,
                canAccessAllLabs = true, // Basic labs accessible, deep audit unlimited in Prem
                canAccessDeepMemory = isPrem,
                canExportDataReport = isPrem,
                canAccessEmergencySpeedDial = true, // Emergency safety is always open for human dignity
                canUseCustomSoundscapes = isPrem
            )
        }
    }
}
