package com.example.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.repository.DRGRepository
import com.example.shared.models.KasCategory
import com.example.shared.models.KasTransaction
import com.example.shared.models.TransactionType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CashManagementViewModel(
    private val repository: DRGRepository
) : ViewModel() {

    val transactions: StateFlow<List<KasTransaction>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalIncome: StateFlow<Long> = transactions.map { list ->
        list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val totalExpense: StateFlow<Long> = transactions.map { list ->
        list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val netBalance: StateFlow<Long> = combine(totalIncome, totalExpense) { income, expense ->
        income - expense
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    fun addTransaction(
        title: String,
        amount: Long,
        type: TransactionType,
        category: KasCategory,
        description: String,
        recordedBy: String
    ) {
        viewModelScope.launch {
            val dateStr = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
            val tx = KasTransaction(
                id = "TX-${System.currentTimeMillis()}",
                title = title,
                amount = amount,
                type = type,
                category = category,
                dateString = dateStr,
                recordedBy = recordedBy,
                description = description
            )
            repository.addKasTransaction(tx)
        }
    }
}
