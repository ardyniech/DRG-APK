package com.example.modules.devtools

/**
 * Bawel scanner: cacat multi-user, sync, dan keamanan.
 * Sumber: temuan bawel/devtools (sejalan audit_minus.py & kondisi repo). Perbarui saat fakta berubah.
 * Finansial rilis awal: iuran Kas saja — marketplace DEFERRED (bukan defect blocker di sini).
 */
object MultiUserGapScanner {
    /** @return cacat tambahan (DEFECT-05 s/d 10) di luar 4 awal, jujur & non-fabricated. */
    fun extraDefects(): List<SystemDefectItem> = listOf(
        SystemDefectItem(
            defectId = "DEFECT-05",
            title = "Sinkronisasi Cloud Parsial (Profil/Kas/SOS/Queue Aktif, Forum/Hazard Menyusul)",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Rendah (Parsial Tersinkron)",
            affectedComponent = "core/sync/BackgroundSyncEngine.kt & FirestoreOperationsRepository",
            honestDefectExplanation = "Profil, Kas, SOS, dan antrean sync kini terhubung ke Firestore secara nyata dan jujur (tetap OFFLINE_SAVED bila offline). Koleksi sekunder (Forum/Hazard/Event penuh) masih bertahap.",
            engineeringRoadmapFix = "Ekspansi sinkron dua arah untuk ForumPosts, Hazards, dan Events ke Firestore."
        ),
        SystemDefectItem(
            defectId = "DEFECT-06",
            title = "Backend Firebase/Auth/App Check Aktif (FCM Server Push Menyusul)",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Rendah (Core Cloud Aktif)",
            affectedComponent = "app/build.gradle.kts & core/firebase/",
            honestDefectExplanation = "Firestore named database, Firebase Auth Google Sign-In, Zero-Trust Rules, dan App Check sudah aktif. Tinggal Cloud Functions/FCM fan-out push antar perangkat.",
            engineeringRoadmapFix = "Tambah Cloud Functions untuk dispatch FCM otomatis saat dokumen emergency_alerts baru dibuat."
        ),
        SystemDefectItem(
            defectId = "DEFECT-07",
            title = "Auth Google Sign-In Tunggal Aktif (UI Login Duplikat, PIN & Demo Dihapus)",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Rendah (Gated Google Auth)",
            affectedComponent = "core/firebase/GoogleAuthManager.kt & modules/auth/",
            honestDefectExplanation = "UI login lokal duplikat, field PIN registrasi, tombol DEMO, dan auto-seeding akun demo lokal telah dihapus. Biodata pendaftaran KTA otomatis tertaut ke Google UID saat user login.",
            engineeringRoadmapFix = "Sinkronkan status screening persetujuan admin antar perangkat melalui Firestore listeners."
        ),
        SystemDefectItem(
            defectId = "DEFECT-08",
            title = "Cadangan Cloud Terenkripsi AES-GCM Aktif (SQLite Lokal Belum SQLCipher)",
            type = DefectType.PRODUCTION_DEFECT,
            severityLevel = "Sedang (Hardening Opsional)",
            affectedComponent = "core/backup/CloudBackupHelper.kt & Room v14",
            honestDefectExplanation = "Pengguna kini bisa mencadangkan & memulihkan database terenkripsi AES-GCM ke Firestore (/users/{uid}/backups). File SQLite lokal saat runtime masih standar Room (bukan SQLCipher).",
            engineeringRoadmapFix = "Evaluasi SQLCipher bila dibutuhkan enkripsi DB lokal saat runtime selain cloud backup terenkripsi."
        ),
        SystemDefectItem(
            defectId = "DEFECT-09",
            title = "Signaling/Discovery P2P & WebRTC Signaling Telah Aktif",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Rendah (Telah Terimplementasi)",
            affectedComponent = "core/p2p/P2PSignalingManager.kt & P2PManager.kt",
            honestDefectExplanation = "Signaling Firestore p2p_peers, p2p_signals (SDP offer/answer/ICE), dan FCM topic discovery kini aktif nyata dengan antrean offline Room sebagai fallback.",
            engineeringRoadmapFix = "Monitoring real-time kestabilan NAT traversal peer-to-peer di berbagai operator seluler."
        ),
        SystemDefectItem(
            defectId = "DEFECT-10",
            title = "Schema Room Historis Hilang (Migrasi Belum Tervalidasi Penuh)",
            type = DefectType.PRODUCTION_DEFECT,
            severityLevel = "Sedang (Data Risk)",
            affectedComponent = "app/schemas/ & Room export",
            honestDefectExplanation = "exportSchema=true namun schema historis 1,2,3,4,8,9.json belum tersedia di repo, sehingga verifikasi migrasi v1→v14 tidak lengkap.",
            engineeringRoadmapFix = "Generate & commit schema hilang via Room export valid (tanpa fabrikasi) dan verifikasi migrasi."
        )
    )
}
