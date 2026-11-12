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
                        name = "Audit DevTools Mandiri (Jujur/Non-Mocking)",
                        status = PhaseTreeStatus.DALAM_PENGERJAAN,
                        hasDefect = false,
                        whatIsIt = "Dashboard diagnosa (bug, RAM, node, CI/SSOT) yang makin selaras fakta riil.",
                        whyRequired = "AI & developer butuh visibilitas jujur atas status aplikasi."
                    ),
                    DevFeatureLeaf(
                        id = "F-5.2.2",
                        name = "Background Sync Queue (Local-Only, Jujur)",
                        status = PhaseTreeStatus.DALAM_PENGERJAAN,
                        hasDefect = false,
                        whatIsIt = "Antrean sinkronisasi lokal diproses secara jujur; unggah cloud/server nyata belum ada.",
                        whyRequired = "Fondasi untuk sinkron multi-user saat backend tersedia.",
                        defectExplanation = "Belum ada upload server/Firestore; status tetap jujur (no fake sync)."
                    )
                )
            )
        )
    )
}
