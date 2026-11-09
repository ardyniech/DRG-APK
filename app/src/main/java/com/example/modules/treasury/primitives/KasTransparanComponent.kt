package com.example.modules.treasury.primitives

import androidx.compose.foundation.background
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
import com.example.shared.models.KasTransaction
import com.example.shared.models.TransactionType
import com.example.ui.theme.*

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

    Column(
        modifier = modifier.fillMaxSize().background(DrgBackground).padding(horizontal = 14.dp, vertical = 8.dp).testTag("kas_transparan_component"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        KasSummaryHeader(totalIncome = totalIncome, totalExpense = totalExpense, netBalance = netBalance)
        KasFinancialChartCard(totalIncome = totalIncome, totalExpense = totalExpense, netBalance = netBalance)
        KasPeriodSummaryCard(transactions = transactions, selectedPeriod = selectedPeriod, onPeriodSelected = { selectedPeriod = it })

        val context = LocalContext.current
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (canAddTransaction) {
                Button(
                    onClick = onAddTransactionClick,
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.3f).height(42.dp).testTag("add_kas_transaction_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah", tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Catat Kas", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            OutlinedButton(
                onClick = { KasReportExporter.exportReport(context, selectedPeriod, totalIncome, totalExpense, netBalance, filteredList) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).height(42.dp).testTag("export_kas_report_button")
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Ekspor", tint = DrgGreenPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Ekspor Rekap", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgGreenPrimary)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Saring Status:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextSecondary)
            FilterChip(
                selected = selectedFilter == null, onClick = { selectedFilter = null },
                label = { Text("Semua", fontSize = 10.sp) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenPrimary, selectedLabelColor = Color.White)
            )
            FilterChip(
                selected = selectedFilter == TransactionType.INCOME, onClick = { selectedFilter = TransactionType.INCOME },
                label = { Text("Pemasukan (+)", fontSize = 10.sp) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenPrimary, selectedLabelColor = Color.White)
            )
            FilterChip(
                selected = selectedFilter == TransactionType.EXPENSE, onClick = { selectedFilter = TransactionType.EXPENSE },
                label = { Text("Pengeluaran (-)", fontSize = 10.sp) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgRedDanger, selectedLabelColor = Color.White)
            )
        }

        if (filteredList.isEmpty()) {
            com.example.shared.atoms.EmptyStateOrganicGraphic(
                title = "Belum Ada Transaksi Kas",
                message = "Transaksi untuk filter/kategori yang Anda pilih saat ini masih kosong."
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredList) { tx -> KasTransactionCard(transaction = tx) }
            }
        }
    }
}
