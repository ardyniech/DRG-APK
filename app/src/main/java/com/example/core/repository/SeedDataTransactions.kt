package com.example.core.repository

import com.example.shared.models.*

object SeedDataTransactions {
    fun getInitialKas(): List<KasTransaction> = listOf(
        KasTransaction(
            id = "KAS-101", title = "Iuran Kas Bulanan 45 Anggota", amount = 900000L,
            type = TransactionType.INCOME, category = KasCategory.IURAN_BULANAN,
            dateString = "01 Sep 2026", recordedBy = "Siti Nurhaliza (Bendahara)",
            description = "Iuran rutin periode September @Rp 20.000 / anggota"
        ),
        KasTransaction(
            id = "KAS-102", title = "Bantuan Santunan Uang Duka", amount = 500000L,
            type = TransactionType.EXPENSE, category = KasCategory.UANG_DUKA,
            dateString = "03 Sep 2026", recordedBy = "Siti Nurhaliza (Bendahara)",
            description = "Santunan duka untuk keluarga alm. rekan driver DRG Malang Timur (Pakis)"
        ),
        KasTransaction(
            id = "KAS-103", title = "Pengadaan Kopi & Logistik Posko Klojen", amount = 175000L,
            type = TransactionType.EXPENSE, category = KasCategory.KAS_POSKO,
            dateString = "04 Sep 2026", recordedBy = "Siti Nurhaliza (Bendahara)",
            description = "Stok gula, kopi sachet, teh, dan galon air posko utama Klojen"
        ),
        KasTransaction(
            id = "KAS-104", title = "Donasi Sukarela dari Donatur Simpatisan", amount = 350000L,
            type = TransactionType.INCOME, category = KasCategory.BANSOS,
            dateString = "05 Sep 2026", recordedBy = "Siti Nurhaliza (Bendahara)",
            description = "Donasi dana darurat ambulans dan pertolongan pertama"
        )
    )

    fun getInitialForum(): List<ForumPost> = listOf(
        ForumPost(
            id = "FRM-201", authorId = "DRG-005", authorName = "Anton Wijaya (Satgas)",
            authorRole = MemberRole.SATGAS, category = ForumCategory.INFO_JALUR,
            title = "Waspada Jalur Rawan Jam 01:00-04:00 Sekitar Karanglo",
            content = "Rekan-rekan yg narik subuh di sekitar Karanglo arah Singosari harap aktifkan tombol Pantau Jalur di app DRG. Satgas standby di posko Suhat.",
            postType = PostType.UPDATE, likesCount = 18, commentsCount = 7, timeAgo = "1 jam lalu"
        ),
        ForumPost(
            id = "FRM-202", authorId = "DRG-001", authorName = "Bambang Pamungkas",
            authorRole = MemberRole.KETUA, category = ForumCategory.TIPS_MESIN,
            title = "Tips Awet V-Belt Matic untuk Driver Harian",
            content = "Ganti V-belt maksimal tiap 20.000 KM dan jangan lupa cek roller tiap servis bulanan. Diskon 20% di bengkel rekanan Barokah Motor!",
            postType = PostType.UPDATE, likesCount = 24, commentsCount = 12, timeAgo = "3 jam lalu"
        ),
        ForumPost(
            id = "FRM-203", authorId = "DRG-003", authorName = "Rudi Hermawan",
            authorRole = MemberRole.ANGGOTA, category = ForumCategory.TANYA_JAWAB,
            title = "Tanya: Rekomendasi Oli Matic yang Dingin Buat Macet Dinoyo?",
            content = "Dulur, ada saran oli matic SAE 10W-30 atau 10W-40 yg rekomen buat motor matic sering macet panjang di Dinoyo?",
            postType = PostType.QUESTION, likesCount = 9, commentsCount = 5, timeAgo = "5 jam lalu"
        )
    )

    fun getInitialWorkshops(): List<WorkshopPartner> = listOf(
        WorkshopPartner(
            id = "BKL-01", name = "Bengkel Barokah Motor (Mitra DRG)",
            address = "Jl. Soekarno Hatta No. 12, Lowokwaru, Malang", area = "Lowokwaru, Malang",
            distanceKm = 1.8, rating = 4.9f, reviewCount = 46, discountPercent = 20,
            services = "Ganti Oli, Servis CVT, Injeksi, Ganti Ban, Setel Rantai", phone = "081234567890"
        ),
        WorkshopPartner(
            id = "BKL-02", name = "Ahass Prima Motor Rekanan",
            address = "Jl. Kawi No. 45, Klojen, Malang", area = "Klojen, Malang",
            distanceKm = 3.2, rating = 4.8f, reviewCount = 38, discountPercent = 15,
            services = "Servis Resmi Honda, Sparepart Original, Fast Pit Tanpa Antri", phone = "081987654321"
        )
    )

    fun getInitialEvents(): List<AttendanceEvent> = SeedDataEvents.getInitialEvents()
    fun getInitialNotifications(): List<CommunityNotification> = SeedDataEvents.getInitialNotifications()
    fun getInitialReviews(): List<DriverReview> = SeedDataEvents.getInitialReviews()
}
