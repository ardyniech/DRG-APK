package com.example.modules.services

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PttWalkieTalkieDialog(
    onDismiss: () -> Unit
) {
    var isTransmitting by remember { mutableStateOf(false) }
    val malangChannels = listOf(
        "Kanal Utama Suhat & Lowokwaru",
        "Kanal Koordinasi Satgas Klojen",
        "Kanal Pantauan Macet Karanglo & Singosari",
        "Kanal Lintas Dingin Batu - Pujon"
    )
    var channelIndex by remember { mutableStateOf(0) }
    val channelName = malangChannels[channelIndex]
    var lastTransmittedText by remember { mutableStateOf("Ketuk tombol di bawah untuk mulai transmisi radio.") }
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    // Sine wave offset animation
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset"
    )

    // Smooth transition for wave amplitude based on state
    val amplitudeMultiplier by animateFloatAsState(
        targetValue = if (isTransmitting) 1.2f else 0.15f,
        animationSpec = tween(300),
        label = "amplitude"
    )

    // Active transmission timer and periodic haptics
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .border(2.dp, DrgGrabGreenPrimary, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Radio, contentDescription = "Radio", tint = DrgGrabGreenPrimary)
                        Text(
                            text = "DRG Walkie-Talkie",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DrgTextPrimary
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isTransmitting) DrgRedDanger.copy(alpha = 0.15f) else DrgGrabGreenContainer.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (isTransmitting) "TX (Active)" else "RX (Ready)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isTransmitting) DrgRedDanger else DrgGrabGreenPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Channel Selector Banner
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            channelIndex = (channelIndex + 1) % malangChannels.size
                            lastTransmittedText = "Beralih ke ${malangChannels[channelIndex]}. Menunggu transmisi..."
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DrgBackground)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Saluran Aktif (Ketuk untuk ganti kanal Malang)", fontSize = 10.sp, color = DrgGrabGreenPrimary, fontWeight = FontWeight.Bold)
                            Text(channelName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                        }
                        Icon(Icons.Default.VolumeUp, contentDescription = "Volume", tint = DrgGrabGreenPrimary)
                    }
                }

                // Beautiful Sine Wave Soundwave Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DrgBackground)
                        .border(1.dp, DrgGrabGreenContainer.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        val path = Path()
                        path.moveTo(0f, height / 2f)

                        for (x in 0..width.toInt() step 6) {
                            val angle = (x.toFloat() / width * 3 * Math.PI) + waveOffset
                            val y = height / 2f + Math.sin(angle).toFloat() * 24f * amplitudeMultiplier
                            path.lineTo(x.toFloat(), y)
                        }

                        drawPath(
                            path = path,
                            color = if (isTransmitting) DrgRedDanger else DrgGrabGreenPrimary,
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }
                }

                // Interactive Status Info
                Text(
                    text = lastTransmittedText,
                    fontSize = 12.sp,
                    color = if (isTransmitting) DrgRedDanger else DrgTextSecondary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                // Large Circular PTT Toggle Button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(120.dp)
                ) {
                    Button(
                        onClick = {
                            isTransmitting = !isTransmitting
                            if (!isTransmitting) {
                                lastTransmittedText = "Transmisi Selesai. Terkirim ke Satgas."
                            }
                        },
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

                // Cancel / Back Button
                TextButton(
                    onClick = onDismiss,
                    enabled = !isTransmitting,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tutup", color = DrgTextSecondary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
