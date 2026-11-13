package com.example.modules.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
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
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary

@Composable
fun AdminGovernanceHeaderCard(currentMember: DriverMember?, modifier: Modifier = Modifier) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 16.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin", tint = theme.primaryColor)
                Spacer(Modifier.width(8.dp))
                Text("Pusat Tata Kelola & Analisis DRG", color = DrgTextDark, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            }
            Text("Hak Akses Anda: ${currentMember?.role?.title ?: "Anggota"}", color = theme.primaryColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Menu khusus Pengurus, Satgas & Audit Kesehatan Komunitas.", color = DrgTextSecondary, fontSize = 11.sp)
        }
    }
}
