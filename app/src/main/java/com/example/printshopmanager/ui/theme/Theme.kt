package com.example.printshopmanager.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrintNavyPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8EAF6),
    onPrimaryContainer = PrintNavyDark,
    secondary = PrintCyanSecondary,
    onSecondary = Color.White,
    secondaryContainer = PrintCyanLight,
    onSecondaryContainer = PrintCyanDark,
    tertiary = PrintAmberAccent,
    onTertiary = Color.White,
    tertiaryContainer = PrintAmberLight,
    onTertiaryContainer = Color(0xFF8A4600),
    background = PrintPaperBackground,
    onBackground = TextPrimary,
    surface = PrintPaperSurface,
    onSurface = TextPrimary,
    surfaceVariant = PrintSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = PrintOutline,
    error = PrintErrorRed,
    onError = Color.White,
    errorContainer = PrintErrorBg,
    onErrorContainer = PrintErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9FA8DA),
    onPrimary = Color(0xFF1A237E),
    primaryContainer = Color(0xFF283593),
    onPrimaryContainer = Color(0xFFE8EAF6),
    secondary = Color(0xFF80DEEA),
    onSecondary = Color(0xFF004D40),
    secondaryContainer = Color(0xFF006064),
    onSecondaryContainer = Color(0xFFE0F7FA),
    tertiary = Color(0xFFFFCC80),
    onTertiary = Color(0xFF4A2800),
    background = Color(0xFF121418),
    onBackground = Color(0xFFE1E2E8),
    surface = Color(0xFF1A1C22),
    onSurface = Color(0xFFE1E2E8),
    surfaceVariant = Color(0xFF252932),
    onSurfaceVariant = Color(0xFFC4C7D0),
    outline = Color(0xFF424750),
    error = Color(0xFFEF9A9A),
    onError = Color(0xFF601410)
)

@Composable
fun PrintShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
