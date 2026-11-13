package com.example.modules.treasury.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.treasury.KasTransactionCard
import com.example.shared.atoms.EmptyStateOrganicGraphic
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.KasTransaction
import com.example.shared.models.TransactionType
import com.example.ui.theme.DrgRedDanger
import com.example.ui.theme.DrgTextSecondary

@Composable
fun KasTransparanComponent(
    transactions: List<KasTransaction>,
    totalIncome: Long,
    totalExpense: Long,
    netBalance: Long,
    canAddTransaction: Boolean = false,
    onAddTransactionClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    val accent = theme.primaryColor
    var selectedFilter by remember { mutableStateOf<TransactionType?>(null) }
    var selectedPeriod by remember { mutableStateOf("Semua") }
    val filteredList = remember(transactions, selectedFilter, selectedPeriod) {
        val byPeriod = when (selectedPeriod) {
            "Bulan Ini" -> transactions.filter { it.dateString.contains("Okt 2026") || it.dateString.contains("Hari ini") }
            "Tahun Ini" -> transactions.filter { it.dateString.contains("2026") }
            else -> transactions
        }
        if (selectedFilter == null) byPeriod else byPeriod.filter { it.type == selectedFilter }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 14.dp).testTag("kas_transparan_component"),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(top = 6.dp, bottom = 24.dp)
    ) {
        item { KasSummaryHeader(totalIncome = totalIncome, totalExpense = totalExpense, netBalance = netBalance) }
        item { KasFinancialChartCard(totalIncome = totalIncome, totalExpense = totalExpense, netBalance = netBalance) }
        item { KasPeriodSummaryCard(transactions = transactions, selectedPeriod = selectedPeriod, onPeriodSelected = { selectedPeriod = it }) }

        item {
            val context = LocalContext.current
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (canAddTransaction) {
                    Button(
                        onClick = onAddTransactionClick,
                        colors = ButtonDefaults.buttonColors(containerColor = accent),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1.3f).height(44.dp).testTag("add_kas_transaction_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Tambah", tint = Color.White, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Catat Kas", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                    }
                }
                OutlinedButton(
                    onClick = { KasReportExporter.exportReport(context, selectedPeriod, totalIncome, totalExpense, netBalance, filteredList) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f).height(44.dp).testTag("export_kas_report_button")
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Ekspor", tint = accent, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ekspor Rekap", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = accent)
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Saring:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextSecondary)
                FilterChip(
                    selected = selectedFilter == null, onClick = { selectedFilter = null },
                    label = { Text("Semua", fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = accent, selectedLabelColor = Color.White)
                )
                FilterChip(
                    selected = selectedFilter == TransactionType.INCOME, onClick = { selectedFilter = TransactionType.INCOME },
                    label = { Text("Masuk (+)", fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = accent, selectedLabelColor = Color.White)
                )
                FilterChip(
                    selected = selectedFilter == TransactionType.EXPENSE, onClick = { selectedFilter = TransactionType.EXPENSE },
                    label = { Text("Keluar (-)", fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgRedDanger, selectedLabelColor = Color.White)
                )
            }
        }

        if (filteredList.isEmpty()) {
            item {
                EmptyStateOrganicGraphic(
                    title = "Belum Ada Transaksi Kas",
                    message = "Transaksi untuk filter/kategori yang Anda pilih saat ini masih kosong."
                )
            }
        } else {
            items(filteredList) { tx -> KasTransactionCard(transaction = tx) }
        }
    }
}
