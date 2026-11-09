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
fun DevUiUxAuditTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    val audit = remember { UiUxAuditEngine.auditExperience() }

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
                        Text("🎯 UI/UX Real Experience Score", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgGreenPrimary)
                        Text("${String.format("%.1f", audit.overallScore)}/100", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = DrgGreenPrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Predikat: ${audit.grade}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text("Kepatuhan: ${audit.passedPillarsCount}/${audit.totalPillarsCount} Pilar Pengalaman Ojol Berkendara Terpenuhi.", fontSize = 10.sp, color = DrgTextSecondary)
                }
            }
        }

        items(audit.pillars) { pillar ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth().border(1.dp, if (pillar.isCompliant) DrgOutline.copy(alpha = 0.4f) else DrgAmberWarning.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (pillar.isCompliant) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (pillar.isCompliant) DrgGreenPrimary else DrgAmberWarning,
                            modifier = Modifier.size(16.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(pillar.title, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgTextPrimary)
                            Text(pillar.category, fontSize = 9.sp, color = DrgTextSecondary)
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (pillar.isCompliant) DrgGreenLight else Color(0xFFFEF3C7)
                        ) {
                            Text(
                                "${pillar.scoreOutOf100.toInt()}%",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (pillar.isCompliant) DrgGreenPrimary else Color(0xFFD97706),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("👀 Observasi: ${pillar.fieldObservation}", fontSize = 9.sp, color = DrgTextPrimary)
                    Text("🛵 Efek Driver: ${pillar.driverImpact}", fontSize = 9.sp, color = DrgTextSecondary)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("🛠️ Solusi: ${pillar.remediationStep}", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (pillar.isCompliant) DrgGreenPrimary else DrgAmberWarning)
                }
            }
        }
    }
}
