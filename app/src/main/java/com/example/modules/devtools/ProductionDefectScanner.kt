package com.example.modules.devtools

enum class DefectType(val label: String) {
    CONCEPT_FLAW("Cacat Konsep / Anti-Pattern"),
    PRODUCTION_DEFECT("Cacat Produksi / Reliability Defect")
}

data class SystemDefectItem(
    val defectId: String,
    val title: String,
    val type: DefectType,
    val severityLevel: String,
    val affectedComponent: String,
    val honestDefectExplanation: String,
    val engineeringRoadmapFix: String
)

object ProductionDefectScanner {
    fun scanKnownDefects(): List<SystemDefectItem> = listOf(
        SystemDefectItem(
            defectId = "DEFECT-01",
            title = "Validasi Role Client-Side Tanpa Edge Signature",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Tinggi (Security Flaw)",
            affectedComponent = "core/RoleManager.kt & MemberDao",
            honestDefectExplanation = "Wewenang super admin/ketua diverifikasi secara lokal di Room SQLite. Pengguna dengan root dapat memanipulasi kolom role di database lokal.",
            engineeringRoadmapFix = "Migrasi wewenang ke Firebase Custom Claims / Edge Token verification di cloud function."
        ),
        SystemDefectItem(
            defectId = "DEFECT-02",
            title = "P2P Walkie Talkie Terbatas Subnet WiFi Posko",
            type = DefectType.CONCEPT_FLAW,
            severityLevel = "Sedang (Architecture Flaw)",
            affectedComponent = "modules/services/PttWalkieTalkieDialog.kt",
            honestDefectExplanation = "Komunikasi suara P2P menggunakan UDP multicast & WiFi Direct lokal. Bila pengemudi beda base tower atau jarak >150m tanpa posko, suara terputus.",
            engineeringRoadmapFix = "Implementasikan WebRTC STUN/TURN public relay server untuk lintas jaringan seluler."
        ),
        SystemDefectItem(
            defectId = "DEFECT-03",
            title = "Fallback SMS Darurat Tidak Otomatis & Tergantung Pulsa/SMS",
            type = DefectType.PRODUCTION_DEFECT,
            severityLevel = "Tinggi (Reliability Flaw)",
            affectedComponent = "core/sync/EmergencySmsFallbackHelper.kt",
            honestDefectExplanation = "Fallback membuka SMS composer (ACTION_SENDTO) berisi info/lokasi; tidak mengirim otomatis. Keberhasilan bergantung pada aksi user, ketersediaan layanan SMS, dan pulsa/saldo (Rp 0 dapat menggagalkan).",
            engineeringRoadmapFix = "Pertimbangkan pengiriman terotomasi yang aman/terotorisasi (dengan consent) atau gateway alternatif (reverse-billing/WA API) bila tersedia."
        ),
        SystemDefectItem(
            defectId = "DEFECT-04",
            title = "GPS Presisi Tinggi Berpotensi Boros Baterai (Perlu Optimasi Lanjutan)",
            type = DefectType.PRODUCTION_DEFECT,
            severityLevel = "Sedang (Battery Risk)",
            affectedComponent = "core/location/BatteryAwareLocationSyncer.kt",
            honestDefectExplanation = "Sinkron lokasi sudah adaptif (profil hemat, interval berbasis gerak, downgrade saat baterai rendah), namun mode presisi tinggi/darurat tetap berpotensi boros baterai; geofencing & pengukuran dampak nyata belum lengkap.",
            engineeringRoadmapFix = "Tambah geofencing/adaptive interval saat diam, uji konsumsi baterai nyata, dan tuning profil per skenario."
        )
    ) + MultiUserGapScanner.extraDefects()
}
