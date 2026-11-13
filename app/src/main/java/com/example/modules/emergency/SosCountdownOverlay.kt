package com.example.modules.emergency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.audio.AudioRouteManager
import com.example.core.audio.SosAlarmSoundManager
import com.example.core.audio.SosHapticManager
import com.example.shared.atoms.DrgGlassPrimaryButton
import com.example.shared.atoms.DrgLuminousGlassModal
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
    val context = LocalContext.current
    val vol = remember(context) { AudioRouteManager.getRecommendedAlarmVolume(context) }

    LaunchedEffect(Unit) {
        if (isAlarmSoundEnabled) SosAlarmSoundManager.playHighDecibelAlarm(initialSeconds * 1000L, vol)
        while (secondsLeft > 0) {
            SosHapticManager.playCountdownTick(secondsLeft)
            delay(1000L)
            secondsLeft--
        }
        SosHapticManager.playEmergencyDispatchedHaptic()
        onTriggerNow()
    }

    DisposableEffect(Unit) {
        onDispose { SosAlarmSoundManager.stopAlarm(); SosHapticManager.stopVibration() }
    }

    val cancelAction: () -> Unit = {
        SosAlarmSoundManager.stopAlarm()
        SosHapticManager.stopVibration()
        SosHapticManager.playSuccessHaptic()
        onCancel()
    }

    DrgLuminousGlassModal(
        title = "Memancarkan SOS Darurat",
        subtitle = "Kategori: ${emergencyType.label}",
        onDismiss = cancelAction,
        icon = Icons.Default.Warning,
        accentColor = DrgRedPanic,
        footer = {
            DrgGlassPrimaryButton(text = "SAYA AMAN (BATALKAN)", icon = Icons.Default.CheckCircle, onClick = cancelAction)
            OutlinedButton(
                onClick = {
                    SosAlarmSoundManager.stopAlarm()
                    SosHapticManager.playEmergencyDispatchedHaptic()
                    onTriggerNow()
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgRedPanic),
                modifier = Modifier.fillMaxWidth().height(46.dp)
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("KIRIM SOS SEKARANG", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SosCountdownCircle(secondsLeft = secondsLeft)
            Text("MEMANCARKAN SOS DARURAT", fontSize = 15.sp, fontWeight = FontWeight.Black, color = DrgRedPanic, textAlign = TextAlign.Center)
            Text(
                text = "Kategori: ${emergencyType.label}\nSinyal SOS & koordinat GPS disiarkan otomatis dalam $secondsLeft detik.",
                fontSize = 12.sp,
                color = DrgTextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
