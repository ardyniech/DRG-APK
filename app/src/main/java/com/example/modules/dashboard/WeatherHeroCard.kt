package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.modules.dashboard.primitives.DriverCommunityAtmosphereBadges
import com.example.modules.dashboard.primitives.GlassCard
import com.example.modules.dashboard.primitives.StatusChips
import com.example.shared.models.DriverMember
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*
import java.util.Calendar

/**
 * Driver Community Hero Card styled with modern floating glassmorphism,
 * squircle ("kotak membulat") icon accents, and driver patrol status.
 */
@Composable
fun WeatherHeroCard(
    member: DriverMember?,
    onUpdateDutyStatus: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isVerified = member?.verificationStatus == VerificationStatus.VERIFIED
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 4..10 -> "Selamat Pagi"
        in 11..14 -> "Selamat Siang"
        in 15..18 -> "Selamat Sore"
        else -> "Selamat Malam"
    }
    val firstName = member?.name?.split(" ")?.firstOrNull() ?: "Rekan Driver"
    val area = member?.baseArea ?: "Malang Raya"

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        backgroundColor = Color.White.copy(alpha = 0.88f),
        contentPadding = 16.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Profile & Squircle Avatar Header
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(DrgGreenContainer)
                        .border(1.5.dp, DrgGreenPrimary.copy(alpha = 0.6f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = member?.name?.take(2)?.uppercase() ?: "DR",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = DrgGreenPrimary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$greeting, $firstName",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DrgTextDark
                        )
                        if (isVerified) {
                            Spacer(modifier = Modifier.width(5.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Terverifikasi",
                                tint = DrgGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = "${member?.driverId ?: "DRG-001"} • ${member?.motorcyclePlate ?: "N 1234 XX"}",
                        fontSize = 12.sp,
                        color = DrgTextSecondary
                    )
                }
            }

            // Atmospheric Driver Patrol Metrics in Squircle Boxes
            DriverCommunityAtmosphereBadges(area = area)

            // Duty status chips (Aktif Narik / Siaga Posko / Rehat)
            StatusChips(
                currentStatus = member?.currentStatus ?: "Aktif Narik",
                onStatusChange = onUpdateDutyStatus
            )
        }
    }
}
