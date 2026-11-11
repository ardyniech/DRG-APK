package com.example.modules.treasury.primitives

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun KasFinancialChartCard(
    totalIncome: Long,
    totalExpense: Long,
    netBalance: Long,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.92f),
        modifier = modifier
            .fillMaxWidth()
            .border(1.2.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
            .testTag("kas_financial_chart_card")
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("📈 Transparansi Grafik Kas DRG", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                    Text("Visualisasi Pemasukan vs Pengeluaran & Tren Saldo", fontSize = 10.sp, color = DrgTextSecondary)
                }
                Surface(shape = RoundedCornerShape(10.dp), color = DrgGreenLight) {
                    Text("Audit Real-Time", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                LegendItem(color = DrgGreenPrimary, label = "Pemasukan")
                LegendItem(color = DrgRedDanger, label = "Pengeluaran")
                LegendItem(color = DrgGoldReward, label = "Tren Saldo")
            }

            // Canvas Chart Rendering
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DrgSlateBgSoft)
                    .border(1.dp, DrgSlateBorder, RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val barWidth = 14f
                    val months = listOf("Jun", "Jul", "Ags", "Sep", "Okt")
                    val incomeVals = listOf(0.4f, 0.65f, 0.55f, 0.8f, 0.95f)
                    val expenseVals = listOf(0.2f, 0.35f, 0.25f, 0.4f, 0.3f)

                    val stepX = w / (months.size)

                    months.forEachIndexed { i, _ ->
                        val centerX = stepX * i + stepX / 2f
                        val incHeight = h * 0.7f * incomeVals[i]
                        val expHeight = h * 0.7f * expenseVals[i]

                        // Income Bar
                        drawRoundRect(
                            color = DrgGreenPrimary,
                            topLeft = Offset(centerX - barWidth - 2f, h - 16f - incHeight),
                            size = Size(barWidth, incHeight),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                        // Expense Bar
                        drawRoundRect(
                            color = DrgRedDanger,
                            topLeft = Offset(centerX + 2f, h - 16f - expHeight),
                            size = Size(barWidth, expHeight),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                    }

                    // Net Trendline Curve
                    val path = Path()
                    months.forEachIndexed { i, _ ->
                        val centerX = stepX * i + stepX / 2f
                        val netY = h - 16f - (h * 0.65f * (incomeVals[i] - expenseVals[i] * 0.5f))
                        if (i == 0) path.moveTo(centerX, netY) else path.lineTo(centerX, netY)
                        drawCircle(color = DrgGoldReward, radius = 4f, center = Offset(centerX, netY))
                    }
                    drawPath(path = path, color = DrgGoldReward, style = Stroke(width = 3f))
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = DrgTextSecondary)
    }
}
