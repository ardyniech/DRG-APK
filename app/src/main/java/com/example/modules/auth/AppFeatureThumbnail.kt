package com.example.modules.auth

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.R
import com.example.ui.theme.*

data class AppFeatureThumbnail(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val icon: ImageVector,
    val drawableResId: Int?,
    val badgeText: String,
    val accentColor: Color
)

object AppFeatureRepository {
    val allFeatures = listOf(
        AppFeatureThumbnail(
            id = "radar",
            title = "Radar Pantau & Live Map",
            category = "Peta & Kopdar",
            description = "Pantau titik kumpul, hotspot gacor, dan info lalulintas Malang Raya real-time.",
            icon = Icons.Default.Radar,
            drawableResId = R.drawable.img_radar_map_real_1789127507652,
            badgeText = "Live GPS",
            accentColor = DrgGrabGreenPrimary
        ),
        AppFeatureThumbnail(
            id = "sos",
            title = "Tombol SOS & Tim Reaksi Cepat",
            category = "Keamanan Jalan",
            description = "Sinyal darurat 3 detik langsung terhubung ke Satgas TRC & SMS fallback GPS.",
            icon = Icons.Default.Warning,
            drawableResId = R.drawable.img_sos_real_1789127642014,
            badgeText = "Siaga 24/7",
            accentColor = Color(0xFFDC2626)
        ),
        AppFeatureThumbnail(
            id = "treasury",
            title = "Transparansi Kas Komunitas",
            category = "Keuangan Transparan",
            description = "Laporan iuran bulanan, saldo kas, dan riwayat santunan terbuka untuk semua anggota.",
            icon = Icons.Default.AccountBalanceWallet,
            drawableResId = R.drawable.img_treasury_real_1789127656802,
            badgeText = "Audit Realtime",
            accentColor = Color(0xFF2563EB)
        ),
        AppFeatureThumbnail(
            id = "forum",
            title = "Forum Q&A & Info Kopdar",
            category = "Diskusi Komunitas",
            description = "Tanya jawab antar driver, info ranjau paku, banjir, dan kuliner legendaris.",
            icon = Icons.Default.Forum,
            drawableResId = R.drawable.img_kopdar_real_1789127670296,
            badgeText = "Diskusi Driver",
            accentColor = Color(0xFFD97706)
        ),
        AppFeatureThumbnail(
            id = "gamification",
            title = "Gamifikasi & Poin Solidaritas",
            category = "Misi & Rewards",
            description = "Kumpulkan poin dari check-in posko dan klaim voucher servis gratis bengkel rekanan.",
            icon = Icons.Default.EmojiEvents,
            drawableResId = R.drawable.img_rewards_real_1789133489569,
            badgeText = "Poin & Diskon",
            accentColor = Color(0xFF059669)
        ),
        AppFeatureThumbnail(
            id = "members",
            title = "Direktori & KTA Digital",
            category = "Identitas Driver",
            description = "Kartu Tanda Anggota QR Code terverifikasi dengan rating solidaritas dan nopol.",
            icon = Icons.Default.Badge,
            drawableResId = R.drawable.img_kta_member_real_1789133467851,
            badgeText = "Terverifikasi",
            accentColor = Color(0xFF7C3AED)
        ),
        AppFeatureThumbnail(
            id = "ai_assistant",
            title = "Asisten AI Driver Malang",
            category = "Kecerdasan Lokal",
            description = "Tanyakan spot gacor sore, rute aman Karanglo, dan rekomendasi posko rehat.",
            icon = Icons.Default.SmartToy,
            drawableResId = R.drawable.img_ai_bot_real_1789133505382,
            badgeText = "AI Smart Bot",
            accentColor = Color(0xFF0284C7)
        ),
        AppFeatureThumbnail(
            id = "ptt",
            title = "Walkie-Talkie PTT Audio",
            category = "Komunikasi Suara",
            description = "Saluran komunikasi audio PTT real-time langsung ke helm/intercom pengemudi.",
            icon = Icons.Default.RecordVoiceOver,
            drawableResId = R.drawable.img_ptt_intercom_real_1789133521397,
            badgeText = "Intercom PTT",
            accentColor = Color(0xFFEA580C)
        )
    )
}
