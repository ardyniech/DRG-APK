package com.example.core.repository

import com.example.shared.models.*

object SeedDataGamification {
    fun getInitialBadges(): List<BadgeItem> = listOf(
        BadgeItem("BDG-01", "Pejuang Kopdar", "KOPDAR", "Hadir 5x Kopdar Bulanan berturut-turut", "🎖️", 100, true, "12 Jan 2024"),
        BadgeItem("BDG-02", "Pahlawan Aspal", "HELP_PATROL", "Merespon >3 panggilan darurat SOS Satgas", "🛡️", 250, true, "20 Feb 2024"),
        BadgeItem("BDG-03", "Donatur Setia Kas", "KAS", "Disiplin bayar iuran kas 6 bulan tanpa tunggakan", "💰", 150, true, "01 Mar 2024"),
        BadgeItem("BDG-04", "Suhu Mekanik Forum", "FORUM", "Membuat 5 postingan solusi mesin yang disukai >10 driver", "🔧", 300, false),
        BadgeItem("BDG-05", "Eksekutor Tugas DRG", "TASK", "Selesaikan 3 tugas kerja bakti / patroli posko komunitas", "⭐", 200, false)
    )

    fun getInitialTasks(): List<CommunityTask> = listOf(
        CommunityTask(
            id = "TSK-001",
            title = "Patroli Jalur Rawan Ranugrati - Sawojajar",
            category = "PATROLI",
            description = "Dampingi tim Satgas pukul 22.00 - 01.00 WIB di titik rawan begal dan ranjau paku.",
            rewardPoints = 80,
            issuerRole = MemberRole.SATGAS,
            deadline = "Malam Ini 22:00 WIB"
        ),
        CommunityTask(
            id = "TSK-002",
            title = "Kerja Bakti Pengecatan Posko Basecamp Lowokwaru",
            category = "POSKO",
            description = "Bantu perbaikan fasilitas ruang istirahat, colokan hp & dispenser posko.",
            rewardPoints = 50,
            issuerRole = MemberRole.KETUA,
            deadline = "Sabtu 09:00 WIB"
        ),
        CommunityTask(
            id = "TSK-003",
            title = "Bantu Distribusi Rompi DRG Seri Baru",
            category = "DOKUMENTASI",
            description = "Bantu sekretaris membagikan rompi & stiker stang ke korwil Malang Selatan.",
            rewardPoints = 40,
            issuerRole = MemberRole.SEKRETARIS,
            deadline = "Minggu Depan"
        )
    )

    fun getInitialRewards(): List<RewardItem> = listOf(
        RewardItem("RWD-01", "Voucher Bensin Pertalite Rp 25.000", "Bisa ditukar di Posko Utama Klojen / QR digital", 200, "BENZIN", "⛽", 15),
        RewardItem("RWD-02", "Bebas Iuran Kas Komunitas 1 Bulan", "Bebas kewajiban kas bulanan sebesar Rp 15.000", 150, "KAS", "🎟️", 50),
        RewardItem("RWD-03", "Stiker Metalik DRG Limited Edition", "Stiker reflektif menyala malam hari untuk spakbor/helm", 100, "MERCH", "✨", 30),
        RewardItem("RWD-04", "Diskon Servis + Oli 25% Bengkel Rekanan", "Kupon servis berkala di Bengkel Barokah Motor Lowokwaru", 250, "BENGKEL", "🔧", 10)
    )

    fun getInitialHazards(): List<HazardArea> = listOf(
        HazardArea(
            id = "HZD-01",
            title = "Jalur Gelap Rawan Sekitar Flyover Arjosari",
            hazardType = HazardType.BEGAL_RISK,
            locationName = "Flyover Arjosari, Malang",
            description = "Lampu jalan mati, sepi di atas jam 23.00 WIB. Disarankan jalan berkonvoi atau panggil Satgas.",
            lat = -7.9250,
            lng = 112.6500,
            reportedBy = "Bambang Satgas",
            reporterRole = MemberRole.SATGAS,
            confirmCount = 8,
            timeAgo = "15 menit lalu"
        ),
        HazardArea(
            id = "HZD-02",
            title = "Genangan Banjir 30cm Underpass Karanglo",
            hazardType = HazardType.FLOOD,
            locationName = "Underpass Karanglo, Malang",
            description = "Hujan deras membuat area tergenang air limpahan. Motor matic berisiko mogok.",
            lat = -7.9010,
            lng = 112.6580,
            reportedBy = "Joko Perkasa",
            reporterRole = MemberRole.ANGGOTA,
            confirmCount = 5,
            timeAgo = "1 jam lalu"
        )
    )
}
