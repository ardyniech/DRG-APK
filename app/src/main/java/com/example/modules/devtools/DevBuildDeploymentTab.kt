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
                color = DrgGreenLight,
                modifier = Modifier.fillMaxWidth().border(1.dp, DrgGreenPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🚀 Build & Deployment Status", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgGreenPrimary)
                        Icon(Icons.Default.CloudDone, contentDescription = null, tint = DrgGreenPrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Commit Terakhir: ${info.lastCommitHash}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text("Status Pipeline: ${if (info.isPipelineHealthy) "HEALTHY (Zero Failures)" else "ACTION REQUIRED"}", fontSize = 10.sp, color = if (info.isPipelineHealthy) DrgGreenPrimary else DrgRedPanic)
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
                    Text("📊 Metrik Performa Build & Test", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.3f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Waktu Build Gradle:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("${info.buildDurationSeconds} Detik (Optimized Cache)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Test Coverage:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("${info.testCoveragePercentage}% (${info.totalTestSuites} Suites)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Gagal Commit (Failures):", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("${info.failedCommitsCount} Commit", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                    }
                }
            }
        }

        if (info.lastFailureLog != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFFEBEE),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color.Red.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("⚠️ Log Kegagalan Commit Terakhir", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFC62828))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(info.lastFailureLog, fontSize = 10.sp, color = Color(0xFFB71C1C))
                    }
                }
            }
        }
    }
}
