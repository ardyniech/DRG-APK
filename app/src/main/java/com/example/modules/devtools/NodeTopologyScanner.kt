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
 * Topologi node (status konseptual/konfigurasi, bukan probe koneksi live end-to-end).
 * isConnected di sini = aktif/tersedia secara konsep lokal, bukan jaminan link jaringan realtime.
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
            syncState = "Persisted (Room v14: 26 tabel aktif; event + tugas panitia + feedback)"
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
            nodeName = "P2P/Mesh Radio Node (Konseptual)",
            nodeType = "Offline Ad-Hoc Intercom (belum end-to-end)",
            endpointHost = "Konseptual: multicast/WiFi Direct (belum aktif)",
            protocol = "Rencana: UDP multicast / WebRTC (belum terimplementasi)",
            isConnected = false,
            activeConnectionsCount = 0,
            syncState = "Kerangka lokal/antrean ada; signaling/discovery & transmisi audio mesh belum fungsional lintas perangkat."
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
