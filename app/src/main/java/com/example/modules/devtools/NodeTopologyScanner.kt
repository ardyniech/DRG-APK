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

/**
 * Topologi kluster 5 node — jujur per audit_minus.py (2026-11-09).
 * Cloud node tidak boleh klaim Synced sebelum Firestore di-enable.
 */
object NodeTopologyScanner {
    @Suppress("UNUSED_PARAMETER")
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
            isConnected = false,
            activeConnectionsCount = 0,
            syncState = "Belum di-enable: firestore off + google-services.json hilang (BLOCKER firebase)"
        ),
        TopologyNode(
            nodeId = "NODE-AI-01",
            nodeName = "Gemini AI Logic Gateway Node",
            nodeType = "Edge Inference Microservice",
            endpointHost = "generativelanguage.googleapis.com:443",
            protocol = "HTTPS REST / gRPC Streaming",
            isConnected = false,
            activeConnectionsCount = 0,
            syncState = "Placeholder lokal: AiAssistantKnowledge (API key dummy)"
        ),
        TopologyNode(
            nodeId = "NODE-MESH-01",
            nodeName = "P2P Posko Local Mesh Radio Node",
            nodeType = "Offline Ad-Hoc Intercom Node",
            endpointHost = "224.0.0.1:multicast_udp / WiFi Direct",
            protocol = "UDP Multicast & WebRTC DataChannel",
            isConnected = true,
            activeConnectionsCount = 0,
            syncState = "Terbatas 1 subnet WiFi posko (DEFECT-02/09, P2P log-only)"
        ),
        TopologyNode(
            nodeId = "NODE-AUTH-01",
            nodeName = "Google Play Integrity Attestation Node",
            nodeType = "Device Fingerprint & Tamper Sentinel",
            endpointHost = "play.googleapis.com/integrity",
            protocol = "HTTPS Play Core IPC",
            isConnected = false,
            activeConnectionsCount = 0,
            syncState = "Belum di-enable: Play Integrity belum diimplementasikan"
        )
    )

    fun countConnectedNodes(isOnline: Boolean): Int = getClusterNodes(isOnline).count { it.isConnected }
    fun countTotalConnections(isOnline: Boolean): Int = getClusterNodes(isOnline).sumOf { it.activeConnectionsCount }
}
