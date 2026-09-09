package com.example.modules.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgBackground
import com.example.ui.theme.DrgRedDanger

@Composable
fun ProfileScreen(
    currentMember: DriverMember?,
    onUpdateProfile: (String, String, String, String, String) -> Unit,
    isSosAlarmEnabled: Boolean = true,
    onToggleSosAlarm: (Boolean) -> Unit = {},
    onOpenSettings: () -> Unit = {},
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
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Konfirmasi Logout", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin keluar dari akun ini? Sesi Anda akan ditutup dengan aman.") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgRedDanger)
                ) {
                    Text("Ya, Keluar", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal")
                }
            }
        )
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
