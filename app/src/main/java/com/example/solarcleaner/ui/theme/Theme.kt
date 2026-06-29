package com.example.solarcleaner.ui.theme

import android.app.Activity
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LuxuryDarkColorScheme = darkColorScheme(
    primary = SolarGold,
    secondary = SolarAccentBlue,
    tertiary = SolarAccentGreen,
    background = SolarDeepNavy,
    surface = SolarSurfaceDark,
    surfaceVariant = SolarCardDark,
    outline = SolarOutlineDark,
    onPrimary = SolarDeepNavy,
    onSecondary = SolarTextPrimary,
    onTertiary = SolarTextPrimary,
    onBackground = SolarTextPrimary,
    onSurface = SolarTextPrimary,
    onSurfaceVariant = SolarTextSecondary
)

@Composable
fun SolarCleanerTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LuxuryDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
