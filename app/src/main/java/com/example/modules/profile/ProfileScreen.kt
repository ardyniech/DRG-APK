package com.example.modules.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.shared.atoms.DrgLuminousAtmosphere
import com.example.shared.models.AppVisualTheme
import com.example.shared.models.BadgeItem
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgRedDanger

/**
 * Redesigned ProfileScreen sharing 1-DNA Luminous Frosted Glass OS with DashboardScreen.
 * Houses the exclusive Settings Launcher entry alongside biodata, role tools, and loyalty progress.
 */
@Composable
fun ProfileScreen(
    currentMember: DriverMember?,
    onUpdateProfile: (String, String, String, String, String, String, String, String, String, String, String) -> Unit,
    isSosAlarmEnabled: Boolean = true,
    onToggleSosAlarm: (Boolean) -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenAdmin: () -> Unit = {},
    onOpenDevTools: () -> Unit = {},
    onOpenAbout: () -> Unit = {},
    onLogout: () -> Unit = {},
    badges: List<BadgeItem> = emptyList(),
    visualTheme: AppVisualTheme = AppVisualTheme.EMERALD_AURORA,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize().testTag("profile_screen")) {
        DrgLuminousAtmosphere(theme = visualTheme)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ProfileGlassHeroCard(
                member = currentMember,
                visualTheme = visualTheme,
                onOpenSettings = onOpenSettings
            )

            ProfileLauncherDeck(
                role = currentMember?.role,
                visualTheme = visualTheme,
                onOpenSettings = onOpenSettings,
                onEditBiodata = { showEditDialog = true },
                onOpenAdmin = onOpenAdmin,
                onOpenDevTools = onOpenDevTools
            )

            GamificationCard(
                member = currentMember,
                badges = badges,
                visualTheme = visualTheme
            )

            VehicleInfoCard(
                member = currentMember,
                onEditClick = { showEditDialog = true },
                visualTheme = visualTheme
            )

            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("btn_logout_profile"),
                colors = ButtonDefaults.buttonColors(containerColor = DrgRedDanger),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Keluar Aplikasi (Logout)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    if (showLogoutDialog) {
        ProfileLogoutDialog(onDismiss = { showLogoutDialog = false }, onConfirm = { showLogoutDialog = false; onLogout() })
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
