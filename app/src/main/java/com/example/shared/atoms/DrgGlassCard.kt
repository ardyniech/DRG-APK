package com.example.shared.atoms

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgGlassBorderBrush
import com.example.ui.theme.DrgShadowGreenGlass

/**
 * Shared Frosted Glass Card aligned to the active 1-DNA Luminous Theme via LocalAppVisualTheme.
 */
@Composable
fun DrgGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color? = null,
    borderBrush: Brush? = null,
    borderWidth: Dp = 1.25.dp,
    elevation: Dp = 8.dp,
    shadowColor: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val theme = LocalAppVisualTheme.current
    val resolvedBg by animateColorAsState(backgroundColor ?: theme.glassBg, tween(320), label = "glassCardBg")
    val resolvedShadow by animateColorAsState(shadowColor ?: theme.glassBorderColor.copy(alpha = 0.24f), tween(320), label = "glassCardShadow")
    val resolvedBorder = borderBrush ?: Brush.linearGradient(
        listOf(Color.White.copy(alpha = 0.96f), Color.White.copy(alpha = 0.52f), theme.glassBorderColor.copy(alpha = 0.45f))
    )

    val baseModifier = modifier
        .shadow(elevation = elevation, shape = shape, ambientColor = resolvedShadow, spotColor = resolvedShadow)
        .clip(shape)
        .background(
            Brush.verticalGradient(
                listOf(resolvedBg, resolvedBg.copy(alpha = (resolvedBg.alpha * 0.90f).coerceAtLeast(0.72f)))
            )
        )
        .border(BorderStroke(borderWidth, resolvedBorder), shape)
        .let { if (onClick != null) it.bouncyClick { onClick() } else it }

    Box(modifier = baseModifier, content = content)
}
