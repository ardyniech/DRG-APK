# Panduan Release CI/CD & Production APK/AAB (GitHub Actions)

Pipeline rilis berada di `.github/workflows/release_delta.yml` (dan validasi PR/branch di `.github/workflows/android_ci.yml`).

## 1. Secrets Wajib di GitHub Repository (`Settings → Secrets and variables → Actions`)

| Secret Name | Wajib Produksi | Keterangan |
|---|---|---|
| `RELEASE_KEYSTORE_BASE64` | Ya | File `.jks` release keystore di-encode Base64 (`base64 -w 0 release.jks`). |
| `RELEASE_STORE_PASSWORD` | Ya | Password keystore. |
| `RELEASE_KEY_ALIAS` | Ya | Alias key di keystore (default `upload`). |
| `RELEASE_KEY_PASSWORD` | Ya | Password key alias. |
| `GOOGLE_SERVICES_JSON_BASE64` | Ya | File `app/google-services.json` asli di-encode Base64 untuk Auth, Firestore, FCM, App Check. |
| `MAPS_API_KEY` | Ya | Google Maps API key (diinjeksi ke `.env` saat build). |
| `GEMINI_API_KEY` | Opsional | Fallback opsional; fitur AI utama memakai Firebase AI Logic App Check. |
| `UPDATE_MANIFEST_URL` | Opsional | Override URL manifest OTA `update.json` (default ke GitHub Releases latest). |

## 2. Alur Eksekusi Pipeline Rilis (`release_delta.yml`)

1. **Job `quality-gate`:**
   - Menjalankan `python3 .github/scripts/check_file_length.py` (hard fail > 125 baris).
   - Menjalankan `python3 .github/scripts/audit_minus.py --fail-on-blocker` (hard fail pada temuan BLOCKER).
   - Menjalankan `./gradlew :app:testDebugUnitTest :app:lintDebug --no-daemon`.
2. **Job `release`:**
   - Mendekode `RELEASE_KEYSTORE_BASE64` dan `GOOGLE_SERVICES_JSON_BASE64` ke folder sementara runner (`$RUNNER_TEMP`).
   - Mem-build signed **`app-release.apk`** (`:app:assembleRelease` + R8 minification & resource shrinking) dan **`app-release.aab`** (`:app:bundleRelease` untuk Play Store).
   - Menghasilkan patch delta BSDIFF40 dari rilis sebelumnya + `update.json` + `mapping-v<ver>.txt` + `SHA256SUMS.txt`.
   - Mempublikasikan seluruh aset ke **GitHub Releases** secara otomatis saat tag `v*` di-push atau via `workflow_dispatch`.

## 3. Menu OTA Update di UI Aplikasi

Driver dapat memantau versi terpasang dan melakukan cek/unduh/pasang pembaruan langsung dari halaman **Pengaturan** (`Profile → Pengaturan`), karena kartu **Pembaruan Aplikasi OTA** ditampilkan langsung di bagian atas halaman Pengaturan (`SettingsScreen` → `ProfileUpdateEntry`).
