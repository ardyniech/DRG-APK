package com.example.modules.profile.primitives

data class RankProgress(
    val currentRank: String,
    val nextRank: String?,
    val currentPoints: Int,
    val minPointsForCurrent: Int,
    val pointsForNext: Int,
    val progressFraction: Float,
    val pointsNeeded: Int,
    val rankBadgeIcon: String
)

data class ActivityPointSource(
    val title: String,
    val rateDesc: String,
    val countEarnedDesc: String,
    val iconEmoji: String
)

object LoyaltyRankUtils {

    fun calculateRankProgress(points: Int): RankProgress {
        return when {
            points < 100 -> RankProgress(
                currentRank = "Junior Rider",
                nextRank = "Road Captain",
                currentPoints = points,
                minPointsForCurrent = 0,
                pointsForNext = 100,
                progressFraction = (points / 100f).coerceIn(0f, 1f),
                pointsNeeded = (100 - points).coerceAtLeast(0),
                rankBadgeIcon = "🥉"
            )
            points in 100..299 -> RankProgress(
                currentRank = "Road Captain",
                nextRank = "Pahlawan Jalanan",
                currentPoints = points,
                minPointsForCurrent = 100,
                pointsForNext = 300,
                progressFraction = ((points - 100) / 200f).coerceIn(0f, 1f),
                pointsNeeded = (300 - points).coerceAtLeast(0),
                rankBadgeIcon = "🥈"
            )
            points in 300..449 -> RankProgress(
                currentRank = "Pahlawan Jalanan",
                nextRank = "Satgas Elit",
                currentPoints = points,
                minPointsForCurrent = 300,
                pointsForNext = 450,
                progressFraction = ((points - 300) / 150f).coerceIn(0f, 1f),
                pointsNeeded = (450 - points).coerceAtLeast(0),
                rankBadgeIcon = "🥇"
            )
            points in 450..699 -> RankProgress(
                currentRank = "Satgas Elit",
                nextRank = "Legenda DRG",
                currentPoints = points,
                minPointsForCurrent = 450,
                pointsForNext = 700,
                progressFraction = ((points - 450) / 250f).coerceIn(0f, 1f),
                pointsNeeded = (700 - points).coerceAtLeast(0),
                rankBadgeIcon = "🛡️"
            )
            else -> RankProgress(
                currentRank = "Legenda DRG",
                nextRank = null,
                currentPoints = points,
                minPointsForCurrent = 700,
                pointsForNext = 700,
                progressFraction = 1.0f,
                pointsNeeded = 0,
                rankBadgeIcon = "👑"
            )
        }
    }

    fun getCommunityActivitySources(kopdarCount: Int): List<ActivityPointSource> {
        return listOf(
            ActivityPointSource("Absen Kopdar DRG", "+50 XP / Kopdar", "$kopdarCount Kali Terlibat", "🤝"),
            ActivityPointSource("Respon Sinyal SOS", "+30 XP / Bantuan", "Satgas Siaga", "🚨"),
            ActivityPointSource("Laporan Area Bahaya", "+15 XP / Laporan", "Kontributor Radar", "⚠️"),
            ActivityPointSource("Iuran Kas Transparan", "+20 XP / Bulan", "Donatur Rutin", "💰")
        )
    }
}
