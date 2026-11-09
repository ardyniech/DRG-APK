package com.example.modules.devtools

import androidx.compose.runtime.Composable

@Composable
fun DevToolsTabSwitcher(selectedTab: Int) {
    when (selectedTab) {
        0 -> DevRootCauseTab()
        1 -> DevSecurityAuditTab()
        2 -> DevNodeTopologyTab()
        3 -> DevDefectsTab()
        4 -> DevDeviceAdvisorTab()
        5 -> DevMaturityTab()
        6 -> DevBugHunterTab()
        7 -> DevProgressTab()
        8 -> DevMemoryTab()
        9 -> DevServerTab()
        10 -> DevTechStackTab()
        else -> DevRepoHealthTab()
    }
}
