package com.example.modules.treasury

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.KasTransaction
import com.example.shared.models.TransactionType
import com.example.ui.theme.*

@Composable
fun TransparencyReportScreen(
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
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = DrgGreenDark,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, DrgGreenPrimary, RoundedCornerShape(14.dp))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "TOTAL SALDO KAS BENDAHARA",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Rp ${java.text.NumberFormat.getInstance().format(netBalance)}",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Masuk: Rp ${java.text.NumberFormat.getInstance().format(totalIncome)}",
                        color = DrgGreenContainer,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Keluar: Rp ${java.text.NumberFormat.getInstance().format(totalExpense)}",
                        color = Color(0xFFFFEBEE),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (canAddTransaction) {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onAddTransactionClick,
                colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah", tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Catat Transaksi Kas Baru", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saring:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = DrgTextSecondary
            )
            FilterChip(
                selected = selectedFilter == null,
                onClick = { selectedFilter = null },
                label = { Text("Semua", fontSize = 10.sp) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenContainer, selectedLabelColor = DrgGreenPrimary)
            )
            FilterChip(
                selected = selectedFilter == TransactionType.INCOME,
                onClick = { selectedFilter = TransactionType.INCOME },
                label = { Text("Pemasukan", fontSize = 10.sp) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenContainer, selectedLabelColor = DrgGreenPrimary)
            )
            FilterChip(
                selected = selectedFilter == TransactionType.EXPENSE,
                onClick = { selectedFilter = TransactionType.EXPENSE },
                label = { Text("Pengeluaran", fontSize = 10.sp) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenContainer, selectedLabelColor = DrgGreenPrimary)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filteredList) { tx ->
                KasTransactionCard(transaction = tx)
            }
        }
    }
}
