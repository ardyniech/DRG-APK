package com.example.modules.services

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
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
fun PttTransmissionButton(
    isTransmitting: Boolean,
    onToggleTransmitting: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(100.dp)
        ) {
            Button(
                onClick = onToggleTransmitting,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isTransmitting) DrgRedDanger else DrgGrabGreenPrimary
                ),
                modifier = Modifier.size(96.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "PTT Button",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Text(
            text = if (isTransmitting) "Ketuk sekali lagi untuk SELESAI" else "Ketuk tombol untuk MULAI BICARA",
            fontSize = 10.sp,
            color = DrgTextMuted,
            fontWeight = FontWeight.Bold
        )
    }
}
