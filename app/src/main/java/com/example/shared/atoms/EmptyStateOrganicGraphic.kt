package com.example.shared.atoms

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextPrimary
import kotlin.math.sin

@Composable
fun EmptyStateOrganicGraphic(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "organic_float")
    val floatAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(animation = tween(4000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "float"
    )

    Column(
        modifier = modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val waveOffset1 = sin(floatAnim) * 8.dp.toPx()
                val waveOffset2 = sin(floatAnim + 1.5f) * 6.dp.toPx()

                drawRoundRect(
                    brush = Brush.radialGradient(listOf(DrgGrabGreenPrimary.copy(alpha = 0.15f), Color.Transparent), center = Offset(size.width * 0.4f, size.height * 0.4f + waveOffset1), radius = size.width * 0.6f),
                    size = size
                )
                drawRoundRect(
                    brush = Brush.radialGradient(listOf(Color(0xFF0288D1).copy(alpha = 0.1f), Color.Transparent), center = Offset(size.width * 0.6f, size.height * 0.6f - waveOffset2), radius = size.width * 0.5f),
                    size = size
                )

                val cardW = size.width * 0.5f
                val cardH = size.height * 0.3f
                val cardTopY = (size.height - cardH) / 2 + (sin(floatAnim * 2) * 4.dp.toPx())
                val cardLeftX = (size.width - cardW) / 2
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.6f),
                    topLeft = Offset(cardLeftX, cardTopY),
                    size = Size(cardW, cardH),
                    cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
                )
                drawRoundRect(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    topLeft = Offset(cardLeftX + 12.dp.toPx(), cardTopY + 12.dp.toPx()),
                    size = Size(cardW * 0.6f, 4.dp.toPx()),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
                drawRoundRect(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    topLeft = Offset(cardLeftX + 12.dp.toPx(), cardTopY + 20.dp.toPx()),
                    size = Size(cardW * 0.4f, 4.dp.toPx()),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = message, fontSize = 12.sp, color = DrgTextMuted, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 20.dp))
    }
}
