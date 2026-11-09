package com.example.core.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import com.example.shared.models.WorkshopPartner
import kotlinx.coroutines.launch

fun DRGViewModel.addWorkshopReview(
    workshop: WorkshopPartner,
    rating: Float,
    serviceType: String,
    comment: String
) = viewModelScope.launch {
    val member = currentMember.value ?: DriverMember(
        id = "DRG-001",
        name = "Driver DRG",
        driverId = "DRG-001",
        phone = "08123456789",
        role = MemberRole.ANGGOTA,
        motorcyclePlate = "N 1234 XX",
        motorcycleModel = "Honda Vario 160",
        rating = 4.9f,
        reviewCount = 10,
        loyaltyPoints = 150,
        loyaltyTier = "Kapten",
        verificationStatus = VerificationStatus.VERIFIED,
        screeningNotes = "Anggota Aktif DRG"
    )
    repository.addWorkshopReview(workshop, member, rating, serviceType, comment)
    showToast("Ulasan untuk ${workshop.name} berhasil disimpan! (+15 XP)")
}

fun DRGViewModel.getReviewsForWorkshop(workshopId: String) =
    repository.getReviewsForWorkshop(workshopId)
