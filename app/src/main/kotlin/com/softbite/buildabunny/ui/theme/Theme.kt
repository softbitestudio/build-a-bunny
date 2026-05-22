package com.softbite.buildabunny.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BunnyPink,
    onPrimary = Color.White,
    primaryContainer = BunnyPinkLight,
    onPrimaryContainer = BunnyPinkDark,
    secondary = BunnyLavender,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3E5F5),
    onSecondaryContainer = Color(0xFF4A148C),
    background = BunnySurface,
    onBackground = BunnyOnSurface,
    surface = BunnySurface,
    onSurface = BunnyOnSurface,
    surfaceVariant = BunnyNeutralLight,
    onSurfaceVariant = BunnyNeutral,
    outline = BunnyOutline,
)

@Composable
fun BuildABunnyTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = LightColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content,
    )
}
