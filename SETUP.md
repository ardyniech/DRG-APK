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
- **Gradle Version**: 8.7+ dengan Kotlin Gradle Plugin 2.0+.

---

## 🔑 2. Konfigurasi Lingkungan & Kunci Rahasia (.env)

Aplikasi ini menggunakan **Secrets Gradle Plugin** untuk menginjeksi API Keys secara aman tanpa melakukan commit ke repositori publik:

1. Buat file `.env` di direktori *root* proyek (salin dari `.env.example`):
   ```bash
   cp .env.example .env
   ```

2. Isi variabel yang diperlukan di dalam `.env`:
   ```properties
   # API Keys & Token Konfigurasi
   GEMINI_API_KEY=your_gemini_api_key_here
   MAPS_API_KEY=your_maps_key_if_used
   FIREBASE_APPCHECK_DEBUG_TOKEN=your_debug_token
   ```

3. Nilai ini akan diinjeksi secara otomatis ke `BuildConfig` saat proses kompilasi Gradle.

---

## 🔥 3. Konfigurasi Firebase (`google-services.json`)

Proyek ini telah dikonfigurasi dengan plugin `com.google.gms.google-services` dan strategi `missingGoogleServicesStrategy = MissingGoogleServicesStrategy.WARN` agar proyek tetap dapat di-build secara lokal tanpa Firebase.

Untuk mengaktifkan fitur notifikasi push FCM atau Firebase Cloud secara penuh:
1. Buka [Firebase Console](https://console.firebase.google.com/).
2. Buat proyek Firebase baru atau gunakan proyek yang ada.
3. Daftarkan aplikasi Android dengan package name yang sesuai (`applicationId`).
4. Unduh file `google-services.json`.
5. Letakkan file tersebut di folder:
   ```
   app/google-services.json
   ```

---

## 🔐 4. Konfigurasi Signing & Keystore Rilis

Untuk keamanan, file `keystore` produksi tidak di-commit ke Git. `app/build.gradle.kts` menggunakan konfigurasi fleksibel berbasis *Environment Variables*:

### Mode Debug:
Secara default, build debug menggunakan keystore debug lokal Android (`debug.keystore`).

### Mode Release:
Untuk menandatangani APK rilis di CI/CD (GitHub Actions) atau mesin lokal, atur environment variables berikut:

```bash
export KEYSTORE_PATH="/path/ke/keystore/my-upload-key.jks"
export STORE_PASSWORD="password_keystore_anda"
export KEY_ALIAS="upload"
export KEY_PASSWORD="password_alias_anda"
```

Jika variabel ini tidak diatur, build release akan mencari file `my-upload-key.jks` di root proyek.

---

## 📦 5. Namespace vs Application ID

- **`namespace` (`com.example`)**: Digunakan secara internal oleh build system untuk paket kelas `R` dan integrasi AI Studio Cloud Container.
- **`applicationId` (`com.aistudio.drgojol.pkrvw`)**: Merupakan ID unik aplikasi yang terdaftar di sistem operasi Android, Google Play Store, dan Firebase.

---

## 🧪 6. Menjalankan Pengujian Unit & Verifikasi

Proyek ini memiliki pengujian otomatis berbasis **Robolectric** (JVM lokal) dan **Roborazzi** (verifikasi tampilan visual):

1. **Jalankan Semua Unit Test**:
   ```bash
   gradle :app:testDebugUnitTest
   ```

2. **Verifikasi Screenshot Roborazzi**:
   ```bash
   gradle :app:verifyRoborazziDebug
   ```

3. **Kompilasi APK Debug**:
   ```bash
   gradle assembleDebug
   ```

---

## 🛡️ 7. Keamanan Jaringan & Privasi Data

- **Network Security Config**: `app/src/main/res/xml/network_security_config.xml` secara ketat memblokir cleartext (HTTP) dan hanya mengizinkan lalu lintas TLS 1.3 / HTTPS.
- **Local Data Protection**: Semua data sensitif (kas, kontak darurat, postingan) disimpan dalam SQLite terenkripsi lokal via Room Database.
