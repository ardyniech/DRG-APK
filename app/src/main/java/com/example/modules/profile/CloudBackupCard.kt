package com.example.modules.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.sync.SyncStatus
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

@Composable
fun CloudBackupCard(
    syncStatus: SyncStatus,
    userEmail: String?,
    onSyncNow: () -> Unit,
    onUploadBackup: () -> Unit,
    onRestoreBackup: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth().testTag("cloud_backup_card"),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 16.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.CloudSync, contentDescription = "Cloud Sync", tint = DrgGreenPrimary, modifier = Modifier.size(22.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Sinkronisasi Cloud & Cadangan Terenkripsi", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text(
                        text = "${syncStatus.label} • ${userEmail ?: "Terautentikasi"}",
                        fontSize = 11.sp,
                        color = DrgTextSecondary
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onSyncNow,
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp).testTag("btn_cloud_sync_now"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CloudSync, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Sinkron", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = onUploadBackup,
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp).testTag("btn_cloud_backup_upload"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor)
                ) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Cadangkan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            OutlinedButton(
                onClick = onRestoreBackup,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("btn_cloud_backup_restore"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Pulihkan Database Terenkripsi (AES-GCM) dari Cloud", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
