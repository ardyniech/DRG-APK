package com.example.modules.treasury

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.modules.treasury.primitives.KasTransparanComponent
import com.example.shared.models.KasTransaction

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
    KasTransparanComponent(
        transactions = transactions,
        totalIncome = totalIncome,
        totalExpense = totalExpense,
        netBalance = netBalance,
        canAddTransaction = canAddTransaction,
        onAddTransactionClick = onAddTransactionClick,
        modifier = modifier
    )
}

