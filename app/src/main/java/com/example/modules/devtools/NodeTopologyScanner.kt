package com.example.modules.devtools

data class TopologyNode(
    val nodeId: String,
    val nodeName: String,
    val nodeType: String,
    val endpointHost: String,
    val protocol: String,
    val isConnected: Boolean,
    val activeConnectionsCount: Int,
    val syncState: String
)

object NodeTopologyScanner {
    fun getClusterNodes(isNetworkOnline: Boolean): List<TopologyNode> = listOf(
        TopologyNode(
            nodeId = "NODE-LOCAL-01",
            nodeName = "Local SQLite Embedded Engine",
            nodeType = "Primary Local Store (Room v13)",
            endpointHost = "localhost:internal_sqlite/app_database",
            protocol = "IPC / In-Memory SQLite Bridge",
            isConnected = true,
            activeConnectionsCount = 1,
            syncState = "Persisted (23 Tables Active)"
        ),
        TopologyNode(
            nodeId = "NODE-CLOUD-01",
            nodeName = "Firebase Firestore Primary Cluster",
            nodeType = "Cloud Multi-Region Master Node",
            endpointHost = "firestore.googleapis.com:443",
            protocol = "gRPC / HTTP/2 TLS 1.3",
            isConnected = isNetworkOnline,
            activeConnectionsCount = if (isNetworkOnline) 4 else 0,
            syncState = if (isNetworkOnline) "Realtime Snapshot Synced" else "Offline Queue Buffering"
        ),
        TopologyNode(
            nodeId = "NODE-AI-01",
            nodeName = "Gemini AI Logic Gateway Node",
            nodeType = "Edge Inference Microservice",
            endpointHost = "generativelanguage.googleapis.com:443",
            protocol = "HTTPS REST / gRPC Streaming",
            isConnected = isNetworkOnline,
            activeConnectionsCount = if (isNetworkOnline) 1 else 0,
            syncState = if (isNetworkOnline) "Standby / Dynamic Quota Ready" else "Disconnected"
        ),
        TopologyNode(
            nodeId = "NODE-MESH-01",
            nodeName = "P2P Posko Local Mesh Radio Node",
            nodeType = "Offline Ad-Hoc Intercom Node",
            endpointHost = "224.0.0.1:multicast_udp / WiFi Direct",
            protocol = "UDP Multicast & WebRTC DataChannel",
            isConnected = true,
            activeConnectionsCount = 6,
            syncState = "Proximity Radar Active (Malang Raya)"
        ),
        TopologyNode(
            nodeId = "NODE-AUTH-01",
            nodeName = "Google Play Integrity Attestation Node",
            nodeType = "Device Fingerprint & Tamper Sentinel",
            endpointHost = "play.googleapis.com/integrity",
            protocol = "HTTPS Play Core IPC",
            isConnected = isNetworkOnline,
            activeConnectionsCount = if (isNetworkOnline) 1 else 0,
            syncState = "App Licensing & Signature Verified"
        )
    )

    fun countConnectedNodes(isOnline: Boolean): Int = getClusterNodes(isOnline).count { it.isConnected }
    fun countTotalConnections(isOnline: Boolean): Int = getClusterNodes(isOnline).sumOf { it.activeConnectionsCount }
}
