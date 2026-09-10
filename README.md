# DRG Driver (Driver Riang Gembira) 🛵💨
### Enterprise Android Application for Online Driver Community & Emergency Response Network

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android_15-green.svg)](https://developer.android.com)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-emerald.svg)](https://developer.android.com/jetpack/compose)
[![Database](https://img.shields.io/badge/Room_Database-v5_Local_First-teal.svg)](https://developer.android.com/training/data-storage/room)
[![Tests](https://img.shields.io/badge/Unit_Tests-Robolectric_Passed-brightgreen.svg)](#-strategi-pengujian-testing-suite)

---

## 📌 Ringkasan Proyek

**DRG Driver** adalah platform komunitas terintegrasi untuk pengemudi ojek online (*Gojek, Grab, Maxim, ShopeeFood, InDrive*) di bawah naungan komunitas **Driver Riang Gembira (DRG)**. Aplikasi ini dirancang dengan prinsip **Local-First Architecture**, **Zero Cognitive Overload**, dan **Material Design 3 (M3)** untuk menghadirkan rasa aman, solidaritas, transparansi keuangan kas, serta respons darurat secepat kilat di jalan raya.

---

## 🏛️ Arsitektur Sistem & Prinsip Desain

Aplikasi ini dibangun menggunakan arsitektur **Clean Architecture + MVI/UDF (Unidirectional Data Flow)** dengan modularisasi ketat:

- **Kotlin & Jetpack Compose**: 100% deklaratif, modern UI tanpa layout XML.
- **Strict Modularity (< 125 Baris per File)**: Setiap komponen UI dan logika dipisah ke micro-primitives untuk keterbacaan dan pemeliharaan maksimal.
- **Material 3 Design System (Custom Brand Tokens)**:
  - `DrgGreenPrimary` (`#00B14F` / Grab-Gojek Green Spirit)
  - `DrgBackground` (`#F8FAF8`) & `DrgSurface` (`#FFFFFF`) — *Light, fresh, and high-contrast (Haram Dark Theme & Non-Monolithic White)*
  - `DrgRedPanic` (`#DC2626`) untuk Tombol Darurat SOS
  - `DrgGoldReward` (`#F59E0B`) untuk Gamifikasi & Poin Solidaritas
- **Local-First with Room Database**: Persistensi data lokal berkecepatan tinggi dengan Flow reaktif, offline-ready, dan non-blocking I/O via Kotlin Coroutines.

---

## 🔒 Keamanan & Perlindungan Privasi (Enterprise Grade)

- **Network Security Configuration (`network_security_config.xml`)**:
  - Lalu lintas jaringan diwajibkan menggunakan TLS 1.3 / HTTPS.
  - Mematikan *cleartext HTTP traffic* untuk mencegah serangan *Man-in-the-Middle (MitM)*.
  - Domain-level pinning untuk OpenStreetMap tiles, Firebase, dan Google Cloud APIs.
- **Least-Privilege Permissions**:
  - `ACCESS_FINE_LOCATION` & `ACCESS_COARSE_LOCATION`: Digunakan hanya saat pengemudi mengaktifkan mode siaga / tombol SOS darurat.
  - `VIBRATE`: Untuk haptic feedback respon tombol dan sirine peringatan darurat.
- **Enclave Secrets & Signing Security**:
  - API Keys dikelola menggunakan **Secrets Gradle Plugin** melalui file `.env`.
  - Keystore rilis dikonfigurasi via *Environment Variables* di CI/CD tanpa menyimpan kredensial sensitif di repositori publik.

---

## 📦 Struktur Modul & Fitur Utama

```
app/src/main/java/com/example/
├── core/
│   ├── database/                 # Room Database (AppDatabase v5, TypeConverters, DAOs)
│   ├── repository/               # DRGRepository & CommunityRepository (Single source of truth)
│   └── viewmodel/                # DRGViewModel & Coordinator Delegates (Centralized state management)
├── shared/
│   ├── atoms/                    # Atomic UI Components (Badges, Buttons, Status Indicators)
│   └── models/                   # Immutable Data Models & Enums lintas modul
├── modules/
│   ├── auth/                     # Registrasi, Login PIN, Verifikasi Akun & Screening
│   ├── dashboard/                # Beranda, Quick SOS, Kartu Status Driver, Cuaca & Statistik
│   ├── emergency/                # Tombol Darurat SOS, Sensor Deteksi Jatuh/Tabrakan, Tim Reaksi Cepat
│   ├── radar/                    # Live Radar Pantauan Peta (OSM), Filter Platform, Basecamp / Kopdar
│   ├── forum_workshop/           # Forum Tanya Rekan (Q&A), Update Jalur, Threaded Comments & Bengkel Rekanan
│   ├── treasury/                 # Transparansi Kas Komunitas, Grafik Pemasukan/Pengeluaran
│   ├── gamification/             # Leaderboard Solidaritas, Misi Harian, Reward Voucher Bengkel
│   ├── members/                  # Direktori Anggota DRG, Detail Kendaraan, KTA Digital & Rating Review
│   ├── admin/                    # Panel Pengurus: Screening Calon Anggota & Audit Logs
│   ├── notifications/            # Riwayat Notifikasi & Pengaturan Preferensi Siaga
│   ├── profile/                  # KTA Digital DRG (QR Code), Edit Profil & Kontak Darurat
│   └── main/                     # Scaffold Utama, Dynamic Insets, TopBar & Bottom Navigation
```

---

## 🗄️ Skema Database Lokal (Room v5)

1. **`members`**: Data anggota pengemudi, peran (*Ketua, Wakil, Satgas, Anggota*), status verifikasi, dan rating solidaritas.
2. **`emergency_alerts`**: Log sinyal darurat SOS, koordinat GPS, status penanganan tim reaksi cepat.
3. **`kas_transactions`**: Riwayat iuran kas masuk dan pengeluaran transparan kas komunitas.
4. **`forum_posts`**: Postingan diskusi internal dengan klasifikasi tipe (*`QUESTION` / Tanya Rekan* vs *`UPDATE` / Update Jalur*), filter kategori, dan pencacah suka/komentar.
5. **`forum_comments`**: Balasan/komentar interaktif pengemudi pada setiap postingan forum diskusi.
6. **`workshop_partners`**: Daftar bengkel rekanan resmi DRG beserta diskon khusus anggota.
7. **`gamification_tasks`**: Daftar misi harian (Kopdar, respon darurat, berbagi info jalur) dan status klaim poin.
8. **`notification_preferences`**: Pengaturan notifikasi getar, suara sirine darurat, dan update kas.
9. **`map_tile_metadata`**: Metadata tile peta lokal (zoom, koordinat X/Y, ukuran byte, timestamp akses, frekuensi hit) untuk disk cache LRU hemat kuota/baterai.
10. **`app_state_settings`**: Persistensi key-value status konfigurasi aplikasi (profil sinkronisasi baterai, kebijakan cache, preferensi radar).

---

## 🧪 Strategi Pengujian (Testing Suite)

Proyek ini dilengkapi dengan suite pengujian unit & integrasi otomatis berbasis **Robolectric** dan **Roborazzi**:

- `ForumPersistenceTest.kt`: Verifikasi CRUD postingan forum, balasan komentar, pencacah komentar, dan toggle suka pada Room Database.
- `MapTileCacheAndStateTest.kt`: Verifikasi persistensi Room metadata tile peta, operasi eviction LRU, tracking penghematan data, dan penyimpanan state aplikasi.
- `CrashDetectionTest.kt`: Simulasi algoritma deteksi deselerasi & guncangan keras sensor akselerometer.
- `CacheAndBatteryTest.kt`: Validasi efisiensi cache dan konsumsi baterai pada interval polling radar.
- `FreeMapAndTrafficTest.kt`: Pengujian layer peta OpenStreetMap dan filter radius armada.
- `GreetingScreenshotTest.kt`: Verifikasi regresi visual antarmuka pengguna.

### Menjalankan Pengujian:
```bash
gradle :app:testDebugUnitTest
```

---

## 🚀 Panduan Setup & Berkontribusi

- **Panduan Setup Pengembang Lokal**: Silakan baca **[SETUP.md](SETUP.md)** untuk konfigurasi Android Studio, `.env`, Firebase `google-services.json`, dan signing keystore.
- **Pedoman Kontribusi**: Silakan baca **[CONTRIBUTING.md](CONTRIBUTING.md)** untuk aturan penulisan kode (< 125 baris/file) dan alur Pull Request.
- **Kode Etik**: Silakan baca **[CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)**.
- **Lisensi**: Proyek ini dilindungi di bawah lisensi **[Apache License 2.0](LICENSE)**.
