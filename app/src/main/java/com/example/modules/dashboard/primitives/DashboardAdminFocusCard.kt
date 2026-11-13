package com.example.modules.dashboard.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgWarningText
import com.example.ui.theme.DrgPurpleAccent
import com.example.core.viewmodel.MainNavTab
import com.example.modules.dashboard.CommunityStatusMiniTile
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily

/**
 * Administration Focus Card (Mode B) styled with matching luminous frosted glass.
 */
@Composable
fun DashboardAdminFocusCard(
    kasFormatted: String,
    memberCount: Int,
    verifiedCount: Int,
    transactionCount: Int,
    visualTheme: DashboardVisualTheme,
    onNavigateTab: (MainNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = visualTheme.primaryColor
    val verifiedRatio = if (memberCount > 0) (verifiedCount.toFloat() / memberCount.toFloat()).coerceIn(0f, 1f) else 1f

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 16.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(visualTheme.pillBg.copy(alpha = 0.85f))
                    .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(accent))
                    Text(text = "Tata Kelola & Kas Organisasi", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                }
                Text(text = "Mode Admin (B)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accent)
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Verifikasi KTA Anggota", fontSize = 11.sp, color = DrgTextSecondary)
                    Text(text = "$verifiedCount / $memberCount Aktif", fontFamily = SpaceGroteskFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accent)
                }
                LinearProgressIndicator(
                    progress = { verifiedRatio },
                    modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(4.dp)),
                    color = accent,
                    trackColor = Color.White.copy(alpha = 0.65f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CommunityStatusMiniTile(
                    title = "Total Kas DRG", value = "Rp $kasFormatted",
                    icon = Icons.Default.AccountBalanceWallet, color = accent,
                    onClick = { onNavigateTab(MainNavTab.KAS) }, modifier = Modifier.weight(1f)
                )
                CommunityStatusMiniTile(
                    title = "Data Anggota", value = "$memberCount Driver",
                    icon = Icons.Default.Group, color = visualTheme.secondaryColor,
                    onClick = { onNavigateTab(MainNavTab.MEMBERS) }, modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CommunityStatusMiniTile(
                    title = "Mutasi Kas", value = "$transactionCount Catatan",
                    icon = Icons.AutoMirrored.Filled.ReceiptLong, color = DrgWarningText,
                    onClick = { onNavigateTab(MainNavTab.KAS) }, modifier = Modifier.weight(1f)
                )
                CommunityStatusMiniTile(
                    title = "Log & Peran", value = "Arsip Lengkap",
                    icon = Icons.AutoMirrored.Filled.Assignment, color = DrgPurpleAccent,
                    onClick = { onNavigateTab(MainNavTab.PROFILE) }, modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
