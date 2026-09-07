package com.example.modules.emergency

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.core.audio.SosAlarmSoundManager
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
        if (isSosAlarmEnabled) {
            SosAlarmSoundManager.playHighDecibelAlarm(10000L)
        }
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
        }
        onConfirmAutoSos()
    }

    DisposableEffect(Unit) {
        onDispose {
            SosAlarmSoundManager.stopAlarm()
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulseScale"
    )

    Dialog(onDismissRequest = onCancel) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = DrgSurface,
            modifier = Modifier.fillMaxWidth().border(2.dp, DrgRedDanger, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = DrgRedContainer,
                    modifier = Modifier.size(76.dp).scale(pulseScale)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "$secondsLeft",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = DrgRedDanger
                        )
                    }
                }

                Text(
                    text = "BENTURAN KERAS TERDETEKSI!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = DrgRedDanger,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "G-Force: %.1fG. Sinyal darurat SOS akan dikirim otomatis ke Satgas dalam $secondsLeft detik jika tidak dibatalkan.".format(detectedGForce),
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
                    onClick = onConfirmAutoSos,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgRedDanger),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("KIRIM SOS SEKARANG", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
