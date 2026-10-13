package com.example.modules.about.primitives

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgOutline
import com.example.ui.theme.DrgSurface
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

@Composable
fun AboutBacklogCard(items: List<String>) {
    if (items.isEmpty()) return
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = DrgSurface,
        modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Antrian & Rencana ke Depan", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = DrgTextPrimary)
            items.take(12).forEach { item ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Text("•", color = DrgGreenPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(item, fontSize = 10.5.sp, color = DrgTextSecondary, modifier = Modifier.weight(1f))
                }
            }
            if (items.size > 12) {
                Text("… dan ${items.size - 12} item lainnya", fontSize = 10.sp, color = DrgTextSecondary)
            }
        }
    }
}