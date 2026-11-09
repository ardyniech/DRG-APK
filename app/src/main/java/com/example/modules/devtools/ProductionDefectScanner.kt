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
            title = "Fallback SMS Darurat Tergantung Pulsa Seluler",
            type = DefectType.PRODUCTION_DEFECT,
            severityLevel = "Tinggi (Reliability Flaw)",
            affectedComponent = "EmergencySmsFallbackHelper.kt",
            honestDefectExplanation = "Saat kuota data habis, sistem mencoba kirim SMS reguler. Jika saldo pulsa prabayar driver Rp 0, SMS darurat gagal terkirim.",
            engineeringRoadmapFix = "Tambahkan integrasi webhook reverse-billing atau WhatsApp Cloud Business API gateway."
        ),
        SystemDefectItem(
            defectId = "DEFECT-04",
            title = "GPS Polling 1 Detik Memakan Baterai 12%/jam",
            type = DefectType.PRODUCTION_DEFECT,
            severityLevel = "Sedang (Battery Flaw)",
            affectedComponent = "core/location/DriverLocationTracker.kt",
            honestDefectExplanation = "Mode gacor/darurat melakukan lock GPS presisi tinggi setiap 1 detik. Menyedot 12-15% baterai per jam jika tidak di-cas di holder motor.",
            engineeringRoadmapFix = "Gunakan geofencing adaptif: turunkan interval ke 5s bila driver diam/stagnan di lampu merah."
        )
    )
}
