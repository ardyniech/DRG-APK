package com.example.core.viewmodel.coordinators

import com.example.core.repository.DRGRepository
import com.example.shared.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GovernanceCoordinator(
    private val repository: DRGRepository,
    private val scope: CoroutineScope,
    private val membersFlow: StateFlow<List<DriverMember>>,
    private val showToast: (String) -> Unit
) {
    private val _adminLogs = MutableStateFlow<List<AdminLog>>(listOf(
        AdminLog("log-1", "Slamet Rahardjo", "Ketua", "menyetujui pendaftaran & verifikasi", "Rudi Hermawan", "08:15"),
        AdminLog("log-2", "Budi Santoso", "Sekretaris", "mengubah peran menjadi Satgas Korlap", "Agus Prasetyo", "Kemarin"),
        AdminLog("log-3", "Dewi Anggraini", "Bendahara", "mencatatkan penerimaan kas Rp 500.000", "Pendaftaran Anggota", "2 hari lalu")
    ))
    val adminLogs: StateFlow<List<AdminLog>> = _adminLogs.asStateFlow()

    fun approveMemberScreening(memberId: String, notes: String) {
        scope.launch {
            runCatching {
                repository.updateScreening(memberId, VerificationStatus.VERIFIED, notes)
            }.onSuccess {
                showToast("Anggota berhasil diverifikasi dan aktif di komunitas DRG.")
            }.onFailure { error ->
                showToast("Gagal verifikasi: ${error.localizedMessage}")
            }
        }
    }

    fun rejectMemberScreening(memberId: String, reason: String) {
        scope.launch {
            runCatching {
                repository.updateScreening(memberId, VerificationStatus.REJECTED, reason)
            }.onSuccess {
                showToast("Pendaftaran anggota ditolak.")
            }.onFailure { error ->
                showToast("Gagal menolak screening: ${error.localizedMessage}")
            }
        }
    }

    fun updateMemberRole(memberId: String, newRole: MemberRole, actor: DriverMember?) {
        scope.launch {
            runCatching {
                val found = membersFlow.value.find { it.id == memberId } ?: return@launch
                val updated = found.copy(role = newRole)
                repository.updateMember(updated)
                val log = AdminLogUtils.createLog(actor, "mengubah peran menjadi ${newRole.title}", found.name)
                _adminLogs.value = listOf(log) + _adminLogs.value
                found.name
            }.onSuccess { name ->
                showToast("Peran $name berhasil diubah menjadi ${newRole.shortName}")
            }.onFailure { error ->
                showToast("Gagal update peran: ${error.localizedMessage}")
            }
        }
    }

    fun updateMemberVerification(memberId: String, newStatus: VerificationStatus, actor: DriverMember?) {
        scope.launch {
            runCatching {
                val found = membersFlow.value.find { it.id == memberId } ?: return@launch
                val updated = found.copy(verificationStatus = newStatus)
                repository.updateMember(updated)
                val log = AdminLogUtils.createLog(actor, "mengubah status verifikasi menjadi ${newStatus.name}", found.name)
                _adminLogs.value = listOf(log) + _adminLogs.value
                found.name
            }.onSuccess { name ->
                showToast("Status verifikasi $name diubah menjadi ${newStatus.name}")
            }.onFailure { error ->
                showToast("Gagal update status: ${error.localizedMessage}")
            }
        }
    }
}
