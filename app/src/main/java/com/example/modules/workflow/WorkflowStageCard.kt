package com.example.modules.workflow

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
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
fun WorkflowStageCard(
    stage: WorkflowStage,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DrgSurface,
        modifier = modifier.fillMaxWidth().border(1.dp, DrgOutline, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stage.iconEmoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = stage.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                        Text(text = stage.timeLabel, fontSize = 10.sp, color = DrgTextSecondary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = DrgRedPanic, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = stage.locationLabel, fontSize = 11.sp, color = DrgTextSecondary, fontWeight = FontWeight.SemiBold)
            }

            HorizontalDivider(color = DrgOutline.copy(alpha = 0.5f))

            // 1. Tantangan Lapangan Nyata
            Surface(shape = RoundedCornerShape(10.dp), color = DrgRedBgSoft, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = DrgRedPanic, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Kondisi Nyata Lapangan (Off-App):", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgRedPanic)
                        Text(stage.realChallenge, fontSize = 11.sp, color = DrgRedPanicDarker, lineHeight = 15.sp)
                    }
                }
            }

            // 2. Proteksi & Peran DRG
            Surface(shape = RoundedCornerShape(10.dp), color = DrgGreenContainer, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DrgGreenDark, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Solusi & Proteksi Aplikasi DRG:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgGreenDark)
                        Text(stage.drgSolution, fontSize = 11.sp, color = DrgOnGreenContainer, lineHeight = 15.sp)
                    }
                }
            }
        }
    }
}
