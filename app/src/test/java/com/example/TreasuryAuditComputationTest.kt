package com.example

import com.example.shared.models.KasCategory
import com.example.shared.models.KasTransaction
import com.example.shared.models.TransactionType
import org.junit.Assert.*
import org.junit.Test

class TreasuryAuditComputationTest {

    @Test
    fun testEmptyTreasuryLedgerComputation() {
        val emptyTransactions = emptyList<KasTransaction>()
        val totalIncome = emptyTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val totalExpense = emptyTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val netBalance = totalIncome - totalExpense

        assertEquals(0L, totalIncome)
        assertEquals(0L, totalExpense)
        assertEquals(0L, netBalance)
    }

    @Test
    fun testComprehensiveTreasuryLedgerCalculations() {
        val transactions = listOf(
            KasTransaction("TX-1", "Iuran 1", 50000, TransactionType.INCOME, KasCategory.IURAN_BULANAN, "01 Sep", 1000L, "Bendahara", "Iuran"),
            KasTransaction("TX-2", "Donasi Bansos", 100000, TransactionType.INCOME, KasCategory.BANSOS, "02 Sep", 2000L, "Bendahara", "Donasi"),
            KasTransaction("TX-3", "Perkakas Rehat", 30000, TransactionType.EXPENSE, KasCategory.KAS_POSKO, "03 Sep", 3000L, "Satgas", "Tools"),
            KasTransaction("TX-4", "Santunan Rekan", 40000, TransactionType.EXPENSE, KasCategory.UANG_DUKA, "04 Sep", 4000L, "Bendahara", "Santunan")
        )

        val totalIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val balance = totalIncome - totalExpense

        assertEquals(150000L, totalIncome)
        assertEquals(70000L, totalExpense)
        assertEquals(80000L, balance)

        val poskoExpenses = transactions.filter { it.category == KasCategory.KAS_POSKO }.sumOf { it.amount }
        assertEquals(30000L, poskoExpenses)
    }

    @Test
    fun testCategoryBreakdownIntegrity() {
        val transactions = listOf(
            KasTransaction("1", "Iuran A", 10000, TransactionType.INCOME, KasCategory.IURAN_BULANAN, "01 Sep", 1000L, "B", "desc"),
            KasTransaction("2", "Iuran B", 20000, TransactionType.INCOME, KasCategory.IURAN_BULANAN, "02 Sep", 2000L, "B", "desc")
        )
        val grouped = transactions.groupBy { it.category }
        assertEquals(1, grouped.size)
        assertEquals(30000L, grouped[KasCategory.IURAN_BULANAN]?.sumOf { it.amount })
    }
}
