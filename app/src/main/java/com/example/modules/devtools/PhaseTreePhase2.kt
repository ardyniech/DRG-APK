package com.example.modules.devtools

object PhaseTreePhase2 {
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
                        name = "Klaim Dokumen Asuransi Laka Driver (DEFERRED)",
                        status = PhaseTreeStatus.DEFERRED,
                        hasDefect = false,
                        whatIsIt = "Unggah dokumen klaim santunan laka lantas driver (ditunda).",
                        whyRequired = "Di luar scope rilis awal (finansial awal: iuran Kas saja).",
                        defectExplanation = "DEFERRED per roadmap — bukan cacat in-scope, tidak dihitung di progres."
                    )
                )
            ),
            DevSubPhase(
                id = "SUB-2.3",
                title = "Event Komunitas & Kesiapan Panitia",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-2.3.1",
                        name = "Engine Event: Tugas Panitia, Anggaran & Feedback",
                        status = PhaseTreeStatus.DALAM_PENGERJAAN,
                        hasDefect = false,
                        whatIsIt = "Room v14: community_events + event_committee_tasks + event_member_feedback, ter-wire ke repository/coordinator/ViewModel.",
                        whyRequired = "Agenda komunitas transparan: progres panitia, anggaran, dan masukan anggota.",
                        defectExplanation = "Engine sudah ter-wire; build/runtime pasca-revamp UI belum diverifikasi di sini."
                    ),
                    DevFeatureLeaf(
                        id = "F-2.3.2",
                        name = "UI Tab EVENTS & Readiness",
                        status = PhaseTreeStatus.DALAM_PENGERJAAN,
                        hasDefect = false,
                        whatIsIt = "Tab EVENTS top-level: daftar event, detail, tugas panitia, feedback, dan readiness.",
                        whyRequired = "Satu jalur event yang konsisten untuk pengurus & anggota.",
                        defectExplanation = "Revamp UI bertahap; sinkronisasi visual/navigasi halaman masih berjalan."
                    )
                )
            )
        )
    )
}
