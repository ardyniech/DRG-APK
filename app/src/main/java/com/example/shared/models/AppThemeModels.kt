package com.example.shared.models

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DrgGrabGreenPrimary

/**
 * 3 Luminous Floating Glass Preset Themes shared across Dashboard, Profile, Settings, and Bottom Dock.
 */
enum class AppVisualTheme(
    val title: String,
    val subtitle: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val glassBg: Color,
    val pillBg: Color,
    val glassBorderColor: Color,
    val skyTop: Color,
    val skyMid: Color,
    val skyBottom: Color,
    val orbPrimary: Color,
    val orbSecondary: Color
) {
    EMERALD_AURORA(
        title = "Emerald Aurora",
        subtitle = "Segar Khas DRG",
        primaryColor = DrgGrabGreenPrimary,
        secondaryColor = Color(0xFF059669),
        glassBg = Color(0xCCFFFFFF),
        pillBg = Color(0xFFE3F8EC),
        glassBorderColor = Color(0xFF86EFAC),
        skyTop = Color(0xFF9DE8C1),
        skyMid = Color(0xFFCFF4E1),
        skyBottom = Color(0xFFECFBF2),
        orbPrimary = Color(0xFF00B14F),
        orbSecondary = Color(0xFFF59E0B)
    ),
    MINIMAL_PEARL(
        title = "Minimal Pearl",
        subtitle = "Frosted Sky Ice",
        primaryColor = Color(0xFF0284C7),
        secondaryColor = Color(0xFF4F46E5),
        glassBg = Color(0xD4FFFFFF),
        pillBg = Color(0xFFE0F2FE),
        glassBorderColor = Color(0xFF93C5FD),
        skyTop = Color(0xFFBBD7FA),
        skyMid = Color(0xFFE1EDFD),
        skyBottom = Color(0xFFF4F8FF),
        orbPrimary = Color(0xFF38BDF8),
        orbSecondary = Color(0xFF818CF8)
    ),
    DEEP_SUNSET_TEAL(
        title = "Sunset Teal",
        subtitle = "Horizon Senja",
        primaryColor = Color(0xFF0D9488),
        secondaryColor = Color(0xFFEA580C),
        glassBg = Color(0xCCFFFDF9),
        pillBg = Color(0xFFCCFBF1),
        glassBorderColor = Color(0xFF5EEAD4),
        skyTop = Color(0xFF8CE5DA),
        skyMid = Color(0xFFD5F7F0),
        skyBottom = Color(0xFFFFF4E6),
        orbPrimary = Color(0xFF14B8A6),
        orbSecondary = Color(0xFFFB923C)
    );

    val backgroundGradient: Brush
        get() = Brush.verticalGradient(listOf(skyTop, skyMid, skyBottom))
}

/**
 * 2 Operating Focus Modes for Cockpit Dashboard:
 * 1. SATGAS_TRC: Prioritas keamanan jalur, live radar, posko, & kesiapan lapangan
 * 2. ADMINISTRASI: Prioritas kas transparan, data anggota, mutasi, & arsip
 */
enum class AppFocusMode(
    val label: String,
    val tag: String
) {
    SATGAS_TRC(label = "Mode Satgas & Jalur (A)", tag = "SATGAS"),
    ADMINISTRASI(label = "Mode Administrasi (B)", tag = "ADMIN")
}
