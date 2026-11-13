package com.example.modules.treasury.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.KasTransaction
import com.example.shared.models.TransactionType
import com.example.ui.theme.*
import java.text.NumberFormat

@Composable
fun KasPeriodSummaryCard(
    transactions: List<KasTransaction>,
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    val periods = listOf("Semua", "Bulan Ini", "Tahun Ini")
    val filteredTxs = remember(transactions, selectedPeriod) {
        when (selectedPeriod) {
            "Bulan Ini" -> transactions.filter { it.dateString.contains("Okt 2026") || it.dateString.contains("Hari ini") }
            "Tahun Ini" -> transactions.filter { it.dateString.contains("2026") }
            else -> transactions
        }
    }
    val periodIncome = filteredTxs.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val periodExpense = filteredTxs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    val periodSurplus = periodIncome - periodExpense

    DrgLuminousGlassCard(
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 14.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Assessment, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Ringkasan Laporan Keuangan", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(3.dp))
                    Text("Tercatat", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                periods.forEach { period ->
                    FilterChip(
                        selected = selectedPeriod == period,
                        onClick = { onPeriodSelected(period) },
                        label = { Text(period, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = theme.primaryColor, selectedLabelColor = Color.White)
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Pemasukan Periode", fontSize = 10.sp, color = DrgTextSecondary)
                    Text("+Rp ${NumberFormat.getInstance().format(periodIncome)}", fontFamily = SpaceGroteskFontFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Pengeluaran Periode", fontSize = 10.sp, color = DrgTextSecondary)
                    Text("-Rp ${NumberFormat.getInstance().format(periodExpense)}", fontFamily = SpaceGroteskFontFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgRedDanger)
                }
            }
            Surface(shape = RoundedCornerShape(10.dp), color = theme.pillBg, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Surplus / Arus Kas Bersih:", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = DrgTextSlate)
                    Text("Rp ${NumberFormat.getInstance().format(periodSurplus)}", fontFamily = SpaceGroteskFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Black, color = if (periodSurplus >= 0) theme.primaryColor else DrgRedDanger)
                }
            }
        }
    }
}
