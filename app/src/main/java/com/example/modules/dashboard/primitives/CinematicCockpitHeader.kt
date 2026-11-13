package com.example.modules.dashboard.primitives

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.dashboard.theme.DashboardFocusMode
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.shared.models.DriverMember
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*
import java.util.Calendar

/**
 * Apple-Weather-inspired Floating Glass Cockpit Hero Stage.
 */
@Composable
fun CinematicCockpitHeader(
    member: DriverMember?,
    activeMemberCount: Int,
    totalMembersCount: Int,
    kasFormatted: String,
    visualTheme: DashboardVisualTheme,
    focusMode: DashboardFocusMode,
    onTriggerEmergency: () -> Unit,
    onUpdateDutyStatus: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isVerified = member?.verificationStatus == VerificationStatus.VERIFIED
    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 4..10 -> "Selamat Pagi"; in 11..14 -> "Selamat Siang"
        in 15..18 -> "Selamat Sore"; else -> "Selamat Malam"
    }
    val firstName = member?.name?.split(" ")?.firstOrNull() ?: "Rekan Driver"
    val accent = visualTheme.primaryColor

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 16.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(visualTheme.pillBg.copy(alpha = 0.85f))
                        .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = accent, modifier = Modifier.size(13.dp))
                    Text(text = "${member?.baseArea ?: "Malang Raya"} • ${focusMode.tag}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accent)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(accent))
                    Text(text = "$greeting, $firstName", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DrgTextSlate)
                    if (isVerified) {
                        Icon(Icons.Default.Verified, contentDescription = "Terverifikasi", tint = accent, modifier = Modifier.size(15.dp))
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                AnimatedContent(targetState = focusMode, label = "heroMetric", modifier = Modifier.weight(1f)) { mode ->
                    val focalTitle = if (mode == DashboardFocusMode.SATGAS_TRC) "$activeMemberCount Siaga" else "Rp $kasFormatted"
                    val focalSub = if (mode == DashboardFocusMode.SATGAS_TRC) {
                        "${member?.driverId ?: "DRG-001"} • ${member?.motorcyclePlate ?: "N 1234 XX"} • $totalMembersCount Anggota"
                    } else {
                        "Kas Komunitas • $totalMembersCount Anggota Terdaftar"
                    }
                    Column {
                        Text(
                            text = focalTitle,
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = if (mode == DashboardFocusMode.SATGAS_TRC) 32.sp else 26.sp,
                            color = DrgTextDark
                        )
                        Text(text = focalSub, fontSize = 12.sp, color = DrgTextSecondary)
                    }
                }
                CockpitHeaderSosButton(onClick = onTriggerEmergency)
            }

            StatusChips(
                currentStatus = member?.currentStatus ?: "Aktif Narik",
                onStatusChange = onUpdateDutyStatus,
                accentColor = accent
            )
        }
    }
}
