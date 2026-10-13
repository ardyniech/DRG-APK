package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.dashboard.primitives.GlassCard
import com.example.modules.dashboard.primitives.StatusChips
import com.example.shared.models.DriverMember
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*
import java.util.Calendar

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

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        backgroundColor = Color.White.copy(alpha = 0.85f),
        contentPadding = 16.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            WeatherAtmosphereRow(baseArea = member?.baseArea)

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(DrgGreenContainer)
                        .border(1.5.dp, DrgGreenPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = member?.name?.take(2)?.uppercase() ?: "DR",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = DrgGreenPrimary
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$greeting, $firstName",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = DrgTextDark
                        )
                        if (isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Verified, contentDescription = "Verified", tint = DrgGreenPrimary, modifier = Modifier.size(15.dp))
                        }
                    }
                    Text(
                        text = "${member?.driverId ?: "DRG-001"} • ${member?.motorcyclePlate ?: "N 1234 XX"}",
                        fontSize = 11.sp,
                        color = DrgTextSecondary
                    )
                }
            }

            StatusChips(
                currentStatus = member?.currentStatus ?: "Aktif Narik",
                onStatusChange = onUpdateDutyStatus
            )
        }
    }
}
