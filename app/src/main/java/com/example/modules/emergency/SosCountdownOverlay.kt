package com.example.modules.emergency

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.core.audio.SosAlarmSoundManager
import com.example.shared.models.EmergencyType
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SosCountdownOverlay(
    emergencyType: EmergencyType,
    initialSeconds: Int = 5,
    isAlarmSoundEnabled: Boolean = true,
    onCancel: () -> Unit,
    onTriggerNow: () -> Unit
) {
    var secondsLeft by remember { mutableIntStateOf(initialSeconds) }

    LaunchedEffect(Unit) {
        if (isAlarmSoundEnabled) {
            SosAlarmSoundManager.playHighDecibelAlarm(initialSeconds * 1000L)
        }
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
        }
        onTriggerNow()
    }

    DisposableEffect(Unit) {
        onDispose { SosAlarmSoundManager.stopAlarm() }
    }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = DrgSurface,
            modifier = Modifier.fillMaxWidth().border(2.dp, DrgRedDanger, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SosCountdownCircle(secondsLeft = secondsLeft)

                Text(
                    text = "MEMANCARKAN SOS DARURAT",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = DrgRedDanger,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Kategori: ${emergencyType.label}\nSinyal SOS & lokasi GPS disiarkan otomatis dalam $secondsLeft detik.",
                    fontSize = 12.sp,
                    color = DrgTextSecondary,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SAYA AMAN (BATALKAN)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onTriggerNow,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgRedDanger),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("KIRIM SOS SEKARANG", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
