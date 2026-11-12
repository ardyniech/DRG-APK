package com.example.modules.devtools

data class UiUxAuditPillar(
    val title: String,
    val category: String,
    val scoreOutOf100: Float,
    val isCompliant: Boolean,
    val fieldObservation: String,
    val driverImpact: String,
    val remediationStep: String
)

data class UiUxAuditSummary(
    val overallScore: Float,
    val grade: String,
    val pillars: List<UiUxAuditPillar>,
    val passedPillarsCount: Int,
    val totalPillarsCount: Int
)

object UiUxAuditEngine {
    /** Heuristik/checklist statis (bukan probe runtime terverifikasi). Skor = estimasi. */
    fun auditExperience(): UiUxAuditSummary {
        val pillars = listOf(
            UiUxAuditPillar("Touch Target Minimum 48x48dp", "Aksesibilitas & Ergonomi", 80f, true,
                "Heuristik: beberapa kontrol utama (SOS/FAB/nav) cenderung besar; perlu verifikasi per layar.",
                "Target sentuh besar membantu saat berkendara.", "Audit touch target per komponen kritis."),
            UiUxAuditPillar("Kontras & Keterbacaan", "Visual & Readability", 80f, true,
                "Heuristik: token warna utama kontras; perlu cek kontras aktual di semua state/siang hari.",
                "Keterbacaan penting di bawah terik.", "Verifikasi rasio kontras dan teks sekunder."),
            UiUxAuditPillar("Beban Kognitif (One-Thumb)", "Safety While Riding", 75f, true,
                "Heuristik: alur utama dirancang ringkas; belum ada pengukuran tugas nyata.",
                "Minim distraksi saat berkendara.", "Uji alur kritis dengan skenario lapangan."),
            UiUxAuditPillar("Performa Render/60 FPS", "Performance & Frame Drops", 70f, false,
                "Heuristik: potensi recomposition di daftar/filter; tidak diukur profil runtime di sini.",
                "Stutter dapat mengganggu.", "Profiling & optimasi derivedStateOf/lazy list bila perlu."),
            UiUxAuditPillar("Respon Haptic/Taktil", "Tactile Haptics", 75f, true,
                "Heuristik: haptic ada pada beberapa aksi darurat/konfirmasi; cakupan belum diverifikasi penuh.",
                "Umpan balik fisik membantu tanpa melirik.", "Audit cakupan haptic aksi kritis."),
            UiUxAuditPillar("Ketahanan Offline / No Dead-End", "Offline Resilience", 75f, true,
                "Heuristik: app local-first; beberapa fitur butuh jaringan/backend — perlu uji state offline nyata.",
                "Data lokal tetap berguna saat sinyal buruk.", "Uji state kosong/error/offline per fitur.")
        )
        val avgScore = pillars.map { it.scoreOutOf100 }.average().toFloat()
        return UiUxAuditSummary(
            overallScore = avgScore,
            grade = "Estimasi heuristik (belum terverifikasi runtime)",
            pillars = pillars,
            passedPillarsCount = pillars.count { it.isCompliant },
            totalPillarsCount = pillars.size
        )
    }
}
