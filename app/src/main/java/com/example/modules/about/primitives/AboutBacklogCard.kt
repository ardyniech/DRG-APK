package com.example.modules.about.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

@Composable
fun AboutBacklogCard(items: List<String>) {
    if (items.isEmpty()) return
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 16.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Antrian & Rencana ke Depan", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = DrgTextPrimary)
            items.take(12).forEach { item ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Text("•", color = theme.primaryColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(item, fontSize = 10.5.sp, color = DrgTextSecondary, modifier = Modifier.weight(1f))
                }
            }
            if (items.size > 12) {
                Text("… dan ${items.size - 12} item lainnya", fontSize = 10.sp, color = DrgTextSecondary)
            }
        }
    }
}