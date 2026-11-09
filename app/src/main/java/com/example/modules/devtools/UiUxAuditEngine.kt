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
    fun auditExperience(): UiUxAuditSummary {
        val pillars = listOf(
            UiUxAuditPillar(
                title = "Touch Target Minimum 48x48dp",
                category = "Aksesibilitas & Ergonomi",
                scoreOutOf100 = 92.0f,
                isCompliant = true,
                fieldObservation = "FAB SOS darurat 96dp jumbo, tombol bottom nav 56dp, card transaksi min 48dp.",
                driverImpact = "Sangat mudah ditekan saat memakai sarung tangan motor tebal.",
                remediationStep = "Pertahankan padding 12dp pada chip filter periode kas."
            ),
            UiUxAuditPillar(
                title = "Kontras Terik Matahari Siang Hari",
                category = "Visual & Readability",
                scoreOutOf100 = 88.0f,
                isCompliant = true,
                fieldObservation = "Brand tokens DrgGreenPrimary (#00875A) dan teks DrgTextPrimary (#0F172A) kontras tinggi.",
                driverImpact = "Teks saldo kas, tarif, dan alamat posko terbaca jelas di bawah terik matahari.",
                remediationStep = "Hindari warna abu-abu tipis (<3.0:1) pada timestamp chat forum."
            ),
            UiUxAuditPillar(
                title = "Beban Kognitif Berkendara (One-Thumb)",
                category = "Safety While Riding",
                scoreOutOf100 = 80.0f,
                isCompliant = true,
                fieldObservation = "Maksimal 3 aksi primer per viewport. Informasi disajikan bertingkat.",
                driverImpact = "Driver tidak terdistraksi mencari tombol darurat saat melaju di lalu lintas padat.",
                remediationStep = "Sembunyikan menu sekunder di balik menu profil / floating sheet."
            ),
            UiUxAuditPillar(
                title = "Recomposition Hitch & 60 FPS Render",
                category = "Performance & Frame Drops",
                scoreOutOf100 = 78.0f,
                isCompliant = false,
                fieldObservation = "Filter transaksi kas masih menggunakan remember standar tanpa derivedStateOf.",
                driverImpact = "Potensi micro-stutter saat scroll daftar 50+ riwayat transaksi kas.",
                remediationStep = "Bungkus filter state dengan remember { derivedStateOf { ... } }."
            ),
            UiUxAuditPillar(
                title = "Cakupan Respon Haptic Getar Taktil",
                category = "Tactile Haptics",
                scoreOutOf100 = 90.0f,
                isCompliant = true,
                fieldObservation = "SosHapticManager aktif pada countdown SOS, konfirmasi modal, dan check-in posko.",
                driverImpact = "Memberikan sensasi fisik bahwa tombol darurat telah terdaftar tanpa harus melirik HP.",
                remediationStep = "Tambahkan getaran mikro (tick haptic) pada toggle filter tab."
            ),
            UiUxAuditPillar(
                title = "Jaminan Zero UI Dead-End Saat Offline",
                category = "Offline Resilience",
                scoreOutOf100 = 85.0f,
                isCompliant = true,
                fieldObservation = "State kosong menampilkan pesan ramah dan tombol coba lagi (retry) tanpa crash.",
                driverImpact = "Driver di daerah pelosok tanpa sinyal tetap bisa melihat peta offline dan data kas lokal.",
                remediationStep = "Pastikan tombol klaim asuransi menampilkan pesan fallback lokal."
            )
        )

        val avgScore = pillars.map { it.scoreOutOf100 }.average().toFloat()
        val grade = when {
            avgScore >= 90f -> "Grade A (Sangat Ergonomis)"
            avgScore >= 80f -> "Grade B+ (Ramah Berkendara)"
            avgScore >= 70f -> "Grade B (Cukup, Perlu Poles)"
            else -> "Grade C (Beresiko Terdistraksi)"
        }

        return UiUxAuditSummary(
            overallScore = avgScore,
            grade = grade,
            pillars = pillars,
            passedPillarsCount = pillars.count { it.isCompliant },
            totalPillarsCount = pillars.size
        )
    }
}
