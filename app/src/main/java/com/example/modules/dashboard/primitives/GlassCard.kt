package com.example.modules.dashboard.primitives

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DrgGlassSurfaceSoft
import com.example.shared.atoms.DrgLuminousGlassCard

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = DrgGlassSurfaceSoft,
    borderHighlightColor: Color = Color(0xFF86EFAC),
    contentPadding: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    DrgLuminousGlassCard(
        modifier = modifier,
        shape = shape,
        backgroundColor = backgroundColor,
        borderHighlightColor = borderHighlightColor,
        contentPadding = contentPadding,
        onClick = onClick,
        content = content
    )
}
