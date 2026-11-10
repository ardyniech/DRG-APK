# Update Delta OTA — Hemat Kuota (patch KB, bukan APK 73 MB)

> Status: **kanal preview** (APK debug yang ditandatangani `debug.keystore` repo).
> Produksi tetap butuh release keystore di secrets — lihat "Batasan jujur" di bawah.

## Masalah

CI/CD GitHub selalu menghasilkan APK penuh. Setiap update minor berarti
mengunduh ±73 MB — boros kuota HP driver yang sering pakai data seluler.

## Solusi

HP hanya mengunduh dua hal kecil:

1. `update.json` — manifest versi terbaru (≈1 KB).
2. Patch **BSDIFF40** dari APK yang sedang terpasang ke APK baru (biasanya 1–4% ukuran APK).

Patch diterapkan di HP ke `applicationInfo.sourceDir` (APK terpasang). Hasilnya
**byte-identik** dengan APK yang ditandatangani CI, sehingga signature APK v2/v3
tetap sah dan Android menerima instalasi tanpa re-sign di perangkat.

## Bukti pengukuran nyata (2026-11-09, host vaio)

| Item | Nilai |
|---|---|
| APK lama `drg-versi1.apk` | 76.426.576 byte (72,9 MB) |
| APK baru `app-debug.apk` | 77.037.592 byte (73,5 MB) |
| Patch BSDIFF40 | 2.512.537 byte (2,4 MB) |
| Rasio patch | **3,26%** dari APK baru (hemat ±96,7%) |
| Roundtrip apply | sha256 hasil == sha256 APK baru → **True** |
| Waktu buat patch | 66,9 detik (di CI, sekali per rilis) |

## Alur di aplikasi

1. Profil → kartu **"Pembaruan Aplikasi"**. Saat Profil dibuka, aplikasi otomatis
   mengecek `update.json` (hanya ≈1 KB) — driver tidak perlu tap dulu.
2. `UpdateManager.check()` mengambil `update.json` dari
   `https://github.com/ardyniech/DRG-APK/releases/latest/download/update.json`
   (bisa dioverride saat build via env `UPDATE_MANIFEST_URL`).
3. Jika `versionCode` manifest > terpasang, cari patch dengan
   `fromVersionCode == terpasang` **dan** `fromSha256 == sha256(APK terpasang)`.
4. Tap **Patch/APK** untuk mengunduh. Progres byte ditampilkan.
5. Setelah patch/unduhan siap, dialog instalasi Android terbuka **otomatis**
   (tetap butuh konfirmasi user — batasan OS). Tombol **Pasang** tersedia
   sebagai fallback (mis. jika dialog tertutup).
6. Jika patch tidak cocok/tidak ada → fallback unduh APK penuh.

## Kode terkait

| File | Peran |
|---|---|
| `core/update/Bsdiff40.kt` | Format BSDIFF40: magic, int64 sign-magnitude, slice stream |
| `core/update/DeltaPatchApplier.kt` | bspatch murni JVM (bzip2 via commons-compress), tanpa NDK |
| `core/update/UpdateManifest.kt` | Model `update.json` + parser toleran |
| `core/update/UpdateChecker.kt` | OkHttp fetch manifest, unduh dengan progres, sha256 |
| `core/update/UpdateManager.kt` | State machine cek → unduh → apply → verifikasi |
| `core/update/ApkInstaller.kt` | FileProvider + intent `application/vnd.android.package-archive` |
| `modules/profile/ProfileUpdateEntry.kt` | Kartu UI di Profil (patch vs APK penuh, progres, Pasang) |
| `modules/profile/ProfileUpdateStatus.kt` | Teks status + format MB |
| `.github/scripts/gen_delta_update.py` | Generator patch + `update.json` (bsdiff4) |
| `.github/workflows/release_delta.yml` | Build → unduh rilis sebelumnya → patch → publish aset |
| `res/xml/file_paths.xml` | Path cache `updates/` yang boleh dibagikan FileProvider |

## Cara merilis

```bash
git tag v1.1
git push origin v1.1
```

Workflow `Delta OTA Release` akan:

1. `assembleDebug` dengan `VERSION_CODE = <versionCode rilis sebelumnya> + 1`.
2. Mengunduh aset rilis terakhir (`app-debug.apk` + `update.json`) sebagai basis patch.
3. Membuat `patch-<lama>-to-<baru>.patch` dan `update.json` (berisi sha256 semua aset).
4. Mempublikasikan ketiganya ke GitHub Release tag tersebut.

Tanpa rilis sebelumnya, workflow tetap jalan tetapi hanya menerbitkan APK penuh
(tanpa patch) — patch pertama baru ada saat rilis berikutnya.

## Batasan jujur (belum beres)

* **Kanal preview, bukan produksi.** APK debug ditandatangani `debug.keystore`
  yang sekarang **ter-commit (base64) agar konsisten antar rilis** — update delta
  tidak lagi bentrok signature. Untuk produksi, gunakan release keystore di secrets.
* **Pemakai v1.1/v1.2 lama wajib instal ulang sekali.** Rilis sebelum perbaikan
  signing ini ditandatangani kunci acak per rilis, sehingga tidak bisa di-update
  in-place. Uninstal APK lama → pasang v1.3 (atau lebih baru) yang sudah
  memakai kunci stabil; setelah itu delta update berfungsi normal.
* Android **selalu** menampilkan dialog konfirmasi instalasi; silent install
  butuh device owner/root — itu batasan OS, bukan bug aplikasi.
* Patch hanya valid untuk base dengan sha256 yang sama. Kalau driver memasang
  APK dari sumber lain (mis. build lokal), patch ditolak → fallback APK penuh.
* Belum ada resume unduhan parsial; sinyal putus = unduh ulang patch (tetap kecil).
* `REQUEST_INSTALL_PACKAGES` ditambahkan; user perlu mengizinkan
  "Install unknown apps" satu kali. Kalau belum diizinkan, tombol **Pasang**
  membuka layar pengaturan izin (bukan tombol mati); setelah itu ketuk Pasang lagi.
* Pengaman anti-rusak: patch dengan ukuran output > 400 MB ditolak
  (`MAX_OUTPUT_BYTES`), dan hasil akhir harus cocok **ukuran + sha256** dengan
  manifest sebelum tombol Pasang muncul.
* Belum diuji di HP fisik pada perubahan ini — verifikasi yang ada: unit test
  bspatch (JVM) + pengukuran patch/roundtrip di host.
* Unit test: `DeltaPatchApplierTest` (roundtrip diff/extra, patch tanpa magic,
  ukuran output tidak wajar, offtin/offtout) dan `UpdateManifestParserTest`
  (manifest lengkap, field opsional hilang, JSON rusak, pemilihan patch by sha).

## Rollback

```bash
git revert <commit>
gh release delete v1.1 --yes   # hapus aset rilis bila perlu
```
