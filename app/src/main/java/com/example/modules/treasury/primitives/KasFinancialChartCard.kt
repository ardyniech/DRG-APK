package com.example.modules.treasury.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgRedDanger
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily
import java.text.NumberFormat

/**
 * Grafik kas jujur: tinggi bar proporsional terhadap total pemasukan/pengeluaran
 * yang benar-benar tercatat. Tidak ada deret bulanan fabrikasi.
 */
@Composable
fun KasFinancialChartCard(
    totalIncome: Long,
    totalExpense: Long,
    netBalance: Long,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    val maxValue = maxOf(totalIncome, totalExpense, 1L).toFloat()
    val incomeFraction = (totalIncome / maxValue).coerceIn(0f, 1f)
    val expenseFraction = (totalExpense / maxValue).coerceIn(0f, 1f)

    DrgLuminousGlassCard(
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 14.dp,
        modifier = modifier.fillMaxWidth().testTag("kas_financial_chart_card")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Grafik Kas Komunitas", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                    Text("Perbandingan pemasukan vs pengeluaran dari transaksi tercatat", fontSize = 10.sp, color = DrgTextSecondary)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(theme.pillBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("Data Tercatat", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                KasBar(label = "Masuk", fraction = incomeFraction, color = theme.primaryColor, modifier = Modifier.weight(1f))
                KasBar(label = "Keluar", fraction = expenseFraction, color = DrgRedDanger, modifier = Modifier.weight(1f))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Saldo bersih", fontSize = 11.sp, color = DrgTextSecondary, fontWeight = FontWeight.SemiBold)
                Text(
                    "Rp ${NumberFormat.getInstance().format(netBalance)}",
                    fontFamily = SpaceGroteskFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (netBalance >= 0) theme.primaryColor else DrgRedDanger
                )
            }
        }
    }
}

@Composable
private fun KasBar(label: String, fraction: Float, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.55f)
                .height((100f * fraction).coerceAtLeast(6f).dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgTextSecondary)
    }
}
