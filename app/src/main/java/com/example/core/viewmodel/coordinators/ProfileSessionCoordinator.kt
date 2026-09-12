package com.example.core.viewmodel.coordinators

import android.content.SharedPreferences
import com.example.core.repository.DRGRepository
import com.example.shared.models.*
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
        sharedPrefs?.getString("logged_in_member_id", null)?.let { memberId ->
            if (SecurityUtils.validateSession(sharedPrefs, memberId)) {
                _currentMemberId.value = memberId
                _isLoggedIn.value = true
            } else {
                SecurityUtils.clearSession(sharedPrefs)
                _isLoggedIn.value = false
                _currentMemberId.value = ""
            }
        }
    }

    fun login(memberId: String) {
        _currentMemberId.value = memberId
        _isLoggedIn.value = true
        sharedPrefs?.let { SecurityUtils.generateAndStoreSession(it, memberId) }
        showToast("Selamat datang kembali di DRG Malang Raya!")
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentMemberId.value = ""
        sharedPrefs?.let { SecurityUtils.clearSession(it) }
        showToast("Sesi berhasil keluar. Salam Gacor!")
    }

    fun registerNewDriver(name: String, phone: String, plate: String, model: String, area: String, pin: String) {
        val timestampHex = System.currentTimeMillis().toString(36).uppercase()
        val randomEntropy = UUID.randomUUID().toString().replace("-", "").take(6).uppercase()
        scope.launch {
            runCatching {
                val newDriver = DriverMember(
                    id = "DRG-$timestampHex-$randomEntropy", name = name, driverId = "DRG-REG-$timestampHex",
                    phone = phone, role = MemberRole.ANGGOTA, motorcyclePlate = plate, motorcycleModel = model,
                    rating = 5.0f, reviewCount = 0, loyaltyPoints = 10, loyaltyTier = "Junior Rider",
                    verificationStatus = VerificationStatus.PENDING_SCREENING,
                    screeningNotes = "Pendaftar baru, menunggu proses screening Admin & Dewan Etika.",
                    isOnline = false, currentStatus = "Pendaftaran Baru (Pending)", baseArea = area
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

    fun updateProfile(
        cur: DriverMember?, phone: String, area: String, model: String, plate: String, photoUrl: String,
        address: String, bloodType: String, emergencyContact: String, emergencyPhone: String, simNumber: String, nik: String
    ) {
        if (cur == null) return
        val updated = cur.copy(
            motorcyclePlate = plate, motorcycleModel = model, phone = phone, baseArea = area, profilePhotoUrl = photoUrl,
            address = address, bloodType = bloodType, emergencyContact = emergencyContact, emergencyPhone = emergencyPhone,
            simNumber = simNumber, nik = nik
        )
        scope.launch { runCatching { repository.updateMember(updated) }.onSuccess { showToast("Biodata lengkap berhasil disimpan.") } }
    }

    fun updateDutyStatus(cur: DriverMember?, status: String, isOnline: Boolean) {
        if (cur == null) return
        val updated = cur.copy(currentStatus = status, isOnline = isOnline)
        scope.launch { runCatching { repository.updateMember(updated) }.onSuccess { showToast("Status: $status") } }
    }
}
