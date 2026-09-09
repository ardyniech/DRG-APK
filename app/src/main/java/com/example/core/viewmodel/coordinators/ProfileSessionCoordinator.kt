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
                val count = repository.memberRepo.getMemberCount()
                val isFirst = count == 0
                val role = if (isFirst) MemberRole.KETUA else MemberRole.ANGGOTA
                val status = if (isFirst) VerificationStatus.VERIFIED else VerificationStatus.PENDING_SCREENING
                val notes = if (isFirst) "Ketua & Administrator Utama Komunitas" else "Pendaftar baru, menunggu proses screening Admin."
                val newDriver = DriverMember(
                    id = "DRG-$timestampHex-$randomEntropy",
                    name = name,
                    driverId = "DRG-REG-$timestampHex",
                    phone = phone,
                    role = role,
                    motorcyclePlate = plate,
                    motorcycleModel = model,
                    rating = 5.0f,
                    reviewCount = 0,
                    loyaltyPoints = if (isFirst) 100 else 10,
                    loyaltyTier = if (isFirst) "Leader" else "Junior Rider",
                    verificationStatus = status,
                    screeningNotes = notes,
                    isOnline = false,
                    currentStatus = if (isFirst) "Aktif (Admin)" else "Pendaftaran Baru",
                    baseArea = area,
                    canManageKas = isFirst,
                    canVerifyDrivers = isFirst,
                    canBroadcastSos = isFirst,
                    canManagePosko = isFirst
                )
                repository.registerMember(newDriver)
                sharedPrefs?.let { sp ->
                    SecurityUtils.storePinHash(sp, newDriver.id, pin)
                    SecurityUtils.storePinHash(sp, newDriver.phone, pin)
                }
                val action = if (isFirst) "mendaftar sebagai Ketua Pertama (Admin)" else "mendaftar sebagai anggota baru"
                val log = AdminLogUtils.createLog(null, action, newDriver.name)
                repository.adminLogRepo.insertLog(log)
                isFirst
            }.onSuccess { isFirst ->
                val msg = if (isFirst) "Pendaftaran berhasil! Anda terdaftar sebagai Ketua/Admin." else "Pendaftaran diajukan! Menunggu screening pengurus DRG."
                showToast(msg)
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
