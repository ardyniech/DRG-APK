package com.example.modules.main.primitives

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.logout
import com.example.core.viewmodel.updateProfile
import com.example.modules.profile.EditProfileDialog
import com.example.modules.profile.ProfileLogoutDialog
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.DriverMember
import kotlinx.coroutines.launch

/**
 * Shortcut bar di bagian atas: Edit Profil & Logout, selalu tersedia.
 */
@Composable
fun TopShortcutHost(
    viewModel: DRGViewModel,
    currentMember: DriverMember?,
    snackbarHostState: androidx.compose.material3.SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    var showEdit by remember { mutableStateOf(false) }
    var showLogout by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { showEdit = true }) {
            Icon(Icons.Default.Edit, contentDescription = "Edit Profil", tint = theme.primaryColor)
        }
        IconButton(onClick = { showLogout = true }) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = Color(0xFFDC2626))
        }
    }

    if (showLogout) {
        ProfileLogoutDialog(
            onDismiss = { showLogout = false },
            onConfirm = { showLogout = false; viewModel.logout() }
        )
    }

    if (showEdit && currentMember != null) {
        EditProfileDialog(
            member = currentMember,
            onDismiss = { showEdit = false },
            onSave = { phone, area, model, plate, photo, addr, blood, emContact, emPhone, sim, ktpNik ->
                viewModel.updateProfile(phone, area, model, plate, photo, addr, blood, emContact, emPhone, sim, ktpNik)
                scope.launch { snackbarHostState.showSnackbar("Biodata lengkap berhasil disimpan!") }
                showEdit = false
            }
        )
    }
}

@Composable
fun TopShortcutBar(onEditProfile: () -> Unit, onLogout: () -> Unit, modifier: Modifier = Modifier) {
    val theme = LocalAppVisualTheme.current
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onEditProfile) {
            Icon(Icons.Default.Edit, contentDescription = "Edit Profil", tint = theme.primaryColor)
        }
        IconButton(onClick = onLogout) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = Color(0xFFDC2626))
        }
    }
}