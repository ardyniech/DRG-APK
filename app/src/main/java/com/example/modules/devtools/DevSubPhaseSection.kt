package com.example.modules.devtools

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgRedPanic
import com.example.ui.theme.DrgTextPrimary

@Composable
fun DevSubPhaseSection(sub: DevSubPhase) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF1F5F9),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "📁 ${sub.title}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = DrgTextPrimary
                )
                Text(
                    "${sub.completedCount}/${sub.totalCount} Selesai",
                    fontSize = 9.sp,
                    color = if (sub.hasAnyDefect) DrgRedPanic else DrgGreenPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            sub.features.forEach { feat ->
                DevFeatureItemCard(feat)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
