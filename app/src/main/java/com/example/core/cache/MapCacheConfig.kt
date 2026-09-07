package com.example.core.cache

enum class MapCachePolicy(val title: String, val description: String) {
    CACHE_FIRST("Cache Utama (Cepat & Hemat)", "Gunakan memori & disk lokal dulu. Ideal untuk koneksi lambat/ojol."),
    NETWORK_FIRST("Jaringan Utama (Update Baru)", "Ambil tile terbaru dari server setiap kali koneksi cepat."),
    OFFLINE_ONLY("Mode Offline Penuh", "Hanya baca dari disk. Bebas kuota internet 100%.")
}

enum class LocationSyncPowerProfile(
    val title: String,
    val normalIntervalSec: Int,
    val stationaryIntervalSec: Int,
    val description: String
) {
    ADAPTIVE_ECO(
        title = "Adaptif Cerdas (Hemat Baterai)",
        normalIntervalSec = 30,
        stationaryIntervalSec = 90,
        description = "Menyesuaikan interval saat bergerak/diam. Hemat baterai s/d 45%."
    ),
    ULTRA_SAVER(
        title = "Ultra Saver (Baterai Kritis)",
        normalIntervalSec = 60,
        stationaryIntervalSec = 180,
        description = "Minimalisir bangun radio seluler. Cocok saat baterai di bawah 25%."
    ),
    HIGH_PRECISION(
        title = "Akurasi Tinggi (Satgas/Patroli)",
        normalIntervalSec = 10,
        stationaryIntervalSec = 25,
        description = "Pembaruan cepat kontinu untuk pengawalan atau respons darurat."
    )
}

data class MapCacheSettings(
    val cachePolicy: MapCachePolicy = MapCachePolicy.CACHE_FIRST,
    val maxCacheSizeMb: Int = 100,
    val locationSyncProfile: LocationSyncPowerProfile = LocationSyncPowerProfile.ADAPTIVE_ECO,
    val isOfflineModeForced: Boolean = false
)
