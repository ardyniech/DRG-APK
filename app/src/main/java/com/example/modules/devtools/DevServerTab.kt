package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun DevServerTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var probeResult by remember { mutableStateOf<NetworkProbeResult?>(null) }
    var isProbing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isProbing = true
        probeResult = RealtimeNetworkProbe.probe(context)
        isProbing = false
    }

    DevTabShell(
        headerContent = headerContent,
        headerIcon = Icons.Default.Cloud,
        headerTitle = "Diagnostik Server & Cloud Riil",
        headerDesc = "Pengukuran socket ping riil tanpa mock data: latency RTT ke Google Cloud & status jaringan.",
        headerTrailing = {
            IconButton(
                onClick = { scope.launch { isProbing = true; probeResult = RealtimeNetworkProbe.probe(context); isProbing = false } },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Ping Ulang", tint = DrgGreenPrimary)
            }
        }
    ) {

        item {
            val isOnline = probeResult?.isOnline == true
            Surface(shape = RoundedCornerShape(12.dp), color = if (isOnline) DrgSuccessBgSoft else DrgErrorBgSoft, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(if (isOnline) "Server Status: ONLINE (${probeResult?.networkType})" else "Server Status: OFFLINE / Gangguan", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (isOnline) DrgSuccessTextDark else DrgErrorText)
                        Text(if (isOnline) "Ping host: firestore.googleapis.com ${probeResult?.firestoreLatencyMs} ms • generativelanguage.googleapis.com ${probeResult?.aiGatewayLatencyMs} ms (konektivitas, bukan status backend app)" else "Koneksi terputus. Data lokal Room tetap tersedia; sinkron cloud bergantung backend.", fontSize = 10.sp, color = if (isOnline) DrgSuccessText else DrgErrorTextDark)
                    }
                }
            }
        }

        probeResult?.preFailureWarning?.let { warning ->
            item {
                Surface(shape = RoundedCornerShape(10.dp), color = DrgWarningBgAmberSoft, modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = DrgAmberWarning, modifier = Modifier.size(16.dp))
                        Text(warning, fontSize = 10.sp, color = DrgWarningTextAmber, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("🌐 Endpoint & Latensi Pengukuran Langsung", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.3f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("firestore.googleapis.com:443 (host probe)", fontSize = 11.sp, color = DrgTextSecondary)
                        Text(if (isProbing) "Probing..." else "${probeResult?.firestoreLatencyMs ?: 0} ms", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if ((probeResult?.firestoreLatencyMs ?: 0) < 300) DrgGreenPrimary else Color.Red)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("generativelanguage.googleapis.com", fontSize = 11.sp, color = DrgTextSecondary)
                        Text(if (isProbing) "Probing..." else "${probeResult?.aiGatewayLatencyMs ?: 0} ms", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if ((probeResult?.aiGatewayLatencyMs ?: 0) < 300) DrgGreenPrimary else Color.Red)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Local-First SQLite Database", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("Room v13 (Zero Latency)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                    }
                }
            }
        }
    }
}
