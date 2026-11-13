package com.example.modules.dashboard.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.MainNavTab
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.shared.models.CommunityNotification
import com.example.ui.theme.DrgTextDark

/**
 * Frosted Glass Live Activity Ticker Capsule
 */
@Composable
fun LiveActivityTickerCapsule(
    announcements: List<CommunityNotification>,
    visualTheme: DashboardVisualTheme,
    onNavigateTab: (MainNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val latest = announcements.firstOrNull()?.title ?: "Kopdar Rutin & Cek Jalur Rawan di Posko Utama"
    val accent = visualTheme.primaryColor

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 12.dp,
        onClick = { onNavigateTab(MainNavTab.FORUM) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accent.copy(alpha = 0.16f))
                    .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Campaign, contentDescription = "Kabar", tint = accent, modifier = Modifier.size(18.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "KABAR KOMUNITAS", fontSize = 9.sp, fontWeight = FontWeight.Black, color = accent)
                Text(
                    text = latest,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgTextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(visualTheme.pillBg.copy(alpha = 0.9f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = "INFO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accent)
            }
        }
    }
}
