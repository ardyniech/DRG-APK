package com.example.modules.main.primitives

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.core.viewmodel.*
import com.example.modules.admin.AdminGovernanceScreen
import com.example.modules.members.role_management.RolePermissionManagementScreen
import com.example.shared.models.DriverMember
import kotlinx.coroutines.launch

@Composable
fun AdminTabDelegate(
    tab: MainNavTab,
    viewModel: DRGViewModel,
    currentMember: DriverMember?,
    members: List<DriverMember>,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()

    if (tab == MainNavTab.ADMIN) {
        AdminGovernanceScreen(
            currentMember = currentMember, members = members,
            onApproveScreening = { viewModel.approveMemberScreening(it) },
            onRejectScreening = { id, r -> viewModel.rejectMemberScreening(id, r) },
            onSendNotification = { t, msg, s ->
                viewModel.sendNotification(t, msg, s)
                scope.launch { snackbarHostState.showSnackbar("Pengumuman berhasil dipancarkan!") }
            },
            onOpenRoleManagement = { viewModel.selectTab(MainNavTab.ROLES) },
            onUpdateMemberPermissions = { id, ro, k, v, so, po, st ->
                viewModel.updateMemberPermissions(id, ro, k, v, so, po, st)
            }
        )
    } else if (tab == MainNavTab.ROLES) {
        RolePermissionManagementScreen(
            members = members, currentMember = currentMember,
            onBack = { viewModel.selectTab(MainNavTab.ADMIN) },
            onSavePermissions = { id, ro, k, v, so, po, st ->
                viewModel.updateMemberPermissions(id, ro, k, v, so, po, st)
            }
        )
    }
}
