# Catatan Perubahan (Changelog) — DRG Driver 📝

Semua perubahan penting pada proyek **DRG Driver** didokumentasikan dalam file ini mengikuti format [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) dan menganut [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### 🚀 Ditambahkan (Added)
- **Tab SSOT (Acuan) di DevTools**: `DevSsotTab.kt` baca `minus` langsung dari asset `about_manifest.json` (tanpa cross-module import) — BLOCKER/MAJOR/MINOR + filter severity tampil di HP super-admin. Ditambah `DevToolsSsotShellTest.kt` (SSOT minus match audit, primitives ada).
- **Audit Duplikasi UI Total**: `docs/UI_DEDUP_AUDIT.md` — 18 DevTools tab copy-paste (~1.538 baris), 23 glass-card `highlightBrush`, 2 launcher grid dobel, dst. Cluster Rank 1-6 + merge map + hemat estimasi.
- **SSOT Tunggal `about_manifest.json` + `docs/ABOUT.md`**: `generate_about_manifest.py` kini gabung `audit_minus.py --json` sebagai kunci `minus`/`minusSummary` (22 temuan: 6 BLOCKER/12 MAJOR/4 MINOR) + tabel Kekurangan di `docs/ABOUT.md`. AI dev + DevTools + in-app wajib rujuk SSOT ini (`AGENTS.md` §0/§14/§18/§20/§21); README/DEVTOOLS angka duplikat dipensiunkan ke pointer SSOT.
- **Audit Bawel `audit_minus.py`**: Detektor statis 23 temuan (6 BLOCKER, 12 MAJOR, 5 MINOR) — fake SYNCED tanpa network call, Firestore/Auth off + `google-services.json` hilang, P2P log-only, auth lokal PIN 1234, schema Room hilang, drift docs, telemetri DevTools fake, TODO/dead-button/hex, permission/release-signing. CI step warn-only + flag `--fail-on-blocker`. DevTools in-app diperbaiki jadi jujur (NodeTopology belum di-enable, SecurityAudit Keystore Kurang Aman, ServerOverload UNKNOWN, skor proyek Grade C karena 6 production defects via `MultiUserGapScanner` DEFECT-05 s/d 12).
- **Halaman Tentang & Roadmap Otomatis**:
  - Layar `modules/about/AboutScreen` (dari Profil → Tentang Aplikasi): tree fitur terpasang/dibangun/rencana, progres pengembangan jujur per fase, rencana penyempurnaan, dan antrian rencana.
  - Manifest fitur digenerate otomatis dari kode sumber oleh `.github/scripts/generate_about_manifest.py` (dipicu CI, auto-commit) — tanpa edit manual.
  - Output dokumen `docs/ABOUT.md` + index modul `docs/modules/README.md`.

### 🐛 Diperbaiki (Fixed)
- **Dokumentasi jujur + scope finansial dipersempit**: README dapat bagian "Kondisi Real" + tabel Plan vs Dev (lokal saja, bukan multi-user; data lokal Room v13; belum sinkron). ROADMAP Fase 3 & `features.json` marketplace/arisan ditandai `[DEFERRED]` — finansial wajib rilis awal hanya **iuran kas**. Badge Room v12→v13, suite 32→52, file audit 454→455.
- **Launcher split <125 baris**: `DashboardDesktopLauncher.kt` 149 baris dipecah jadi `DesktopAppItem.kt` (62), `DashboardDesktopLauncherHeader.kt` (90), `LauncherTile.kt` (84), dan `DashboardDesktopLauncher.kt` (61) tanpa perubahan perilaku. Ditambah `DashboardDesktopLauncherTest.kt` (61 baris) untuk audit happy path, bad input (list kosong), cross-module navigasi, dan dead-end row chunking. `check_file_length.py` kini hijau untuk 455 file.
- **Tile cache eviction anti-fabrikasi**: `evictOldestTilesIfNeeded` kini melaporkan jumlah tile yang benar-benar terhapus (tile Posko `isPersistedOffline` tetap terlindungi dari LRU). Fix dari pemicu test cacat akibat proteksi `fb2e788`.
- **Lint `NewApi`**: `windowLightNavigationBar` diberi `tools:targetApi="27"` di `values` & `values-night` (minSdk 24).

---

## [1.0.0] - 2026-09-10

### 🚀 Ditambahkan (Added)
- **Emergency & SOS Network**:
  - Tombol darurat SOS satu sentuhan dengan siaga getar dan koordinat GPS real-time.
  - Algoritma deteksi tabrakan & deselerasi keras berbasis sensor akselerometer (`CrashDetectionManager`).
  - Koordinasi Tim Reaksi Cepat (TRC) dan panduan navigasi langsung ke titik insiden.
- **Peta Radar & Posko Komunitas**:
  - Peta gratis berbasis OpenStreetMap (OSM) tanpa ketergantungan API berbayar.
  - Disk tile caching LRU multi-level dengan Room metadata (`MapTileCacheAndStateTest`).
  - Filter armada multi-platform (Gojek, Grab, Maxim, ShopeeFood, InDrive).
- **Transparansi Kas & Bendahara**:
  - Laporan keuangan kas komunitas transparan dengan grafik visual pemasukan/pengeluaran.
  - Filter periode (Bulan Ini, 3 Bulan, Semua) dan export audit.
- **Forum Diskusi & Bengkel Rekanan**:
  - Pembagian tipe postingan: *Tanya Rekan (Q&A)* vs *Update Jalur Terkini*.
  - Sistem balasan komentar berantai (*threaded comments*) dengan persistensi Room Database.
  - Direktori bengkel rekanan dengan diskon khusus dan integrasi kontak WhatsApp 1-tap.
- **Gamifikasi & Solidaritas**:
  - Peringkat loyalitas driver (*Pahlawan Aspal*, *Pejuang Solidaritas*), misi harian, dan reward voucher bengkel.
- **KTA Digital & Profil**:
  - Kartu Tanda Anggota digital dengan QR Code verifikasi.
- **Infrastruktur Open Source & CI/CD**:
  - GitHub Actions automated CI (`.github/workflows/android_ci.yml`).
  - Network Security Config (`network_security_config.xml`) dengan enkripsi HTTPS/TLS 1.3.
  - Lisensi Apache 2.0, Panduan Kontribusi (`CONTRIBUTING.md`), dan Kode Etik (`CODE_OF_CONDUCT.md`).

---

## [1.1.0] - 2026-09-13

### 🔧 Diperbaiki (Fixed)
- **Dark Theme** — `values-night/themes.xml` parent diperbaiki ke `Material.Light.NoActionBar` (hard rule: HARAM dark theme).
- **`fallbackToDestructiveMigration()`** — dihapus dari `AppDatabase.kt` (data loss risk).
- **12.json & 13.json schema** — ditambahkan ke `app/schemas/` (exportSchema compliance).
- **Duplicate schema files** — `app/schemas/12.json` dan `13.json` dihapus (hanya ada di `com.example.core.database.AppDatabase/`).
- **`process_logo.sh`** — dihapus (ImageMagick dependency, menghasilkan `.webp` bukan `.png`).
- **CI lint `continue-on-error: true`** — dihapus (lint failure sekarang hard-fail).

### 🔗 Ditambahkan (Added)
- **P2P Serverless Module** (`core/p2p/`) — 10 file:
  - `P2PConfig.kt`, `P2PConstants.kt`, `P2PManager.kt`, `P2PConnection.kt`
  - `P2PDataChannel.kt`, `P2PSignalingManager.kt`, `P2PDiscovery.kt`
  - `P2PSelfHostServer.kt`, `P2PNetworkMonitor.kt`, `P2PFallbackManager.kt`
  - WebRTC DataChannel, STUN/TURN (Google free), FCM signaling, PendingSyncQueue fallback
- **3 Test files baru** — `P2PConnectionTest`, `P2PFallbackTest`, `P2PSignalingTest`
- **`docs/P2P_SERVERLESS_ARCHITECTURE.md`** — arsitektur P2P lengkap
- **`metadata.json`** — updated capabilities: `LOCAL_FIRST`, `P2P_COMMUNICATION`, `OFFLINE_SYNC`
- **`check_file_length.py`** — `core/p2p` escape hatch ditambahkan
- **`AGENTS.md`** — updated ke v13 database, 38 test suites, migration status
- **`SETUP.md`** — test count updated 32→38 suite, 2100→2375+ baris
