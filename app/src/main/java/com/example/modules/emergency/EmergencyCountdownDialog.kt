package com.example.modules.emergency

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.audio.SosAlarmSoundManager
import com.example.shared.atoms.DrgGlassPrimaryButton
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun EmergencyCountdownDialog(
    detectedGForce: Float,
    isSosAlarmEnabled: Boolean,
    onCancel: () -> Unit,
    onConfirmAutoSos: () -> Unit
) {
    var secondsLeft by remember { mutableIntStateOf(10) }

    LaunchedEffect(Unit) {
        if (isSosAlarmEnabled) SosAlarmSoundManager.playHighDecibelAlarm(10000L)
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
        }
        onConfirmAutoSos()
    }

    DisposableEffect(Unit) { onDispose { SosAlarmSoundManager.stopAlarm() } }

    val pulseScale by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulseScale"
    )

    DrgLuminousGlassModal(
        title = "Benturan Keras Terdeteksi",
        subtitle = "Crash Guard • Deteksi G-Force",
        onDismiss = onCancel,
        icon = Icons.Default.Warning,
        accentColor = DrgRedPanic,
        footer = {
            DrgGlassPrimaryButton(text = "SAYA AMAN (BATALKAN)", icon = Icons.Default.CheckCircle, onClick = onCancel)
            OutlinedButton(
                onClick = onConfirmAutoSos,
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
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(DrgRedPanic.copy(alpha = 0.14f))
                    .border(2.dp, DrgRedPanic.copy(alpha = 0.55f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("$secondsLeft", fontSize = 32.sp, fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Black, color = DrgRedPanic)
            }
            Text("BENTURAN KERAS TERDETEKSI!", fontSize = 15.sp, fontWeight = FontWeight.Black, color = DrgRedPanic, textAlign = TextAlign.Center)
            Text(
                text = "G-Force: %.1fG. Sinyal darurat SOS dikirim otomatis ke Satgas dalam $secondsLeft detik jika tidak dibatalkan.".format(detectedGForce),
                fontSize = 12.sp,
                color = DrgTextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
