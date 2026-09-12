package com.example.modules.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.ui.theme.DrgBackground
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgRedDanger

@Composable
fun ProfileScreen(
    currentMember: DriverMember?,
    onUpdateProfile: (String, String, String, String, String, String, String, String, String, String, String) -> Unit,
    isSosAlarmEnabled: Boolean = true,
    onToggleSosAlarm: (Boolean) -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenAdmin: () -> Unit = {},
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp)
    ) {
        item { KtaDigitalCard(member = currentMember) }

        if (currentMember?.role == MemberRole.SUPER_ADMIN || currentMember?.role?.isLeadership == true) {
            item {
                Button(
                    onClick = onOpenAdmin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_admin_governance"),
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Masuk Panel Admin & Jabatan", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        item {
            ProfileSettingsCard(
                isSosAlarmEnabled = isSosAlarmEnabled,
                onToggleSosAlarm = onToggleSosAlarm,
                onOpenSettings = onOpenSettings
            )
        }

        item { GamificationCard(member = currentMember) }

        item {
            VehicleInfoCard(
                member = currentMember,
                onEditClick = { showEditDialog = true }
            )
        }

        item {
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_logout_profile"),
                colors = ButtonDefaults.buttonColors(containerColor = DrgRedDanger),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Keluar Aplikasi (Logout)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }

    if (showLogoutDialog) {
        ProfileLogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            }
        )
    }

    if (showEditDialog && currentMember != null) {
        EditProfileDialog(
            member = currentMember,
            onDismiss = { showEditDialog = false },
            onSave = { phone, area, model, plate, photo, addr, blood, emContact, emPhone, sim, ktpNik ->
                onUpdateProfile(phone, area, model, plate, photo, addr, blood, emContact, emPhone, sim, ktpNik)
                showEditDialog = false
            }
        )
    }
}
