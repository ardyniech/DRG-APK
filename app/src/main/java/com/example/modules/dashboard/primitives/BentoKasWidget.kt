package com.example.modules.dashboard.primitives

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.shared.models.KasTransaction
import com.example.shared.models.TransactionType
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily

/**
 * Bento Grid Tile 2: Frosted Glass Kas Transparan with Live Cashflow Sparkline
 */
@Composable
fun BentoKasWidget(
    kasFormatted: String,
    cashTransactions: List<KasTransaction>,
    visualTheme: DashboardVisualTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = visualTheme.secondaryColor
    val points = remember(cashTransactions) {
        val recent = cashTransactions.take(7).reversed()
        if (recent.size < 2) listOf(0.45f, 0.58f, 0.52f, 0.72f, 0.68f, 0.88f)
        else {
            var acc = 50f
            val raw = recent.map { tx ->
                acc += if (tx.type == TransactionType.INCOME) 14f else -10f
                acc.coerceIn(10f, 100f) / 100f
            }
            raw
        }
    }
    val linePath = remember { Path() }

    GlassCard(
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 12.dp,
        onClick = onClick
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(accent))
                    Text(text = "KAS TRANSPARAN", fontSize = 10.sp, fontWeight = FontWeight.Black, color = accent)
                }
                Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Kas", tint = accent, modifier = Modifier.size(16.dp))
            }

            Canvas(modifier = Modifier.fillMaxWidth().height(34.dp).padding(vertical = 4.dp)) {
                linePath.reset()
                val step = size.width / (points.size - 1).coerceAtLeast(1)
                points.forEachIndexed { idx, v ->
                    val x = idx * step
                    val y = size.height * (1f - v.coerceIn(0.1f, 0.95f))
                    if (idx == 0) linePath.moveTo(x, y) else linePath.lineTo(x, y)
                }
                drawPath(path = linePath, color = accent, style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round))
                val lastY = size.height * (1f - points.last().coerceIn(0.1f, 0.95f))
                drawCircle(color = accent, radius = 3.5.dp.toPx(), center = Offset(size.width, lastY))
            }

            Column {
                Text(text = "Saldo Aktif", fontSize = 10.sp, color = DrgTextSecondary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Rp $kasFormatted", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextDark)
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Brush.horizontalGradient(listOf(accent.copy(alpha = 0.16f), accent.copy(alpha = 0.08f)))).padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "AUDIT", fontSize = 9.sp, fontWeight = FontWeight.Black, color = accent)
                    }
                }
            }
        }
    }
}
