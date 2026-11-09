package com.example.modules.devtools

import androidx.compose.runtime.Composable

@Composable
fun DevToolsTabSwitcher(selectedTab: Int) {
    when (selectedTab) {
        0 -> DevProgressTab()
        1 -> DevUiUxAuditTab()
        2 -> DevCiCdTab()
        3 -> DevBuildDeploymentTab()
        4 -> DevCodeIntegrityTab()
        5 -> DevDevicePerformanceTab()
        6 -> DevRootCauseTab()
        7 -> DevSecurityAuditTab()
        8 -> DevNodeTopologyTab()
        9 -> DevDefectsTab()
        10 -> DevDeviceAdvisorTab()
        11 -> DevMaturityTab()
        12 -> DevBugHunterTab()
        13 -> DevMemoryTab()
        14 -> DevServerTab()
        15 -> DevTechStackTab()
        else -> DevRepoHealthTab()
    }
}
