package com.example.modules.dashboard.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DrgGreenPrimary

/**
 * Modern Glassmorphic Container with translucent frosted glass fill,
 * soft rounded corners (large curve), and subtle inner-border highlight.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = Color.White.copy(alpha = 0.82f),
    borderHighlightColor: Color = Color.White,
    contentPadding: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val highlightBrush = Brush.linearGradient(
        colors = listOf(
            borderHighlightColor.copy(alpha = 0.95f),
            Color.White.copy(alpha = 0.45f),
            DrgGreenPrimary.copy(alpha = 0.15f)
        )
    )

    val baseModifier = modifier
        .fillMaxWidth()
        .shadow(
            elevation = 6.dp,
            shape = shape,
            ambientColor = Color(0x18003617),
            spotColor = Color(0x12003617)
        )
        .clip(shape)
        .border(
            border = BorderStroke(1.2.dp, highlightBrush),
            shape = shape
        )

    val finalModifier = if (onClick != null) {
        baseModifier.clickable { onClick() }
    } else {
        baseModifier
    }

    Surface(
        modifier = finalModifier,
        shape = shape,
        color = backgroundColor,
        tonalElevation = 0.dp
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}
