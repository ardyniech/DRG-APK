package com.example.modules.members

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.AdminLog
import com.example.ui.theme.*

@Composable
fun AdminLogsTabSection(adminLogs: List<AdminLog>) {
    val theme = LocalAppVisualTheme.current
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)) {
        item {
            DrgLuminousGlassCard(
                shape = RoundedCornerShape(18.dp),
                backgroundColor = DrgAmberSecondary.copy(alpha = 0.14f),
                borderHighlightColor = DrgAmberSecondary.copy(alpha = 0.5f),
                contentPadding = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text("Buku Log Transparansi Otoritas DRG", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgAmberDark)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Setiap keputusan pergantian jabatan, pengangkatan pengurus, atau penangguhan anggota dicatat seketika tanpa rekayasa data demi transparansi total.",
                        fontSize = 10.sp, color = DrgTextSecondary
                    )
                }
            }
        }
        if (adminLogs.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text("Belum ada catatan log otorisasi pengurus.", fontSize = 11.sp, color = DrgTextMuted)
                }
            }
        } else {
            items(adminLogs) { log ->
                DrgLuminousGlassCard(
                    shape = RoundedCornerShape(18.dp),
                    backgroundColor = theme.glassBg,
                    borderHighlightColor = theme.glassBorderColor,
                    contentPadding = 12.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(theme.pillBg).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                    Text(log.actorRole, color = theme.primaryColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(Modifier.width(6.dp))
                                Text(log.actorName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                            }
                            Text(log.timestamp, fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, color = DrgTextMuted)
                        }
                        Spacer(Modifier.height(6.dp))
                        Text("Melakukan tindakan ${log.action} kepada:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text(log.targetName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = theme.primaryColor, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }
        }
    }
}
