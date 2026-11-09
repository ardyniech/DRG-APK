package com.example.modules.devtools

/**
 * Bawel scanner: cacat multi-user, sync, dan keamanan.
 *
 * Sumber: .github/scripts/audit_minus.py (23 temuan, 6 BLOCKER per 2026-11-09).
 * Finansial komunitas sementara hanya iuran kas — marketplace/arisan DEFERRED,
 * jadi tidak dimasukkan sebagai defect blocker di sini.
 */
object MultiUserGapScanner {
    /**
     * @return 8 cacat tambahan (DEFECT-05 s/d 12) di luar 4 awal.
     */
    fun extraDefects(): List<SystemDefectItem> = listOf(
        SystemDefectItem(
            defectId = "DEFECT-05",
            title = "Fake SYNCED ke Cloud Tanpa Network Call",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Tinggi (Anti-Fabrikasi)",
            affectedComponent = "core/sync/BackgroundSyncEngine.kt",
            honestDefectExplanation = "Label Tersinkronisasi ke Cloud tampil padahal triggerBackgroundSync cuma delay lalu removeSyncItem antrean lokal. Tidak ada Retrofit/OkHttp/Firestore upload.",
            engineeringRoadmapFix = "Ganti jadi upload Retrofit/Firestore sungguhan atau label jujur OFFLINE_SAVED; sinkron via Worker + retry."
        ),
        SystemDefectItem(
            defectId = "DEFECT-06",
            title = "Firestore/Auth Disabled + google-services.json Hilang",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Tinggi (Blocker Multi-User)",
            affectedComponent = "app/build.gradle.kts & app/google-services.json",
            honestDefectExplanation = "firestore/auth dikomentari dan google-services.json hanya .example, sehingga FCM/Auth/AI tidak jalan produksi dan setiap HP terisolasi.",
            engineeringRoadmapFix = "Uncomment firestore+auth+credentials, isi google-services.json asli, migrasi Room lokal ke Firestore sinkron."
        ),
        SystemDefectItem(
            defectId = "DEFECT-07",
            title = "Auth Lokal PIN + Seed Default 1234",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Tinggi (Security)",
            affectedComponent = "modules/auth/LoginAuthValidator.kt & .env.example",
            honestDefectExplanation = "Login cari user di daftar lokal HP itu dan PIN SHA-256 tanpa salt di SharedPrefs. Seed super-admin default DRG-001/1234 bila tidak diganti.",
            engineeringRoadmapFix = "Pindah ke akun server (Firebase Auth Phone), PIN ke PBKDF2+Keystore, tolak PIN default sebelum rilis."
        ),
        SystemDefectItem(
            defectId = "DEFECT-08",
            title = "Data Lokal Saja — Ganti HP = Hilang",
            type = DefectType.PRODUCTION_DEFECT,
            severityLevel = "Tinggi (Data Loss)",
            affectedComponent = "AndroidManifest allowBackup=false & Room v13",
            honestDefectExplanation = "allowBackup=false tanpa cloud backup/sync. Ganti HP/reset/uninstal menghapus kas/forum/kontak permanen; SQLite juga belum SQLCipher.",
            engineeringRoadmapFix = "Tambah export/import kas terenkripsi + cloud backup, pertimbangkan SQLCipher untuk NIK/SIM."
        ),
        SystemDefectItem(
            defectId = "DEFECT-09",
            title = "P2P Signaling Log-Only — Discovery 0%",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Tinggi (Blocker Multi-User)",
            affectedComponent = "core/p2p/P2PSignalingManager.kt & P2PManager.kt",
            honestDefectExplanation = "broadcastPeerInfo/onMessageReceived cuma Log.d dan ICE candidate string palsu candidate:1 — peer tidak pernah dicatat atau dibentuk SDP.",
            engineeringRoadmapFix = "Implementasikan FCM signaling SDP/ICE penuh + STUN/TURN relay lintas seluler."
        ),
        SystemDefectItem(
            defectId = "DEFECT-10",
            title = "Schema Room Hilang + Drift Docs",
            type = DefectType.PRODUCTION_DEFECT,
            severityLevel = "Sedang (Data Risk)",
            affectedComponent = "app/schemas/ & README.md",
            honestDefectExplanation = "exportSchema=true tapi 1-4,8,9.json belum dicommit; README masih v12/32 padahal v13/51. Migrasi v1 ke v13 tidak tervalidasi penuh.",
            engineeringRoadmapFix = "Generate schema via Room export, commit, dan sinkronkan badge README ke v13/51."
        ),
        SystemDefectItem(
            defectId = "DEFECT-11",
            title = "Dead Button Upload Asuransi + TODO Status",
            type = DefectType.PRODUCTION_DEFECT,
            severityLevel = "Sedang (Dead-End)",
            affectedComponent = "services/DriverServicesScreen & dashboard/DashboardHeader",
            honestDefectExplanation = "Upload berkas RS hanya snackbar lokal tanpa pipeline upload; onStatusChange DashboardHeader masih TODO sehingga status shift mati.",
            engineeringRoadmapFix = "Hubungkan PhotoPicker ke upload worker retry offline; implementasikan update status shift."
        ),
        SystemDefectItem(
            defectId = "DEFECT-12",
            title = "PII Dummy Hardcode di Skema + Hex Magic",
            type = DefectType.PRODUCTION_DEFECT,
            severityLevel = "Sedang (Privacy)",
            affectedComponent = "AppDatabaseMigrations 12_13 & 79 file Color(0x..)",
            honestDefectExplanation = "DEFAULT members NIK 3507123456780001/SIM dummy masuk skema; 79 file pakai hex mentah bukan DesignTokens, melanggar No Magic Numbers.",
            engineeringRoadmapFix = "DEFAULT jadi kosongkan; pindah hex ke DesignTokens/Theme terpusat."
        )
    )
}
