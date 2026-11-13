package com.example.modules.devtools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun DevFeatureItemCard(feature: DevFeatureLeaf) {
    var isExpanded by remember { mutableStateOf(feature.hasDefect) }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (feature.hasDefect) DrgRoseBgSoft else DrgSlateBgSoft,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (feature.hasDefect) DrgRedPanic.copy(alpha = 0.4f) else DrgOutline.copy(alpha = 0.3f),
                RoundedCornerShape(8.dp)
            )
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = when (feature.status) {
                        PhaseTreeStatus.LULUS_SEMPURNA -> Icons.Default.CheckCircle
                        PhaseTreeStatus.TERKENDALA_CACAT -> Icons.Default.Warning
                        PhaseTreeStatus.DALAM_PENGERJAAN -> Icons.Default.HourglassEmpty
                        PhaseTreeStatus.PENDING -> Icons.Default.Schedule
                        PhaseTreeStatus.DEFERRED -> Icons.Default.Block
                    },
                    contentDescription = null,
                    tint = when (feature.status) {
                        PhaseTreeStatus.LULUS_SEMPURNA -> DrgGreenPrimary
                        PhaseTreeStatus.TERKENDALA_CACAT -> DrgRedPanic
                        PhaseTreeStatus.DALAM_PENGERJAAN -> DrgAmberWarning
                        PhaseTreeStatus.PENDING -> Color.Gray
                        PhaseTreeStatus.DEFERRED -> Color.Gray
                    },
                    modifier = Modifier.size(16.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(feature.name, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgTextPrimary)
                    Text(feature.id, fontSize = 9.sp, color = DrgTextSecondary)
                }
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = when (feature.status) {
                        PhaseTreeStatus.LULUS_SEMPURNA -> DrgGreenLight
                        PhaseTreeStatus.TERKENDALA_CACAT -> DrgRoseBorderSoft
                        else -> DrgWarningBgSoft
                    }
                ) {
                    Text(
                        feature.status.label,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (feature.status) {
                            PhaseTreeStatus.LULUS_SEMPURNA -> DrgGreenPrimary
                            PhaseTreeStatus.TERKENDALA_CACAT -> DrgRedPanic
                            else -> DrgWarningText
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(color = DrgOutline.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(6.dp))
                Text("📌 Apa Fungsinya:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                Text(feature.whatIsIt, fontSize = 9.sp, color = DrgTextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                Text("🎯 Kenapa Wajib Ada:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                Text(feature.whyRequired, fontSize = 9.sp, color = DrgTextSecondary)
                if (feature.defectExplanation != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("⚠️ Cacat / Alasan Belum Lulus:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DrgRedPanic)
                    Text(feature.defectExplanation, fontSize = 9.sp, color = DrgRedPanic)
                }
            }
        }
    }
}
