package com.example.core.viewmodel.coordinators

import android.content.Context
import android.content.SharedPreferences
import com.example.core.firebase.GoogleAuthManager
import com.example.core.repository.DRGRepository
import com.example.shared.models.*
import com.example.shared.utils.SecurityUtils
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
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
        if (savedId != null && SecurityUtils.validateSession(sharedPrefs, savedId)) {
            _currentMemberId.value = savedId
            _isLoggedIn.value = true
        }
    }

    fun login(memberId: String) {
        _currentMemberId.value = memberId
        _isLoggedIn.value = true
        sharedPrefs?.let { SecurityUtils.generateAndStoreSession(it, memberId) }
        showToast("Selamat datang kembali di DRG Malang Raya!")
    }

    fun loginWithGoogleUser(user: FirebaseUser, onSynced: (DriverMember) -> Unit = {}) {
        scope.launch {
            val isAdmin = user.isEmailVerified && user.email == ADMIN_EMAIL
            val mappedId = if (isAdmin) "DRG-001" else user.uid
            val draft = PendingRegistrationPrefs.consume(sharedPrefs)
            val existing = repository.db.memberDao().getMemberById(mappedId)
            val member = existing?.let {
                if (draft != null) it.copy(name = draft.name, phone = draft.phone, motorcyclePlate = draft.plate, motorcycleModel = draft.model, baseArea = draft.area)
                    .also { updated -> repository.updateMember(updated) }
                else it
            } ?: PendingRegistrationPrefs.buildGoogleMember(mappedId, user, isAdmin, draft).also { repository.registerMember(it) }
            if (draft != null && draft.draftId.isNotBlank() && draft.draftId != mappedId) {
                repository.db.memberDao().deleteMemberById(draft.draftId)
            }
            _currentMemberId.value = member.id
            _isLoggedIn.value = true
            sharedPrefs?.let { SecurityUtils.generateAndStoreSession(it, member.id) }
            onSynced(member)
            showToast("Masuk sebagai ${member.name} (${user.email ?: "Google"})")
        }
    }

    fun logout(context: Context? = null) {
        _isLoggedIn.value = false
        _currentMemberId.value = ""
        sharedPrefs?.let { SecurityUtils.clearSession(it) }
        if (context != null) scope.launch { GoogleAuthManager.signOut(context) }
        showToast("Sesi berhasil keluar. Salam Gacor!")
    }

    fun registerNewDriver(name: String, phone: String, plate: String, model: String, area: String) {
        val ts = System.currentTimeMillis().toString(36).uppercase()
        val rand = UUID.randomUUID().toString().replace("-", "").take(6).uppercase()
        val draftId = "DRG-$ts-$rand"
        scope.launch {
            runCatching {
                PendingRegistrationPrefs.save(sharedPrefs, PendingRegistrationDraft(draftId, name, phone, plate, model, area))
                val driver = PendingRegistrationPrefs.buildPendingMember(draftId, "DRG-REG-$ts", name, phone, plate, model, area)
                repository.registerMember(driver)
                repository.adminLogRepo.insertLog(AdminLogUtils.createLog(null, "mengajukan pendaftaran KTA", driver.name))
                driver.name
            }.onSuccess { showToast("Biodata $it tersimpan! Masuk dengan Google untuk menautkan KTA.") }
                .onFailure { showToast("Gagal mendaftar: ${it.localizedMessage}") }
        }
    }

    fun updateProfile(
        cur: DriverMember?, phone: String, area: String, model: String, plate: String, photoUrl: String,
        address: String, bloodType: String, emergencyContact: String, emergencyPhone: String, simNumber: String, nik: String
    ) {
        if (cur == null) return
        val updated = cur.copy(
            motorcyclePlate = plate, motorcycleModel = model, phone = phone, baseArea = area, profilePhotoUrl = photoUrl,
            address = address, bloodType = bloodType, emergencyContact = emergencyContact,
            emergencyPhone = emergencyPhone, simNumber = simNumber, nik = nik
        )
        scope.launch { runCatching { repository.updateMember(updated) }.onSuccess { showToast("Biodata lengkap berhasil disimpan.") } }
    }

    fun updateDutyStatus(cur: DriverMember?, status: String, isOnline: Boolean) {
        if (cur == null) return
        scope.launch { runCatching { repository.updateMember(cur.copy(currentStatus = status, isOnline = isOnline)) }.onSuccess { showToast("Status: $status") } }
    }

    private companion object { const val ADMIN_EMAIL = "ardy.syafii@gmail.com" }
}
