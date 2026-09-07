package com.example.modules.gamification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.RoleBadge
import com.example.shared.models.*
import com.example.ui.theme.*

@Composable
fun LeaderboardTabContent(members: List<DriverMember>, onAwardPointsClick: () -> Unit) {
    val sortedMembers = members.sortedByDescending { it.loyaltyPoints }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = onAwardPointsClick,
            colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(44.dp)
        ) {
            Icon(Icons.Default.VolunteerActivism, contentDescription = "Beri Poin", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Beri Poin Apresiasi Kepadatan / Bantuan Rekan", fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            itemsIndexed(sortedMembers) { index, m ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DrgSurface,
                    modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "#${index + 1}", fontWeight = FontWeight.Black, fontSize = 14.sp, color = if (index < 3) DrgAmberDark else DrgTextMuted)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = m.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    RoleBadge(role = m.role)
                                }
                                Text(text = m.motorcycleModel, fontSize = 11.sp, color = DrgTextSecondary)
                            }
                        }
                        Text(text = "⭐ ${m.loyaltyPoints} XP", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgAmberSecondary)
                    }
                }
            }
        }
    }
}

@Composable
fun BadgesTabContent(badges: List<BadgeItem>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(badges) { b ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DrgSurface,
                modifier = Modifier.fillMaxWidth().border(1.dp, if (b.isEarned) DrgAmberSecondary else DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = b.iconEmoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = b.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                        Text(text = b.description, fontSize = 11.sp, color = DrgTextSecondary)
                        Text(
                            text = if (b.isEarned) "DIERAIH (${b.earnedDate})" else "Dibutuhkan ${b.requiredXp} XP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (b.isEarned) DrgGreenPrimary else DrgTextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PointHistoryTabContent(pointTransactions: List<PointTransaction>, currentMember: DriverMember?) {
    val myTransactions = pointTransactions.filter { 
        it.targetMemberId == currentMember?.id || it.giverMemberId == currentMember?.id 
    }.sortedByDescending { it.timestamp }

    if (myTransactions.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Belum Ada Riwayat",
                    tint = DrgTextMuted,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Belum ada riwayat transaksi koin.",
                    fontSize = 12.sp,
                    color = DrgTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(myTransactions) { tx ->
                val isReceived = tx.targetMemberId == currentMember?.id
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DrgSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    if (isReceived) DrgGreenContainer else DrgAmberSecondary.copy(alpha = 0.15f),
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isReceived) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                contentDescription = if (isReceived) "Masuk" else "Keluar",
                                tint = if (isReceived) DrgGreenPrimary else DrgAmberSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isReceived) "Menerima Poin Apresiasi" else "Memberikan Poin Apresiasi",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = DrgTextPrimary
                            )
                            Text(
                                text = tx.reason,
                                fontSize = 11.sp,
                                color = DrgTextSecondary
                            )
                            Text(
                                text = if (isReceived) "Dari: ${tx.giverMemberName} (${tx.giverRole.shortName})" else "Untuk: ${tx.targetMemberName}",
                                fontSize = 10.sp,
                                color = DrgTextMuted
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isReceived) "+${tx.weightPoints} Koin" else "-${tx.weightPoints} Koin",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = if (isReceived) DrgGreenPrimary else DrgAmberSecondary
                        )
                    }
                }
            }
        }
    }
}
