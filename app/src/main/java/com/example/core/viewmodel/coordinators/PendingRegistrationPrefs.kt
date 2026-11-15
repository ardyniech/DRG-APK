package com.example.core.viewmodel.coordinators

import android.content.SharedPreferences
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import com.google.firebase.auth.FirebaseUser

internal data class PendingRegistrationDraft(
    val draftId: String,
    val name: String,
    val phone: String,
    val plate: String,
    val model: String,
    val area: String
)

internal object PendingRegistrationPrefs {
    private const val KEY_ID = "pending_reg_member_id"
    private const val KEY_NAME = "pending_reg_name"
    private const val KEY_PHONE = "pending_reg_phone"
    private const val KEY_PLATE = "pending_reg_plate"
    private const val KEY_MODEL = "pending_reg_model"
    private const val KEY_AREA = "pending_reg_area"

    fun save(prefs: SharedPreferences?, draft: PendingRegistrationDraft) {
        prefs?.edit()
            ?.putString(KEY_ID, draft.draftId)
            ?.putString(KEY_NAME, draft.name)
            ?.putString(KEY_PHONE, draft.phone)
            ?.putString(KEY_PLATE, draft.plate)
            ?.putString(KEY_MODEL, draft.model)
            ?.putString(KEY_AREA, draft.area)
            ?.apply()
    }

    fun consume(prefs: SharedPreferences?): PendingRegistrationDraft? {
        if (prefs == null) return null
        val name = prefs.getString(KEY_NAME, null)?.takeIf { it.isNotBlank() } ?: return null
        val draft = PendingRegistrationDraft(
            draftId = prefs.getString(KEY_ID, "").orEmpty(),
            name = name,
            phone = prefs.getString(KEY_PHONE, "").orEmpty(),
            plate = prefs.getString(KEY_PLATE, "-").orEmpty(),
            model = prefs.getString(KEY_MODEL, "Unit Ojol").orEmpty(),
            area = prefs.getString(KEY_AREA, "Malang Raya").orEmpty()
        )
        prefs.edit().remove(KEY_ID).remove(KEY_NAME).remove(KEY_PHONE).remove(KEY_PLATE).remove(KEY_MODEL).remove(KEY_AREA).apply()
        return draft
    }

    fun buildGoogleMember(
        mappedId: String,
        user: FirebaseUser,
        isAdmin: Boolean,
        draft: PendingRegistrationDraft?
    ): DriverMember = DriverMember(
        id = mappedId,
        name = draft?.name ?: user.displayName?.takeIf { it.isNotBlank() } ?: "Driver Google",
        driverId = if (isAdmin) "DRG-001" else "DRG-${user.uid.take(6).uppercase()}",
        phone = draft?.phone ?: user.phoneNumber.orEmpty(),
        role = if (isAdmin) MemberRole.SUPER_ADMIN else MemberRole.ANGGOTA,
        motorcyclePlate = draft?.plate ?: "-",
        motorcycleModel = draft?.model ?: "Unit Ojol",
        rating = 5.0f, reviewCount = 0, loyaltyPoints = 10,
        loyaltyTier = if (isAdmin) "Commander" else "Junior Rider",
        verificationStatus = if (isAdmin) VerificationStatus.VERIFIED else VerificationStatus.PENDING_SCREENING,
        screeningNotes = if (isAdmin) "Super Admin Google" else "Terdaftar via Google Sign-In",
        isOnline = true, currentStatus = "Siap Mengaspal", baseArea = draft?.area ?: "Malang Raya",
        address = "", bloodType = "", emergencyContact = "", emergencyPhone = "", simNumber = "", nik = ""
    )

    fun buildPendingMember(draftId: String, driverId: String, name: String, phone: String, plate: String, model: String, area: String): DriverMember =
        DriverMember(
            id = draftId, name = name, driverId = driverId, phone = phone,
            role = MemberRole.ANGGOTA, motorcyclePlate = plate, motorcycleModel = model,
            rating = 5.0f, reviewCount = 0, loyaltyPoints = 10, loyaltyTier = "Junior Rider",
            verificationStatus = VerificationStatus.PENDING_SCREENING,
            screeningNotes = "Menunggu masuk Google & screening Admin.",
            isOnline = false, currentStatus = "Pendaftaran Baru (Pending)", baseArea = area,
            address = "", bloodType = "", emergencyContact = "", emergencyPhone = "", simNumber = "", nik = ""
        )
}
