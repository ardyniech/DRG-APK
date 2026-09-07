package com.example.modules.members

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.AdminLog
import com.example.ui.theme.*

@Composable
fun AdminLogsTabSection(adminLogs: List<AdminLog>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = DrgAmberSecondary.copy(alpha = 0.1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DrgAmberSecondary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Buku Log Transparansi Otoritas DRG",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = DrgAmberSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Setiap keputusan pergantian jabatan, pengangkatan pengurus, atau penangguhan anggota dicatat seketika tanpa rekayasa data demi transparansi total.",
                        fontSize = 10.sp,
                        color = DrgTextSecondary
                    )
                }
            }
        }

        if (adminLogs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Belum ada catatan log otorisasi pengurus.", fontSize = 11.sp, color = DrgTextMuted)
                }
            }
        } else {
            items(adminLogs) { log ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = DrgSurface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, DrgOutline, RoundedCornerShape(10.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(DrgGreenContainer)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = log.actorRole,
                                        color = DrgGreenPrimary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = log.actorName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = DrgTextPrimary
                                )
                            }
                            Text(
                                text = log.timestamp,
                                fontSize = 10.sp,
                                color = DrgTextMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Melakukan tindakan ${log.action} kepada:",
                            fontSize = 11.sp,
                            color = DrgTextSecondary
                        )
                        Text(
                            text = log.targetName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = DrgGreenPrimary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
