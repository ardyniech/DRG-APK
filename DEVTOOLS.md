# DRG DevTools — Dedicated Engineering & Diagnostics Manual (v7.0)

> **MANDATE:** Dilarang keras memalsukan skor (No Fake 100%) dan **DILARANG MENGGUNAKAN MOCKING DATA** di DevTools. Seluruh telemetri, latensi, penggunaan memori, deteksi dead button, audit keamanan, topologi node, cacat produksi, model pohon fase pengembangan, audit UI/UX berkendara, dan status CI/CD di-probe secara **otomatis (auto-detect)** langsung dari runtime Android riil.
>
> **SINGLE SOURCE OF TRUTH:** Hasil audit (`audit_minus.py`) + progres (`generate_about_manifest.py`) digabung menjadi `docs/ABOUT.md` + `app/src/main/assets/about_manifest.json`. File ini adalah *cara pakai* DevTools — **bukan sumber angka**. AI dev wajib baca SSOT di `docs/ABOUT.md` (lihat `AGENTS.md` §18).

---

## 1. TUJUAN & FILOSOFI OBSERVABILITAS ENTERPRISE
DevTools DRG (Driver Riang Gembira) diciptakan sebagai **Pusat Kendali Observabilitas, Audit Keamanan, & Diagnosis Kritis (Pre-Failure Watchdog)** untuk AI Developer dan Human Engineer:
1. **Model Pohon Fase Pengembangan (Hierarchical Phase & Sub-Feature Tree):** Tidak ada klaim lulus prematur! Jika dalam suatu sub-fase terdapat cacat atau tombol mati, fase tersebut ditandai **TERKENDALA CACAT**, lengkap dengan rincian *Apa Fungsinya* dan *Kenapa Wajib Ada* (*"what & why"*).
2. **Audit UI/UX Real Experience Ojol:** Mengevaluasi ergonomi berkendara (target sentuh min 48x48dp, kontras terik matahari siang hari, beban kognitif one-thumb, getaran taktil haptic, dan jaminan zero dead-end saat offline).
3. **Observabilitas Real GitHub Actions CI/CD:** Memonitor 5 tahapan pipeline build, audit batas baris (<125 lines), linting, 55 test suites Robolectric, dan retention artifact.
4. **Root Cause Analysis (RCA) Mendalam:** Setiap anomali langsung menyertakan file sumber (`sourceFile`), nama method (`sourceFunction`), penjelasan teknis akar masalah (`rootCauseTechnical`), dan langkah perbaikan solutif (`actionableFix`).
5. **Audit Keamanan Ketat (Strict Security Audit):** Deteksi root/su binary, enforce TLS 1.3 & disable cleartext traffic, proteksi Android Keystore PIN, SQLite App-Private isolation, dan kerahasiaan koordinat SOS.
6. **Topologi Jaringan & Kluster Multi-Node:** Memantau 5 node distributed system (Local SQLite, Firestore Cloud Cluster, Gemini AI Gateway, P2P WiFi Direct Mesh, Google Play Integrity).
7. **Transparansi Cacat Produksi & Konsep:** Pengungkapan jujur atas kelemahan arsitektural (misal: role validation client-side, P2P tanpa STUN/TURN, fallback SMS butuh pulsa operator).
8. **Device Android Advisor:** Mengevaluasi kondisi HP driver (RAM, Suhu Baterai, Battery Optimization Whitelist) dan memberikan rekomendasi optimasi praktis di lapangan.
9. **Zero Mocking Telemetry:** Latensi diuji langsung menggunakan socket TCP/SSL handshake (`RealtimeNetworkProbe`), memori diinspeksi dari OS `ActivityManager` dan JVM `Runtime` (`RealtimeMemoryProbe`).
10. **Matriks Kematangan Modul 4 Tingkat:** Mengklasifikasikan modul secara jujur: *Benih*, *Anak*, *Mature*, *Lulus*.

---

## 2. AKSES KEAMANAN (SUPER ADMIN ONLY)
- **Role Restriction:** Fitur DevTools hanya dapat dibuka oleh akun dengan peran `MemberRole.SUPER_ADMIN` (Founding Ketua / System Controller).
- **Access Point:** Tombol **"🛠️ Dev Tools AI & Super Admin Monitor"** di layar `ProfileScreen.kt` (dibawah KTA Digital).
- **Safety Gate:** Akun non-Super Admin otomatis diblokir dengan kartu *Access Denied*.

---

## 3. 14 TAB DIAGNOSTIK DEVTOOLS TERINTEGRASI

| Tab # | Nama Tab | File Komponen | Engine / Scanner | Fungsi Utama |
|---|---|---|---|---|
| 1 | **Tree Progress** | `DevProgressTab.kt` | `PhaseTreeScanner.kt` | Model pohon fase & sub-fitur jujur, rincian apa & kenapa, larangan lulus bila ada cacat. |
| 2 | **Audit UI/UX** | `DevUiUxAuditTab.kt` | `UiUxAuditEngine.kt` | Skor ergonomi berkendara, target sentuh min 48dp, kontras terik matahari, & haptic. |
| 3 | **Git CI/CD** | `DevCiCdTab.kt` | `CiCdStatusScanner.kt` | Status riil GitHub Actions CI/CD (5 langkah build, 55 test suites, hard fail policy). |
| 4 | **RCA & Gejala** | `DevRootCauseTab.kt` | `EarlySymptomDetector.kt` | Root Cause Analysis: file sumber, fungsi, akar masalah, & kode solusi. |
| 5 | **Audit Keamanan** | `DevSecurityAuditTab.kt` | `SecurityAuditEngine.kt` | Audit 5 vektor: Root/SU, TLS/Cleartext, Keystore, Sandbox SQLite, SOS Privacy. |
| 6 | **Node Server** | `DevNodeTopologyTab.kt` | `NodeTopologyScanner.kt` | Status 5 distributed node kluster & protokol komunikasi. |
| 7 | **Cacat Sistem** | `DevDefectsTab.kt` | `ProductionDefectScanner.kt` | Transparansi cacat produksi, konsep arsitektur rapuh, & roadmap perbaikan. |
| 8 | **Saran Device** | `DevDeviceAdvisorTab.kt` | `DeviceAdvisorEngine.kt` | Evaluasi HP driver (RAM, Baterai, Suhu, Whitelist) & saran lapangan. |
| 9 | **Maturity** | `DevMaturityTab.kt` | `FeatureMaturity.kt` | 4 Tingkat kematangan: Benih, Anak, Mature, Lulus. |
| 10 | **Bug Hunter** | `DevBugHunterTab.kt` | `DeadButtonShallowScanner.kt` | Deteksi tombol mati, form dangkal, dan penalti bobot. |
| 11 | **RAM & Memory** | `DevMemoryTab.kt` | `RealtimeMemoryProbe.kt` | Heap JVM, total RAM OS, available RAM, dan status low memory device. |
| 12 | **Server API** | `DevServerTab.kt` | `RealtimeNetworkProbe.kt` | Live socket ping ke Firestore, Gemini, & posko nodes (zero mock). |
| 13 | **Tech Stack** | `DevTechStackTab.kt` | Runtime reflection & Git | Stack library, toolchain Kotlin/Compose/Room, & Git commit tracking. |
| 14 | **Repo Health** | `DevRepoHealthTab.kt` | Static architecture audit | Audit 465 file sumber terhadap batas baris < 125 baris per file. |

---

## 4. MODEL POHON FASE PENGEMBANGAN (PHASE TREE ARCHITECTURE)

Engine: `PhaseTreeScanner.kt`, `DevPhaseCard.kt`, `DevFeatureItemCard.kt`

Prinsip Kejujuran Dev: **Jika salah satu sub-fitur memiliki cacat atau berstatus pending, maka sub-fase dan fase induknya HARAM dinyatakan Lulus!** Status langsung ditandai `TERKENDALA_CACAT`.

### Struktur 5 Fase Pengembangan DRG:
1. **Fase 1: Fondasi Arsitektur, Keamanan & RBAC (Status: TERKENDALA CACAT)**
   - Sub-Fase 1.1: Room v13 Storage & Multi-Entity (Lulus Sempurna - 23 tabel aktif).
   - Sub-Fase 1.2: Wewenang Role & Keamanan Sesi (Terkendala Cacat - Validasi role masih client-side di Room).
2. **Fase 2: Komunitas, Kas Transparan & Mitra (Status: TERKENDALA CACAT)**
   - Sub-Fase 2.1: Kas Transparan & Laporan Keuangan (Lulus Sempurna - Input kas & export text/CSV).
   - Sub-Fase 2.2: Forum Diskusi & Rating Bengkel (Terkendala Cacat - Tombol kirim dokumen asuransi belum terhubung ke upload worker).
3. **Fase 3: Hardware, Audio Intercom & Offline P2P (Status: TERKENDALA CACAT)**
   - Sub-Fase 3.1: P2P Walkie Talkie & Mesh Radar (Terkendala Cacat - Terbatas satu subnet WiFi posko).
   - Sub-Fase 3.2: Integrasi Bluetooth Intercom Helm (Terkendala Cacat - Dead button pairing GATT helm).
4. **Fase 4: Emergency SOS & Telemetri Lapangan (Status: TERKENDALA CACAT)**
   - Sub-Fase 4.1: Trigger Darurat & Sirene Taktis (Lulus Sempurna - Countdown 5s & pulsa haptic).
   - Sub-Fase 4.2: Transmisi Sinyal Darurat Lapangan (Terkendala Cacat - SMS fallback butuh pulsa & GPS polling 1s memakan baterai).
5. **Fase 5: AI Engine, Observabilitas & CI/CD (Status: DALAM PENGERJAAN)**
   - Sub-Fase 5.1: AI Assistant & Gamifikasi Lapangan (Lulus Sempurna - Gemini AI route summary & loyalty badge).
   - Sub-Fase 5.2: Observabilitas DevTools & Pipeline CI/CD (Dalam Pengerjaan - Task sync worker cloud masih pending).

Setiap sub-fitur menyertakan uraian jelas:
- **Apa Fungsinya:** Penjelasan operasional komponen.
- **Kenapa Wajib Ada:** Urgensi fungsional bagi kelangsungan driver di jalan raya.
- **Alasan Belum Lulus:** Fakta teknis mengapa belum boleh dinyatakan selesai 100%.

---

## 5. AUDIT UI/UX REAL EXPERIENCE DRIVER

Engine: `UiUxAuditEngine.kt`, `DevUiUxAuditTab.kt`

Audit mengevaluasi 6 pilar ergonomi nyata pengemudi:
1. **Touch Target Standard (Min 48x48dp - Skor 92%):** Seluruh elemen aksi interaktif memenuhi batas M3. FAB SOS darurat berukuran jumbo (96dp) ramah sarung tangan tebal.
2. **Kontras Terik Matahari Siang Hari (Skor 88%):** Warna dasar terang dengan teks hitam pekat `DrgTextPrimary` (#0F172A) menjamin keterbacaan tinggi saat berkendara di siang terik.
3. **Beban Kognitif Berkendara (One-Thumb Usability - Skor 80%):** Membatasi maksimal 3 tombol aksi primer per layar agar pengemudi tidak terdistraksi mencari menu saat melaju.
4. **Recomposition Smoothness & 60 FPS (Skor 78%):** Memeriksa penggunaan `derivedStateOf` pada filter kas dan navigasi agar tidak memicu micro-stutter.
5. **Cakupan Respon Haptic Taktil (Skor 90%):** Injeksi mikro-vibrasi pada countdown SOS, konfirmasi kas, dan pergantian tab navigasi agar driver merasakan konfirmasi tanpa melirik layar.
6. **Zero Dead-End Resilience Saat Offline (Skor 85%):** Layar kosong menyajikan pesan informatif dan tombol muat ulang tanpa infinite loading atau layar putih.

---

## 6. OBSERVABILITAS REAL GITHUB ACTIONS CI/CD

Engine: `CiCdStatusScanner.kt`, `DevCiCdTab.kt`
Konfigurasi: `.github/workflows/android_ci.yml`

| Langkah | Nama Tugas | Perintah CI | Waktu Est. | Kebijakan Kegagalan |
|---|---|---|---|---|
| Step 1 | **Architecture Quality Audit** | `python3 .github/scripts/check_file_length.py` | 2 detik | **HARD FAIL** (Stop bila ada file > 125 baris) |
| Step 2 | **Feature Manifest Sync** | `python3 .github/scripts/generate_about_manifest.py` | 3 detik | Auto-Commit via `github-actions[bot]` |
| Step 3 | **Android Lint Static Analysis** | `./gradlew :app:lintDebug --no-daemon` | 45 detik | Upload Lint HTML Report |
| Step 4 | **Robolectric & JVM Unit Tests** | `./gradlew :app:testDebugUnitTest --no-daemon` | 30 detik | **HARD FAIL** (Reject PR bila 1 test gagal) |
| Step 5 | **Assemble Debug APK** | `./gradlew assembleDebug --no-daemon` | 50 detik | Upload APK Artifact (14 hari) |

---

## 7. MEKANISME ADAPTASI APK BELUM DI-UPDATE (100% AKURAT PADA BUILD APAPUN)

Engine: `DynamicApkInspector.kt`, `DynamicDbIntrospector.kt`, `RuntimeCapabilityProber.kt`, `DevApkSkewCard.kt`

Masalah di Lapangan: Developer atau tester sering kali menguji aplikasi menggunakan APK lama (belum di-update ke build terbaru). Jika DevTools mengasumsikan data statis, maka laporan menjadi palsu, bias, atau bahkan crash karena tabel database / class yang belum ada di APK lawas tersebut.

Solusi Arsitektur DevTools DRG:
1. **Introspeksi PackageManager Riil:** Membaca `versionCode`, `versionName`, dan timestamp kompilasi langsung dari kernel Android via `PackageInfo`. DevTools secara otomatis menghitung *Version Skew Delta*.
2. **Introspeksi Pragma SQLite Langsung:** Menjalankan query `PRAGMA user_version` dan `SELECT name FROM sqlite_master` langsung pada database SQLite yang sedang aktif di HP pengguna. Jika APK lawas masih menjalankan Room v11 atau v12, DevTools akan melaporkan versi v11/v12 secara jujur tanpa crash!
3. **Penyelidikan Bytecode DEX via Reflection:** Menggunakan `Class.forName(...)` untuk mendeteksi modul yang benar-benar ada di dalam file APK yang sedang berjalan. Modul yang belum ter-compile ditandai `Belum Di-compile (Versi APK Lama)` tanpa memicu `ClassNotFoundException`.
4. **DevApkSkewCard Adaptif:** Banner peringatan otomatis muncul di dashboard atas (`⚡ RUNNING ON OUTDATED APK`), menyajikan fakta binary yang terpasang dan adaptasi pengujian secara transparan.

---

## 8. COMMANDS & VERIFIKASI LOKAL

```bash
# 1. Audit kepatuhan batas baris kode (< 125 baris per file)
python3 .github/scripts/check_file_length.py

# 2. Jalankan test suite Robolectric (55 test suites per 2026-11-09)
gradle :app:testDebugUnitTest

# 3. Jalankan test khusus DevTools
gradle :app:testDebugUnitTest --tests "com.example.DevTools*"

# 4. Verifikasi kompilasi aplikasi
gradle assembleDebug

# 5. Audit bawel + refresh SSOT (satukan ke docs/ABOUT.md)
python3 .github/scripts/audit_minus.py
python3 .github/scripts/generate_about_manifest.py  # SSOT: about_manifest.json + docs/ABOUT.md
python3 .github/scripts/audit_minus.py --fail-on-blocker  # CI strict (view only, minus digabung via generator)
```

---

## 9. BAWEL MINUS AUDIT — SSOT (per 2026-11-09)

Skrip `.github/scripts/audit_minus.py` adalah detektor statis paling cerewet repo. Hasilnya **bukan ditulis di sini** — digabung ke **SSOT** oleh `generate_about_manifest.py` menjadi `docs/ABOUT.md` (manusia) + `app/src/main/assets/about_manifest.json` (mesin). Baca SSOT untuk hitungan terkini; jangan copy angka ke README/DEVTOOLS (pasti basi).

| Severity | Contoh temuan | Artinya |
|---|---|---|
| BLOCKER | Fake "Tersinkronisasi ke Cloud" tanpa network call; Firestore/Auth dikomentari + `google-services.json` hilang; seed PIN 1234; P2P signaling `Log.d` saja | **BELUM multi-user** — dilarang klaim sinkron/berbagi data |
| MAJOR | Permission kurang (RECORD_AUDIO, dsb.); ICE palsu `candidate:1`; login lokal saja; `allowBackup=false` tanpa backup; NIK/SIM dummy di DEFAULT; PIN SHA-256 bukan Keystore; schema `1-4,8,9.json` hilang; TODO `DashboardHeader`; CI tanpa release | Risiko produksi / data-loss / keamanan |
| MINOR | SQLite belum SQLCipher; gradlew 644; androidTest 1 file; Detekt hilang; hex hardcode | Hygiene / debt kecil yang tetap dilaporkan |
| NOTE | Marketplace/arisan [DEFERRED] (scope kas saja) | Deferred — bukan blocker rilis awal |

**Aturan finansial (2026-11-09):** Marketplace & arisan = `[DEFERRED]` di `.github/about/features.json` + `ROADMAP.md` + SSOT. Finansial wajib rilis awal hanya **iuran kas** (Kas Transparan, phase-1).

**DevTools in-app wajib jujur:** `NodeTopologyScanner` (Firestore belum di-enable), `SecurityAuditEngine` (Keystore = Kurang Aman), `ServerOverloadMonitor` (tanpa backend = UNKNOWN), `ProductionDefectScanner` (+ `MultiUserGapScanner` DEFECT-05 s/d 12), `BuildDeploymentEngine`/`CiCdStatusScanner` (angka statis basi, label UNKNOWN bila runtime). Jangan tulis "PASSED / Synced / HEALTHY" dari hardcode — sumber kebenaran adalah **SSOT** (`docs/ABOUT.md`), bukan file ini.

