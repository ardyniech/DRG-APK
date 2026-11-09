# DRG DevTools — Dedicated Engineering & Diagnostics Manual (v5.0)

> **MANDATE:** Dilarang keras memalsukan skor (No Fake 100%) dan **DILARANG MENGGUNAKAN MOCKING DATA** di DevTools. Seluruh telemetri, latensi, penggunaan memori, deteksi dead button, audit keamanan, topologi node, cacat produksi, dan saran optimasi device di-probe secara **otomatis (auto-detect)** langsung dari runtime Android riil.

---

## 1. TUJUAN & FILOSOFI OBSERVABILITAS ENTERPRISE
DevTools DRG (Driver Riang Gembira) diciptakan sebagai **Pusat Kendali Observabilitas, Audit Keamanan, & Diagnosis Kritis (Pre-Failure Watchdog)** untuk AI Developer dan Human Engineer:
1. **Root Cause Analysis (RCA) Mendalam:** Setiap anomali langsung menyertakan file sumber (`sourceFile`), nama method (`sourceFunction`), penjelasan teknis akar masalah (`rootCauseTechnical`), dan langkah perbaikan solutif (`actionableFix`).
2. **Pendeteksian Gejala Awal (Pre-Failure Symptom Early Warning):** Mampu mengenali gejala performa, saturasi memori, atau timeout sebelum pengguna atau developer mengalami crash/freeze di jalanan.
3. **Audit Keamanan Ketat (Strict Security Audit):** Deteksi root/su binary, enforce TLS 1.3 & disable cleartext traffic, proteksi Android Keystore PIN, SQLite App-Private isolation, dan kerahasiaan koordinat SOS.
4. **Topologi Jaringan & Kluster Multi-Node:** Memantau 5 node distributed system (Local SQLite, Firestore Cloud Cluster, Gemini AI Gateway, P2P WiFi Direct Mesh, Google Play Integrity).
5. **Transparansi Cacat Produksi & Konsep:** Pengungkapan jujur atas kelemahan arsitektural (misal: role validation client-side, P2P tanpa STUN/TURN, fallback SMS butuh pulsa operator).
6. **Device Android Advisor:** Mengevaluasi kondisi HP driver (RAM, Suhu Baterai, Battery Optimization Whitelist) dan memberikan rekomendasi optimasi praktis di lapangan.
7. **Zero Mocking Telemetry:** Latensi diuji langsung menggunakan socket TCP/SSL handshake (`RealtimeNetworkProbe`), memori diinspeksi dari OS `ActivityManager` dan JVM `Runtime` (`RealtimeMemoryProbe`).
8. **Deteksi Tombol Mati & Fitur Dangkal:** Menangkap tombol tanpa backend receiver dan komponen UI yang belum terhubung ke Room database.
9. **Matriks Kematangan Modul 4 Tingkat:** Mengklasifikasikan modul secara jujur: *Benih*, *Anak*, *Mature*, *Lulus*.
10. **Skor Penalti Ketat:** Memotong skor proyek secara berlipat agar tidak ada klaim sukses palsu.

---

## 2. AKSES KEAMANAN (SUPER ADMIN ONLY)
- **Role Restriction:** Fitur DevTools hanya dapat dibuka oleh akun dengan peran `MemberRole.SUPER_ADMIN` (Founding Ketua / System Controller).
- **Access Point:** Tombol **"🛠️ Dev Tools AI & Super Admin Monitor"** di layar `ProfileScreen.kt` (dibawah KTA Digital).
- **Safety Gate:** Akun non-Super Admin (Anggota, Bendahara, Satgas) otomatis diblokir dengan kartu *Access Denied* demi keamanan kredensial dan telemetri sistem.

---

## 3. 12 TAB DIAGNOSTIK DEVTOOLS TERINTEGRASI

| Tab # | Nama Tab | File Komponen | Engine / Probe | Fungsi Utama |
|---|---|---|---|---|
| 1 | **RCA** | `DevRootCauseTab.kt` | `EarlySymptomDetector.kt` | Root Cause Analysis: file sumber, fungsi, akar masalah, & kode solusi. |
| 2 | **Security** | `DevSecurityAuditTab.kt` | `SecurityAuditEngine.kt` | Audit 5 vektor: Root/SU, TLS/Cleartext, Keystore, Sandbox SQLite, SOS Privacy. |
| 3 | **Nodes** | `DevNodeTopologyTab.kt` | `NodeTopologyScanner.kt` | Status 5 distributed node kluster & protokol komunikasi. |
| 4 | **Defects** | `DevDefectsTab.kt` | `ProductionDefectScanner.kt` | Transparansi cacat produksi, konsep arsitektur rapuh, & roadmap perbaikan. |
| 5 | **Device** | `DevDeviceAdvisorTab.kt` | `DeviceAdvisorEngine.kt` | Evaluasi HP driver (RAM, Baterai, Suhu, Whitelist) & saran lapangan. |
| 6 | **Tech Stack** | `DevTechStackTab.kt` | Runtime reflection & Git | Stack library, toolchain Kotlin/Compose/Room, & Git commit tracking. |
| 7 | **Maturity** | `DevMaturityTab.kt` | `FeatureMaturity.kt` | 4 Tingkat kematangan: Benih, Anak, Mature, Lulus. |
| 8 | **Bug Hunter** | `DevBugHunterTab.kt` | `DeadButtonShallowScanner.kt` | Deteksi tombol mati, form dangkal, dan penalti bobot. |
| 9 | **Progress** | `DevProgressTab.kt` | `DevProgressTab.kt` | Roadmap pengerjaan fitur DRG & backlog yang belum tuntas. |
| 10 | **Memory** | `DevMemoryTab.kt` | `RealtimeMemoryProbe.kt` | Heap JVM, total RAM OS, available RAM, dan status low memory device. |
| 11 | **Server** | `DevServerTab.kt` | `RealtimeNetworkProbe.kt` | Live socket ping ke Firestore, Gemini, & posko nodes (zero mock). |
| 12 | **Repo** | `DevRepoHealthTab.kt` | Static architecture audit | Audit 415+ file sumber terhadap batas baris < 125 baris per file. |

---

## 4. AUDIT KEAMANAN KETAT (SECURITY AUDIT ENGINE)

Engine: `SecurityAuditEngine.kt`
Menguji 5 parameter keamanan langsung pada device Android:
1. **Root / Jailbreak Binary Check:**
   - Memeriksa keberadaan file binary `su`, `busybox`, `magisk` pada direktori `/system/bin`, `/system/xbin`, `/sbin`, `/vendor/bin`, dll.
2. **Network Security Config (Cleartext Traffic Policy):**
   - Memastikan `usesCleartextTraffic` dimatikan (`false`) dan TLS 1.3 enforced via `network_security_config.xml`.
3. **Android Keystore Hardware-Backed PIN:**
   - Memeriksa alias `DRG_SECURITY_KEY` pada provider `AndroidKeyStore` untuk enkripsi data sensitif (KTA, wallet, role audit).
4. **App-Private SQLite Sandbox Isolation:**
   - Memastikan database `drg_master_v13.db` tersimpan di sandbox private internal `/data/user/0/com.example/databases` tanpa permission `WorldReadable`.
5. **SOS Emergency Telemetry Confidentiality:**
   - Memverifikasi koordinat GPS latitude/longitude tidak pernah diekspos ke Logcat atau shared storage publik.

---

## 5. TOPOLOGI KLUSTER MULTI-NODE DISTRIBUSI

Engine: `NodeTopologyScanner.kt`
Melacak konektivitas 5 node jaringan yang membentuk ekosistem DRG:
1. **Local SQLite Node (Room v13):**
   - Protocol: `sqlite3 / Room Flow`
   - Role: Local-first persistence (23 entitas). Selalu aktif & mandiri saat offline.
2. **Firebase Firestore Master Cluster:**
   - Protocol: `gRPC over TLS (port 443)`
   - Role: Cloud synchronizer untuk profil anggota, status posko, dan feed komunitas.
3. **Gemini AI Logic Gateway:**
   - Protocol: `HTTPS / TLS 1.3 REST`
   - Role: AI Assistant pengendali rute cerdas dan summary keselamatan lapangan.
4. **P2P WiFi Direct Mesh Network:**
   - Protocol: `WiFi P2P / NSD (mDNS UDP)`
   - Role: Komunikasi konvoi lokal, audio walkie-talkie tanpa internet saat di pelosok.
5. **Google Play Integrity & Safety Attestation:**
   - Protocol: `Google Play Services RPC`
   - Role: Attestasi integritas APK, pencegahan anti-tampering dan kloning aplikasi.

---

## 6. CACAT PRODUKSI & KONSEP YANG CACAT (HONEST DEFECT DISCLOSURE)

Engine: `ProductionDefectScanner.kt`
DRG menerapkan kejujuran teknis tanpa manipulasi status:
1. **Konsep Cacat #1 (CRITICAL): Client-Side Role Validation:**
   - *Masalah:* Validasi role admin saat ini dilakukan secara lokal di perangkat via Room `RoleManager`. Pengguna dengan akses root dapat memodifikasi SQLite secara lokal.
   - *Rencana Solusi:* Validasi role wajib diverifikasi melalui Cloud Firestore Security Rules dan Firebase Custom Claims / Ephemeral JWT Token.
2. **Konsep Cacat #2 (WARNING): P2P Mesh Terbatas Satu Subnet:**
   - *Masalah:* Fitur P2P Walkie Talkie dan Mesh Posko hanya berfungsi saat sesama driver berada di SSID WiFi Direct yang sama. Tidak bisa tembus NAT publik.
   - *Rencana Solusi:* Implementasi STUN/TURN public relay server (misal: LiveKit atau coturn) untuk bridging WebSockets publik.
3. **Cacat Produksi #1 (WARNING): Fallback SMS Butuh Pulsa Operator:**
   - *Masalah:* `EmergencySmsFallbackHelper` mengirimkan SMS darurat otomatis saat sinyal internet drop. Jika pulsa SIM driver habis (Rp 0), SMS gagal terkirim.
   - *Rencana Solusi:* Berikan peringatan visual status pulsa minimal Rp 1.000 atau sediakan integrasi API gateway toll-free SMS berbasis komunitas.
4. **Cacat Produksi #2 (MINOR): GPS Polling 1 Detik Boros Baterai:**
   - *Masalah:* Live driver map memperbarui lokasi setiap 1.000 ms. Menyebabkan konsumsi baterai meningkat signifikan saat ngojek seharian.
   - *Rencana Solusi:* Gunakan Adaptive Location Request (polling 1 detik saat bergerak >20 km/jam, polling 10-30 detik saat berhenti di posko).

---

## 7. DEVICE ANDROID ADVISOR UNTUK PENGEMUDI

Engine: `DeviceAdvisorEngine.kt`
Menganalisis kondisi hardware HP driver dan memberikan saran taktis:
1. **Battery Optimization Whitelist (`PowerManager.isIgnoringBatteryOptimizations`):**
   - Jika belum whitelisted, background SOS listener berpotensi di-kill oleh vendor agresif (Xiaomi MIUI, Oppo ColorOS, Vivo FuntouchOS, Samsung OneUI). DevTools langsung memberi instruksi membuka Settings battery.
2. **Suhu Baterai (`BatteryManager.EXTRA_TEMPERATURE`):**
   - Jika suhu baterai melebihi 40°C, driver disarankan melepas casing tebal atau memindahkan posisi holder HP dari sorotan terik matahari langsung saat memasang charger motor.
3. **Kapasitas RAM (`ActivityManager.MemoryInfo`):**
   - Menghitung sisa RAM bebas. Jika tersisa < 500 MB, menyarankan driver menutup aplikasi berat di latar belakang agar peta tidak freeze saat menerima orderan.
4. **Status Baterai Rendah (< 20%):**
   - Memberikan notifikasi taktis untuk segera menghubungkan powerbank atau charger motor agar sistem SOS darurat tetap aktif.

---

## 8. STACK TEKNOLOGI & GIT CLI AUDIT

Engine: `DevTechStackTab.kt`
- **Bahasa:** Kotlin 2.2.10
- **UI Framework:** Jetpack Compose 1.7+ (Material 3)
- **Database Lokal:** Android Room v13 (23 entities, Room KSP Compiler)
- **Asynchronous:** Kotlin Coroutines & Flow UDF
- **Image Loader:** Coil 3.0.4
- **Testing Framework:** Robolectric 4.14.1, JUnit 4, Kotlinx Coroutines Test
- **Security:** AndroidX Security Crypto & Network Security Config TLS 1.3
- **Build System:** Gradle Kotlin DSL (AGP 8.8+)

---

## 9. COMMANDS & VERIFIKASI LOKAL

```bash
# 1. Audit kepatuhan batas baris kode (< 125 baris per file)
python3 .github/scripts/check_file_length.py

# 2. Jalankan test suite Robolectric
gradle :app:testDebugUnitTest

# 3. Jalankan test khusus DevTools
gradle :app:testDebugUnitTest --tests "com.example.DevToolsMaturityScoringTest"

# 4. Verifikasi kompilasi aplikasi
gradle assembleDebug
```
