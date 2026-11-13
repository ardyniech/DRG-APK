package com.example.modules.events.logic

import com.example.shared.models.*

enum class EventRiskLevel(val label: String) {
    AMAN("Siap Tepat Waktu"),
    WASPADA("Perlu Pengawalan"),
    KRITIS_RAWAN_GAGAL("Rawan Gagal / Molor")
}

data class EventReadinessReport(
    val overallReadinessPct: Int,
    val budgetCoveragePct: Int,
    val completedTasks: Int,
    val blockedTasks: Int,
    val totalTasks: Int,
    val unresolvedFeedback: Int,
    val criticalFeedback: Int,
    val daysRemaining: Int,
    val riskLevel: EventRiskLevel,
    val riskHeadline: String
)

object EventReadinessCalculator {
    private const val DAY_MS = 86_400_000L

    fun analyze(
        event: CommunityEventEntity,
        tasks: List<EventCommitteeTaskEntity>,
        feedback: List<EventMemberFeedbackEntity>,
        nowMs: Long = System.currentTimeMillis()
    ): EventReadinessReport {
        val totalTasks = tasks.size
        val completed = tasks.count { it.status == CommitteeTaskStatus.SELESAI || it.progressPercent >= 100 }
        val blocked = tasks.count { it.status == CommitteeTaskStatus.TERKENDALA }
        val unresolvedFb = feedback.count { !it.isResolved }
        val criticalFb = feedback.count { !it.isResolved && it.severityLevel.equals("KRITIS", ignoreCase = true) }

        val rawAvg = if (totalTasks == 0) 20 else tasks.sumOf { it.progressPercent.coerceIn(0, 100) } / totalTasks
        val penalty = (blocked * 12) + (criticalFb * 14) + if (totalTasks == 0) 15 else 0
        val readiness = (rawAvg - penalty).coerceIn(0, 100)
        val budgetPct = if (event.estimatedBudgetRupiah <= 0L) 100
        else ((event.collectedFundsRupiah * 100L) / event.estimatedBudgetRupiah).toInt().coerceIn(0, 100)
        val daysLeft = ((event.eventTimestamp - nowMs) / DAY_MS).toInt()

        val (risk, headline) = evaluateRisk(event, totalTasks, blocked, criticalFb, unresolvedFb, rawAvg, budgetPct, daysLeft)
        return EventReadinessReport(readiness, budgetPct, completed, blocked, totalTasks, unresolvedFb, criticalFb, daysLeft, risk, headline)
    }

    private fun evaluateRisk(
        event: CommunityEventEntity,
        totalTasks: Int,
        blocked: Int,
        criticalFb: Int,
        unresolvedFb: Int,
        avgProgress: Int,
        budgetPct: Int,
        daysLeft: Int
    ): Pair<EventRiskLevel, String> = when {
        event.stage == EventPhaseStage.SELESAI ->
            EventRiskLevel.AMAN to "Event selesai diselenggarakan & laporan transparan tersimpan."
        event.stage == EventPhaseStage.RAWAN_TERTUNDA || criticalFb > 0 || (blocked > 0 && daysLeft <= 7) ->
            EventRiskLevel.KRITIS_RAWAN_GAGAL to buildString {
                if (blocked > 0) append("$blocked tugas panitia terkendala. ")
                if (criticalFb > 0) append("$criticalFb masukan kritis anggota belum direspon. ")
                append("Segera tindak lanjuti agar event tidak molor!")
            }.trim()
        totalTasks == 0 ->
            EventRiskLevel.KRITIS_RAWAN_GAGAL to "Belum ada pembagian tugas panitia! Tambahkan divisi & PIC segera."
        daysLeft <= 3 && avgProgress < 75 ->
            EventRiskLevel.KRITIS_RAWAN_GAGAL to "Tinggal H-$daysLeft namun progres panitia baru $avgProgress%!"
        blocked > 0 || unresolvedFb > 0 || budgetPct < 60 || avgProgress < 80 ->
            EventRiskLevel.WASPADA to "Perlu pantauan: Dana $budgetPct%, $unresolvedFb masukan terbuka, $blocked kendala."
        else ->
            EventRiskLevel.AMAN to "Seluruh panitia bergerak sesuai target jadwal."
    }

    fun defaultTaskDrafts(coordinatorId: String, coordinatorName: String): List<EventCommitteeTaskEntity> = listOf(
        EventCommitteeTaskEntity("EVTK-D1", "", "Sie Perizinan", "Izin Lokasi, Parkir & Koordinasi Wilayah", coordinatorId, coordinatorName, "H-5", 0, CommitteeTaskStatus.BELUM_MULAI, "", 250_000L),
        EventCommitteeTaskEntity("EVTK-D2", "", "Sie Perlengkapan", "Tenda, Pengeras Suara, Spanduk & P3K", coordinatorId, coordinatorName, "H-3", 0, CommitteeTaskStatus.BELUM_MULAI, "", 600_000L),
        EventCommitteeTaskEntity("EVTK-D3", "", "Sie Konsumsi", "Air Mineral, Konsumsi Peserta & Panitia", coordinatorId, coordinatorName, "H-2", 0, CommitteeTaskStatus.BELUM_MULAI, "", 1_000_000L),
        EventCommitteeTaskEntity("EVTK-D4", "", "Sie Satgas & Jalur", "Pengaturan Parkir & Titik Kumpul Aman", coordinatorId, coordinatorName, "H-1", 0, CommitteeTaskStatus.BELUM_MULAI, "", 150_000L)
    )
}
