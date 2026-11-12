package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DevBuildDeploymentTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    val info = remember { BuildDeploymentEngine.inspectBuildStatus() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        if (headerContent != null) {
            item { headerContent() }
        }
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DrgSurfaceVariant,
                modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🚀 Build & Deployment Status", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                        Icon(Icons.Default.CloudDone, contentDescription = null, tint = DrgTextSecondary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Commit Terakhir: ${info.lastCommitHash}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text("Status Pipeline: UNKNOWN di runtime app (cek GitHub Actions / gh)", fontSize = 10.sp, color = DrgTextSecondary)
                }
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("📊 Metrik Build & Test (runtime app tidak bisa probe)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.3f))
                    fun fmt(v: Int) = if (v < 0) "UNKNOWN" else "$v"
                    fun fmtF(v: Float) = if (v < 0f) "UNKNOWN" else "${v}%"
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Waktu Build Gradle:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text(if (info.buildDurationSeconds < 0) "UNKNOWN" else "${info.buildDurationSeconds} detik", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Test Coverage:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text(if (info.testCoveragePercentage < 0f) "UNKNOWN" else fmtF(info.testCoveragePercentage), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Test Suites:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text(fmt(info.totalTestSuites), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Gagal Commit (Failures):", fontSize = 11.sp, color = DrgTextSecondary)
                        Text(fmt(info.failedCommitsCount), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                }
            }
        }

        if (info.lastFailureLog != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DrgErrorBgSoft,
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color.Red.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("⚠️ Log Kegagalan Commit Terakhir", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgErrorText)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(info.lastFailureLog, fontSize = 10.sp, color = DrgErrorTextDark)
                    }
                }
            }
        }
    }
}
