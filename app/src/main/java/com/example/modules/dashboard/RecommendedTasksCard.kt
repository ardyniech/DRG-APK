package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*

@Composable
fun RecommendedTasksCard(
    member: DriverMember?,
    modifier: Modifier = Modifier
) {
    val isConsentOn = member?.isLocationSharingConsent ?: true
    val isVerified = member?.verificationStatus == VerificationStatus.VERIFIED

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DrgSurface),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, DrgOutline, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "📋 DAFTAR REKOMENDASI TUGAS PENDING",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = DrgAmberSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))

            TaskStatusRow(
                title = "Nyalakan Uplink GPS Live Radar",
                subtitle = "Penting untuk keselamatan berkendara & koordinasi Satgas",
                statusLabel = if (isConsentOn) "SELESAI" else "PENDING",
                isSuccess = isConsentOn
            )

            HorizontalDivider(color = DrgOutline.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 6.dp))

            TaskStatusRow(
                title = "Absen Kehadiran Kopdar DRG",
                subtitle = "Saling sapa anggota ojol Malang Raya & raih +50 Poin",
                statusLabel = "READY",
                isSuccess = true
            )

            HorizontalDivider(color = DrgOutline.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 6.dp))

            TaskStatusRow(
                title = "Verifikasi Status Keanggotaan KTA",
                subtitle = "Verifikasi KTP/SIM/STNK oleh Satgas & Ketua DRG",
                statusLabel = if (isVerified) "VERIFIED" else "SCREENING",
                isSuccess = isVerified,
                isWarning = !isVerified
            )
        }
    }
}

@Composable
private fun TaskStatusRow(
    title: String,
    subtitle: String,
    statusLabel: String,
    isSuccess: Boolean,
    isWarning: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
            Text(text = subtitle, fontSize = 10.sp, color = DrgTextSecondary)
        }
        val (bgColor, textColor) = when {
            isSuccess -> Pair(DrgGreenContainer, DrgGreenPrimary)
            isWarning -> Pair(DrgAmberContainer, DrgAmberDark)
            else -> Pair(DrgRedContainer, DrgRedDanger)
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(bgColor)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(text = statusLabel, color = textColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}
