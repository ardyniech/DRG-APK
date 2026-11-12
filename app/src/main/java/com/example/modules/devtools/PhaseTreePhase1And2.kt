package com.example.modules.devtools

object PhaseTreePhase1And2 {
    fun createPhase1(): DevPhase = DevPhase(
        phaseNumber = 1,
        title = "Fondasi Arsitektur, Keamanan & RBAC",
        objective = "Memastikan persistensi lokal kokoh dan hak wewenang terlindungi.",
        subPhases = listOf(
            DevSubPhase(
                id = "SUB-1.1",
                title = "Room v13 Storage & Multi-Entity",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-1.1.1",
                        name = "23 Tabel Database & Migrasi Eksplisit",
                        status = PhaseTreeStatus.LULUS_SEMPURNA,
                        hasDefect = false,
                        whatIsIt = "Skema lokal SQLite Room v13 untuk seluruh modul data.",
                        whyRequired = "Data driver, posko, kas, dan forum tersimpan aman di internal."
                    ),
                    DevFeatureLeaf(
                        id = "F-1.1.2",
                        name = "UDF Flow & ViewModel MVI Architecture",
                        status = PhaseTreeStatus.LULUS_SEMPURNA,
                        hasDefect = false,
                        whatIsIt = "StateFlow reactive pipeline dengan isolasi total modul.",
                        whyRequired = "Mencegah race condition dan recomposition lag pada layar driver."
                    )
                )
            ),
            DevSubPhase(
                id = "SUB-1.2",
                title = "Wewenang Role & Keamanan Sesi",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-1.2.1",
                        name = "Audit Log Mutasi Role (RoleAuditLogEntity)",
                        status = PhaseTreeStatus.LULUS_SEMPURNA,
                        hasDefect = false,
                        whatIsIt = "Pencatatan riwayat pergantian jabatan kepengurusan komunitas.",
                        whyRequired = "Transparansi pertanggungjawaban penunjukan pengurus posko."
                    ),
                    DevFeatureLeaf(
                        id = "F-1.2.2",
                        name = "Validasi Wewenang Edge Token Signature",
                        status = PhaseTreeStatus.TERKENDALA_CACAT,
                        hasDefect = true,
                        whatIsIt = "Verifikasi tanda tangan kriptografi peran admin di serverless edge.",
                        whyRequired = "Cegah manipulasi lokal tabel role SQLite oleh perangkat root.",
                        defectExplanation = "Saat ini verifikasi role masih client-side di Room RoleManager."
                    )
                )
            )
        )
    )

    fun createPhase2(): DevPhase = DevPhase(
        phaseNumber = 2,
        title = "Komunitas, Kas Transparan & Mitra",
        objective = "Transparansi finansial iuran dan jejaring perawatan armada.",
        subPhases = listOf(
            DevSubPhase(
                id = "SUB-2.1",
                title = "Kas Transparan & Laporan Keuangan",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-2.1.1",
                        name = "Pencatatan Pemasukan & Pengeluaran Kas",
                        status = PhaseTreeStatus.LULUS_SEMPURNA,
                        hasDefect = false,
                        whatIsIt = "Buku kas dengan saldo berjalan dan kategori (data tercatat di aplikasi).",
                        whyRequired = "Membantu transparansi pencatatan dana kas paguyuban ojol."
                    ),
                    DevFeatureLeaf(
                        id = "F-2.1.2",
                        name = "Export Laporan CSV / Text Transparan",
                        status = PhaseTreeStatus.LULUS_SEMPURNA,
                        hasDefect = false,
                        whatIsIt = "Generator ringkasan keuangan untuk dibagikan ke grup WA.",
                        whyRequired = "Anggota tanpa aplikasi tetap dapat memeriksa saldo kas bulanan."
                    )
                )
            ),
            DevSubPhase(
                id = "SUB-2.2",
                title = "Forum Diskusi & Rating Bengkel",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-2.2.1",
                        name = "Ulasan & Rating Bintang Bengkel Rekanan",
                        status = PhaseTreeStatus.LULUS_SEMPURNA,
                        hasDefect = false,
                        whatIsIt = "Direktori bengkel rekanan dengan diskon dan ulasan driver.",
                        whyRequired = "Membantu armada mendapatkan servis terpercaya saat mogok."
                    ),
                    DevFeatureLeaf(
                        id = "F-2.2.2",
                        name = "Klaim Dokumen Asuransi Laka Driver (PENDING DEV)",
                        status = PhaseTreeStatus.PENDING,
                        hasDefect = false,
                        whatIsIt = "Unggah dokumen klaim santunan laka lantas driver (ditunda).",
                        whyRequired = "Di luar scope rilis awal (finansial awal: iuran Kas saja).",
                        defectExplanation = "DEFERRED/PENDING DEV per roadmap — bukan cacat in-scope."
                    )
                )
            )
        )
    )
}
