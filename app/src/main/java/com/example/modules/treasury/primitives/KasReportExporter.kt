package com.example.modules.treasury.primitives

import android.content.Context
import android.content.Intent
import com.example.shared.models.KasTransaction
import com.example.shared.models.TransactionType
import java.text.NumberFormat
import java.util.Locale

object KasReportExporter {
    fun exportReport(
        context: Context,
        period: String,
        totalIncome: Long,
        totalExpense: Long,
        netBalance: Long,
        transactions: List<KasTransaction>
    ) {
        val rupiah = NumberFormat.getNumberInstance(Locale("id", "ID"))
        val builder = StringBuilder()
        builder.appendLine("=================================")
        builder.appendLine("📊 LAPORAN KAS KOMUNITAS DRG")
        builder.appendLine("Driver Riang Gembira (Malang Raya)")
        builder.appendLine("Periode: $period | Real-time Transparency")
        builder.appendLine("=================================")
        builder.appendLine("💰 Total Pemasukan  : Rp ${rupiah.format(totalIncome)}")
        builder.appendLine("💸 Total Pengeluaran: Rp ${rupiah.format(totalExpense)}")
        builder.appendLine("📈 Saldo Kas Bersih : Rp ${rupiah.format(netBalance)}")
        builder.appendLine("=================================")
        builder.appendLine("RINCIAN MUTASI TERKINI:")

        transactions.take(15).forEachIndexed { idx, tx ->
            val sign = if (tx.type == TransactionType.INCOME) "(+)" else "(-)"
            builder.appendLine("${idx + 1}. [${tx.dateString}] ${tx.title}")
            builder.appendLine("   $sign Rp ${rupiah.format(tx.amount)} | Kat: ${tx.category.name} | Catat: ${tx.recordedBy}")
        }

        if (transactions.size > 15) {
            builder.appendLine("... dan ${transactions.size - 15} transaksi lainnya.")
        }
        builder.appendLine("=================================")
        builder.appendLine("Transparan, Terpercaya, Guyub Rukun DRG")

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, builder.toString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Ekspor Laporan Kas DRG")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}
