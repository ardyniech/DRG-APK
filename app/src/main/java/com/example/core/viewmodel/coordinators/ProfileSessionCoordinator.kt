package com.example.core.viewmodel.coordinators

import android.content.SharedPreferences
import com.example.core.repository.DRGRepository
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ProfileSessionCoordinator(
    private val repository: DRGRepository,
    private val sharedPrefs: SharedPreferences?,
    private val scope: CoroutineScope,
    private val showToast: (String) -> Unit
) {
    private val _currentMemberId = MutableStateFlow("DRG-001")
    val currentMemberId: StateFlow<String> = _currentMemberId.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        val savedId = sharedPrefs?.getString("logged_in_member_id", null)
        if (savedId != null) {
            _currentMemberId.value = savedId
            _isLoggedIn.value = true
        }
    }

    fun login(memberId: String) {
        _currentMemberId.value = memberId
        _isLoggedIn.value = true
        sharedPrefs?.edit()?.putString("logged_in_member_id", memberId)?.apply()
        showToast("Selamat datang kembali di DRG Malang Raya!")
    }

    fun logout() {
        _isLoggedIn.value = false
        sharedPrefs?.edit()?.remove("logged_in_member_id")?.apply()
        showToast("Sesi berhasil keluar. Salam Gacor!")
    }

    fun switchActiveMember(memberId: String) {
        _currentMemberId.value = memberId
        showToast("Beralih profil: $memberId")
    }

    fun registerNewDriver(name: String, phone: String, plate: String, model: String, area: String, pin: String) {
        val timestampHex = System.currentTimeMillis().toString(36).uppercase()
        val randomEntropy = UUID.randomUUID().toString().replace("-", "").take(6).uppercase()
        val newDriver = DriverMember(
            id = "DRG-$timestampHex-$randomEntropy",
            name = name,
            driverId = "DRG-REG-$timestampHex",
            phone = phone,
            role = MemberRole.ANGGOTA,
            motorcyclePlate = plate,
            motorcycleModel = model,
            rating = 0.0f,
            reviewCount = 0,
            loyaltyPoints = 10,
            loyaltyTier = "Junior Rider",
            verificationStatus = VerificationStatus.PENDING_SCREENING,
            screeningNotes = "Pendaftar baru, menunggu proses screening Admin.",
            isOnline = false,
            currentStatus = "Pendaftaran Baru",
            baseArea = area
        )
        scope.launch {
            runCatching {
                repository.registerMember(newDriver)
            }.onSuccess {
                sharedPrefs?.edit()?.putString("pin_hash_${newDriver.id}", com.example.shared.utils.SecurityUtils.hashPin(pin))?.apply()
                showToast("Pendaftaran diajukan! Menunggu screening pengurus DRG.")
            }.onFailure { error ->
                showToast("Gagal mendaftar: ${error.localizedMessage}")
            }
        }
    }

    fun updateProfile(cur: DriverMember?, phone: String, area: String, model: String, plate: String, photoUrl: String) {
        if (cur == null) return
        val updated = cur.copy(motorcyclePlate = plate, motorcycleModel = model, phone = phone, baseArea = area, profilePhotoUrl = photoUrl)
        scope.launch {
            runCatching {
                repository.updateMember(updated)
            }.onSuccess {
                showToast("Data profil & foto anggota berhasil diperbarui.")
            }.onFailure { error ->
                showToast("Gagal memperbarui profil: ${error.localizedMessage}")
            }
        }
    }
}
