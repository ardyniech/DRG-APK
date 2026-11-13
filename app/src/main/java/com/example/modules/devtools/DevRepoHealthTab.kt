package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DevRepoHealthTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    DevTabShell(
        headerContent = headerContent,
        headerIcon = Icons.Default.Code,
        headerTitle = "Monitor Kesehatan Repository & Arsitektur",
        headerDesc = "Auto-audit line counts per file (< 125 lines cap), dependency alignment, and modular isolation."
    ) {

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = DrgSurfaceVariant, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DrgTextSecondary, modifier = Modifier.size(24.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Architecture Quality Audit: cek CI / script", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                        Text("Runtime app tidak menjalankan audit repo. Jalankan: python3 .github/scripts/check_file_length.py & audit_minus.py (atau lihat GitHub Actions).", fontSize = 10.sp, color = DrgTextSecondary)
                    }
                }
            }
        }

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("📦 Info Repo (statis/cek sumber)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.3f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Files Audited:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("UNKNOWN (jalankan check_file_length.py)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Max Line Cap Violation:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("Cek CI / script", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Room DB Entities:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("26 Tabel (v14, schema 14.json)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Test Suite Status:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("Cek GitHub Actions / test report", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                }
            }
        }
    }
}
