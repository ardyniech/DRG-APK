package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType(val label: String) {
    INCOME("Pemasukan"),
    EXPENSE("Pengeluaran")
}

enum class KasCategory(val label: String) {
    IURAN_BULANAN("Iuran Kas Anggota"),
    UANG_DUKA("Santunan / Uang Duka"),
    BANSOS("Bansos & Kepedulian"),
    KAS_POSKO("Operasional & Logistik Posko"),
    EVENT_KOPDAR("Agenda & Kopdar Bulanan"),
    SERVIS_ARMADA("Subsidi Servis Darurat"),
    LAINNYA("Lain-lain")
}

@Entity(tableName = "kas_transactions")
data class KasTransaction(
    @PrimaryKey val id: String,
    val title: String,
    val amount: Long,
    val type: TransactionType,
    val category: KasCategory,
    val dateString: String,
    val timestamp: Long = System.currentTimeMillis(),
    val recordedBy: String,
    val description: String,
    val receiptProof: String = "Terverifikasi Bendahara"
)
