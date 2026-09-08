package com.example.modules.services

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun EscortSatgasCard(onOpenPtt: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DrgSurface),
        border = BorderStroke(1.dp, DrgGrabGreenContainer),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Radio,
                    contentDescription = "Escort Walkie-Talkie",
                    tint = DrgGrabGreenPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Konvoi & PTT Walkie-Talkie Satgas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DrgTextPrimary
                )
            }
            Text(
                text = "Fitur komunikasi radio PTT terhubung langsung antar-driver dan rombongan touring komunitas tanpa pulsa.",
                fontSize = 12.sp,
                color = DrgTextSecondary
            )
            OutlinedButton(
                onClick = onOpenPtt,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buka Mode Radio PTT (Komunikasi)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
