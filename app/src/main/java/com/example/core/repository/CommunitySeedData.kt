package com.example.core.repository

import com.example.shared.models.*

object CommunitySeedData {
    fun defaultWorkshops(): List<WorkshopPartner> = listOf(
        WorkshopPartner(
            id = "BKL-01",
            name = "Bengkel Resmi Ahass Merjosari",
            address = "Jl. Raya Joyo Agung No. 12, Lowokwaru, Malang",
            area = "Lowokwaru",
            distanceKm = 1.2,
            rating = 4.8f,
            reviewCount = 38,
            discountPercent = 15,
            services = "Servis Injeksi, Ganti Oli Cepat, Sparepart Asli Honda, Tune-up",
            phone = "081234567801",
            openHours = "08:00 - 18:00",
            isVerifiedPartner = true
        ),
        WorkshopPartner(
            id = "BKL-02",
            name = "Bengkel Sumber Rezeki Dinoyo",
            address = "Jl. MT Haryono No. 88, Dinoyo, Malang",
            area = "Dinoyo",
            distanceKm = 2.4,
            rating = 4.9f,
            reviewCount = 52,
            discountPercent = 20,
            services = "Tambal Ban Tubeless 24 Jam, Kelistrikan & Aki, Servis Darurat Malam",
            phone = "081234567802",
            openHours = "24 Jam Nonstop",
            isVerifiedPartner = true
        ),
        WorkshopPartner(
            id = "BKL-03",
            name = "Bengkel Mitra Jaya Sawojajar",
            address = "Jl. Danau Toba No. 15, Sawojajar, Malang",
            area = "Kedungkandang",
            distanceKm = 4.1,
            rating = 4.6f,
            reviewCount = 27,
            discountPercent = 10,
            services = "Turun Mesin Matic, Ganti Kampas Rem, CVT Refresh, Sparepart Umum",
            phone = "081234567803",
            openHours = "08:30 - 20:00",
            isVerifiedPartner = true
        ),
        WorkshopPartner(
            id = "BKL-04",
            name = "Sentral Ban & Aki Soekarno Hatta",
            address = "Jl. Soekarno Hatta No. 45, Lowokwaru, Malang",
            area = "Soekarno Hatta",
            distanceKm = 2.8,
            rating = 4.7f,
            reviewCount = 44,
            discountPercent = 15,
            services = "Press Velg & Ban Tubeless, Aki Delivery Cepat, Spooring Roda",
            phone = "081234567804",
            openHours = "07:30 - 21:00",
            isVerifiedPartner = true
        )
    )

    fun defaultBadges(): List<BadgeItem> = listOf(
        BadgeItem(
            id = "BDG-01",
            title = "Anggota Teraktif",
            category = "KOPDAR",
            description = "Hadir kopdar rutin, aktif patroli siaga, dan konsisten di posko pantau.",
            iconEmoji = "🏆",
            requiredXp = 100,
            isEarned = true,
            earnedDate = "Okt 2026"
        ),
        BadgeItem(
            id = "BDG-02",
            title = "Pelopor Keselamatan",
            category = "HELP_PATROL",
            description = "Zero accident record, respon cepat sinyal darurat TRC, dan tertib safety gear.",
            iconEmoji = "🛡️",
            requiredXp = 150,
            isEarned = true,
            earnedDate = "Okt 2026"
        ),
        BadgeItem(
            id = "BDG-03",
            title = "Srikandi Komunitas",
            category = "FORUM",
            description = "Pengemudi wanita tangguh DRG dengan dedikasi tinggi dan rating bintang 5.",
            iconEmoji = "👑",
            requiredXp = 120,
            isEarned = false,
            earnedDate = ""
        ),
        BadgeItem(
            id = "BDG-04",
            title = "Ksatria TRC",
            category = "HELP_PATROL",
            description = "Terjun langsung mengevakuasi dan memberi pertolongan pertama rekan laka di jalan.",
            iconEmoji = "🚨",
            requiredXp = 200,
            isEarned = true,
            earnedDate = "Sep 2026"
        ),
        BadgeItem(
            id = "BDG-05",
            title = "Penjaga Posko",
            category = "TASK",
            description = "Piket siaga posko, merawat kebersihan, dan koordinasi logistik rehat rekan.",
            iconEmoji = "⛺",
            requiredXp = 80,
            isEarned = true,
            earnedDate = "Agu 2026"
        )
    )
}
