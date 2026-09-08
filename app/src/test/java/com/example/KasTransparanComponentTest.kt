package com.example

import com.example.shared.models.KasCategory
import com.example.shared.models.KasTransaction
import com.example.shared.models.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Test

class KasTransparanComponentTest {

    @Test
    fun testKasTransactionSummaryCalculations() {
        val transactions = listOf(
            KasTransaction(
                id = "TX-001",
                title = "Iuran Bulanan Anggota",
                amount = 150000,
                type = TransactionType.INCOME,
                category = KasCategory.IURAN_BULANAN,
                dateString = "07 Sep 2026",
                recordedBy = "Bendahara DRG",
                description = "Iuran reguler September"
            ),
            KasTransaction(
                id = "TX-002",
                title = "Pembelian Perkakas Bengkel",
                amount = 50000,
                type = TransactionType.EXPENSE,
                category = KasCategory.KAS_POSKO,
                dateString = "07 Sep 2026",
                recordedBy = "Pengurus DRG",
                description = "Perkakas darurat posko"
            )
        )

        val totalIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val netBalance = totalIncome - totalExpense

        assertEquals(150000L, totalIncome)
        assertEquals(50000L, totalExpense)
        assertEquals(100000L, netBalance)
    }

    @Test
    fun testFilterTransactionsByType() {
        val transactions = listOf(
            KasTransaction("1", "Iuran 1", 10000, TransactionType.INCOME, KasCategory.IURAN_BULANAN, "01 Jan", 1000L, "Admin", "Desc 1"),
            KasTransaction("2", "Kopi Kopdar", 5000, TransactionType.EXPENSE, KasCategory.EVENT_KOPDAR, "02 Jan", 2000L, "Admin", "Desc 2"),
            KasTransaction("3", "Iuran 2", 15000, TransactionType.INCOME, KasCategory.IURAN_BULANAN, "03 Jan", 3000L, "Admin", "Desc 3")
        )

        val incomeOnly = transactions.filter { it.type == TransactionType.INCOME }
        val expenseOnly = transactions.filter { it.type == TransactionType.EXPENSE }

        assertEquals(2, incomeOnly.size)
        assertEquals(1, expenseOnly.size)
    }
}
