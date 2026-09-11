# DRG Driver (Driver Riang Gembira) 🛵💨
### Enterprise Android Application for Online Driver Community & Emergency Response Network

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android_15-green.svg)](https://developer.android.com)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-emerald.svg)](https://developer.android.com/jetpack/compose)
[![Database](https://img.shields.io/badge/Room_Database-v12_Posko_RBAC_Local_First-teal.svg)](docs/DATABASE_MIGRATIONS.md)
[![Architecture Gating](https://img.shields.io/badge/Architecture-125_Lines_Cap_Enforced-success.svg)](.github/scripts/check_file_length.py)
[![Tests](https://img.shields.io/badge/Unit_Tests-27_Suites_Passed-brightgreen.svg)](#-strategi-pengujian-testing-suite)

---

## 📌 Ringkasan Proyek

**DRG Driver** adalah platform komunitas terintegrasi untuk pengemudi ojek online (*Gojek, Grab, Maxim, ShopeeFood, InDrive*) di bawah naungan komunitas **Driver Riang Gembira (DRG)**. Aplikasi ini dirancang dengan prinsip **Local-First Architecture**, **Zero Cognitive Overload**, dan **Material Design 3 (M3)** untuk menghadirkan rasa aman, solidaritas, transparansi keuangan kas, serta respons darurat secepat kilat di jalan raya.

---

## 🏛️ Arsitektur Sistem & Prinsip Desain

Aplikasi ini dibangun menggunakan arsitektur **Clean Architecture + MVI/UDF (Unidirectional Data Flow)** dengan modularisasi ketat:

- **Kotlin & Jetpack Compose**: 100% deklaratif, modern UI tanpa layout XML.
- **Strict Modularity (< 125 Baris per File)**: Setiap komponen UI dan logika dipisah ke micro-primitives untuk keterbacaan dan pemeliharaan maksimal (diverifikasi otomatis via `.github/scripts/check_file_length.py` di CI).
- **Material 3 Design System (Custom Brand Tokens)**:
  - `DrgGreenPrimary` (`#00B14F` / Grab-Gojek Green Spirit)
  - `DrgBackground` (`#F8FAF8`) & `DrgSurface` (`#FFFFFF`) — *Light, fresh, and high-contrast*
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
│   ├── database/                 # Room Database (AppDatabase v10, TypeConverters, DAOs)
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

## 🗄️ Skema Database Lokal (Room v12)

Detail skema dan panduan migrasi dapat dibaca di **[Dokumentasi Migrasi Database (docs/DATABASE_MIGRATIONS.md)](docs/DATABASE_MIGRATIONS.md)**.

1. **`members`**: Data anggota pengemudi, peran (*Ketua, Wakil, Satgas, Dewan Etika, Anggota*), status verifikasi, dan rating solidaritas.
2. **`member_role_permissions`**: Hak akses izin granular per-anggota (kelola kas, verifikasi driver, siaran SOS, kelola posko).
3. **`role_audit_logs`**: Rekam jejak audit otorisasi & promosi/demosi jabatan pengurus.
4. **`posko_check_ins`**: Riwayat check-in singgah driver di Posko Komunitas untuk akumulasi loyalitas.
5. **`emergency_alerts`**: Log sinyal darurat SOS, koordinat GPS, status penanganan tim reaksi cepat.
6. **`kas_transactions`**: Riwayat iuran kas masuk dan pengeluaran transparan kas komunitas.
7. **`forum_posts`**: Postingan diskusi internal (*`QUESTION`* vs *`UPDATE`*), filter kategori, dan pencacah suka.
8. **`forum_comments`**: Balasan/komentar interaktif pengemudi pada setiap postingan forum diskusi.
9. **`workshop_partners`**: Daftar bengkel rekanan resmi DRG beserta diskon khusus anggota.
10. **`community_tasks` & `reward_items`**: Misi gotong-royong, kupon servis, dan saldo poin.
11. **`notification_preferences`**: Pengaturan notifikasi getar, suara sirine darurat, dan update kas.
12. **`map_tile_cache`**: Metadata tile peta lokal untuk disk cache LRU hemat kuota/baterai.
13. **`app_state_settings`**: Persistensi konfigurasi aplikasi dan profil hemat daya.
14. **`pending_sync_queue`**: Antrean mutasi offline untuk sinkronisasi sinkron latar belakang.
15. **`admin_logs`**: Rekam jejak audit screening pengurus.

---

## 🧪 Strategi Pengujian (Testing Suite)

Proyek ini dilengkapi dengan 27 suite pengujian unit & integrasi otomatis (1.850+ baris kode tes) berbasis **Robolectric** dan **Roborazzi**:

- `PoskoCheckInRepositoryTest.kt`: Pengujian pencatatan check-in posko dan pemberian poin loyalitas.
- `RolePermissionRepositoryTest.kt`: Pengujian granular access control (kas, SOS, dashboard, promosi role).
- `MemberRolePermissionDatabaseTest.kt`: Pengujian persistensi Room v11 RBAC & audit logging.
- `PoskoProximityAndWatermarkTest.kt`: Validasi geofencing Haversine posko dan anti-forgery KTA.
- `AuthAndProfileSecurityTest.kt`: Validasi PIN 6-digit, normalisasi nopol, enkripsi payload KTA QR, dan RBAC pengurus.
- `TreasuryAuditComputationTest.kt`: Verifikasi matematika kas masuk/keluar, saldo bersih, dan kalkulasi kategori.
- `GamificationAndLoyaltyTest.kt`: Validasi ambang batas lencana XP, verifikasi kupon servis, dan siklus tugas komunitas.
- `EmergencyTrcDispatchTest.kt`: Validasi koordinat GPS, kalkulasi jarak Haversine, dan alur eskalasi SOS tim reaksi cepat.
- `AdversarialAuditScenariosTest.kt`: 3 skenario negatif mandatori (Bad Input, Cross-Module Failure, UI Dead-End Offline Fallback).
- `ForumPersistenceTest.kt`: Verifikasi CRUD postingan forum, balasan komentar, dan counter.
- `MapTileCacheAndStateTest.kt`: Verifikasi metadata tile peta, penghematan kuota, dan state aplikasi.
- `CrashDetectionTest.kt`: Algoritma deteksi guncangan keras sensor akselerometer.
- `CacheAndBatteryTest.kt`: Validasi efisiensi cache dan konsumsi baterai.
- `FreeMapAndTrafficTest.kt`: Pengujian layer peta OpenStreetMap dan radius armada.
- `GreetingScreenshotTest.kt`: Verifikasi regresi visual antarmuka pengguna.

### Menjalankan Pengujian Lokal:
```bash
./gradlew :app:testDebugUnitTest
```

### Menjalankan Audit Arsitektur (Batas 125 Baris):
```bash
python3 .github/scripts/check_file_length.py
```

### Membangun APK Debug:
```bash
./gradlew assembleDebug
```

---

## 🚀 Panduan Setup & Berkontribusi

- **Panduan Setup Pengembang Lokal**: Silakan baca **[SETUP.md](SETUP.md)** untuk konfigurasi Android Studio, `.env`, Firebase `google-services.json`, dan signing keystore.
- **Pedoman Kontribusi**: Silakan baca **[CONTRIBUTING.md](CONTRIBUTING.md)** untuk aturan penulisan kode (< 125 baris/file) dan alur Pull Request.
- **Kode Etik**: Silakan baca **[CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)**.
- **Lisensi**: Proyek ini dilindungi di bawah lisensi **[Apache License 2.0](LICENSE)**.
