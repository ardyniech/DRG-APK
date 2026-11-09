package com.example.modules.workflow

data class WorkflowStage(
    val id: Int,
    val title: String,
    val timeLabel: String,
    val locationLabel: String,
    val realChallenge: String,
    val drgSolution: String,
    val speedKmh: Int,
    val weather: String,
    val gForceText: String,
    val statusText: String,
    val iconEmoji: String
)

object WorkflowStagesData {
    val stages = listOf(
        WorkflowStage(
            id = 1,
            title = "Inspeksi Fisik Armada & Briefing Posko",
            timeLabel = "06:30 WIB • Pra-Operasional",
            locationLabel = "Posko Pantau Klojen, Malang",
            realChallenge = "Pengecekan fisik tekanan ban (30-33 PSI), oli mesin, kampas rem, BBM, dan jas hujan sebelum turun ke aspal.",
            drgSolution = "Aktivasi KTA Digital DRG, pantau status cuaca Malang Raya, dan briefing rute aman via Posko terdekat.",
            speedKmh = 0,
            weather = "Pagi Cerah (23°C)",
            gForceText = "1.0G (Normal)",
            statusText = "Armada Siap Narik",
            iconEmoji = "🛵"
        ),
        WorkflowStage(
            id = 2,
            title = "On-Duty Lapangan Menembus Macet",
            timeLabel = "09:15 WIB • Jam Sibuk",
            locationLabel = "Jl. Basuki Rahmat - Pasar Besar",
            realChallenge = "Kemacetan padat merayap, aspal berlubang tertutup genangan air, dan titik buta (blind spot) kendaraan besar.",
            drgSolution = "Radar Bahaya Live DRG memancarkan peringatan audio: 'Waspada jalan berlubang 300m di depan'. Hemat baterai & offline-first.",
            speedKmh = 38,
            weather = "Gerimis Rintik (24°C)",
            gForceText = "1.2G (Stabil)",
            statusText = "Patroli Rute Aman",
            iconEmoji = "🌧️"
        ),
        WorkflowStage(
            id = 3,
            title = "Kondisi Kritis & Respon TRC Cepat",
            timeLabel = "14:00 WIB • Situasi Darurat",
            locationLabel = "Jl. Danau Toba, Sawojajar",
            realChallenge = "Rekan driver mengalami ban bocor tertusuk paku / senggolan jalanan dan butuh pertolongan darurat segera.",
            drgSolution = "Sinyal SOS Darurat satu sentuh & CrashGuard memancarkan lokasi GPS ke Satgas TRC terdekat (radius 1.2 km) + rujukan Bengkel Rekanan.",
            speedKmh = 0,
            weather = "Mendung Gelap (25°C)",
            gForceText = "2.8G (Impak Ringan)",
            statusText = "Satgas TRC Meluncur (3 Menit)",
            iconEmoji = "🚨"
        ),
        WorkflowStage(
            id = 4,
            title = "Singgah di Posko DRG & Solidaritas",
            timeLabel = "17:15 WIB • Rehat Sore",
            locationLabel = "Posko Dinoyo - Suhat",
            realChallenge = "Kelelahan fisik setelah narik 8 jam, baterai ponsel menipis, dan perlunya rekonsiliasi kas bersama.",
            drgSolution = "Istirahat kopi di posko singgah, bayar iuran kas transparan Rp 5.000 / minggu, dan sharing rekomendasi bengkel di Forum.",
            speedKmh = 0,
            weather = "Sore Teduh (22°C)",
            gForceText = "1.0G (Rehat)",
            statusText = "Rehat & Iuran Kas Tercatat",
            iconEmoji = "⛺"
        ),
        WorkflowStage(
            id = 5,
            title = "Penutupan Shift, Evaluasi & Gamifikasi",
            timeLabel = "21:00 WIB • Pasca-Operasional",
            locationLabel = "Rumah Driver (Malang Raya)",
            realChallenge = "Memastikan pengemudi pulang dengan selamat, armada tanpa kendala, dan evaluasi capaian harian.",
            drgSolution = "Sistem mencatat rekor Zero Accident, reward +25 XP Loyalitas DRG, dan pembukaan lencana 'Pelopor Keselamatan' di profil.",
            speedKmh = 0,
            weather = "Malam Sejuk (20°C)",
            gForceText = "1.0G (Selesai)",
            statusText = "Shift Selesai • Zero Accident",
            iconEmoji = "🏆"
        )
    )
}
