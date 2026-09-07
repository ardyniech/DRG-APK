package com.example.shared.models

enum class MemberRole(
    val title: String,
    val shortName: String,
    val description: String,
    val badgeColorHex: Long
) {
    KETUA("Ketua Komunitas", "Ketua", "Pimpinan tertinggi komunitas DRG", 0xFF00875A),
    WAKIL_KETUA("Wakil Ketua", "Waket", "Membantu kepemimpinan operasional", 0xFF00A86B),
    SEKRETARIS("Sekretaris / Admin", "Sekretaris", "Manajemen data anggota & administrasi", 0xFF0288D1),
    BENDAHARA("Bendahara", "Bendahara", "Pengelola kas & transparansi keuangan", 0xFFFF8F00),
    SATGAS("Satuan Tugas (Satgas)", "Satgas", "Pengamanan jalur & tanggap darurat", 0xFFE53935),
    DEWAN_ETIKA("Dewan Bimbingan & Etika", "Dewan Etika", "Pengawasan kode etik & screening", 0xFF7B1FA2),
    ANGGOTA("Anggota Biasa", "Driver", "Driver aktif komunitas DRG", 0xFF475569);

    val isLeadership: Boolean
        get() = this != ANGGOTA
}

enum class VerificationStatus {
    PENDING_SCREENING,
    VERIFIED,
    SUSPENDED,
    REJECTED
}
