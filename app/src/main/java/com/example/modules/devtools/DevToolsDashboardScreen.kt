package com.example.modules.devtools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevToolsDashboardScreen(
    currentMember: DriverMember?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isSuperAdmin = currentMember?.role == MemberRole.SUPER_ADMIN
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        "Tree Progress", "Audit UI/UX", "Git CI/CD", "Build & Deploy", "Code Integrity", "Device Perf",
        "RCA & Gejala", "Audit Keamanan", "Node Server", "Cacat Sistem", "Saran Device",
        "Maturity", "Bug Hunter", "RAM & Memory", "Server API", "Tech Stack", "Repo Health", "SSOT (Acuan)"
    )
    val score = remember { ProjectHealthScore() }
    val apkInfo = remember { DynamicApkInspector.inspectRunningApk(context) }
    val dbReport = remember { DynamicDbIntrospector.introspectDatabase(context) }
    val capsReport = remember { RuntimeCapabilityProber.probeRunningCapabilities() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackgroundGradient)
            .testTag("dev_tools_dashboard_screen")
    ) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Terminal, contentDescription = null, tint = DrgGreenPrimary)
                    Column {
                        Text("DRG Dev Tools AI & Super Admin", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DrgTextPrimary)
                        Text("Observabilitas Enterprise: CI/CD, Code Integrity, Device Perf, & Keamanan", fontSize = 10.sp, color = DrgTextMuted)
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        if (!isSuperAdmin) {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Surface(shape = RoundedCornerShape(16.dp), color = DrgErrorBgSoft, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Red, modifier = Modifier.size(36.dp))
                        Text("Akses Dibatasi Khusus Super Admin", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DrgErrorText)
                        Text("Fitur Dev Tools AI hanya dapat diakses oleh Super Admin sistem DRG.", fontSize = 12.sp, color = DrgTextSecondary)
                    }
                }
            }
        } else {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = DrgGreenPrimary,
                edgePadding = 12.dp
            ) {
                tabs.forEachIndexed { idx, title ->
                    Tab(
                        selected = selectedTab == idx,
                        onClick = { selectedTab = idx },
                        text = { Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 6.dp)) {
                DevToolsTabSwitcher(
                    selectedTab = selectedTab,
                    headerContent = {
                        DevScoreBanner(score = score)
                        Spacer(modifier = Modifier.height(6.dp))
                        DevApkSkewCard(apkInfo = apkInfo, dbReport = dbReport, capsReport = capsReport)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                )
            }
        }
    }
}
