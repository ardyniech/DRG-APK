package com.example.modules.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgBackground

@Composable
fun ProfileScreen(
    currentMember: DriverMember?,
    onUpdateProfile: (String, String, String, String, String) -> Unit,
    isSosAlarmEnabled: Boolean = true,
    onToggleSosAlarm: (Boolean) -> Unit = {},
    onOpenSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp)
    ) {
        // Digital KTA Master Card DRG (Simple & Clean Styling)
        item {
            KtaDigitalCard(member = currentMember)
        }

        // Quick Settings Card
        item {
            ProfileSettingsCard(
                isSosAlarmEnabled = isSosAlarmEnabled,
                onToggleSosAlarm = onToggleSosAlarm,
                onOpenSettings = onOpenSettings
            )
        }

        // Gamification & Badges Card
        item {
            GamificationCard(member = currentMember)
        }

        // Vehicle & Personal Info Card
        item {
            VehicleInfoCard(
                member = currentMember,
                onEditClick = { showEditDialog = true }
            )
        }
    }

    if (showEditDialog && currentMember != null) {
        EditProfileDialog(
            member = currentMember,
            onDismiss = { showEditDialog = false },
            onSave = { phone, area, model, plate, photo ->
                onUpdateProfile(phone, area, model, plate, photo)
                showEditDialog = false
            }
        )
    }
}
