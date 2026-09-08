package com.example.modules.gamification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.PointTransaction
import com.example.ui.theme.*

@Composable
fun PointHistoryTabContent(pointTransactions: List<PointTransaction>, currentMember: DriverMember?) {
    val myTransactions = pointTransactions.filter { 
        it.targetMemberId == currentMember?.id || it.giverMemberId == currentMember?.id 
    }.sortedByDescending { it.timestamp }

    if (myTransactions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Default.History, contentDescription = "Belum Ada Riwayat", tint = DrgTextMuted, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Belum ada riwayat transaksi koin.", fontSize = 12.sp, color = DrgTextSecondary, fontWeight = FontWeight.Medium)
            }
        }
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
            items(myTransactions) { tx ->
                val isReceived = tx.targetMemberId == currentMember?.id
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DrgSurface,
                    modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(if (isReceived) DrgGreenContainer else DrgAmberSecondary.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
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
                            Text(text = tx.reason, fontSize = 11.sp, color = DrgTextSecondary)
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
