package com.example.modules.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LiveLocationSharingSettingRow(
    isLocationSharingConsent: Boolean,
    onToggleLocationSharing: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth().padding(vertical = 2.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Share Live GPS 12 Jam Komunitas", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
            Text("Aktif otomatis jam 06.00 - 18.00 demi keselamatan", fontSize = 10.sp, color = DrgTextSecondary)
        }
        Switch(
            checked = isLocationSharingConsent,
            onCheckedChange = onToggleLocationSharing,
            colors = SwitchDefaults.colors(checkedThumbColor = DrgGreenPrimary)
        )
    }
}
