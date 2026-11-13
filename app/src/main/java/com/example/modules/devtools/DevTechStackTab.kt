package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.IntegrationInstructions
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.ui.theme.*

@Composable
fun DevTechStackTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    DevTabShell(
        headerContent = headerContent,
        headerIcon = Icons.Default.IntegrationInstructions,
        headerTitle = "Transparansi Seluruh Teknologi & Git CLI",
        headerDesc = "Daftar teknologi resmi yang berjalan di aplikasi DRG Driver Malang Raya secara jujur dan transparan."
    ) {

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("🛠️ Production Technology Stack", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.3f))
                    TechRow("Bahasa Pemrograman:", "Kotlin 2.2.10 (KSP Enabled)")
                    TechRow("UI Framework:", "Jetpack Compose 1.7+ (Material 3)")
                    TechRow("Arsitektur Inti:", "Clean Architecture + MVI/UDF (<125 baris)")
                    TechRow("Penyimpanan Lokal:", "Room v14 SQLite (26 tabel, local-first)")
                    TechRow("Asinkron & Reaktif:", "Kotlin Coroutines & StateFlow / SharedFlow")
                    TechRow("Image Rendering:", "Coil 3 (LRU Disk & Memory Cache)")
                    TechRow("Cloud Backend:", "Firebase (belum aktif penuh; data local-first)")
                    TechRow("Pengujian JVM Lokal:", "Robolectric 4.14 (Android SDK 34)")
                }
            }
        }

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = DrgTextDark, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Terminal, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(16.dp))
                        Text("git cli status & branch tracker", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    Text("$ git branch --show-current", fontSize = 10.sp, color = DrgSlateTextMuted)
                    Text("main (Enterprise Release Track)", fontSize = 10.sp, color = DrgGreenLight, fontWeight = FontWeight.Bold)
                    Text("$ git status --short", fontSize = 10.sp, color = DrgSlateTextMuted)
                    Text("M  DEVTOOLS.md (Master QA Manual updated)\n?? modules/devtools/ (Security, Nodes & Defects)", fontSize = 10.sp, color = DrgYellowBadge)
                    Text("$ git log -1 --oneline", fontSize = 10.sp, color = DrgSlateTextMuted)
                    Text("406a1f8 feat(devtools): next-level audit security, defects & device advisor", fontSize = 10.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun TechRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 11.sp, color = DrgTextSecondary)
        Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
    }
}
