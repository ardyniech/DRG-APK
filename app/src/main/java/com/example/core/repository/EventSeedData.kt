package com.example.core.repository

import com.example.shared.models.*

object EventSeedData {
    private const val DAY_MS = 86_400_000L

    fun defaultEvents(now: Long = System.currentTimeMillis()): List<CommunityEventEntity> = listOf(
        CommunityEventEntity(
            id = "EVT-DRG-2026-01",
            title = "Kopdar Akbar & Santunan Panti Malang",
            category = "Baksos",
            description = "Agenda bakti sosial bulanan, doa keselamatan bersama, dan pembagian paket sembako di area Klojen.",
            targetDateText = "Minggu, 22 Nov 2026 • 16:00 WIB",
            eventTimestamp = now + 5 * DAY_MS,
            locationName = "Posko Induk Alun-Alun Malang",
            coordinatorId = "DRG-001",
            coordinatorName = "Ketua Panitia DRG",
            estimatedBudgetRupiah = 3_500_000L,
            collectedFundsRupiah = 2_450_000L,
            targetAttendees = 65,
            stage = EventPhaseStage.PERSIAPAN_PANITIA,
            readinessNotes = "Izin lingkungan RW & titik parkir sedang difinalisasi. Cek kesiapan terpal hujan sebelum H-2.",
            createdByMemberId = "DRG-001",
            createdByMemberName = "Pengurus Pusat DRG",
            createdAt = now - 3 * DAY_MS
        ),
        CommunityEventEntity(
            id = "EVT-DRG-2026-02",
            title = "Safety Riding Clinic & Cek Rem Gratis",
            category = "Safety Clinic",
            description = "Pelatihan teknik pengereman darurat jalan licin dan pengecekan kampas rem gratis bersama bengkel rekanan.",
            targetDateText = "Sabtu, 5 Des 2026 • 09:00 WIB",
            eventTimestamp = now + 18 * DAY_MS,
            locationName = "Lapangan Parkir Rampal",
            coordinatorId = "DRG-001",
            coordinatorName = "Koordinator Satgas TRC",
            estimatedBudgetRupiah = 2_000_000L,
            collectedFundsRupiah = 2_000_000L,
            targetAttendees = 50,
            stage = EventPhaseStage.SIAP_PELAKSANAAN,
            readinessNotes = "Seluruh instruktur & alat P3K siap di lokasi.",
            createdByMemberId = "DRG-001",
            createdByMemberName = "Pengurus Pusat DRG",
            createdAt = now - 2 * DAY_MS
        )
    )

    fun defaultTasks(now: Long = System.currentTimeMillis()): List<EventCommitteeTaskEntity> = listOf(
        EventCommitteeTaskEntity("EVTK-01", "EVT-DRG-2026-01", "Sie Perizinan", "Surat Izin Lokasi & Koordinasi Dishub/Parkir", "DRG-001", "Sekretaris DRG", "H-3 (19 Nov)", 45, CommitteeTaskStatus.TERKENDALA, "Menunggu stempel RW setempat karena ketua RW luar kota", 250_000L, now),
        EventCommitteeTaskEntity("EVTK-02", "EVT-DRG-2026-01", "Sie Konsumsi", "Booking 75 Paket Nasi Kotak & Air Mineral", "DRG-001", "Bendahara DRG", "H-2 (20 Nov)", 80, CommitteeTaskStatus.DIKERJAKAN, "DP 50% sudah ditransfer, pelunasan H-1", 1_500_000L, now),
        EventCommitteeTaskEntity("EVTK-03", "EVT-DRG-2026-01", "Sie Perlengkapan", "Sewa Tenda Portabel, Sound & P3K Lengkap", "DRG-001", "Satgas Perlengkapan", "H-2 (20 Nov)", 100, CommitteeTaskStatus.SELESAI, "Unit tenda 4x6 & megaphone sudah diamankan di Posko Induk", 900_000L, now),
        EventCommitteeTaskEntity("EVTK-04", "EVT-DRG-2026-02", "Sie Acara", "Susun Rundown & Briefing Instruktur Safety", "DRG-001", "Ketua Satgas", "H-7", 100, CommitteeTaskStatus.SELESAI, "Materi simulasi rem basah siap", 600_000L, now),
        EventCommitteeTaskEntity("EVTK-05", "EVT-DRG-2026-02", "Sie Bengkel", "Koordinasi 4 Mekanik Bengkel Rekanan", "DRG-001", "Humas Bengkel", "H-5", 90, CommitteeTaskStatus.DIKERJAKAN, "3 mekanik konfirmasi hadir, 1 menyusul sore ini", 800_000L, now)
    )

    fun defaultFeedback(now: Long = System.currentTimeMillis()): List<EventMemberFeedbackEntity> = listOf(
        EventMemberFeedbackEntity(
            id = "EVFB-01",
            eventId = "EVT-DRG-2026-01",
            authorMemberId = "DRG-019",
            authorName = "Anggota Jalur Selatan",
            authorRole = MemberRole.ANGGOTA,
            category = "Risiko Keterlambatan",
            severityLevel = "KRITIS",
            message = "Izin parkir belum terbit sedangkan H-5 sudah dekat. Mohon siapkan titik parkir cadangan agar peserta tidak kena tegur.",
            committeeResponse = "",
            isResolved = false,
            createdAt = now - 3_600_000L
        ),
        EventMemberFeedbackEntity(
            id = "EVFB-02",
            eventId = "EVT-DRG-2026-01",
            authorMemberId = "DRG-024",
            authorName = "Rekan Klojen",
            authorRole = MemberRole.ANGGOTA,
            category = "Saran Logistik",
            severityLevel = "SARAN",
            message = "Sebaiknya sediakan jas hujan cadangan & kotak P3K tambahan mengingat sore sering hujan deras.",
            committeeResponse = "Disetujui! Sie Perlengkapan sudah menambahkan 10 jas hujan & tas P3K posko.",
            isResolved = true,
            createdAt = now - 7_200_000L
        )
    )
}
