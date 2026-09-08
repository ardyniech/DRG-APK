package com.example.modules.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AppSettingsHeaderRow(onClose: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Settings, contentDescription = "Pengaturan", tint = DrgGreenPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pengaturan & Keamanan", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DrgTextPrimary)
        }
        IconButton(onClick = onClose) {
            Icon(Icons.Default.Close, contentDescription = "Tutup", tint = DrgTextSecondary)
        }
    }
}
