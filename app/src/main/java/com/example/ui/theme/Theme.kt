package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Color Aliases mapped to Grab Green Brand Theme
val DrgBackground = DrgCanvasBg
val DrgSurface = DrgCardSurface
val DrgSurfaceVariant = Color(0xFFE2F4E9)

val DrgBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFBCE8D0), // Radiant Grab Emerald atmospheric top aura
        Color(0xFFD6F2E2), // Vibrant fresh mint middle
        Color(0xFFEBF6F0)  // Luminous pearl-mint base
    )
)

val DrgGlassBorderBrush = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.95f),
        Color.White.copy(alpha = 0.50f),
        DrgGrabGreenPrimary.copy(alpha = 0.25f)
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
