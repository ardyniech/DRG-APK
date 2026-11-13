package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
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
fun DevCodeIntegrityTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    val tree = remember { CodeIntegrityAuditor.auditCodebase() }
    val totalSmells = tree.sumOf { it.totalPendingIssues }

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
                color = if (totalSmells > 0) DrgWarningBgAmber else DrgGreenLight,
                modifier = Modifier.fillMaxWidth().border(1.dp, if (totalSmells > 0) DrgAmberWarning.copy(alpha = 0.5f) else DrgGreenPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("🛡️ Code Integrity Dependency Tree & Smell Audit", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (totalSmells > 0) DrgWarningTextDark else DrgGreenPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Ditemukan $totalSmells pending code smell / deprecation pattern dalam struktur pohon modul.", fontSize = 11.sp, color = DrgTextPrimary)
                }
            }
        }

        items(tree) { feature ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = devGlassBg(),
                modifier = Modifier.fillMaxWidth().border(1.dp, devGlassBorder().copy(alpha = 0.45f), RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("📁 Feature: ${feature.featureName}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (feature.totalPendingIssues > 0) DrgWarningBgSoft else DrgGreenLight
                        ) {
                            Text(
                                if (feature.totalPendingIssues > 0) "${feature.totalPendingIssues} Issues" else "Clean",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (feature.totalPendingIssues > 0) DrgWarningText else DrgGreenPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    feature.subFeatures.forEach { sub ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DrgSlateBgSoft,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (sub.hasPendingIssues) Icons.Default.Warning else Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (sub.hasPendingIssues) DrgAmberWarning else DrgGreenPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text("Sub: ${sub.subFeatureName}", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = DrgTextPrimary)
                                }
                                sub.smells.forEach { smell ->
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("• [${smell.severity}] ${smell.title} (${smell.affectedModule})", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DrgRedPanic)
                                    Text("  Solusi: ${smell.recommendedFix}", fontSize = 9.sp, color = DrgTextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
