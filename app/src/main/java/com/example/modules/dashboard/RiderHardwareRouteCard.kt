package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.audio.AudioRouteManager
import com.example.ui.theme.*

@Composable
fun RiderHardwareRouteCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val audioType = remember(context) { AudioRouteManager.getConnectedAudioDeviceType(context) }
    val isHeadset = audioType.isHeadset

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, DrgOutline.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isHeadset) DrgGreenContainer else DrgAmberContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isHeadset) Icons.Default.Headset else Icons.Default.VolumeUp,
                    contentDescription = audioType.label,
                    tint = if (isHeadset) DrgGreenPrimary else DrgAmberSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = audioType.label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = DrgTextPrimary
                )
                Text(
                    text = if (isHeadset) "Audio kalibrasi 70% (Aman di helm)" else "Audio siaga 100% (Speaker motor)",
                    fontSize = 10.sp,
                    color = DrgTextSecondary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(DrgSurface)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudDone,
                    contentDescription = "Sync",
                    tint = DrgGreenPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Lokal-First",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DrgGreenPrimary
                )
            }
        }
    }
}
