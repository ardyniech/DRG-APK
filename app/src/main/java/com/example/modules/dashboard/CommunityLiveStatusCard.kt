package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.MainNavTab
import com.example.ui.theme.*

@Composable
fun CommunityLiveStatusCard(
    activeMemberCount: Int,
    activeHazardCount: Int,
    kasFormatted: String,
    poskoCount: Int,
    onNavigateTab: (MainNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSafe = activeHazardCount == 0
    val statusColor = if (isSafe) DrgGreenPrimary else DrgAmberSecondary
    val statusBg = if (isSafe) DrgGreenContainer else DrgAmberContainer
    val statusMsg = if (isSafe) "Kondisi Jalur Aman & Terkendali" else "$activeHazardCount Peringatan Bahaya di Radar"

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(statusBg)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(statusColor))
                    Text(text = statusMsg, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = statusColor)
                }
                Text(text = "Live Radar", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = statusColor)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CommunityStatusMiniTile(
                    title = "Driver Siaga",
                    value = "$activeMemberCount Online",
                    icon = Icons.Default.People,
                    color = DrgGreenPrimary,
                    onClick = { onNavigateTab(MainNavTab.MEMBERS) },
                    modifier = Modifier.weight(1f)
                )
                CommunityStatusMiniTile(
                    title = "Bahaya Jalan",
                    value = "$activeHazardCount Titik",
                    icon = Icons.Default.Warning,
                    color = if (activeHazardCount > 0) DrgAmberSecondary else DrgGreenPrimary,
                    onClick = { onNavigateTab(MainNavTab.RADAR) },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CommunityStatusMiniTile(
                    title = "Kas Transparan",
                    value = "Rp $kasFormatted",
                    icon = Icons.Default.AccountBalanceWallet,
                    color = DrgGreenPrimary,
                    onClick = { onNavigateTab(MainNavTab.KAS) },
                    modifier = Modifier.weight(1f)
                )
                CommunityStatusMiniTile(
                    title = "Posko Rehat",
                    value = "$poskoCount Titik",
                    icon = Icons.Default.Place,
                    color = DrgGreenDark,
                    onClick = { onNavigateTab(MainNavTab.RADAR) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CommunityStatusMiniTile(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DrgSurface)
            .border(0.8.dp, DrgOutline.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(15.dp))
                Text(text = title, fontSize = 11.sp, color = DrgTextSecondary)
            }
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
        }
    }
}
