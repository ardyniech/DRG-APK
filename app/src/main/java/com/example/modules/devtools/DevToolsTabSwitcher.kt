package com.example.modules.devtools

import androidx.compose.runtime.Composable

@Composable
fun DevToolsTabSwitcher(
    selectedTab: Int,
    headerContent: (@Composable () -> Unit)? = null
) {
    when (selectedTab) {
        0 -> DevProgressTab(headerContent = headerContent)
        1 -> DevUiUxAuditTab(headerContent = headerContent)
        2 -> DevCiCdTab(headerContent = headerContent)
        3 -> DevBuildDeploymentTab(headerContent = headerContent)
        4 -> DevCodeIntegrityTab(headerContent = headerContent)
        5 -> DevDevicePerformanceTab(headerContent = headerContent)
        6 -> DevRootCauseTab(headerContent = headerContent)
        7 -> DevSecurityAuditTab(headerContent = headerContent)
        8 -> DevNodeTopologyTab(headerContent = headerContent)
        9 -> DevDefectsTab(headerContent = headerContent)
        10 -> DevDeviceAdvisorTab(headerContent = headerContent)
        11 -> DevMaturityTab(headerContent = headerContent)
        12 -> DevBugHunterTab(headerContent = headerContent)
        13 -> DevMemoryTab(headerContent = headerContent)
        14 -> DevServerTab(headerContent = headerContent)
        15 -> DevTechStackTab(headerContent = headerContent)
        else -> DevRepoHealthTab(headerContent = headerContent)
    }
}
