# P2P Serverless Architecture — DRG Driver

> **Version:** 1.0 — Draft  
> **Date:** 2026-09-13  
> **Status:** Approved for Implementation  
> **Goal:** Setiap Android device = server. Komunikasi P2P via internet, nol server tengah, nol cost.

---

## 🎯 Objective

Membangun komunikasi antar device ojol (driver) **tanpa server tengah**. Setiap device Android bertindak sebagai **self-hosted server** sekaligus client. Data jalan langsung device-ke-device via internet menggunakan **WebRTC DataChannel**. Satu-satunya "server" yang dibutuhkan adalah **Google STUN/TURN** (gratis) untuk NAT traversal.

---

## 🏗️ Arsitektur Tinggi

```
┌─────────────────────────────────────────────────────────────────┐
│                    Android Device A (Driver)                       │
│  ┌─────────────────────────────────────────────────────────────┐ │
│  │                    Self-Hosted Server                        │ │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │ │
│  │  │ WebRTC      │  │ STUN/TURN   │  │ FCM Signaling│         │ │
│  │  │ Listener    │  │ Client      │  │ (FCM SDK)   │          │ │
│  │  │ port: 8080  │  │ (free)      │  │             │          │ │
│  │  └─────────────┘  └─────────────┘  └─────────────┘          │ │
│  │                                                               │ │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │ │
│  │  │ Room DB     │  │ OkHttp      │  │ PendingSync │          │ │
│  │  │ v13 (local) │  │ HTTP Client │  │ Queue       │          │ │
│  │  └─────────────┘  └─────────────┘  └─────────────┘          │ │
│  └─────────────────────────────────────────────────────────────┘ │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           │  P2P DataChannel (STUN hole punching)
                           │  ATAS = data jalan langsung, TIDAK lewat server
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Android Device B (Driver)                       │
│  ┌─────────────────────────────────────────────────────────────┐ │
│  │                    Self-Hosted Server                        │ │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │ │
│  │  │ WebRTC      │  │ STUN/TURN   │  │ FCM Signaling│         │ │
│  │  │ Listener    │  │ Client      │  │ (FCM SDK)   │          │ │
│  │  │ port: 8080  │  │ (free)      │  │             │          │ │
│  │  └─────────────┘  └─────────────┘  └─────────────┘          │ │
│  │                                                               │ │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │ │
│  │  │ Room DB     │  │ OkHttp      │  │ PendingSync │          │ │
│  │  │ v13 (local) │  │ HTTP Client │  │ Queue       │          │ │
│  │  └─────────────┘  └─────────────┘  └─────────────┘          │ │
│  └─────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

### Alur Komunikasi:

1. **Device A** mendengarkan port 8080 (self-hosted server)
2. **Device B** ingin kirim data → minta IP Device A via **FCM signaling**
3. **FCM** (gratis) meneruskan IP & port Device A ke Device B
4. **Device B** melakukan **STUN hole punching** ke Device A
5. **P2P DataChannel** terbuka → data jalan **langsung** device-ke-device
6. Jika P2P gagal → data disimpan di **PendingSyncQueue**, retry saat koneksi balik

---

## 📦 Module Structure: `core/p2p/`

```
app/src/main/java/com/example/core/p2p/
├── P2PManager.kt                    # Inisialisasi & lifecycle WebRTC
├── P2PConfig.kt                     # STUN/TURN server configuration
├── P2PSignalingManager.kt           # FCM-based signaling exchange
├── P2PConnection.kt                 # WebRTC PeerConnection management
├── P2PDataChannel.kt                # DataChannel send/receive wrapper
├── P2PDiscovery.kt                  # Peer discovery via FCM
├── P2PSelfHostServer.kt             # Self-hosted server on port 8080
├── P2PNetworkMonitor.kt             # Network status & auto-reconnect
├── P2PFallbackManager.kt            # PendingSyncQueue fallback logic
└── P2PConstants.kt                  # Constants, ports, timeouts
```

### Dependency yang Ditambahkan (`gradle/libs.versions.toml`)

```toml
[versions]
webrtc = "1.0.36670"
ktor-client = "2.3.9"

[libraries]
webrtc = { group = "org.webrtc", name = "google-webrtc", version.ref = "webrtc" }
ktor-client-core = { group = "io.ktor", name = "ktor-client-core", version.ref = "ktor-client" }
ktor-client-cio = { group = "io.ktor", name = "ktor-client-cio", version.ref = "ktor-client" }
ktor-client-websockets = { group = "io.ktor", name = "ktor-client-websockets", version.ref = "ktor-client" }
```

```kotlin
// app/build.gradle.kts (tambahkan)
dependencies {
    implementation(libs.webrtc)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.websockets)
}
```

---

## 🔧 STUN/TURN Configuration

### Public STUN/TURN Server (Gratis, Google-hosted)

```kotlin
// P2PConfig.kt
object P2PConfig {
    // Google Public STUN Server — gratis, tidak perlu akun
    val STUN_SERVER = PeerConnection.IceServer.builder(
        "stun:stun.l.google.com:19302"
    ).createIceServer()

    // Google TURN Relay — jika STUN gagal (CGNAT)
    val TURN_SERVER = PeerConnection.IceServer.builder(
        "turn:stun.l.google.com:19302?transport=udp"
    ).createIceServer()

    // Self-host server port (setiap device mendengarkan)
    const val SELF_HOST_PORT = 8080

    // STUN connection timeout (ms)
    const val STUN_TIMEOUT_MS = 5000L

    // P2P connection retry interval (ms)
    const val RETRY_INTERVAL_MS = 10000L

    // Max pending sync queue size
    const val MAX_PENDING_QUEUE_SIZE = 500

    // Signaling via FCM topic
    const val FCM_P2P_TOPIC = "drg_p2p_discovery"
}
```

### Kenapa STUN/TURN Gratis?

Google menyediakan **public STUN/TURN server** tanpa registrasi:
- `stun:stun.l.google.com:19302` — bantu device tau IP public-nya
- `turn:stun.l.google.com:19302` — relay jika koneksi langsung gagal
- **Biaya: Rp0** — Google hosting untuk developer

---

## 📡 Signaling Mechanism (via FCM — Gratuis)

### Masalah: Device A harus tau IP Device B

Di internet, device tidak bisa langsung "nanya" ke device lain. Butuh mekanisme **signaling** untuk bertukar info koneksi.

### Solusi: FCM (Firebase Cloud Messaging) — Free Tier

```
Device A                                     FCM                                     Device B
   │                                           │                                           │
   │── 1. Daftarkan FCM token ke local DB ────>│                                           │
   │                                           │── 2. Kirim signaling via FCM topic ──────>│
   │                                           │    (topic: "drg_p2p_discovery")           │
   │                                           │                                           │
   │<──────── 3. FCM kirim notifikasi ────────  │                                           │
   │    (berisi IP & port Device B)            │                                           │
   │                                           │                                           │
   │── 4. STUN hole punching ke Device B ────>  │                                           │
   │                                           │                                           │
   │<══════════════════════════════════════════>│                                           │
   │         5. P2P DataChannel jalan!          │                                           │
```

### Implementasi:

```kotlin
// P2PSignalingManager.kt
class P2PSignalingManager(
    private val context: Context,
    private val db: AppDatabase
) {
    private val messaging = FirebaseMessaging.getInstance()
    private val functions = Firebase.functions

    // Daftarkan device ke FCM topic agar device lain bisa kirim signaling
    suspend fun joinDiscoveryTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(P2PConfig.FCM_P2P_TOPIC)
            .await()
    }

    // Kirim signaling info ke semua device di topic
    suspend fun broadcastPeerInfo(myIP: String, myPort: Int) {
        val signalData = mapOf(
            "senderId" to currentDriverId,
            "ip" to myIP,
            "port" to myPort,
            "timestamp" to System.currentTimeMillis()
        )

        // Kirim via FCM topic (gratis)
        messaging.send(Message.Builder()
            .setTopic(P2PConfig.FCM_P2P_TOPIC)
            .setData(signalData)
            .build()
        ).await()
    }

    // Terima signaling dari device lain
    fun onMessageReceived(message: RemoteMessage) {
        val senderIP = message.data["ip"] ?: return
        val senderPort = message.data["port"]?.toInt() ?: return
        val senderId = message.data["senderId"] ?: return

        // Ini bukan "server" yang handle data — ini cuma tukar IP info
        // Data P2P jalan langsung setelah ini
        P2PConnection.connectToPeer(senderIP, senderPort)
    }
}
```

### Kenapa Bukan Server Signaling Lain?

FCM **sudah gratis** dan **sudah ada di proyek** (ditandai di `build.gradle.kts`). Tidak perlu server tambahan. FCM cuma bertukar metadata (IP & port), **bukan data aktual**. Data aktual jalan **langsung P2P**.

---

## 🔗 P2P Connection Flow

### Langkah demi Langkah:

```
1. Inisialisasi WebRTC
   │
   ├── Buat PeerConnectionFactory
   ├── Konfigurasi STUN/TURN server
   └── Buat DataChannel
        │
2. Self-Host Server (listen port 8080)
   │
   ├── Buka socket listener
   ├── Tunggu incoming connection
   └── Setiap koneksi masuk → terima DataChannel
        │
3. Device Lain Ingin Connect
   │
   ├── Minta IP Device target via FCM signaling
   ├── Terima IP & port target
   ├── Buat outgoing PeerConnection
   │
4. STUN Hole Punching
   │
   ├── Kirim STUN binding request ke Google STUN
   │   → Dapatkan public IP:port
   │
   ├── Kirim connect request ke target IP:port
   │
   ├── Target terima → balas
   │
   └── NAT mapping terbuka → P2P jalan!
        │
5. DataChannel Aktif
   │
   ├── Kirim data langsung device-ke-device
   ├── Tidak ada server tengah yang baca data
   └── Fallback jika gagal → PendingSyncQueue
```

### Code Flow:

```kotlin
// P2PManager.kt
class P2PManager(private val context: Context) {

    private var peerConnectionFactory: PeerConnectionFactory? = null
    private var peerConnection: PeerConnection? = null
    private var dataChannel: DataChannel? = null

    fun initialize() {
        val options = PeerConnectionFactory.InitializationOptions
            .builder(context)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)

        peerConnectionFactory = PeerConnectionFactory.builder()
            .setOptions(options)
            .createPeerConnectionFactory()
    }

    // Self-host: buka listener
    fun startSelfHost() {
        val config = PeerConnection.Configuration(
            listOf(P2PConfig.STUN_SERVER, P2PConfig.TURN_SERVER)
        )

        peerConnection = peerConnectionFactory?.createPeerConnection(
            config,
            PeerObserver()
        )

        // Buat DataChannel — ini tempat data jalan P2P
        dataChannel = peerConnection?.createDataChannel(
            "drg_driver_channel",
            DataChannel.Init()
        )
    }

    // Connect ke peer lain
    fun connectToPeer(remoteIP: String, remotePort: Int) {
        val config = PeerConnection.Configuration(
            listOf(P2PConfig.STUN_SERVER, P2PConfig.TURN_SERVER)
        )

        peerConnection = peerConnectionFactory?.createPeerConnection(
            config,
            PeerObserver()
        )

        // Setup ICE candidate listener untuk tukar IP info
        peerConnection?.addIceCandidate(
            IceCandidate(
                sdpMid = "data",
                sdpMLineIndex = 0,
                candidate = remoteIP to remotePort
            )
        )
    }

    // Kirim data P2P langsung
    fun sendData(message: String) {
        val buffer = DataChannel.Buffer(
            ByteBuffer.wrap(message.toByteArray()),
            false
        )
        dataChannel?.send(buffer)
    }

    // Terima data P2P
    inner class PeerObserver : PeerConnection.Observer {
        override fun onDataChannel(channel: DataChannel) {
            channel.registerObserver(object : DataChannel.Observer {
                override fun onMessage(buffer: DataChannel.Buffer) {
                    val message = String(buffer.data.array())
                    // Simpan ke Room DB lokal
                    db.p2pDao().insert(P2PMessage(
                        senderId = "peer",
                        message = message,
                        timestamp = System.currentTimeMillis()
                    ))
                }
            })
        }
    }
}
```

---

## 🔄 Fallback: PendingSyncQueue

### Jika P2P Gagal (mati internet, CGNAT block, dsb):

```kotlin
// P2PFallbackManager.kt
class P2PFallbackManager(
    private val db: AppDatabase,
    private val syncQueueDao: SyncQueueDao
) {
    // Kirim data: coba P2P dulu, kalau gagal simpan lokal
    suspend fun sendWithFallback(targetId: String, message: String) {
        val connected = tryP2PConnection(targetId)
        
        if (connected) {
            // P2P jalan → kirim langsung
            P2PManager.sendData(message)
        } else {
            // P2P gagal → simpan ke PendingSyncQueue
            syncQueueDao.insertSyncItem(
                PendingSyncEntity(
                    id = UUID.randomUUID().toString(),
                    entityType = "P2P_ALERT",
                    payloadJson = message,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    // Auto-retry saat koneksi balik
    fun startAutoRetry() {
        // Monitor network status
        // Setiap kali internet menyala
        // Ambil semua pending messages dari Queue
        // Coba kirim lagi via P2P
    }
}
```

---

## 📊 Data Flow: Alert SOS via P2P

```
Driver A (SOS Trigger)
    │
    ├── 1. Generate Alert → simpan di Room DB lokal
    │
    ├── 2. Kirim via P2P:
    │     ├── FCM signaling → dapat IP Driver B
    │     ├── STUN hole punching → P2P connect
    │     └── DataChannel → kirim alert langsung
    │
    ├── 3. Jika P2P gagal:
    │     └── Simpan di PendingSyncQueue → retry nanti
    │
    Driver B (Alert Received)
    │
    ├── 1. Terima via P2P DataChannel
    ├── 2. Simpan ke Room DB lokal
    ├── 3. Tampilkan notification
    └── 4. User bisa balas → kirim balik via P2P
```

---

## 🛡️ Security Considerations

| Aspek | Pendekatan |
|---|---|
| **Data Encryption** | WebRTC menggunakan **DTLS** secara default — semua data P2P terenkripsi |
| **Authentication** | Setiap device punya FCM token unik — hanya device terdaftar yang bisa kirim signaling |
| **Spam Prevention** | Rate limit per device — maks 10 signaling request/menit |
| **Data Integrity** | WebRTC DataChannel menggunakan **SCTP** — pesan tidak bisa di-ubah di transit |
| **Privacy** | Data **tidak lewat server tengah** — bahkan Google STUN server juga nggak baca isi pesan |

---

## 📱 Android Permissions

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<!-- Untuk STUN hole punching di beberapa device -->
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
```

---

## 🧪 Testing Strategy

### Test 1: STUN Connection (WiFi)
```kotlin
@Test
fun testSTUNConnectViaWiFi() {
    // 2 device di WiFi yang sama
    // Device A: startSelfHost()
    // Device B: connectToPeer(A.IP, 8080)
    // Assert: P2P DataChannel aktif
}
```

### Test 2: P2P Data Transfer
```kotlin
@Test
fun testP2PDataTransfer() {
    // Device A kirim "SOS Alert" via DataChannel
    // Device B terima pesan
    // Assert: pesan diterima utuh, tanpa server tengah
}
```

### Test 3: Fallback PendingSyncQueue
```kotlin
@Test
fun testFallbackWhenP2PFails() {
    // Matikan internet Device B
    // Device A kirim alert
    // Assert: alert masuk PendingSyncQueue
    // Nyalakan internet Device B
    // Assert: alert terkirim via retry
}
```

### Test 4: CGNAT Traversal (Seluler)
```kotlin
@Test
fun testSTUNTraversalOnMobile() {
    // Device di Telkomsel/XL/Indosat
    // Test STUN hole punching
    // Assert: connection berhasil via TURN relay
}
```

---

## ⚠️ Keterbatasan & Asumsi

### Known Limitations:

| Keterbatasan | Solusi |
|---|---|
| **CGNAT (Telkomsel/XL/Indosat)** — device tidak punya public IP | Gunakan TURN relay (Google hosting gratis) |
| **Mati internet total** — P2P mustahil | Fallback PendingSyncQueue, retry saat koneksi balik |
| **Device offline** — tidak bisa connect | Device lain simpan di PendingQueue |
| **IP sering ganti** (pindah jaringan) | Reconnection logic, auto-re-register ke FCM |
| **Battery drain** — WebRTC aktif | `BatteryAwareSyncScheduler` sudah ada — reuse |

### Asumsi:

1. **Device minimal RAM 2GB** — WebRTC butuh ~200MB heap
2. **Android 8+ (API 24+)** — WebRTC supported
3. **Internet connection** — minimal 1 Mbps untuk signaling (STUN kecil)
4. **FCM working** — butuh Google Play Services (standard di Android)
5. **One-time FCM setup** — device hanya perlu subscribe topic sekali

---

## 📁 File yang Perlu Dibuat

```
docs/
├── P2P_SERVERLESS_ARCHITECTURE.md    ← file ini

app/src/main/java/com/example/core/p2p/
├── P2PManager.kt
├── P2PConfig.kt
├── P2PSignalingManager.kt
├── P2PConnection.kt
├── P2PDataChannel.kt
├── P2PDiscovery.kt
├── P2PSelfHostServer.kt
├── P2PNetworkMonitor.kt
├── P2PFallbackManager.kt
└── P2PConstants.kt

app/src/test/java/com/example/p2p/
├── P2PConnectionTest.kt
├── P2PSignalingTest.kt
├── P2PFallbackTest.kt
└── STUNTraversalTest.kt

docs/modules/
├── P2P_IMPLEMENTATION.md             ← detail implementation guide
└── P2P_TESTING.md                    ← test plan
```

---

## 📜 Dependencies Checklist

```
TAMBAH DI gradle/libs.versions.toml:
  [versions]
  webrtc = "1.0.36670"

  [libraries]
  webrtc = { group = "org.webrtc", name = "google-webrtc", version.ref = "webrtc" }

TAMBAH DI app/build.gradle.kts:
  implementation(libs.webrtc)

TIDAK PERLU TAMBAH:
  ✅ Firebase Messaging — SUDAH ada
  ✅ OkHttp — SUDAH ada
  ✅ Retrofit — SUDAH ada
  ✅ Room Database — SUDAH ada (v13)
  ✅ PendingSyncEntity + SyncQueueDao — SUDAH ada
```

---

## 📋 Implementation Phases

### Phase 1: Foundation (1 hari)
- [ ] Tambah `webrtc` dependency ke `libs.versions.toml`
- [ ] Buat `core/p2p/P2PConfig.kt` — STUN/TURN configuration
- [ ] Buat `core/p2p/P2PConstants.kt` — constants & ports
- [ ] Buat `core/p2p/P2PManager.kt` — WebRTC init & lifecycle
- [ ] Buat `core/p2p/P2PSelfHostServer.kt` — listen port 8080

### Phase 2: Signaling (1 hari)
- [ ] Buat `core/p2p/P2PSignalingManager.kt` — FCM signaling
- [ ] Buat `core/p2p/P2PDiscovery.kt` — peer discovery via FCM topic
- [ ] Integrasi dengan `DRGFirebaseMessagingService` — tambah signaling handler
- [ ] Test FCM topic subscription

### Phase 3: Connection & Data (1 hari)
- [ ] Buat `core/p2p/P2PConnection.kt` — PeerConnection management
- [ ] Buat `core/p2p/P2PDataChannel.kt` — send/receive wrapper
- [ ] Implementasi STUN hole punching
- [ ] Test P2P data transfer antar device

### Phase 4: Fallback & Monitoring (1 hari)
- [ ] Buat `core/p2p/P2PNetworkMonitor.kt` — network status
- [ ] Buat `core/p2p/P2PFallbackManager.kt` — PendingSyncQueue fallback
- [ ] Integrasi dengan `BackgroundSyncEngine` — reuse sync logic
- [ ] Test fallback scenario (mati internet, reconnect)

### Phase 5: Testing & Polish (1 hari)
- [ ] Tulis test cases (STUN, P2P, fallback)
- [ ] Test di WiFi & seluler (Telkomsel/XL)
- [ ] Battery drain test
- [ ] Optimize connection logic
- [ ] Final documentation

---

## 🔗 Related Documents

- [DATABASE_MIGRATIONS.md](DATABASE_MIGRATIONS.md) — Room DB v13 migrations
- [AGENTS.md](AGENTS.md) — Operational SOP v3.0
- [modules/README.md](modules/README.md) — Module documentation
- [SECURITY.md](SECURITY.md) — Security guidelines

---

## ✅ Approval

| Role | Status | Date |
|---|---|---|
| **Architect** | ✅ Approved | 2026-09-13 |
| **Lead Developer** | ⬜ Pending | |
| **QA Lead** | ⬜ Pending | |

---

*Document ini adalah landasan sebelum branch `p2p-serverless` dibuat dan implementasi dimulai.*
