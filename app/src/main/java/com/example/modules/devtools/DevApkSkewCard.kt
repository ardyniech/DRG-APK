package com.example.modules.devtools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.ui.theme.*

@Composable
fun DevApkSkewCard(
    apkInfo: ApkVersionInfo,
    dbReport: DatabaseRuntimeReport,
    capsReport: RuntimeCapabilitiesReport
) {
    DrgLuminousGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = DrgGlassSurfaceSoft,
        borderHighlightColor = Color(0xFF93C5FD),
        contentPadding = 12.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(16.dp))
                    Text("Runtime APK / DB / DEX Telemetry", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextDark)
                }
                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFE0F2FE)).padding(horizontal = 7.dp, vertical = 2.dp)) {
                    Text("v${apkInfo.installedVersionName}", fontFamily = SpaceGroteskFontFamily, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DrgBlueAccent)
                }
            }
            Text(apkInfo.versionSkewDescription, fontSize = 10.sp, color = DrgTextSecondary)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Code ${apkInfo.installedVersionCode} • ${apkInfo.updateTimeFormatted}", fontFamily = SpaceGroteskFontFamily, fontSize = 9.5.sp, color = DrgTextSecondary)
                Text("SQLite v${dbReport.actualUserVersion} (${dbReport.actualTableCount} tbl)", fontFamily = SpaceGroteskFontFamily, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = DrgTextDark)
            }
            Text("DEX Modul: ${capsReport.presentCount}/${capsReport.totalCount} (${capsReport.compatibilityPercentage.toInt()}%)", fontFamily = SpaceGroteskFontFamily, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
        }
    }
}
