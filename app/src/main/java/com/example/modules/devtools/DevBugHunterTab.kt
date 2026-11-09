package com.example.modules.devtools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DevBugHunterTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    DevTabShell(
        headerContent = headerContent,
        headerIcon = Icons.Default.BugReport,
        headerTitle = "Bug, Dead Buttons & Shallow Features Hunter",
        headerDesc = "Deteksi tanpa kompromi: Dead Button (-4%), Fitur Dangkal (-3%), Bug (-2%), Progres Gap (-2%)."
    ) {

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFFFEBEE), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = DrgRedDanger, modifier = Modifier.size(24.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("6 Masalah Terdeteksi (Akumulasi Penalti: -20.0%)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFC62828))
                        Text("Skor Proyek turun dari 95.0% ke 75.0% (Grade B: Perlu Penguatan).", fontSize = 10.sp, color = Color(0xFFB71C1C))
                    }
                }
            }
        }

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("⚠️ Rincian Temuan & Penalti Ketat", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    Divider(color = DrgOutline.copy(alpha = 0.3f))
                    Text("1. [DEAD-BUTTON] Upload Berkas RS di Asuransi (Penalti: -4.0%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgRedDanger)
                    Text("   Tombol belum memiliki backend receiver / multipart upload.", fontSize = 10.sp, color = DrgTextSecondary)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("2. [DEAD-BUTTON] Pairing Bluetooth Helm GATT (Penalti: -4.0%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgRedDanger)
                    Text("   Tombol interkom helm hanya listener mock tanpa socket RFCOMM.", fontSize = 10.sp, color = DrgTextSecondary)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("3. [SHALLOW] Modul Klaim Asuransi Instan (Penalti: -3.0%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF57C00))
                    Text("   Hanya form lokal tanpa verifikasi API polis asuransi riil.", fontSize = 10.sp, color = DrgTextSecondary)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("4. [SHALLOW] Interkom Helm Bluetooth GATT (Penalti: -3.0%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF57C00))
                    Text("   Status fitur masih konsep/benih belum production hardware.", fontSize = 10.sp, color = DrgTextSecondary)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("5. [BUG] Minor Recomposition di KasTransparan (Penalti: -2.0%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgRedDanger)
                    Text("6. [BUG] Background Sync Timeout Warning (Penalti: -2.0%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgRedDanger)
                }
            }
        }
    }
}
