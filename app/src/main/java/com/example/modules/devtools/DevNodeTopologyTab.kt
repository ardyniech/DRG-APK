package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hub
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

@Composable
fun DevNodeTopologyTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    val context = LocalContext.current
    var isOnline by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        val probe = RealtimeNetworkProbe.probe(context)
        isOnline = probe.isOnline
    }

    val nodes = remember(isOnline) { NodeTopologyScanner.getClusterNodes(isOnline) }
    val connectedCount = remember(isOnline) { NodeTopologyScanner.countConnectedNodes(isOnline) }
    val totalConnections = remember(isOnline) { NodeTopologyScanner.countTotalConnections(isOnline) }

    DevTabShell(
        headerContent = headerContent,
        headerIcon = Icons.Default.Hub,
        headerTitle = "Topologi Multi-Node & Arsitektur Cluster",
        headerDesc = "Auto-detect status $connectedCount dari ${nodes.size} node terhubung dengan total $totalConnections koneksi aktif."
    ) {

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = DrgGreenLight, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Total Node Terhubung: $connectedCount / ${nodes.size}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgGreenPrimary)
                        Text("Mencakup Local SQLite, Cloud Firestore, AI Gateway, & Mesh Radio.", fontSize = 10.sp, color = DrgTextSecondary)
                    }
                    Surface(shape = RoundedCornerShape(6.dp), color = DrgGreenPrimary) {
                        Text("$totalConnections Links", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                    }
                }
            }
        }

        items(nodes) { node ->
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(node.nodeName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                            Text("Tipe: ${node.nodeType}", fontSize = 10.sp, color = DrgTextSecondary)
                        }
                        Surface(shape = RoundedCornerShape(6.dp), color = if (node.isConnected) DrgGreenLight else DrgErrorBgSoft) {
                            Text(if (node.isConnected) "ONLINE" else "DISCONNECTED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (node.isConnected) DrgGreenPrimary else Color.Red, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.2f))
                    Text("📍 Endpoint: ${node.endpointHost}", fontSize = 10.sp, color = DrgInfoBlue)
                    Text("⚡ Protokol: ${node.protocol} • Status: ${node.syncState}", fontSize = 10.sp, color = DrgTextSecondary)
                }
            }
        }
    }
}
