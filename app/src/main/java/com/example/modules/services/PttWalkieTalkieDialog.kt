package com.example.modules.services

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.core.audio.AudioRouteManager
import com.example.core.audio.PttAudioEngine
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun PttWalkieTalkieDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val audioEngine = remember { PttAudioEngine(context) }

    var isTransmitting by remember { mutableStateOf(false) }
    var channelIndex by remember { mutableIntStateOf(0) }
    val malangChannels = listOf("Kanal Utama Suhat", "Satgas Klojen", "Karanglo & Singosari", "Batu - Pujon")
    val channelName = malangChannels[channelIndex]
    var statusMessage by remember { mutableStateOf("Siap transmisi radio P2P & Firestore.") }
    val rmsLevel by audioEngine.rmsLevel.collectAsState()
    val audioDevice = remember { AudioRouteManager.getConnectedAudioDeviceType(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        if (grants[Manifest.permission.RECORD_AUDIO] == true) {
            isTransmitting = true
            statusMessage = "Merekam suara 8kHz (mu-law)..."
            scope.launch {
                audioEngine.recordTransmission { base64, durationMs ->
                    isTransmitting = false
                    statusMessage = "Berhasil kirim PTT (${durationMs}ms, ${base64.length}B) ke $channelName."
                }
            }
        } else {
            statusMessage = "Izin mikrofon ditolak. Transmisi PTT tidak dapat dimulai."
        }
    }

    Dialog(onDismissRequest = { if (!isTransmitting) onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(24.dp), color = DrgSurface,
            modifier = Modifier.fillMaxWidth().padding(12.dp).border(2.dp, DrgGrabGreenPrimary, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Radio, contentDescription = "Radio", tint = DrgGrabGreenPrimary)
                        Text("DRG Walkie-Talkie P2P", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                    Surface(shape = RoundedCornerShape(8.dp), color = if (isTransmitting) DrgRedDanger.copy(alpha = 0.15f) else DrgGrabGreenContainer.copy(alpha = 0.15f)) {
                        Text(if (isTransmitting) "TX (Live)" else "RX (Ready)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isTransmitting) DrgRedDanger else DrgGrabGreenPrimary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth().clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        channelIndex = (channelIndex + 1) % malangChannels.size
                    },
                    shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = DrgBackground)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Saluran: $channelName", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                            Text("Audio: ${audioDevice.label}", fontSize = 10.sp, color = DrgGrabGreenPrimary)
                        }
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Audio Route", tint = DrgGrabGreenPrimary)
                    }
                }

                PttWaveformCanvas(isTransmitting = isTransmitting)

                Text(statusMessage, fontSize = 11.sp, color = if (isTransmitting) DrgRedDanger else DrgTextSecondary, fontWeight = FontWeight.Medium)

                PttTransmissionButton(
                    isTransmitting = isTransmitting,
                    onToggleTransmitting = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (!isTransmitting) {
                            val perms = mutableListOf(Manifest.permission.RECORD_AUDIO)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) perms.add(Manifest.permission.BLUETOOTH_CONNECT)
                            permissionLauncher.launch(perms.toTypedArray())
                        } else {
                            audioEngine.stopRecording()
                            isTransmitting = false
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
