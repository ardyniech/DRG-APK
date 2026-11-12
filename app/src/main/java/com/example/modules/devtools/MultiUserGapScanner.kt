package com.example.modules.devtools

/**
 * Bawel scanner: cacat multi-user, sync, dan keamanan.
 * Sumber: temuan bawel/devtools (sejalan audit_minus.py & kondisi repo). Perbarui saat fakta berubah.
 * Finansial rilis awal: iuran Kas saja — marketplace/arisan DEFERRED (bukan defect blocker di sini).
 */
object MultiUserGapScanner {
    /** @return cacat tambahan (DEFECT-05 s/d 11) di luar 4 awal, jujur & non-fabricated. */
    fun extraDefects(): List<SystemDefectItem> = listOf(
        SystemDefectItem(
            defectId = "DEFECT-05",
            title = "Sinkronisasi Cloud/Server Belum Nyata (Antrean Lokal Saja)",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Tinggi (Blocker Multi-User)",
            affectedComponent = "core/sync/BackgroundSyncEngine.kt",
            honestDefectExplanation = "Engine sinkron kini jujur memproses antrean lokal (bukan klaim upload cloud palsu), namun belum ada unggah server/Firestore/Retrofit nyata. Data belum tersinkron lintas perangkat.",
            engineeringRoadmapFix = "Implementasikan upload server sungguhan (Worker + retry) saat backend/akun tersedia; pertahankan status jujur."
        ),
        SystemDefectItem(
            defectId = "DEFECT-06",
            title = "Backend Firebase/Auth/FCM Belum Aktif Penuh",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Tinggi (Blocker Multi-User)",
            affectedComponent = "app/build.gradle.kts & app/google-services.json",
            honestDefectExplanation = "Layanan cloud (Firestore/Auth/FCM) bergantung konfigurasi & kredensial server yang belum aktif end-to-end untuk produksi multi-user, sehingga perangkat bisa terisolasi.",
            engineeringRoadmapFix = "Aktifkan kredensial & dependensi cloud terverifikasi, lalu migrasikan sinkron Room→server secara bertahap."
        ),
        SystemDefectItem(
            defectId = "DEFECT-07",
            title = "Auth Lokal PIN + Seed Default 1234",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Tinggi (Security)",
            affectedComponent = "modules/auth/LoginAuthValidator.kt & .env.example",
            honestDefectExplanation = "Login masih berbasis data lokal perangkat dan PIN dengan proteksi lokal; seed default berisiko bila tidak diganti sebelum rilis.",
            engineeringRoadmapFix = "Pindah ke akun server (mis. Firebase Auth Phone), perkuat penyimpanan PIN, dan tolak kredensial default sebelum produksi."
        ),
        SystemDefectItem(
            defectId = "DEFECT-08",
            title = "Data Lokal Saja — Ganti HP = Hilang",
            type = DefectType.PRODUCTION_DEFECT,
            severityLevel = "Tinggi (Data Loss)",
            affectedComponent = "AndroidManifest allowBackup=false & Room v13",
            honestDefectExplanation = "allowBackup=false tanpa cloud backup/sync. Ganti HP/reset/uninstal menghapus kas/forum/kontak lokal; SQLite belum terenkripsi (risiko data-at-rest).",
            engineeringRoadmapFix = "Tambah export/import kas terenkripsi + cloud backup, dan evaluasi SQLCipher untuk data sensitif."
        ),
        SystemDefectItem(
            defectId = "DEFECT-09",
            title = "Signaling/Discovery P2P Belum Fungsional Lintas Perangkat",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Tinggi (Blocker Multi-User)",
            affectedComponent = "core/p2p/P2PSignalingManager.kt & P2PManager.kt",
            honestDefectExplanation = "Signaling kini berbasis antrean lokal (bukan broadcast debug palsu), namun SDP/ICE/discovery FCM/STUN-TURN nyata belum terbentuk end-to-end.",
            engineeringRoadmapFix = "Implementasikan signaling FCM + pertukaran SDP/ICE penuh dan relay STUN/TURN saat siap."
        ),
        SystemDefectItem(
            defectId = "DEFECT-10",
            title = "Schema Room Historis Hilang (Migrasi Belum Tervalidasi Penuh)",
            type = DefectType.PRODUCTION_DEFECT,
            severityLevel = "Sedang (Data Risk)",
            affectedComponent = "app/schemas/ & Room export",
            honestDefectExplanation = "exportSchema=true namun schema historis 1,2,3,4,8,9.json belum tersedia di repo, sehingga verifikasi migrasi v1→v13 tidak lengkap.",
            engineeringRoadmapFix = "Generate & commit schema hilang via Room export valid (tanpa fabrikasi) dan verifikasi migrasi."
        ),
        SystemDefectItem(
            defectId = "DEFECT-11",
            title = "Fitur Asuransi & Intercom Helm Masih Pending (Di Luar Scope Rilis Awal)",
            type = DefectType.PRODUCTION_DEFECT,
            severityLevel = "Sedang (Scope/Pending)",
            affectedComponent = "roadmap (Asuransi/Intercom Helm) — lihat juga PTT radio (DEFECT-02/F-3.1.1)",
            honestDefectExplanation = "Integrasi klaim asuransi & intercom helm (Bluetooth/GATT/headset) masih pending/deferred sesuai roadmap, bukan bagian scope iuran Kas rilis awal. (PTT walkie-talkie di app saat ini simulasi UI, dilacak terpisah.)",
            engineeringRoadmapFix = "Tandai jelas sebagai PENDING/DEFERRED dan kerjakan setelah prioritas inti produksi stabil; pisahkan dari isu PTT radio."
        )
    )
}
