package com.example.modules.radar.primitives

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.radar.models.FreeMapMode
import com.example.shared.utils.WhatsAppLauncher
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun MapTopControlBar(
    mapMode: FreeMapMode, onToggleMapMode: (FreeMapMode) -> Unit,
    isTrafficEnabled: Boolean = false, onToggleTraffic: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var isExpandedMode by remember { mutableStateOf(true) }
    var isExpandedTraffic by remember { mutableStateOf(true) }
    var showInfo by remember { mutableStateOf(false) }

    LaunchedEffect(isExpandedMode) { if (isExpandedMode) { delay(2500); isExpandedMode = false } }
    LaunchedEffect(isExpandedTraffic) { if (isExpandedTraffic) { delay(2500); isExpandedTraffic = false } }

    Surface(
        color = Color.Black.copy(alpha = 0.82f), shape = RoundedCornerShape(14.dp),
        modifier = modifier.border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
    ) {
        Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            val isRoad = mapMode == FreeMapMode.ROAD
            Surface(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    isExpandedMode = true
                    onToggleMapMode(if (isRoad) FreeMapMode.SATELLITE else FreeMapMode.ROAD)
                },
                shape = RoundedCornerShape(10.dp), color = if (isRoad) DrgGreenPrimary else DrgGoldReward, modifier = Modifier.height(34.dp)
            ) {
                Row(modifier = Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(imageVector = if (isRoad) Icons.Default.Map else Icons.Default.Satellite, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                    AnimatedVisibility(visible = isExpandedMode) {
                        Text(if (isRoad) "Jalan" else "Satelit", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Surface(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    isExpandedTraffic = true
                    onToggleTraffic(!isTrafficEnabled)
                    WhatsAppLauncher.openLiveTrafficMap(context)
                },
                shape = RoundedCornerShape(10.dp), color = DrgTrafficRed, modifier = Modifier.height(34.dp)
            ) {
                Row(modifier = Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(imageVector = Icons.Default.Traffic, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                    AnimatedVisibility(visible = isExpandedTraffic) {
                        Text("Live Traffic", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            IconButton(onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); showInfo = true }, modifier = Modifier.size(34.dp)) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Info", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }

    if (showInfo) {
        AlertDialog(
            onDismissRequest = { showInfo = false },
            title = { Text("Peta & Lalu Lintas Real-Time", fontSize = 13.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Layer Live Traffic:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                    Text("Memuat data kemacetan dan kecepatan jalan real-time dari Google Maps Traffic Layer resmi.", fontSize = 10.sp, color = DrgTextSecondary)
                    Text("Laporan Komunitas:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                    Text("Titik kemacetan dilaporkan langsung oleh driver DRG di lapangan via Room Database.", fontSize = 10.sp, color = DrgTextSecondary)
                }
            },
            confirmButton = {
                Button(onClick = { showInfo = false }, colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary)) {
                    Text("Paham", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = DrgSurface, shape = RoundedCornerShape(16.dp)
        )
    }
}
