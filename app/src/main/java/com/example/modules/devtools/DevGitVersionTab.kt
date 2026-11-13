package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Source
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
fun DevGitVersionTab() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Source, contentDescription = null, tint = DrgGreenPrimary)
                        Text("Git & Metadata Versi Asli BuildConfig", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                    }
                    Text("Auto-detect langsung dari build config kompilasi Android tanpa edit manual.", fontSize = 11.sp, color = DrgTextSecondary)
                }
            }
        }

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("🌿 Informasi Environment & Build", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.3f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Package Name:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text(BuildConfig.APPLICATION_ID, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Build Type:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text(BuildConfig.BUILD_TYPE.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Versi Aplikasi:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Kotlin / Toolchain:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("2.2.10 (KSP Room v14)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                }
            }
        }

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("📜 Riwayat Commit", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.3f))
                    Text("Runtime app tidak membaca riwayat git. Cek: git log / GitHub commits untuk HEAD & pesan commit terbaru.", fontSize = 10.sp, color = DrgTextSecondary)
                    Text("Build info di atas berasal dari BuildConfig (kompilasi).", fontSize = 10.sp, color = DrgTextMuted)
                }
            }
        }
    }
}
