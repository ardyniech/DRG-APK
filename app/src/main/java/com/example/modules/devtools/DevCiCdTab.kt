package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
fun DevCiCdTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    val ci = remember { CiCdStatusScanner.inspectPipeline() }

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
                        Text("🚀 Real GitHub Actions CI/CD", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgGreenPrimary)
                        Surface(shape = RoundedCornerShape(4.dp), color = DrgGreenPrimary) {
                            Text("HEALTHY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Workflow: ${ci.workflowName} | Branches: ${ci.targetBranches.joinToString(", ")}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text("Timeout: ${ci.timeoutMinutes} Menit | Concurrency Cancel: ${ci.isConcurrencyCancelled} | Unit Tests: ${ci.totalTestSuites} Suites Aktif.", fontSize = 9.sp, color = DrgTextSecondary)
                }
            }
        }

        items(ci.steps) { step ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Step ${step.stepNumber}: ${step.name}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgTextPrimary)
                            Text(step.command, fontSize = 9.sp, color = DrgTextSecondary)
                        }
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFE2E8F0)) {
                            Text(step.executionTimeEstimate, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Status: ${step.status}", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                    Text("Aturan Kegagalan: ${step.failurePolicy}", fontSize = 8.sp, color = if (step.failurePolicy.contains("HARD FAIL")) DrgRedPanic else DrgTextSecondary)
                }
            }
        }
    }
}
