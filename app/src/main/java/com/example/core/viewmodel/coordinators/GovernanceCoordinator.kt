package com.example.core.viewmodel.coordinators

import com.example.core.repository.DRGRepository
import com.example.shared.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GovernanceCoordinator(
    private val repository: DRGRepository,
    private val scope: CoroutineScope,
    private val membersFlow: StateFlow<List<DriverMember>>,
    private val showToast: (String) -> Unit
) {
    val adminLogs: StateFlow<List<AdminLog>> = repository.allAdminLogs.stateIn(
        scope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun approveMemberScreening(memberId: String, notes: String, actor: DriverMember? = null) {
        scope.launch {
            runCatching {
                val target = membersFlow.value.find { it.id == memberId }
                repository.updateScreening(memberId, VerificationStatus.VERIFIED, notes)
                val targetName = target?.name ?: memberId
                val log = AdminLogUtils.createLog(actor, "menyetujui verifikasi keanggotaan", targetName)
                repository.adminLogRepo.insertLog(log)
            }.onSuccess {
                showToast("Anggota berhasil diverifikasi dan aktif di komunitas DRG.")
            }.onFailure { error ->
                showToast("Gagal verifikasi: ${error.localizedMessage}")
            }
        }
    }

    fun rejectMemberScreening(memberId: String, reason: String, actor: DriverMember? = null) {
        scope.launch {
            runCatching {
                val target = membersFlow.value.find { it.id == memberId }
                repository.updateScreening(memberId, VerificationStatus.REJECTED, reason)
                val targetName = target?.name ?: memberId
                val log = AdminLogUtils.createLog(actor, "menolak screening anggota ($reason)", targetName)
                repository.adminLogRepo.insertLog(log)
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
                repository.adminLogRepo.insertLog(log)
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
                repository.adminLogRepo.insertLog(log)
                found.name
            }.onSuccess { name ->
                showToast("Status verifikasi $name diubah menjadi ${newStatus.name}")
            }.onFailure { error ->
                showToast("Gagal update status: ${error.localizedMessage}")
            }
        }
    }

    fun updateMemberPermissions(
        memberId: String,
        newRole: MemberRole,
        canKas: Boolean,
        canVerify: Boolean,
        canSos: Boolean,
        canPosko: Boolean,
        newStatus: VerificationStatus,
        actor: DriverMember?
    ) {
        scope.launch {
            runCatching {
                val found = membersFlow.value.find { it.id == memberId } ?: return@launch
                val updated = found.copy(
                    role = newRole,
                    canManageKas = canKas,
                    canVerifyDrivers = canVerify,
                    canBroadcastSos = canSos,
                    canManagePosko = canPosko,
                    verificationStatus = newStatus
                )
                repository.updateMember(updated)
                val actionDesc = "mengubah peran (${newRole.shortName}) & izin akses operasional"
                val log = AdminLogUtils.createLog(actor, actionDesc, found.name)
                repository.adminLogRepo.insertLog(log)
                found.name
            }.onSuccess { name ->
                showToast("Otoritas & izin $name berhasil diperbarui.")
            }.onFailure { error ->
                showToast("Gagal update otoritas: ${error.localizedMessage}")
            }
        }
    }
}
