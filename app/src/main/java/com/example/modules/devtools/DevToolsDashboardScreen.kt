package com.example.modules.devtools

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.shared.atoms.DrgLuminousAtmosphere
import com.example.shared.models.AppVisualTheme
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole

private val DEVTOOLS_TABS = listOf(
    "Tree Progress", "Audit UI/UX", "Git CI/CD", "Build & Deploy", "Code Integrity", "Device Perf",
    "RCA & Gejala", "Audit Keamanan", "Node Server", "Cacat Sistem", "Saran Device",
    "Maturity", "Bug Hunter", "RAM & Memory", "Server API", "Tech Stack", "Repo Health", "SSOT (Acuan)"
)

/**
 * Super-Admin-only DevTools Enterprise Console redesigned with 1-DNA Luminous Frosted Glass OS.
 */
@Composable
fun DevToolsDashboardScreen(
    currentMember: DriverMember?,
    onBack: () -> Unit,
    visualTheme: AppVisualTheme = AppVisualTheme.EMERALD_AURORA,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isSuperAdmin = currentMember?.role == MemberRole.SUPER_ADMIN
    var selectedTab by remember { mutableIntStateOf(0) }
    val score = remember { ProjectHealthScore() }
    val apkInfo = remember { DynamicApkInspector.inspectRunningApk(context) }
    val dbReport = remember { DynamicDbIntrospector.introspectDatabase(context) }
    val capsReport = remember { RuntimeCapabilityProber.probeRunningCapabilities() }

    BackHandler { onBack() }

    Box(modifier = modifier.fillMaxSize().testTag("dev_tools_dashboard_screen")) {
        DrgLuminousAtmosphere(theme = visualTheme)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding() + 10.dp, start = 14.dp, end = 14.dp)
        ) {
            DevToolsGlassHeader(
                tabs = DEVTOOLS_TABS,
                selectedTab = selectedTab,
                onSelectTab = { selectedTab = it },
                isSuperAdmin = isSuperAdmin,
                score = score,
                visualTheme = visualTheme,
                onBack = onBack
            )

            if (!isSuperAdmin) {
                DevToolsLockedGlassCard(
                    roleName = currentMember?.role?.shortName ?: "Tamu",
                    visualTheme = visualTheme,
                    onBack = onBack
                )
            } else {
                Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(top = 4.dp)) {
                    DevToolsTabSwitcher(
                        selectedTab = selectedTab,
                        headerContent = {
                            DevScoreBanner(score = score)
                            Spacer(modifier = Modifier.height(8.dp))
                            DevApkSkewCard(apkInfo = apkInfo, dbReport = dbReport, capsReport = capsReport)
                        }
                    )
                }
            }
        }
    }
}
