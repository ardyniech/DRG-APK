package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material3.Icon
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
import com.example.modules.dashboard.primitives.GlassCard
import com.example.ui.theme.*

@Composable
fun RiderHardwareRouteCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val audioType = remember(context) { AudioRouteManager.getConnectedAudioDeviceType(context) }
    val isHeadset = audioType.isHeadset

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = Color.White.copy(alpha = 0.80f),
        contentPadding = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isHeadset) DrgGreenContainer else DrgAmberContainer)
                    .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isHeadset) Icons.Default.Headset else Icons.AutoMirrored.Filled.VolumeUp,
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
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.7f))
                    .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudDone,
                    contentDescription = "Sync",
                    tint = DrgGreenPrimary,
                    modifier = Modifier.size(13.dp)
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
