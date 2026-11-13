package com.example.modules.dashboard.primitives

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.dashboard.theme.DashboardFocusMode
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary

/**
 * Control-Center style frosted pill bar for choosing 3 luminous visual themes
 * and 2 operational focus modes (Mode Satgas A vs Mode Administrasi B).
 */
@Composable
fun DashboardThemeModeSelector(
    selectedTheme: DashboardVisualTheme,
    selectedMode: DashboardFocusMode,
    onSelectTheme: (DashboardVisualTheme) -> Unit,
    onSelectMode: (DashboardFocusMode) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = selectedTheme.glassBg,
        borderHighlightColor = selectedTheme.glassBorderColor,
        contentPadding = 12.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Icon(Icons.Default.Palette, contentDescription = null, tint = selectedTheme.primaryColor, modifier = Modifier.size(15.dp))
                    Text(text = "Tema", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    DashboardVisualTheme.values().forEach { theme ->
                        val isSelected = theme == selectedTheme
                        val chipBg by animateColorAsState(if (isSelected) theme.primaryColor.copy(alpha = 0.16f) else Color.White.copy(alpha = 0.55f), tween(250), label = "tBg")
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(11.dp))
                                .background(chipBg)
                                .border(1.2.dp, if (isSelected) theme.primaryColor else Color.White.copy(alpha = 0.85f), RoundedCornerShape(11.dp))
                                .bouncyClick { onSelectTheme(theme) }
                                .padding(horizontal = 9.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(theme.primaryColor))
                                Text(
                                    text = theme.title.split(" ").first(),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) theme.primaryColor else DrgTextSecondary
                                )
                            }
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Icon(Icons.Default.Tune, contentDescription = null, tint = selectedTheme.primaryColor, modifier = Modifier.size(15.dp))
                    Text(text = "Mode", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    DashboardFocusMode.values().forEach { mode ->
                        val isSelected = mode == selectedMode
                        val modeBg by animateColorAsState(if (isSelected) selectedTheme.primaryColor else Color.White.copy(alpha = 0.55f), tween(250), label = "mBg")
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(11.dp))
                                .background(modeBg)
                                .border(1.dp, if (isSelected) selectedTheme.primaryColor else Color.White.copy(alpha = 0.85f), RoundedCornerShape(11.dp))
                                .bouncyClick { onSelectMode(mode) }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (mode == DashboardFocusMode.SATGAS_TRC) "Satgas (A)" else "Admin (B)",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else DrgTextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}
