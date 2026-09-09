package com.example.modules.main.primitives

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.core.viewmodel.CashManagementViewModel
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.*
import com.example.modules.emergency.EmergencyCountdownDialog
import com.example.modules.emergency.EmergencyTriggerDialog
import com.example.modules.gamification.AwardPointDialog
import com.example.modules.notifications.NotificationPreferencesDialog
import com.example.modules.radar.AddHazardDialog
import com.example.modules.treasury.AddTransactionDialog
import com.example.shared.atoms.RoleSwitcherDialog
import com.example.shared.models.CrashSensitivity
import com.example.shared.models.DriverMember
import com.example.shared.models.NotificationPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrgAppDialogs(
    viewModel: DRGViewModel,
    cashViewModel: CashManagementViewModel,
    context: Context,
    members: List<DriverMember>,
    currentMember: DriverMember?,
    showRoleSwitcher: Boolean,
    onDismissRoleSwitcher: () -> Unit,
    showEmergencyTrigger: Boolean,
    onDismissEmergencyTrigger: () -> Unit,
    showAddKasDialog: Boolean,
    onDismissAddKasDialog: () -> Unit,
    showAwardPointDialog: Boolean,
    selectedTargetForAward: DriverMember?,
    onDismissAwardPointDialog: () -> Unit,
    showAddHazardDialog: Boolean,
    onDismissAddHazardDialog: () -> Unit,
    showNotifPrefDialog: Boolean,
    onDismissNotifPrefDialog: () -> Unit,
    showSettingsDialog: Boolean,
    onDismissSettingsDialog: () -> Unit,
    isSosAlarmEnabled: Boolean,
    isCrashGuardEnabled: Boolean,
    crashSensitivity: CrashSensitivity,
    mapCachePolicy: MapCachePolicy,
    locationSyncProfile: LocationSyncPowerProfile,
    isDataSaverMode: Boolean,
    isPowerSaverMode: Boolean,
    crashDetectedEvent: Float?,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    if (showRoleSwitcher) {
        RoleSwitcherDialog(members = members, currentMemberId = currentMember?.id ?: "", onSelectMember = { viewModel.switchRole(it) }, onDismiss = onDismissRoleSwitcher)
    }

    if (showEmergencyTrigger) {
        EmergencyTriggerDialog(onDismiss = onDismissEmergencyTrigger, onConfirm = { type, msg, loc ->
            viewModel.triggerEmergency(type, msg, loc)
            scope.launch { snackbarHostState.showSnackbar("Sinyal SOS Dipancarkan ke Radar Satgas!") }
        }, isSosAlarmEnabled = isSosAlarmEnabled)
    }

    if (showAddKasDialog) {
        AddTransactionDialog(onDismiss = onDismissAddKasDialog, onConfirm = { title, amount, type, cat, desc ->
            cashViewModel.addTransaction(title = title, amount = amount, type = type, category = cat, description = desc, recordedBy = currentMember?.name ?: "Pengurus")
            scope.launch { snackbarHostState.showSnackbar("Pencatatan kas berhasil disimpan!") }
        })
    }

    if (showAwardPointDialog && selectedTargetForAward != null && currentMember != null) {
        AwardPointDialog(targetDriver = selectedTargetForAward, currentGiverRole = currentMember.role, onDismiss = onDismissAwardPointDialog, onAward = { isBeneficiary, reason ->
            viewModel.awardPeerPoints(selectedTargetForAward.id, selectedTargetForAward.name, isBeneficiary, reason)
            onDismissAwardPointDialog()
            scope.launch { snackbarHostState.showSnackbar("Apresiasi Poin Berhasil Dikirim!") }
        })
    }

    if (showAddHazardDialog) {
        AddHazardDialog(onDismiss = onDismissAddHazardDialog, onSubmit = { title, type, location, desc, lat, lng ->
            viewModel.reportHazard(title, type, location, desc, lat, lng)
            onDismissAddHazardDialog()
            scope.launch { snackbarHostState.showSnackbar("Area Rawan Ditandai di Radar!") }
        })
    }

    if (showNotifPrefDialog) {
        NotificationPreferencesDialog(initialPref = NotificationPreference(currentMember?.id ?: "DRG-001"), onDismiss = onDismissNotifPrefDialog, onSave = {
            viewModel.saveNotificationPref(it)
            onDismissNotifPrefDialog()
            scope.launch { snackbarHostState.showSnackbar("Preferensi notifikasi disimpan!") }
        }, isSosAlarmEnabled = isSosAlarmEnabled, onToggleSosAlarm = { viewModel.toggleSosAlarmSound(it) })
    }

    if (showSettingsDialog) {
        AppSettingsDialogDelegate(
            viewModel = viewModel, context = context, currentMember = currentMember,
            isSosAlarmEnabled = isSosAlarmEnabled, isCrashGuardEnabled = isCrashGuardEnabled,
            crashSensitivity = crashSensitivity, mapCachePolicy = mapCachePolicy,
            locationSyncProfile = locationSyncProfile, isDataSaverMode = isDataSaverMode,
            isPowerSaverMode = isPowerSaverMode, snackbarHostState = snackbarHostState,
            scope = scope, onDismiss = onDismissSettingsDialog
        )
    }

    crashDetectedEvent?.let { gForce ->
        EmergencyCountdownDialog(detectedGForce = gForce, isSosAlarmEnabled = isSosAlarmEnabled, onCancel = { viewModel.dismissCrashCountdown() }, onConfirmAutoSos = { viewModel.confirmCrashAutoSos(gForce) })
    }
}
