package com.example.modules.dashboard.primitives

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.shared.atoms.DrgGlassCard
import com.example.ui.theme.DrgGreenPrimary

/**
 * GlassCard kanonis dashboard — hanya wrapper DrgGlassCard.
 * Dulu duplikat shadow/border/clickable DrgGlassCard (~85% overlap); kini delegasi.
 * Signature dipertahankan agar 8 callers dashboard tidak perlu diubah.
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
    val brush = Brush.linearGradient(
        listOf(
            borderHighlightColor.copy(alpha = 0.95f),
            Color.White.copy(alpha = 0.45f),
            DrgGreenPrimary.copy(alpha = 0.15f)
        )
    )
    DrgGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        backgroundColor = backgroundColor,
        borderBrush = brush,
        onClick = onClick
    ) {
        Box(modifier = Modifier.padding(contentPadding)) { content() }
    }
}
