package com.example.shared.models

import com.example.ui.theme.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

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
        glassBg = DrgPresetGlassWhite,
        pillBg = DrgPresetEmeraldPill,
        glassBorderColor = DrgGlassBorderGreenLight,
        skyTop = DrgPresetEmeraldSkyTop,
        skyMid = DrgPresetEmeraldSkyMid,
        skyBottom = DrgPresetEmeraldSkyBot,
        orbPrimary = DrgGreenPrimary,
        orbSecondary = DrgAmberWarning
    ),
    MINIMAL_PEARL(
        title = "Minimal Pearl",
        subtitle = "Frosted Sky Ice",
        primaryColor = DrgBlueAccent,
        secondaryColor = DrgIndigoVioletAccent,
        glassBg = DrgPresetGlassPearl,
        pillBg = DrgGlassTintBlueSoft,
        glassBorderColor = DrgGlassBorderBlueLight,
        skyTop = DrgPresetPearlSkyTop,
        skyMid = DrgPresetPearlSkyMid,
        skyBottom = DrgPresetPearlSkyBot,
        orbPrimary = DrgPresetPearlOrbPrimary,
        orbSecondary = DrgPresetPearlOrbSecondary
    ),
    DEEP_SUNSET_TEAL(
        title = "Sunset Teal",
        subtitle = "Horizon Senja",
        primaryColor = DrgTealAccent,
        secondaryColor = DrgOrangeWarning,
        glassBg = DrgPresetGlassWarm,
        pillBg = DrgPresetSunsetPill,
        glassBorderColor = DrgPresetSunsetBorder,
        skyTop = DrgPresetSunsetSkyTop,
        skyMid = DrgPresetSunsetSkyMid,
        skyBottom = DrgPresetSunsetSkyBot,
        orbPrimary = DrgPresetSunsetOrbPrimary,
        orbSecondary = DrgPresetSunsetOrbSecondary
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
