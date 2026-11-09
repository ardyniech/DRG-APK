package com.example.modules.devtools

object PhaseTreePhase5 {
    fun createPhase5(): DevPhase = DevPhase(
        phaseNumber = 5,
        title = "AI Engine, Observabilitas & CI/CD",
        objective = "Pengawasan kesehatan sistem menyeluruh dan otomatisasi cerdas.",
        subPhases = listOf(
            DevSubPhase(
                id = "SUB-5.1",
                title = "AI Assistant & Gamifikasi Lapangan",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-5.1.1",
                        name = "Summary Rute Cerdas & Cuaca Gemini AI",
                        status = PhaseTreeStatus.LULUS_SEMPURNA,
                        hasDefect = false,
                        whatIsIt = "Asisten AI pemberi rekomendasi rute aman dan posko terdekat.",
                        whyRequired = "Efisiensi navigasi saat cuaca hujan atau jalanan macet."
                    ),
                    DevFeatureLeaf(
                        id = "F-5.1.2",
                        name = "Gamifikasi Lencana Loyalitas Driver",
                        status = PhaseTreeStatus.LULUS_SEMPURNA,
                        hasDefect = false,
                        whatIsIt = "Pemberian lencana seperti 'Pelopor Keselamatan' & poin loyalitas.",
                        whyRequired = "Menumbuhkan motivasi saling bantu antar sesama pengemudi."
                    )
                )
            ),
            DevSubPhase(
                id = "SUB-5.2",
                title = "Observabilitas DevTools & Pipeline CI/CD",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-5.2.1",
                        name = "Audit DevTools Mandiri Tanpa Mocking Data",
                        status = PhaseTreeStatus.LULUS_SEMPURNA,
                        hasDefect = false,
                        whatIsIt = "Dashboard diagnosa lengkap untuk inspeksi bug, RAM, & node.",
                        whyRequired = "AI & human developer dapat mengawasi status riil aplikasi."
                    ),
                    DevFeatureLeaf(
                        id = "F-5.2.2",
                        name = "Background Sync Cloud Worker Queue",
                        status = PhaseTreeStatus.DALAM_PENGERJAAN,
                        hasDefect = false,
                        whatIsIt = "Worker background penampung queue sinkronisasi offline ke cloud.",
                        whyRequired = "Menjamin transaksi kas & postingan forum tidak hilang.",
                        defectExplanation = "Saat ini status pending penalti -2% dalam antrean roadmap."
                    )
                )
            )
        )
    )
}
