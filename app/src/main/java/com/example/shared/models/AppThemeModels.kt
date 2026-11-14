package com.example.shared.models

import com.example.ui.theme.DrgTealAccent
import com.example.ui.theme.DrgOrangeWarning
import com.example.ui.theme.DrgIndigoVioletAccent
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgGreenDark
import com.example.ui.theme.DrgGreenContainer
import com.example.ui.theme.DrgGlassTintBlueSoft
import com.example.ui.theme.DrgGlassBorderGreenLight
import com.example.ui.theme.DrgGlassBorderBlueLight
import com.example.ui.theme.DrgEmeraldAccent
import com.example.ui.theme.DrgBlueAccent
import com.example.ui.theme.DrgAmberWarning
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
        secondaryColor = DrgEmeraldAccent,
        glassBg = Color(0xCCFFFFFF),
        pillBg = Color(0xFFE3F8EC),
        glassBorderColor = DrgGlassBorderGreenLight,
        skyTop = Color(0xFF9DE8C1),
        skyMid = Color(0xFFCFF4E1),
        skyBottom = Color(0xFFECFBF2),
        orbPrimary = DrgGreenPrimary,
        orbSecondary = DrgAmberWarning
    ),
    MINIMAL_PEARL(
        title = "Minimal Pearl",
        subtitle = "Frosted Sky Ice",
        primaryColor = DrgBlueAccent,
        secondaryColor = DrgIndigoVioletAccent,
        glassBg = Color(0xD4FFFFFF),
        pillBg = DrgGlassTintBlueSoft,
        glassBorderColor = DrgGlassBorderBlueLight,
        skyTop = Color(0xFFBBD7FA),
        skyMid = Color(0xFFE1EDFD),
        skyBottom = Color(0xFFF4F8FF),
        orbPrimary = Color(0xFF38BDF8),
        orbSecondary = Color(0xFF818CF8)
    ),
    DEEP_SUNSET_TEAL(
        title = "Sunset Teal",
        subtitle = "Horizon Senja",
        primaryColor = DrgTealAccent,
        secondaryColor = DrgOrangeWarning,
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
