package com.example.core.repository

import com.example.shared.models.*

object SeedDataEvents {
    fun getInitialEvents(): List<AttendanceEvent> = listOf(
        AttendanceEvent(
            id = "EVT-301",
            title = "Kopdar Akbar & Santunan Bulanan DRG",
            dateText = "Sabtu, 12 Sep 2026",
            timeText = "19:30 - 22:00 WIB",
            location = "Posko Utama DRG Klojen",
            pointsReward = 50,
            checkInCode = "DRG2026",
            description = "Evaluasi kas, sosialisasi program santunan BPJS, dan pembagian stiker identitas resmi DRG.",
            attendedCount = 28
        )
    )

    fun getInitialNotifications(): List<CommunityNotification> = listOf(
        CommunityNotification(
            id = "NOTIF-01",
            title = "Laporan Kas Bulan September Telah Rilis",
            message = "Saldo kas komunitas saat ini Rp 1.575.000. Transparan dan dapat dicek di tab Kas.",
            senderName = "Siti Nurhaliza",
            senderRole = MemberRole.BENDAHARA,
            severity = NotificationSeverity.INFO,
            timeAgo = "2 jam lalu"
        ),
        CommunityNotification(
            id = "NOTIF-02",
            title = "Waspada Genangan Air & Jalur Rawan",
            message = "Hujan deras berpotensi genangan di sekitar Bareng, Galunggung & Sukun. Utamakan keselamatan, rek!",
            senderName = "Anton Wijaya",
            senderRole = MemberRole.SATGAS,
            severity = NotificationSeverity.WARNING,
            timeAgo = "4 jam lalu"
        ),
        CommunityNotification(
            id = "NOTIF-03",
            title = "⚠️ DARURAT BANJIR: Hindari Jl. Galunggung!",
            message = "Perhatian dulur! Jalan Galunggung saat ini banjir setinggi betis orang dewasa. Cari rute alternatif!",
            senderName = "Satgas Tim Siaga",
            senderRole = MemberRole.SATGAS,
            severity = NotificationSeverity.WARNING,
            timeAgo = "Baru Saja"
        )
    )

    fun getInitialReviews(): List<DriverReview> = listOf(
        DriverReview(
            id = "REV-01",
            targetDriverId = "DRG-001",
            reviewerName = "Anton Wijaya (Satgas)",
            reviewerRole = MemberRole.SATGAS,
            rating = 5.0f,
            tag = "Solid & Berjiwa Sosial",
            comment = "Ketua yg selalu fast response saat ada driver mogok di jalan. Teladan sejati!",
            dateText = "2 Sep 2026"
        )
    )
}
