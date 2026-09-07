package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Color Aliases mapped to Grab Green Brand Theme
val DrgBackground = DrgCanvasBg
val DrgSurface = DrgCardSurface
val DrgSurfaceVariant = Color(0xFFEBF6F0)

val DrgBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFD2EFE0), // Subtle Grab Green top tint
        Color(0xFFE9F5EE), // Soft fresh middle
        Color(0xFFF4F9F6)  // Light clean bottom
    )
)
val DrgOutline = DrgBorderLight
val DrgTextPrimary = DrgTextDark
val DrgTextSecondary = DrgTextSlate
val DrgGreenPrimary = DrgGrabGreenPrimary
val DrgGreenDark = DrgGrabGreenDark
val DrgGreenContainer = DrgGrabGreenContainer
val DrgOnGreenContainer = DrgOnGrabGreenContainer
val DrgGreenLight = Color(0xFF00B14F)
val DrgAmberSecondary = DrgGrabGreenPrimary
val DrgAmberWarning = Color(0xFFF59E0B)
val DrgGoldReward = Color(0xFFF59E0B)
val DrgOrangeWarning = Color(0xFFEA580C)
val DrgRedDanger = DrgRedPanic
val DrgBlueInfo = DrgIndigoAccent
val DrgRadarGlow = Color(0xFF00B14F)
val DrgTrafficGreen = Color(0xFF10B981)
val DrgTrafficOrange = Color(0xFFF59E0B)
val DrgTrafficRed = Color(0xFFEF4444)

private val GrabColorScheme = lightColorScheme(
    primary = DrgGrabGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = DrgGrabGreenContainer,
    onPrimaryContainer = DrgOnGrabGreenContainer,
    secondary = DrgGrabGreenDark,
    onSecondary = Color.White,
    secondaryContainer = DrgGrabGreenContainer,
    onSecondaryContainer = DrgOnGrabGreenContainer,
    tertiary = DrgGrabGreenPrimary,
    background = DrgCanvasBg,
    surface = DrgCardSurface,
    onBackground = DrgTextDark,
    onSurface = DrgTextDark,
    outline = DrgBorderLight
)

@Composable
fun DRGDriverTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GrabColorScheme,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}

