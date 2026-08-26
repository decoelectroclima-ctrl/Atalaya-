package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

val SoltarDarkColorScheme = darkColorScheme(
  primary = RawDarkAmber,
  onPrimary = RawDarkBackground,
  primaryContainer = RawDarkAmberDark,
  onPrimaryContainer = RawDarkAmberLight,
  secondary = RawDarkSage,
  onSecondary = RawDarkBackground,
  secondaryContainer = RawDarkSageDark,
  onSecondaryContainer = RawDarkSageLight,
  tertiary = RawDarkTerracotta,
  onTertiary = RawDarkBackground,
  background = RawDarkBackground,
  onBackground = RawDarkTextPrimary,
  surface = RawDarkSurface,
  onSurface = RawDarkTextPrimary,
  surfaceVariant = RawDarkSurfaceElevated,
  onSurfaceVariant = RawDarkTextSecondary,
  outline = RawDarkBorder,
  outlineVariant = RawDarkBorderSubtle,
  error = RawDarkUrgeRed,
  errorContainer = RawDarkUrgeBackground,
  onError = Color.White
)

val SoltarLightColorScheme = lightColorScheme(
  primary = RawLightAmber,
  onPrimary = Color.White,
  primaryContainer = RawLightAmberLight,
  onPrimaryContainer = RawLightAmberDark,
  secondary = RawLightSage,
  onSecondary = Color.White,
  secondaryContainer = RawLightSageLight,
  onSecondaryContainer = RawLightSageDark,
  tertiary = RawLightTerracotta,
  onTertiary = Color.White,
  background = RawLightBackground,
  onBackground = RawLightTextPrimary,
  surface = RawLightSurface,
  onSurface = RawLightTextPrimary,
  surfaceVariant = RawLightSurfaceElevated,
  onSurfaceVariant = RawLightTextSecondary,
  outline = RawLightBorder,
  outlineVariant = RawLightBorderSubtle,
  error = RawLightUrgeRed,
  errorContainer = RawLightUrgeBackground,
  onError = Color.White
)

@Composable
fun SoltarTheme(
  themeMode: String = "DARK",
  content: @Composable () -> Unit
) {
  val isSystemDark = isSystemInDarkTheme()
  val isDark = when (themeMode.uppercase()) {
    "LIGHT" -> false
    "SYSTEM" -> isSystemDark
    else -> true // "DARK" is default
  }

  val colorScheme = if (isDark) SoltarDarkColorScheme else SoltarLightColorScheme
  val customColors = if (isDark) SoltarDarkColors else SoltarLightColors

  CompositionLocalProvider(LocalSoltarColors provides customColors) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
  }
}

// Aliases for compatibility
@Composable
fun SoltarTheme(
  darkTheme: Boolean,
  content: @Composable () -> Unit
) {
  SoltarTheme(themeMode = if (darkTheme) "DARK" else "LIGHT", content = content)
}

@Composable
fun AdrianaTheme(content: @Composable () -> Unit) {
  SoltarTheme(content = content)
}

@Composable
fun AtalayaTheme(content: @Composable () -> Unit) {
  SoltarTheme(content = content)
}

@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
  SoltarTheme(content = content)
}

