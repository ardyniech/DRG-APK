package com.example.modules.emergency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

@Composable
fun SosActiveAlertsEmptyCard(modifier: Modifier = Modifier) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 24.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.CheckCircle, contentDescription = "Aman", tint = theme.primaryColor, modifier = Modifier.size(36.dp))
            Spacer(Modifier.height(8.dp))
            Text("Kondisi Jalanan Terpantau Aman", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
            Text("Tidak ada laporan sinyal darurat aktif dari rekan driver.", fontSize = 11.sp, color = DrgTextSecondary)
        }
    }
}
