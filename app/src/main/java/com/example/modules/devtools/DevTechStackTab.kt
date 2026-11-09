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
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
        if (headerContent != null) {
            item { headerContent() }
        }
        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.IntegrationInstructions, contentDescription = null, tint = DrgGreenPrimary)
                        Text("Transparansi Seluruh Teknologi & Git CLI", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                    }
                    Text("Daftar teknologi resmi yang berjalan di aplikasi DRG Driver Malang Raya secara jujur dan transparan.", fontSize = 11.sp, color = DrgTextSecondary)
                }
            }
        }

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("🛠️ Production Technology Stack", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.3f))
                    TechRow("Bahasa Pemrograman:", "Kotlin 2.2.10 (KSP Enabled)")
                    TechRow("UI Framework:", "Jetpack Compose 1.7+ (Material 3)")
                    TechRow("Arsitektur Inti:", "Clean Architecture + MVI/UDF (<125 baris)")
                    TechRow("Penyimpanan Lokal:", "Room v13 SQLite (23 Entity, Zero Loss)")
                    TechRow("Asinkron & Reaktif:", "Kotlin Coroutines & StateFlow / SharedFlow")
                    TechRow("Image Rendering:", "Coil 3 (LRU Disk & Memory Cache)")
                    TechRow("Cloud Backend:", "Firebase Firestore & AI Logic Gateway")
                    TechRow("Pengujian JVM Lokal:", "Robolectric 4.14 (Android SDK 34)")
                }
            }
        }

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFF0F172A), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Terminal, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(16.dp))
                        Text("git cli status & branch tracker", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    Text("$ git branch --show-current", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    Text("main (Enterprise Release Track)", fontSize = 10.sp, color = DrgGreenLight, fontWeight = FontWeight.Bold)
                    Text("$ git status --short", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    Text("M  DEVTOOLS.md (Master QA Manual updated)\n?? modules/devtools/ (Security, Nodes & Defects)", fontSize = 10.sp, color = Color(0xFFFDE047))
                    Text("$ git log -1 --oneline", fontSize = 10.sp, color = Color(0xFF94A3B8))
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
