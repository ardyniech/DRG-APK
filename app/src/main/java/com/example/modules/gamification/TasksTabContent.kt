package com.example.modules.gamification

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.*
import com.example.ui.theme.*

@Composable
fun TasksTabContent(
    tasks: List<CommunityTask>,
    currentMember: DriverMember?,
    onClaim: (String) -> Unit,
    onComplete: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(tasks) { t ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DrgSurface,
                modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = t.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                        Text(text = "+${t.rewardPoints} XP", fontWeight = FontWeight.Black, fontSize = 12.sp, color = DrgAmberSecondary)
                    }
                    Text(text = t.description, fontSize = 11.sp, color = DrgTextSecondary)
                    Text(text = "Dikeluarkan oleh: ${t.issuerRole.title} • Batas Waktu: ${t.deadline}", fontSize = 10.sp, color = DrgTextMuted)

                    when (t.status) {
                        TaskStatus.OPEN -> {
                            Button(
                                onClick = { onClaim(t.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary),
                                modifier = Modifier.fillMaxWidth().height(36.dp)
                            ) {
                                Text("Ambil Tugas Komunitas Ini", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        TaskStatus.IN_PROGRESS -> {
                            if (t.assignedMemberId == currentMember?.id) {
                                Button(
                                    onClick = { onComplete(t.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                                    modifier = Modifier.fillMaxWidth().height(36.dp)
                                ) {
                                    Text("Laporkan Tugas Selesai", fontSize = 11.sp)
                                }
                            } else {
                                Text("Status: Sedang Dikerjakan oleh ${t.assignedMemberName}", fontSize = 11.sp, color = DrgAmberDark)
                            }
                        }
                        TaskStatus.COMPLETED, TaskStatus.VERIFIED -> {
                            Text("Status: Selesai & Terverifikasi ✅", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                        }
                    }
                }
            }
        }
    }
}
