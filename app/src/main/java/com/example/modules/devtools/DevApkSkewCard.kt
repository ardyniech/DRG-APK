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
        color = if (apkInfo.isOutdatedApk) Color(0xFFFFFBEB) else DrgGreenLight,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (apkInfo.isOutdatedApk) DrgAmberWarning.copy(alpha = 0.6f) else DrgGreenPrimary.copy(alpha = 0.4f),
                RoundedCornerShape(12.dp)
            )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(
                        imageVector = if (apkInfo.isOutdatedApk) Icons.Default.Update else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (apkInfo.isOutdatedApk) DrgAmberWarning else DrgGreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        if (apkInfo.isOutdatedApk) "⚡ Adaptasi APK Belum Di-update" else "🛡️ Binary APK Sinkron Penuh",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (apkInfo.isOutdatedApk) Color(0xFFB45309) else DrgGreenPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (apkInfo.isOutdatedApk) Color(0xFFFEF3C7) else DrgGreenPrimary
                ) {
                    Text(
                        if (apkInfo.isOutdatedApk) "LEGACY APK" else "LATEST BUILD",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (apkInfo.isOutdatedApk) Color(0xFFB45309) else Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(apkInfo.versionSkewDescription, fontSize = 9.sp, color = DrgTextPrimary, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "📦 Versi Terpasang: v${apkInfo.installedVersionName} (Build ${apkInfo.installedVersionCode})",
                    fontSize = 9.sp,
                    color = DrgTextSecondary
                )
                Text(
                    "🗄️ SQLite: PRAGMA v${dbReport.actualUserVersion} (${dbReport.actualTableCount} Tabel)",
                    fontSize = 9.sp,
                    color = DrgTextSecondary
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "🧩 Modul DEX: ${capsReport.presentCount}/${capsReport.totalCount} Ter-compile (${capsReport.compatibilityPercentage.toInt()}%)",
                    fontSize = 9.sp,
                    color = DrgTextSecondary
                )
                Text(
                    "🕒 Update: ${apkInfo.updateTimeFormatted}",
                    fontSize = 9.sp,
                    color = DrgTextSecondary
                )
            }
        }
    }
}
