package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DevApkSkewCard(
    apkInfo: ApkVersionInfo,
    dbReport: DatabaseRuntimeReport,
    capsReport: RuntimeCapabilitiesReport
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DrgSurfaceVariant,
        modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DrgTextSecondary, modifier = Modifier.size(16.dp))
                    Text("🛡️ Info APK/DB/Modul (Runtime)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgTextPrimary)
                }
                Surface(shape = RoundedCornerShape(4.dp), color = DrgSlateBorder) {
                    Text("CEK UPDATE: MANUAL", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(apkInfo.versionSkewDescription, fontSize = 9.sp, color = DrgTextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("📦 Terpasang: v${apkInfo.installedVersionName} (code ${apkInfo.installedVersionCode})", fontSize = 9.sp, color = DrgTextSecondary)
                Text("🗄️ SQLite: v${dbReport.actualUserVersion} (${dbReport.actualTableCount} tabel)", fontSize = 9.sp, color = DrgTextSecondary)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("🧩 Modul DEX: ${capsReport.presentCount}/${capsReport.totalCount} (${capsReport.compatibilityPercentage.toInt()}%)", fontSize = 9.sp, color = DrgTextSecondary)
                Text("🕒 Update: ${apkInfo.updateTimeFormatted}", fontSize = 9.sp, color = DrgTextSecondary)
            }
        }
    }
}
