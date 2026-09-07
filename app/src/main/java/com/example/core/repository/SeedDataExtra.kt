package com.example.core.repository

import com.example.shared.models.*

object SeedDataExtra {
    fun getInitialPosko(): List<PoskoLocation> = listOf(
        PoskoLocation(
            id = "PSK-01",
            name = "Posko Utama DRG Klojen",
            area = "Klojen, Malang",
            address = "Jl. Retawu No. 10, Klojen (Dekat Museum Brawijaya)",
            lat = -7.9732,
            lng = 112.6214,
            coordinatorName = "Rian Pratama",
            phone = "081555667788",
            facilities = "Kopi gratis, Charger cepat, Kompresor ban, Tempat istirahat, WiFi",
            activeDriversCount = 8,
            isMainPosko = true
        ),
        PoskoLocation(
            id = "PSK-02",
            name = "Posko Satgas Pantau Suhat",
            area = "Lowokwaru, Malang",
            address = "Jl. Soekarno Hatta No. 24 (Dekat Jembatan Suhat)",
            lat = -7.9425,
            lng = 112.6150,
            coordinatorName = "Anton Wijaya (Satgas)",
            phone = "082199887766",
            facilities = "Toolkit darurat, Tambal ban mandiri, P3K, Air mineral, Shelter hujan",
            activeDriversCount = 5,
            isMainPosko = false
        ),
        PoskoLocation(
            id = "PSK-03",
            name = "Posko Sahabat Batu",
            area = "Batu",
            address = "Jl. Diponegoro No. 45 (Samping Alun-Alun Batu)",
            lat = -7.8732,
            lng = 112.5276,
            coordinatorName = "Agus Supriadi",
            phone = "085612345678",
            facilities = "Charger station, Ruang rehat sejuk, Kopi, Informasi jalur satu arah Batu",
            activeDriversCount = 4,
            isMainPosko = false
        )
    )

    fun getInitialEmergency(): List<EmergencyAlert> = listOf(
        EmergencyAlert(
            id = "SOS-901",
            driverId = "DRG-007",
            driverName = "Agus Supriadi",
            driverPhone = "085612345678",
            plateNumber = "N 4567 POI",
            type = EmergencyType.MOGOK,
            message = "V-Belt putus di dekat Jembatan Soekarno-Hatta (Suhat), butuh bantuan dorong / toolkit bengkel terdekat.",
            lat = -7.9430,
            lng = 112.6145,
            locationName = "Jembatan Soekarno-Hatta, Malang",
            isActive = true,
            responderCount = 2
        )
    )
}
