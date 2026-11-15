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
    val member = currentMember.value ?: run {
        showToast("Silakan masuk dengan Google terlebih dahulu untuk memberi ulasan.")
        return@launch
    }
    repository.addWorkshopReview(workshop, member, rating, serviceType, comment)
    showToast("Ulasan untuk ${workshop.name} berhasil disimpan! (+15 XP)")
}

fun DRGViewModel.getReviewsForWorkshop(workshopId: String) =
    repository.getReviewsForWorkshop(workshopId)
