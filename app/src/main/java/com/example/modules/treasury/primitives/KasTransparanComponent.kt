package com.example.modules.treasury.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val filteredList = remember(transactions, selectedFilter) {
        if (selectedFilter == null) transactions else transactions.filter { it.type == selectedFilter }
    }

    Column(
        modifier = modifier.fillMaxSize().background(DrgBackground).padding(horizontal = 14.dp, vertical = 8.dp).testTag("kas_transparan_component"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        KasSummaryHeader(totalIncome = totalIncome, totalExpense = totalExpense, netBalance = netBalance)

        if (canAddTransaction) {
            Button(
                onClick = onAddTransactionClick,
                colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(44.dp).testTag("add_kas_transaction_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah", tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Catat Mutasi Kas Transparan", fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                Text(text = "Belum ada transaksi kas untuk kategori ini.", fontSize = 12.sp, color = DrgTextMuted)
            }
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
