package com.example.modules.main.primitives

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.MainNavTab
import com.example.core.viewmodel.logout
import com.example.core.viewmodel.toggleSosAlarmSound
import com.example.core.viewmodel.updateProfile
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.modules.profile.ProfileScreen
import com.example.shared.models.BadgeItem
import com.example.shared.models.DriverMember
import kotlinx.coroutines.launch

@Composable
fun ProfileTabDelegate(
    viewModel: DRGViewModel,
    currentMember: DriverMember?,
    badges: List<BadgeItem>,
    isSosAlarmEnabled: Boolean,
    dashboardTheme: DashboardVisualTheme,
    snackbarHostState: SnackbarHostState,
    scaffoldPadding: PaddingValues
) {
    val scope = rememberCoroutineScope()
    ProfileScreen(
        currentMember = currentMember,
        onUpdateProfile = { p, a, m, pl, pu, ad, b, ec, ep, s, k ->
            viewModel.updateProfile(p, a, m, pl, pu, ad, b, ec, ep, s, k)
            scope.launch { snackbarHostState.showSnackbar("Biodata lengkap berhasil disimpan!") }
        },
        isSosAlarmEnabled = isSosAlarmEnabled,
        onToggleSosAlarm = { viewModel.toggleSosAlarmSound(it) },
        onOpenSettings = { viewModel.selectTab(MainNavTab.SETTINGS) },
        onOpenAdmin = { viewModel.selectTab(MainNavTab.ADMIN) },
        onOpenDevTools = { viewModel.selectTab(MainNavTab.DEV_TOOLS) },
        onOpenAbout = { viewModel.selectTab(MainNavTab.ABOUT) },
        onLogout = { viewModel.logout() },
        badges = badges,
        visualTheme = dashboardTheme,
        contentPadding = scaffoldPadding
    )
}
