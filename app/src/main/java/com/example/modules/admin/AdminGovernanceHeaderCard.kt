package com.example.modules.admin

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun AdminGovernanceHeaderCard(currentMember: DriverMember?, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DrgGreenDark,
        modifier = modifier.fillMaxWidth().border(1.dp, DrgGreenPrimary, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pusat Tata Kelola & Analisis DRG", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Text("Hak Akses Anda: ${currentMember?.role?.title ?: "Anggota"}", color = Color.White.copy(alpha = 0.95f), fontSize = 12.sp)
            Text("Menu khusus Pengurus, Satgas & Audit Kesehatan Komunitas.", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
        }
    }
}
