# Catatan Perubahan (Changelog) — DRG Driver 📝

Semua perubahan penting pada proyek **DRG Driver** didokumentasikan dalam file ini mengikuti format [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) dan menganut [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
