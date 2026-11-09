package com.example.core.repository

import com.example.shared.models.CommunityTask
import com.example.shared.models.MemberRole
import com.example.shared.models.RewardItem
import com.example.shared.models.TaskStatus

object CommunitySeedGamification {
    fun defaultRewards(): List<RewardItem> = listOf(
        RewardItem(
            id = "RWD-01",
            title = "Voucher Diskon Oli Mesin Ahass",
            description = "Potongan Rp 25.000 untuk ganti oli MPX/SPX di Bengkel Ahass Merjosari.",
            requiredPoints = 50,
            category = "BENGKEL",
            iconEmoji = "🛢️",
            stockAvailable = 15
        ),
        RewardItem(
            id = "RWD-02",
            title = "Kupon Tambal Tubeless 24 Jam",
            description = "Gratis 1x tambal ban tubeless darurat di Bengkel Sumber Rezeki Dinoyo.",
            requiredPoints = 30,
            category = "BENGKEL",
            iconEmoji = "🛞",
            stockAvailable = 20
        ),
        RewardItem(
            id = "RWD-03",
            title = "Sticker Reflektif Helm DRG Safety First",
            description = "Sticker prismatik 3M glow-in-dark penanda anggota resmi komunitas DRG.",
            requiredPoints = 20,
            category = "MERCH",
            iconEmoji = "✨",
            stockAvailable = 50
        ),
        RewardItem(
            id = "RWD-04",
            title = "Kaos Polo Driver Riang Gembira",
            description = "Jersey polo dry-fit resmi bordir DRG Malang Raya untuk kopdar & touring.",
            requiredPoints = 150,
            category = "MERCH",
            iconEmoji = "👕",
            stockAvailable = 8
        )
    )

    fun defaultTasks(): List<CommunityTask> = listOf(
        CommunityTask(
            id = "TSK-01",
            title = "Patroli Rawan Laka Jalur Cangar-Batu",
            category = "PATROLI",
            description = "Pantau kondisi rem blong & kabut tebal turunan tajam Cangar saat akhir pekan.",
            rewardPoints = 40,
            issuerRole = MemberRole.SATGAS,
            status = TaskStatus.OPEN,
            deadline = "Minggu 17:00"
        ),
        CommunityTask(
            id = "TSK-02",
            title = "Piket Kebersihan & Kopi Posko Dinoyo",
            category = "POSKO",
            description = "Menyiapkan air galon, stopkontak cas HP, dan kebersihan area rehat rekan.",
            rewardPoints = 25,
            issuerRole = MemberRole.SEKRETARIS,
            status = TaskStatus.OPEN,
            deadline = "Hari Ini 21:00"
        ),
        CommunityTask(
            id = "TSK-03",
            title = "Bantu Dorong Armada Mogot Rekan",
            category = "BANTUAN",
            description = "Beri bantuan stut/dorong armada rekan mogok ke bengkel terdekat.",
            rewardPoints = 35,
            issuerRole = MemberRole.SATGAS,
            status = TaskStatus.OPEN,
            deadline = "Setiap Saat"
        )
    )
}
