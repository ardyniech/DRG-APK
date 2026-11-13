package com.example.shared.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.utils.bouncyClick

@Composable
fun DrgGlassPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    val theme = LocalAppVisualTheme.current
    val shape = RoundedCornerShape(18.dp)
    val gradient = Brush.horizontalGradient(listOf(theme.primaryColor, theme.secondaryColor))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(8.dp, shape, ambientColor = theme.primaryColor.copy(alpha = 0.36f), spotColor = theme.primaryColor.copy(alpha = 0.25f))
            .clip(shape)
            .background(if (enabled) gradient else Brush.linearGradient(listOf(Color(0xFF94A3B8), Color(0xFF64748B))))
            .let { if (enabled) it.bouncyClick { onClick() } else it },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Text(text = text, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.3.sp)
        }
    }
}

@Composable
fun DrgGlassSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 0.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(text = text, color = theme.primaryColor, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}
