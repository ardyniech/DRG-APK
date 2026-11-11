# Tentang DRG Driver — Fitur & Progres

> Aplikasi komunitas driver ojek online Driver Riang Gembira (DRG)

**Progres pengembangan keseluruhan: 64%** (13 selesai · 12 dikerjakan · 8 direncanakan)

Versi `v1.10` · commit `3806278` · 2026-11-11 · 17 modul · 472 file sumber · 33848 baris · 59 test suite

**Kekurangan (Bawel Audit): 10 temuan (2 BLOCKER · 3 MAJOR · 5 MINOR · 0 NOTE)**

> **SINGLE SOURCE OF TRUTH** — dokumen ini adalah acuan tunggal untuk AI dev, DevTools in-app, dan in-app Tentang. Digenerate otomatis oleh `generate_about_manifest.py` (mengandung `.github/scripts/audit_minus.py --json`). Jangan tulis manual. Jangan merujuk ke DEVTOOLS.md/README untuk angka — rujuk ke sini.

## Fase Pengembangan

### Fase 1 — Fondasi Komunitas & Darurat — 85% (v1.0.0)
Arsitektur Clean MVI + Compose M3, Room local-first, SOS/CrashGuard, Radar OSM, Kas Transparan, Forum, KTA Digital, dan RBAC.

| Fitur | Status | Progres |
|---|---|---|
| Pusat Kendali & Governance | Selesai | 100% |
| Autentikasi & Onboarding | Selesai | 100% |
| Hub Komunitas | Sedang Dibangun | 75% |
| Audio Route & Alarm | Sedang Dibangun | 70% |
| Hemat Baterai & Data | Sedang Dibangun | 65% |
| Map Tile LRU Cache | Sedang Dibangun | 70% |
| Database Local-First (Room v13) | Sedang Dibangun | 70% |
| GPS & Location Sharing | Sedang Dibangun | 70% |
| RBAC & Role Audit Trail | Sedang Dibangun | 65% |
| Deteksi Tabrakan | Sedang Dibangun | 65% |
| Sinkronisasi Offline | Sedang Dibangun | 70% |
| Dashboard Driver | Selesai | 100% |
| Darurat SOS & CrashGuard | Selesai | 100% |
| Forum & Bengkel Rekanan | Selesai | 100% |
| Misi, Poin & Loyalitas | Selesai | 100% |
| Direktori Anggota & Posko | Selesai | 100% |
| Notifikasi & Pengaturan | Selesai | 100% |
| KTA Digital & Profil | Selesai | 100% |
| Radar Live & Peta | Selesai | 100% |
| Navigasi & Shell Aplikasi | Sedang Dibangun | 70% |
| Kas Transparan | Selesai | 100% |

### Fase 2 — Komunikasi P2P Serverless — 42% (v1.1.0)
WebRTC DataChannel, self-host per device, discovery via FCM, fallback PendingSyncQueue, dan pematangan menuju APK production.

| Fitur | Status | Progres |
|---|---|---|
| Halaman Tentang & Roadmap Otomatis | Selesai | 100% |
| Firebase (FCM, Auth, AI) | Sedang Dibangun | 65% |
| P2P Serverless (WebRTC) | Sedang Dibangun | 70% |
| Layanan Driver (PTT, AI, Proteksi) | Selesai | 100% |
| Discovery Peer via FCM Topic | Direncanakan | 0% |
| Adaptive Icon & Branding | Direncanakan | 0% |
| Play Integrity & Anti-Tamper | Direncanakan | 0% |
| Release Signing & Distribusi | Direncanakan | 0% |

### Fase 3 — Layanan Finansial & Kemitraan — 0% (v2.0.0)
Arisan digital, dana talangan darurat, marketplace kebutuhan operasional, dan klaim asuransi/santunan driver.

| Fitur | Status | Progres |
|---|---|---|
| Arisan Digital & Dana Talangan | Direncanakan | 0% |
| Marketplace Kebutuhan Operasional | Direncanakan | 0% |
| Klaim Asuransi & Santunan | Direncanakan | 0% |
| Rekonsiliasi CRDT | Direncanakan | 0% |

## Rencana Penyempurnaan

- **Instrumented UI Tests** (pending) — Tambah cakupan androidTest untuk alur kritis (login, SOS, kas).
- **Detekt & KtLint di CI** (pending) — Static analysis otomatis untuk kualitas kode selain batas 125 baris.
- **Gemini Live & AI Assistant Production** (in-progress) — Ganti placeholder AI dengan integrasi Gemini API production + rate limiting.
- **Verifikasi Migrasi Room v1→v13** (done) — Uji migrasi tiap versi + commit semua schema JSON.
- **Dokumentasi Publik** (pending) — Situs dokumentasi (GitHub Pages) untuk pengurus & developer.

## Kekurangan (Bawel Audit) — WAJIB dibaca AI dev sebelum klaim

| Severity | Kategori | Detail | Perbaikan |
|---|---|---|---|
| **BLOCKER** | fake-sync | BackgroundSyncEngine label "Tersinkronisasi ke Cloud" padahal triggerBackgroundSync() cuma delay + removeSyncItem() — TIDAK ada network call (Retrofit/OkHttp/Firestore). | Ganti jadi upload sungguhan atau label jujur OFFLINE_SAVED (anti-fabrikasi AGENTS §7). |
| **BLOCKER** | p2p | P2PSignalingManager.broadcastPeerInfo() dan onMessageReceived() hanya Log.d — discovery FCM topic 0%, peer tidak pernah dicatat/dihubungi. | Implementasikan FCM signaling sungguhan + WebRTC SDP/ICE exchange. |
| **MAJOR** | multi-user | Seed hanya bila members kosong → tiap HP baru punya data dummy sendiri, bukan data komunitas bersama. | Butuh backend + akun server agar komunitas sinkron. |
| **MINOR** | security | Room SQLite belum terenkripsi (SQLCipher tidak ada) — NIK/SIM/kas plain di file DB. | Enkripsi database atau minimal jelaskan risiko di docs. |
| **MAJOR** | database | Schema Room hilang ['1.json', '2.json', '3.json', '4.json', '8.json', '9.json'] (exportSchema=true tapi 1-4,8,9.json tidak commit) → verifikasi migrasi v1→v13 tidak lengkap. | Generate & commit schema hilang via Room export. |
| **MINOR** | docs | README klaim 55 suite, realita 59 file Test.kt. | Update README badge + SETUP/DEVTOOLS jumlahnya. |
| **MINOR** | code | Hardcode hex Color(0x...) di78 file — melanggar 'No Magic Numbers' AGENTS §1. | Pindah ke DesignTokens/Theme. |
| **MINOR** | test | androidTest cuma 1 file contoh → alur kritis login/SOS/kas belum ter-cover instrumented. | Tambah UI test untuk login, SOS, kas. |
| **MINOR** | ci | Detekt/KtLint tidak ada di CI — hanya line-cap + lint. | Tambah static analysis (sudah masuk backlog). |
| **MAJOR** | ci | CI hanya assembleDebug, tidak ada release/AAB signing & distribusi. | Tambah job release dengan Keystore via Secrets. |

BLOCKER = dilarang klaim multi-user/sinkron sebelum diperbaiki. Jalankan `python3 .github/scripts/audit_minus.py --json` untuk detail mesin.

## Backlog Berjalan

- Room schema `1-4,8,9.json` hilang — generate & commit (detail: `audit_minus.py` category `database`).
- P2P discovery via FCM topic — device menemukan peer terdekat (BAWEL: `audit_minus.py` BLOCKER `p2p`; signaling sekarang cuma `Log.d`)
- Backend multi-user + sinkron nyata — uncomment `firestore`/`auth`, isi `google-services.json`, ganti `BackgroundSyncEngine` dari "hapus antrean lokal" ke upload sungguhan (BAWEL: BLOCKER `firebase`/`fake-sync`)
- Adaptive icon/logo — proses image attachment ke `app/src/main/res/drawable/`
- Release signing & distribusi AAB — CI sekarang hanya `assembleDebug` (BAWEL: MAJOR `ci`)
- [DEFERRED] Fitur Arisan & Dana Talangan Darurat Berbasis Smart Contract / Kas Terverifikasi.
- [DEFERRED] Marketplace Kebutuhan Operasional (Oli, Ban, Helm, Jas Hujan) langsung dari distributor resmi dengan diskon anggota.
- [DEFERRED / PENDING DEV] **Fitur Asuransi** — Integrasi Klaim Asuransi Mandiri & Santunan Cepat bagi driver korban kecelakaan di jalan raya. (Pengembangan ditunda/pending.)
- [DEFERRED] Rekonsiliasi CRDT — conflict-free multi-device tanpa locking.
- [PENDING DEV] **Intercom Helm (Bluetooth/GATT, PTT Radio)** — integrasi pairing & audio intercom helm. Pengembangan ditunda/pending sampai prioritas inti stabil.
- Discovery Peer via FCM Topic — Device menemukan peer terdekat otomatis lewat FCM topic + broadcast IP/port signaling.
- Adaptive Icon & Branding — Konversi logo ke adaptive icon (foreground/background) + splash screen production.
- Play Integrity & Anti-Tamper — Verifikasi integritas aplikasi, R8 aggressive obfuscation, dan stripping simbol sensitif.
- Release Signing & Distribusi — Keystore rilis aman, signing di CI, dan alur distribusi APK production.
- Arisan Digital & Dana Talangan — [DEFERRED — di luar scope rilis awal. Finansial komunitas sementara hanya iuran kas (Kas Transparan phase-1). Jangan dianggap blocker.] Arisan komunitas dan dana talangan darurat berbasis kas terverifikasi.
- Marketplace Kebutuhan Operasional — [DEFERRED — di luar scope rilis awal. Finansial komunitas sementara hanya iuran kas (Kas Transparan phase-1). Jangan dianggap blocker.] Oli, ban, helm, jas hujan langsung dari distributor resmi dengan diskon anggota.
- Klaim Asuransi & Santunan — Integrasi klaim asuransi mandiri dan santunan cepat bagi driver korban kecelakaan. (PENDING DEV)
- Rekonsiliasi CRDT — Conflict-free replicated data types untuk sinkronisasi multi-device tanpa locking.
- Instrumented UI Tests — Tambah cakupan androidTest untuk alur kritis (login, SOS, kas).
- Detekt & KtLint di CI — Static analysis otomatis untuk kualitas kode selain batas 125 baris.
- Gemini Live & AI Assistant Production — Ganti placeholder AI dengan integrasi Gemini API production + rate limiting.
- Dokumentasi Publik — Situs dokumentasi (GitHub Pages) untuk pengurus & developer.

*Total fitur: 33 · 64% selesai.*
