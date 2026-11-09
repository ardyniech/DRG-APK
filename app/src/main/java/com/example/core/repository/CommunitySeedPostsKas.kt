package com.example.core.repository

import com.example.shared.models.*

object CommunitySeedPostsKas {
    fun defaultPosts(): List<ForumPost> = listOf(
        ForumPost(
            id = "PST-01",
            authorId = "DRG-001",
            authorName = "Ardy Syafii (Ketua DRG)",
            authorRole = MemberRole.KETUA,
            category = ForumCategory.TIPS_MESIN,
            postType = PostType.UPDATE,
            title = "Tips Berkendara Saat Hujan Lebat & Aspal Licin Malang Raya",
            content = "Rekan-rekan, waspadai genangan di Jl. Borobudur dan tikungan Dinoyo saat hujan deras. Turunkan kecepatan, gunakan rem belakang halus, dan pastikan lampu utama menyala.",
            likesCount = 28,
            commentsCount = 6,
            isLikedByMe = true,
            timeAgo = "2 jam lalu"
        ),
        ForumPost(
            id = "PST-02",
            authorId = "DRG-002",
            authorName = "Bambang Santoso (Satgas)",
            authorRole = MemberRole.SATGAS,
            category = ForumCategory.INFO_JALUR,
            postType = PostType.UPDATE,
            title = "Rekomendasi Bengkel: Sumber Rezeki Dinoyo Siaga Tambal Ban 24 Jam",
            content = "Tadi malam rekan kita ban bocor di Dinoyo jam 01.30 WIB, langsung dibantu dengan cepat di Bengkel Sumber Rezeki. Cukup tunjukkan KTA DRG langsung diskon 20%.",
            likesCount = 35,
            commentsCount = 8,
            isLikedByMe = false,
            timeAgo = "5 jam lalu"
        ),
        ForumPost(
            id = "PST-03",
            authorId = "DRG-003",
            authorName = "Siti Nurhaliza (Srikandi)",
            authorRole = MemberRole.ANGGOTA,
            category = ForumCategory.SAPA_REKAN,
            postType = PostType.QUESTION,
            title = "Pengalaman Mengurai Kemacetan Saat Jam Pulang Sekolah Klojen",
            content = "Alhamdulillah tadi koordinasi via walkie-talkie posko untuk hindari jalur Jl. Bandung yang padat total. Sangat terbantu dengan radar live posko rekan DRG!",
            likesCount = 19,
            commentsCount = 4,
            isLikedByMe = true,
            timeAgo = "1 hari lalu"
        )
    )

    fun defaultTransactions(): List<KasTransaction> = listOf(
        KasTransaction(
            id = "KAS-01",
            title = "Iuran Kas Mingguan Anggota DRG (Batch 1)",
            amount = 1250000,
            type = TransactionType.INCOME,
            category = KasCategory.IURAN_BULANAN,
            dateString = "30 Okt 2026",
            recordedBy = "Bendahara DRG",
            description = "Iuran 25 anggota terdaftar untuk dana operasional posko & dana cadangan"
        ),
        KasTransaction(
            id = "KAS-02",
            title = "Donasi Kas Sukarela Solidaritas Rekan",
            amount = 500000,
            type = TransactionType.INCOME,
            category = KasCategory.BANSOS,
            dateString = "28 Okt 2026",
            recordedBy = "Bendahara DRG",
            description = "Sumbangan sukarela donatur pengurus & simpatisan ojol Malang"
        ),
        KasTransaction(
            id = "KAS-03",
            title = "Santunan Pengobatan & Servis Darurat TRC",
            amount = 350000,
            type = TransactionType.EXPENSE,
            category = KasCategory.SERVIS_ARMADA,
            dateString = "26 Okt 2026",
            recordedBy = "Bendahara DRG",
            description = "Bantuan langsung rekan driver korban senggolan di Sawojajar"
        ),
        KasTransaction(
            id = "KAS-04",
            title = "Pengadaan Kotak P3K & Pompa Angin Posko Klojen",
            amount = 250000,
            type = TransactionType.EXPENSE,
            category = KasCategory.KAS_POSKO,
            dateString = "22 Okt 2026",
            recordedBy = "Bendahara DRG",
            description = "Perlengkapan darurat medis ringan & pompa elektrik di posko pantau"
        )
    )
}
