# Panduan Setup Pengembang Lokal — DRG Driver 🛠️📱

Panduan ini berisi instruksi langkah demi langkah untuk menyiapkan, mengonfigurasi, dan menjalankan aplikasi **DRG Driver** di lingkungan pengembangan lokal menggunakan Android Studio atau terminal Gradle.

---

## 📋 1. Prasyarat Sistem

- **Android Studio**: Android Studio Hedgehog / Iguana / Jellyfish / Koala (2024.x) atau lebih baru.
- **Java Development Kit (JDK)**: JDK 17 atau JDK 21 (direkomendasikan Temurin/Corretto).
- **Android SDK**:
  - `compileSdk`: 36 (Android 15+)
  - `minSdk`: 24 (Android 7.0+)
  - `targetSdk`: 36
- **Gradle Version**: Gradle 8.11+ / 9.x dengan Gradle Wrapper (`./gradlew`).

---

## 🔑 2. Konfigurasi Lingkungan & Kunci Rahasia (.env)

Aplikasi ini menggunakan **Secrets Gradle Plugin** untuk menginjeksi API Keys secara aman tanpa melakukan commit ke repositori publik:

1. Buat file `.env` di direktori *root* proyek (salin dari `.env.example`):
   ```bash
   cp .env.example .env
   ```

2. Isi variabel yang diperlukan di dalam `.env`:
   ```properties
   GEMINI_API_KEY=your_gemini_api_key_here
   MAPS_API_KEY=your_maps_key_if_used
   FIREBASE_APPCHECK_DEBUG_TOKEN=your_debug_token
   ```

3. Nilai ini diinjeksi ke `BuildConfig` saat proses kompilasi Gradle.

---

## 🔥 3. Konfigurasi Firebase (`google-services.json`)

Repositori ini telah dilengkapi dengan mock konfigurasi `app/google-services.json` sehingga proyek dapat langsung dikompilasi di CI/CD tanpa kegagalan:

Untuk menghubungkan ke Firebase Console proyek produksi Anda:
1. Buka [Firebase Console](https://console.firebase.google.com/).
2. Buat proyek Firebase baru dan daftarkan package name Android (`com.drg.driver` atau `com.aistudio.drgojol.pkrvw`).
3. Unduh file `google-services.json` resmi dari Firebase Console.
4. Gantikan file di `app/google-services.json` (referensi template tersedia di `app/google-services.json.example`).

---

## 🔐 4. Konfigurasi Signing & Keystore Rilis

Untuk keamanan, file `keystore` produksi tidak di-commit ke Git publik. `app/build.gradle.kts` menggunakan konfigurasi fleksibel berbasis *Environment Variables*:

### Mode Debug:
Build debug menggunakan keystore debug otomatis Android (`debug.keystore`).

### Mode Release:
Untuk menandatangani APK rilis di CI/CD (GitHub Actions) atau mesin lokal, atur environment variables berikut:

```bash
export KEYSTORE_PATH="/path/ke/keystore/my-upload-key.jks"
export STORE_PASSWORD="password_keystore_anda"
export KEY_ALIAS="upload"
export KEY_PASSWORD="password_alias_anda"
```

---

## 📦 5. Namespace vs Application ID

- **`namespace` (`com.example`)**: Digunakan secara internal oleh build system untuk paket kelas `R` dan keutuhan struktur file internal.
- **`applicationId` (`com.aistudio.drgojol.pkrvw` / `com.drg.driver`)**: Merupakan ID unik aplikasi yang terdaftar di Android OS, Google Play Store, dan Firebase. Untuk build independen di Play Store, Anda dapat mengarahkan `applicationId` ke `com.drg.driver`.

---

## 🧪 6. Menjalankan Pengujian Unit & Verifikasi

Proyek ini memiliki 32 suite pengujian otomatis (2.100+ baris test) berbasis **Robolectric** dan **Roborazzi**:

1. **Jalankan Semua Unit Test**:
   ```bash
   ./gradlew :app:testDebugUnitTest
   ```

2. **Jalankan Verifikasi Batas Baris Arsitektur (< 125 Baris)**:
   ```bash
   python3 .github/scripts/check_file_length.py
   ```

3. **Jalankan Android Lint**:
   ```bash
   ./gradlew :app:lintDebug
   ```

4. **Kompilasi APK Debug**:
   ```bash
   ./gradlew assembleDebug
   ```

---

## 🛡️ 7. Keamanan Jaringan & Privasi Data

- **Network Security Config**: `app/src/main/res/xml/network_security_config.xml` secara ketat memblokir cleartext (HTTP) dan hanya mengizinkan lalu lintas TLS 1.3 / HTTPS.
- **Local Data Protection & RBAC**: Semua data sensitif (kas, kontak darurat, postingan, izin jabatan, riwayat check-in posko) disimpan dalam SQLite terenkripsi lokal via Room Database v12 (`docs/DATABASE_MIGRATIONS.md`).
