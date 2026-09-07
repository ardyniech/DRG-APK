# DRG Driver (Driver Riang Gembira) 🛵💨
### Enterprise Android Application for Online Driver Community & Emergency Response Network

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

## 📦 Struktur Modul & Fitur Utama

```
app/src/main/java/com/example/
├── core/
│   ├── database/                 # Room Database (AppDatabase v4, TypeConverters, DAOs)
│   ├── repository/               # DRGRepository (Single source of truth)
│   └── viewmodel/                # DRGViewModel (Centralized state management)
├── shared/
│   ├── atoms/                    # Atomic UI Components (Badges, Buttons, Status Indicators)
│   └── models/                   # Immutable Data Models & Enums lintas modul
├── modules/
│   ├── auth/                     # Registrasi, Login PIN, Verifikasi Akun & Screening
│   ├── dashboard/                # Beranda, Quick SOS, Kartu Status Driver, Cuaca & Statistik
│   ├── emergency/                # Tombol Darurat SOS, Sensor Deteksi Jatuh/Tabrakan, Tim Reaksi Cepat
│   ├── radar/                    # Live Radar Pantauan Peta (OSM), Filter Platform, Basecamp / Kopdar
│   ├── forum_workshop/           # Forum Tanya Rekan (Q&A), Update Jalur Terkini & Bengkel Rekanan
│   ├── treasury/                 # Transparansi Kas Komunitas, Grafik Pemasukan/Pengeluaran
│   ├── gamification/             # Leaderboard Solidaritas, Misi Harian, Reward Voucher Bengkel
│   ├── members/                  # Direktori Anggota DRG, Detail Kendaraan & Rating Review
│   ├── admin/                    # Panel Pengurus: Screening Calon Anggota & Audit Logs
│   ├── notifications/            # Riwayat Notifikasi & Pengaturan Preferensi Siaga
│   ├── profile/                  # KTA Digital DRG (QR Code), Edit Profil & Kontak Darurat
│   └── main/                     # Scaffold Utama, Dynamic Insets, TopBar & Bottom Navigation
```

---

## 🗄️ Skema Database Lokal (Room v4)

1. **`members`**: Data anggota pengemudi, peran (*Ketua, Wakil, Satgas, Anggota*), status verifikasi, dan rating solidaritas.
2. **`emergency_alerts`**: Log sinyal darurat SOS, koordinat GPS, status penanganan tim reaksi cepat.
3. **`kas_transactions`**: Riwayat iuran kas masuk dan pengeluaran transparan kas komunitas.
4. **`forum_posts`**: Postingan diskusi internal dengan klasifikasi tipe (*`QUESTION` / Tanya Rekan* vs *`UPDATE` / Update Jalur*), filter kategori, dan pencacah suka/komentar.
5. **`workshop_partners`**: Daftar bengkel rekanan resmi DRG beserta diskon khusus anggota.
6. **`gamification_tasks`**: Daftar misi harian (Kopdar, respon darurat, berbagi info jalur) dan status klaim poin.
7. **`notification_preferences`**: Pengaturan notifikasi getar, suara sirine darurat, dan update kas.

---

## 🧪 Strategi Pengujian (Testing Suite)

Proyek ini dilengkapi dengan suite pengujian unit & integrasi berbasis **Robolectric** dan **Roborazzi**:

- `ForumPersistenceTest.kt`: Verifikasi CRUD postingan forum & pertanyaan Q&A pada Room Database.
- `CrashDetectionTest.kt`: Simulasi algoritma deteksi deselerasi & guncangan keras sensor akselerometer.
- `CacheAndBatteryTest.kt`: Validasi efisiensi cache dan konsumsi baterai pada interval polling radar.
- `FreeMapAndTrafficTest.kt`: Pengujian layer peta OpenStreetMap dan filter radius armada.
- `GreetingScreenshotTest.kt`: Verifikasi regresi visual antarmuka pengguna.

### Menjalankan Pengujian:
```bash
gradle :app:testDebugUnitTest
```

---

## 🚀 Panduan Menjalankan Aplikasi

1. Buka repositori di lingkungan Android Studio / AI Studio.
2. Sinkronkan dependensi Gradle (`libs.versions.toml`).
3. Jalankan target build debug:
   ```bash
   gradle assembleDebug
   ```
4. Pasang APK ke perangkat / Streaming Emulator Android.
