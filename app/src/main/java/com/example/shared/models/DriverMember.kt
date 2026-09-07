package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class DriverMember(
    @PrimaryKey val id: String,
    val name: String,
    val driverId: String,
    val phone: String,
    val role: MemberRole,
    val motorcyclePlate: String,
    val motorcycleModel: String,
    val rating: Float = 4.8f,
    val reviewCount: Int = 12,
    val loyaltyPoints: Int = 150,
    val loyaltyTier: String = "Road Captain",
    val verificationStatus: VerificationStatus = VerificationStatus.VERIFIED,
    val screeningNotes: String = "Lulus verifikasi SIM, STNK, SKCK & wawancara loyalitas",
    val isOnline: Boolean = true,
    val currentStatus: String = "Tersedia",
    val currentLat: Double = -7.9839,
    val currentLng: Double = 112.6214,
    val baseArea: String = "Klojen, Malang",
    val joinedDate: String = "Jan 2024",
    val kasPaidStatus: Boolean = true,
    val kopdarAttendanceCount: Int = 8,
    val isLocationSharingConsent: Boolean = true,
    val profilePhotoUrl: String = ""
)
