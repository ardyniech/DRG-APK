package com.example.modules.emergency

import androidx.compose.runtime.Composable
import com.example.shared.models.EmergencyType

@Composable
fun EmergencyTriggerDialog(
    onDismiss: () -> Unit,
    onConfirm: (EmergencyType, String, String) -> Unit,
    isSosAlarmEnabled: Boolean = true
) {
    CommunityEmergencyModal(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        isSosAlarmEnabled = isSosAlarmEnabled
    )
}
