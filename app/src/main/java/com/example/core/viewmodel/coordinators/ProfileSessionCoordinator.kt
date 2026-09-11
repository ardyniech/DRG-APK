package com.example.core.viewmodel.coordinators

import android.content.SharedPreferences
import com.example.core.repository.DRGRepository
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import com.example.shared.utils.SecurityUtils
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
    private val _currentMemberId = MutableStateFlow("")
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
        _currentMemberId.value = ""
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
        scope.launch {
            runCatching {
                val newDriver = DriverMember(
                    id = "DRG-$timestampHex-$randomEntropy",
                    name = name,
                    driverId = "DRG-REG-$timestampHex",
                    phone = phone,
                    role = MemberRole.ANGGOTA,
                    motorcyclePlate = plate,
                    motorcycleModel = model,
                    rating = 5.0f,
                    reviewCount = 0,
                    loyaltyPoints = 10,
                    loyaltyTier = "Junior Rider",
                    verificationStatus = VerificationStatus.PENDING_SCREENING,
                    screeningNotes = "Pendaftar baru, menunggu proses screening Admin & Dewan Etika.",
                    isOnline = false,
                    currentStatus = "Pendaftaran Baru (Pending)",
                    baseArea = area,
                    canManageKas = false,
                    canVerifyDrivers = false,
                    canBroadcastSos = false,
                    canManagePosko = false
                )
                repository.registerMember(newDriver)
                sharedPrefs?.let { sp ->
                    SecurityUtils.storePinHash(sp, newDriver.id, pin)
                    SecurityUtils.storePinHash(sp, newDriver.phone, pin)
                    SecurityUtils.storePinHash(sp, newDriver.driverId, pin)
                }
                val log = AdminLogUtils.createLog(null, "mendaftar sebagai calon anggota baru (menunggu screening)", newDriver.name)
                repository.adminLogRepo.insertLog(log)
                newDriver.name
            }.onSuccess { driverName ->
                showToast("Pendaftaran $driverName diajukan! Menunggu screening pengurus DRG.")
            }.onFailure { error ->
                showToast("Gagal mendaftar: ${error.localizedMessage}")
            }
        }
    }

    fun updateProfile(cur: DriverMember?, phone: String, area: String, model: String, plate: String, photoUrl: String) {
        if (cur == null) return
        val updated = cur.copy(motorcyclePlate = plate, motorcycleModel = model, phone = phone, baseArea = area, profilePhotoUrl = photoUrl)
        scope.launch { runCatching { repository.updateMember(updated) }.onSuccess { showToast("Profil diperbarui.") } }
    }

    fun updateDutyStatus(cur: DriverMember?, status: String, isOnline: Boolean) {
        if (cur == null) return
        val updated = cur.copy(currentStatus = status, isOnline = isOnline)
        scope.launch { runCatching { repository.updateMember(updated) }.onSuccess { showToast("Status: $status") } }
    }
}
