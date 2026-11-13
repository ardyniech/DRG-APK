package com.example.shared.atoms

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
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
import com.example.ui.theme.DrgGlassSurfaceSoft
import com.example.shared.utils.bouncyClick

/**
 * Shared Luminous Frosted Glass Card with top-left specular bevel rim and optional bouncy click handler.
 */
@Composable
fun DrgLuminousGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = DrgGlassSurfaceSoft,
    borderHighlightColor: Color = Color(0xFF86EFAC),
    contentPadding: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val animatedBg by animateColorAsState(backgroundColor, tween(350), label = "luminousCardBg")
    val animatedBorder by animateColorAsState(borderHighlightColor, tween(350), label = "luminousCardBorder")
    val borderBrush = Brush.linearGradient(
        colors = listOf(Color.White.copy(alpha = 0.96f), Color.White.copy(alpha = 0.50f), animatedBorder.copy(alpha = 0.42f))
    )
    val baseMod = modifier
        .shadow(10.dp, shape, ambientColor = animatedBorder.copy(alpha = 0.25f), spotColor = animatedBorder.copy(alpha = 0.16f))
        .clip(shape)
        .background(Brush.verticalGradient(listOf(animatedBg, animatedBg.copy(alpha = (animatedBg.alpha * 0.88f).coerceAtLeast(0.68f)))))
        .border(1.3.dp, borderBrush, shape)
        .let { m -> if (onClick != null) m.bouncyClick { onClick() } else m }
        .padding(contentPadding)

    Box(modifier = baseMod, content = content)
}
