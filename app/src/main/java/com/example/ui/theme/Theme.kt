package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SoltarColorScheme = darkColorScheme(
  primary = SoltarAmber,
  onPrimary = SoltarBackground,
  primaryContainer = SoltarAmberDark,
  onPrimaryContainer = SoltarAmberLight,
  secondary = SoltarSage,
  onSecondary = SoltarBackground,
  secondaryContainer = SoltarSageDark,
  onSecondaryContainer = SoltarSageLight,
  tertiary = SoltarTerracotta,
  onTertiary = SoltarBackground,
  background = SoltarBackground,
  onBackground = TextPrimary,
  surface = SoltarSurface,
  onSurface = TextPrimary,
  surfaceVariant = SoltarSurfaceElevated,
  onSurfaceVariant = TextSecondary,
  outline = SoltarBorder,
  outlineVariant = SoltarBorderSubtle,
  error = UrgeAlertRed,
  errorContainer = UrgeAlertBackground,
  onError = Color.White
)

@Composable
fun SoltarTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = SoltarColorScheme,
    typography = Typography,
    content = content
  )
}

// Aliases for compatibility
@Composable
fun AtalayaTheme(content: @Composable () -> Unit) {
  SoltarTheme(content = content)
}

@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
  SoltarTheme(content = content)
}
