package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ==========================================
// 1. RAW COLOR DEFINITIONS (DARK & LIGHT)
// ==========================================

// Raw Dark Palette (Obsidiana Kintsugi)
val RawDarkBackground = Color(0xFF100E0C)
val RawDarkSurface = Color(0xFF1B1713)
val RawDarkSurfaceElevated = Color(0xFF211C17)
val RawDarkSurfaceHighlight = Color(0xFF28221B)
val RawDarkBorder = Color(0xFF423625)
val RawDarkBorderSubtle = Color(0xFF241F19)
val RawDarkTextPrimary = Color(0xFFF5F1EA)
val RawDarkTextSecondary = Color(0xFFB6ABA0)
val RawDarkTextMuted = Color(0xFF7F7469)
val RawDarkAmber = Color(0xFFE7A94F)
val RawDarkAmberLight = Color(0xFFFBE3B8)
val RawDarkAmberDark = Color(0xFF8F5A1E)
val RawDarkSage = Color(0xFF7FA694)
val RawDarkSageLight = Color(0xFFD8E8DF)
val RawDarkSageDark = Color(0xFF3B5A4C)
val RawDarkTerracotta = Color(0xFFDB7C5C)
val RawDarkTerracottaLight = Color(0xFFF6D4C6)
val RawDarkBlue = Color(0xFF6E96BE)
val RawDarkBlueLight = Color(0xFFD5E3F0)
val RawDarkUrgeRed = Color(0xFFE2604F)
val RawDarkUrgeBackground = Color(0xFF2C1712)
val RawDarkSuccessGreen = Color(0xFF63AE82)

// Raw Light Palette (Porcelana Cálida & Oro)
val RawLightBackground = Color(0xFFF7F4F0)
val RawLightSurface = Color(0xFFFFFFFF)
val RawLightSurfaceElevated = Color(0xFFEFECE5)
val RawLightSurfaceHighlight = Color(0xFFE4DDD2)
val RawLightBorder = Color(0xFFDCD2C5)
val RawLightBorderSubtle = Color(0xFFEAE3D9)
val RawLightTextPrimary = Color(0xFF241B15)
val RawLightTextSecondary = Color(0xFF66594E)
val RawLightTextMuted = Color(0xFF96887B)
val RawLightAmber = Color(0xFFB8731E)
val RawLightAmberLight = Color(0xFFF3D5A5)
val RawLightAmberDark = Color(0xFF7A480D)
val RawLightSage = Color(0xFF3D6B58)
val RawLightSageLight = Color(0xFFD6EAE0)
val RawLightSageDark = Color(0xFF1B3D30)
val RawLightTerracotta = Color(0xFFB85338)
val RawLightTerracottaLight = Color(0xFFF8D8CE)
val RawLightBlue = Color(0xFF3B6B96)
val RawLightBlueLight = Color(0xFFD6E4F0)
val RawLightUrgeRed = Color(0xFFC9352B)
val RawLightUrgeBackground = Color(0xFFFBEBE9)
val RawLightSuccessGreen = Color(0xFF2E7D52)

// Static constant compatibility
val WarmPorcelainBg = Color(0xFFF4F0EB)

// ==========================================
// 2. THEME DATA STRUCTURE & LOCAL PROVIDER
// ==========================================

data class SoltarColors(
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val surfaceHighlight: Color,
    val border: Color,
    val borderSubtle: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val amber: Color,
    val amberLight: Color,
    val amberDark: Color,
    val sage: Color,
    val sageLight: Color,
    val sageDark: Color,
    val terracotta: Color,
    val terracottaLight: Color,
    val blue: Color,
    val blueLight: Color,
    val urgeRed: Color,
    val urgeBackground: Color,
    val successGreen: Color,
    val isDark: Boolean
)

val SoltarDarkColors = SoltarColors(
    background = RawDarkBackground,
    surface = RawDarkSurface,
    surfaceElevated = RawDarkSurfaceElevated,
    surfaceHighlight = RawDarkSurfaceHighlight,
    border = RawDarkBorder,
    borderSubtle = RawDarkBorderSubtle,
    textPrimary = RawDarkTextPrimary,
    textSecondary = RawDarkTextSecondary,
    textMuted = RawDarkTextMuted,
    amber = RawDarkAmber,
    amberLight = RawDarkAmberLight,
    amberDark = RawDarkAmberDark,
    sage = RawDarkSage,
    sageLight = RawDarkSageLight,
    sageDark = RawDarkSageDark,
    terracotta = RawDarkTerracotta,
    terracottaLight = RawDarkTerracottaLight,
    blue = RawDarkBlue,
    blueLight = RawDarkBlueLight,
    urgeRed = RawDarkUrgeRed,
    urgeBackground = RawDarkUrgeBackground,
    successGreen = RawDarkSuccessGreen,
    isDark = true
)

val SoltarLightColors = SoltarColors(
    background = RawLightBackground,
    surface = RawLightSurface,
    surfaceElevated = RawLightSurfaceElevated,
    surfaceHighlight = RawLightSurfaceHighlight,
    border = RawLightBorder,
    borderSubtle = RawLightBorderSubtle,
    textPrimary = RawLightTextPrimary,
    textSecondary = RawLightTextSecondary,
    textMuted = RawLightTextMuted,
    amber = RawLightAmber,
    amberLight = RawLightAmberLight,
    amberDark = RawLightAmberDark,
    sage = RawLightSage,
    sageLight = RawLightSageLight,
    sageDark = RawLightSageDark,
    terracotta = RawLightTerracotta,
    terracottaLight = RawLightTerracottaLight,
    blue = RawLightBlue,
    blueLight = RawLightBlueLight,
    urgeRed = RawLightUrgeRed,
    urgeBackground = RawLightUrgeBackground,
    successGreen = RawLightSuccessGreen,
    isDark = false
)

val LocalSoltarColors = staticCompositionLocalOf {
    SoltarDarkColors
}

// ==========================================
// 3. DYNAMIC COMPOSABLE ACCESSORS
// ==========================================

val SoltarBackground: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.background

val SoltarSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.surface

val SoltarSurfaceElevated: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.surfaceElevated

val SoltarSurfaceHighlight: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.surfaceHighlight

val SoltarBorder: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.border

val SoltarBorderSubtle: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.borderSubtle

val TextPrimary: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.textPrimary

val TextSecondary: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.textSecondary

val TextMuted: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.textMuted

val SoltarAmber: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.amber

val SoltarAmberLight: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.amberLight

val SoltarAmberDark: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.amberDark

val SoltarSage: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.sage

val SoltarSageLight: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.sageLight

val SoltarSageDark: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.sageDark

val SoltarTerracotta: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.terracotta

val SoltarTerracottaLight: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.terracottaLight

val SoltarBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.blue

val SoltarBlueLight: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.blueLight

val UrgeAlertRed: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.urgeRed

val UrgeAlertBackground: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.urgeBackground

val SuccessGreen: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.successGreen

val InfoBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.blue

// Legacy alias compatibility
val ObsidianBackground: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.background

val ObsidianSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.surface

val ObsidianSurfaceVariant: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.surfaceElevated

val ObsidianBorder: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.border

val KintsugiGold: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.amber

val KintsugiGoldLight: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.amberLight

val KintsugiGoldDark: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.amberDark

val KintsugiAmber: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.amber

val SomaticRed: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.urgeRed

val SomaticRedContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.urgeBackground

val SomaticGreen: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.successGreen

val SomaticBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSoltarColors.current.blue

