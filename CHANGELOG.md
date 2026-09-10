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
