package com.example.modules.services

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun PttWalkieTalkieDialog(onDismiss: () -> Unit) {
    var isTransmitting by remember { mutableStateOf(false) }
    val malangChannels = listOf(
        "Kanal Utama Suhat & Lowokwaru",
        "Kanal Koordinasi Satgas Klojen",
        "Kanal Pantauan Macet Karanglo & Singosari",
        "Kanal Lintas Dingin Batu - Pujon"
    )
    var channelIndex by remember { mutableIntStateOf(0) }
    val channelName = malangChannels[channelIndex]
    var lastTransmittedText by remember { mutableStateOf("Ketuk tombol di bawah untuk mulai transmisi radio.") }
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(isTransmitting) {
        if (isTransmitting) {
            lastTransmittedText = "Sedang Memancarkan... (Suara Anda tersalurkan ke 142 Driver terdekat)"
            while (isTransmitting) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                delay(600)
            }
        }
    }

    Dialog(onDismissRequest = { if (!isTransmitting) onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = DrgSurface,
            modifier = Modifier.fillMaxWidth().padding(12.dp).border(2.dp, DrgGrabGreenPrimary, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Radio, contentDescription = "Radio", tint = DrgGrabGreenPrimary)
                        Text(text = "DRG Walkie-Talkie", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                    Surface(shape = RoundedCornerShape(8.dp), color = if (isTransmitting) DrgRedDanger.copy(alpha = 0.15f) else DrgGrabGreenContainer.copy(alpha = 0.15f)) {
                        Text(
                            text = if (isTransmitting) "TX (Active)" else "RX (Ready)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isTransmitting) DrgRedDanger else DrgGrabGreenPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth().clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        channelIndex = (channelIndex + 1) % malangChannels.size
                        lastTransmittedText = "Beralih ke ${malangChannels[channelIndex]}. Menunggu transmisi..."
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DrgBackground)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Saluran Aktif (Ketuk untuk ganti kanal Malang)", fontSize = 10.sp, color = DrgGrabGreenPrimary, fontWeight = FontWeight.Bold)
                            Text(channelName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                        }
                        Icon(Icons.Default.VolumeUp, contentDescription = "Volume", tint = DrgGrabGreenPrimary)
                    }
                }

                PttWaveformCanvas(isTransmitting = isTransmitting)

                Text(
                    text = lastTransmittedText,
                    fontSize = 12.sp,
                    color = if (isTransmitting) DrgRedDanger else DrgTextSecondary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                PttTransmissionButton(
                    isTransmitting = isTransmitting,
                    onToggleTransmitting = {
                        isTransmitting = !isTransmitting
                        if (!isTransmitting) {
                            lastTransmittedText = "Transmisi Selesai. Terkirim ke Satgas."
                        }
                    }
                )

                TextButton(onClick = onDismiss, enabled = !isTransmitting, modifier = Modifier.fillMaxWidth()) {
                    Text("Tutup", color = DrgTextSecondary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
