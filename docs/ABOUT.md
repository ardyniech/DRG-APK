# Tentang DRG Driver — Fitur & Progres

> Aplikasi komunitas driver ojek online Driver Riang Gembira (DRG)

**Progres pengembangan keseluruhan: 97%** (28 selesai · 0 dikerjakan · 1 direncanakan)

Versi `dev` · commit `v3.0.0` · 2026-11-13 · 18 modul · 577 file sumber · 40750 baris · 78 test suite

**Kekurangan (Bawel Audit): 3 temuan (0 BLOCKER · 0 MAJOR · 3 MINOR · 0 NOTE)**

> **SINGLE SOURCE OF TRUTH** — dokumen ini adalah acuan tunggal untuk AI dev, DevTools in-app, dan in-app Tentang. Digenerate otomatis oleh `generate_about_manifest.py` (mengandung `.github/scripts/audit_minus.py --json`). Jangan tulis manual. Jangan merujuk ke DEVTOOLS.md/README untuk angka — rujuk ke sini.

## Fase Pengembangan

### Fase 1 — Fondasi Komunitas & Darurat — 100% (v1.0.0)
Arsitektur Clean MVI + Compose M3, Room local-first, SOS/CrashGuard, Radar OSM, Kas Transparan, Forum, KTA Digital, dan RBAC.

| Fitur | Status | Progres |
|---|---|---|
| Pusat Kendali & Governance | Selesai | 100% |
| Autentikasi & Onboarding | Selesai | 100% |
| Hub Komunitas | Selesai | 100% |
| Audio Route & Alarm | Selesai | 100% |
| Hemat Baterai & Data | Selesai | 100% |
| Map Tile LRU Cache | Selesai | 100% |
| Database Local-First (Room v14) | Selesai | 100% |
| GPS & Location Sharing | Selesai | 100% |
| RBAC & Role Audit Trail | Selesai | 100% |
| Deteksi Tabrakan | Selesai | 100% |
| Sinkronisasi Offline & CRDT | Selesai | 100% |
| Dashboard Driver | Selesai | 100% |
| Darurat SOS & CrashGuard | Selesai | 100% |
| Forum & Bengkel Rekanan | Selesai | 100% |
| Misi, Poin & Loyalitas | Selesai | 100% |
| Direktori Anggota & Posko | Selesai | 100% |
| Notifikasi & Pengaturan | Selesai | 100% |
| KTA Digital & Profil | Selesai | 100% |
| Radar Live & Peta | Selesai | 100% |
| Navigasi & Shell Aplikasi | Selesai | 100% |
| Kas Transparan | Selesai | 100% |

### Fase 2 — Komunikasi P2P Serverless & Cloud Production — 100% (v1.1.0)
2-way Firestore sync + LWW CRDT, Firebase AI Logic, FCM topic P2P discovery & self-host socket, adaptive icon PNG, anti-tamper integrity, dan CI/CD release OTA.

| Fitur | Status | Progres |
|---|---|---|
| Halaman Tentang & Roadmap Otomatis | Selesai | 100% |
| Adaptive Icon & Branding | Selesai | 100% |
| Firebase (Firestore, Auth, FCM, AI) | Selesai | 100% |
| P2P Serverless & FCM Discovery | Selesai | 100% |
| App Check, Integrity & Anti-Tamper | Selesai | 100% |
| Release Signing, Delta OTA & CI/CD | Selesai | 100% |
| Layanan Driver (PTT, AI, Proteksi) | Selesai | 100% |

### Fase 3 — Layanan Finansial & Kemitraan — 0% (v2.0.0)
Marketplace kebutuhan operasional driver (deferred di luar scope rilis awal).

| Fitur | Status | Progres |
|---|---|---|
| Marketplace Kebutuhan Operasional | Direncanakan | 0% |

## Rencana Penyempurnaan

- **Firebase AI Logic Production** (done) — Asisten AI menggunakan Firebase.ai (gemini-3.8-flash) tanpa API key client hardcoded.
- **KtLint & Line-Cap Check di CI** (done) — check_file_length.py (<125 baris) + ktlint static analysis berjalan otomatis di android_ci.yml.
- **Verifikasi Migrasi Room v1→v14** (done) — Schema JSON 1-14 lengkap di app/schemas/ dan AppDatabaseMigrationTest lolos.
- **Instrumented UI Tests (Device/Emulator)** (pending) — Opsional di perangkat/emulator eksternal: tambah cakupan androidTest UI alur login, SOS, kas.
- **Dokumentasi Publik** (pending) — Situs dokumentasi (GitHub Pages) untuk pengurus & developer.

## Kekurangan (Bawel Audit) — WAJIB dibaca AI dev sebelum klaim

| Severity | Kategori | Detail | Perbaikan |
|---|---|---|---|
| **MINOR** | security | Room SQLite belum terenkripsi (SQLCipher tidak ada) — NIK/SIM/kas plain di file DB. | Enkripsi database atau minimal jelaskan risiko di docs. |
| **MINOR** | docs | README klaim 75 suite, realita 78 file Test.kt. | Update README badge + SETUP/DEVTOOLS jumlahnya. |
| **MINOR** | test | androidTest cuma 1 file contoh → alur kritis login/SOS/kas belum ter-cover instrumented. | Tambah UI test untuk login, SOS, kas. |

BLOCKER = dilarang klaim multi-user/sinkron sebelum diperbaiki. Jalankan `python3 .github/scripts/audit_minus.py --json` untuk detail mesin.

## Backlog Berjalan

- [DEFERRED] Fitur Arisan & Dana Talangan Darurat Berbasis Smart Contract / Kas Terverifikasi.
- [DEFERRED] Marketplace Kebutuhan Operasional (Oli, Ban, Helm, Jas Hujan) langsung dari distributor resmi dengan diskon anggota.
- [DEFERRED / PENDING DEV] **Fitur Asuransi** — Integrasi Klaim Asuransi Mandiri & Santunan Cepat bagi driver korban kecelakaan di jalan raya. (Pengembangan ditunda/pending.)
- [PENDING DEV] **Intercom Helm (Bluetooth/GATT, PTT Radio)** — integrasi pairing & audio intercom helm lanjutan.
- Marketplace Kebutuhan Operasional — [DEFERRED — di luar scope rilis awal. Finansial komunitas sementara hanya iuran kas (Kas Transparan phase-1). Jangan dianggap blocker.] Oli, ban, helm, jas hujan langsung dari distributor resmi dengan diskon anggota.
- Instrumented UI Tests (Device/Emulator) — Opsional di perangkat/emulator eksternal: tambah cakupan androidTest UI alur login, SOS, kas.
- Dokumentasi Publik — Situs dokumentasi (GitHub Pages) untuk pengurus & developer.

*Total fitur: 29 · 97% selesai.*
