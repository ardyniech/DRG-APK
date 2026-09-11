package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*

@Composable
fun DriverProgressiveOnboardingCard(
    member: DriverMember?,
    onStartShift: () -> Unit,
    onOpenEmergency: () -> Unit,
    onOpenForum: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (member == null) return

    val isShiftReady = member.isOnline
    val isVerified = member.verificationStatus == VerificationStatus.VERIFIED
    val isEngaged = member.kopdarAttendanceCount > 0 || member.loyaltyPoints > 100

    val completedSteps = listOf(isShiftReady, isVerified, isEngaged).count { it }
    val isAllCompleted = completedSteps == 3

    if (isAllCompleted) return // Progressive Disclosure: auto-hidden once master level reached

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(DrgGreenPrimary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Langkah Awal Anggota", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                        Text("$completedSteps dari 3 misi pengenalan selesai", fontSize = 11.sp, color = DrgTextSecondary)
                    }
                }
                LinearProgressIndicator(
                    progress = { completedSteps / 3f },
                    modifier = Modifier.width(60.dp).height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = DrgGreenPrimary,
                    trackColor = DrgOutline
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step 1: Mode Siaga Narik
            OnboardingStepItem(
                title = "Aktifkan Mode Narik & Pantauan",
                isDone = isShiftReady,
                onClick = onStartShift
            )

            // Step 2: Siaga Darurat
            OnboardingStepItem(
                title = "Pahami Tombol Darurat & TRC",
                isDone = isVerified,
                onClick = onOpenEmergency
            )

            // Step 3: Komunitas & Jalur
            OnboardingStepItem(
                title = "Sapa Rekan Ojol di Forum Jalur",
                isDone = isEngaged,
                onClick = onOpenForum
            )
        }
    }
}
