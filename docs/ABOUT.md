# Tentang DRG Driver — Fitur & Progres

> Aplikasi komunitas driver ojek online Driver Riang Gembira (DRG)

**Progres pengembangan keseluruhan: 64%** (12 selesai · 13 dikerjakan · 8 direncanakan)

Versi `2fbfa24` · commit `2fbfa24` · 2026-11-09 · 17 modul · 457 file sumber · 32521 baris · 53 test suite

**Kekurangan (Bawel Audit): 22 temuan (6 BLOCKER · 12 MAJOR · 4 MINOR · 0 NOTE)**

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
| Dashboard Driver | Sedang Dibangun | 95% |
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
| **BLOCKER** | firebase | firestore dependency dikomentari → multi-user tidak bisa sinkron. | Uncomment firestore + auth + credentials bersama google-services.json asli. |
| **BLOCKER** | firebase | auth dependency dikomentari → akun server tidak ada, login lokal saja. | Uncomment auth + Credential Manager. |
| **BLOCKER** | firebase | app/google-services.json HILANG (hanya .example) → FCM/Auth/AI tidak jalan produksi, P2P signaling gagal join topic. | Isi google-services.json asli dari Firebase Console (jangan commit key prod, tapi CI perlu). |
| **BLOCKER** | auth | Seed super-admin PIN default 1234 di .env.example — kalau tidak diganti, akun pengurus bisa ditebak. | Ganti sebelum rilis; tolak login bila PIN masih default. |
| **MAJOR** | permission | Permission RECORD_AUDIO belum dideklarasi (PTT/notif/foreground/Bluetooth). | Tambah di manifest + runtime request. |
| **MAJOR** | permission | Permission POST_NOTIFICATIONS belum dideklarasi (PTT/notif/foreground/Bluetooth). | Tambah di manifest + runtime request. |
| **MAJOR** | permission | Permission FOREGROUND_SERVICE belum dideklarasi (PTT/notif/foreground/Bluetooth). | Tambah di manifest + runtime request. |
| **MAJOR** | permission | Permission BLUETOOTH_CONNECT belum dideklarasi (PTT/notif/foreground/Bluetooth). | Tambah di manifest + runtime request. |
| **BLOCKER** | p2p | P2PSignalingManager.broadcastPeerInfo() dan onMessageReceived() hanya Log.d — discovery FCM topic 0%, peer tidak pernah dicatat/dihubungi. | Implementasikan FCM signaling sungguhan + WebRTC SDP/ICE exchange. |
| **MAJOR** | p2p | P2PManager.connectToPeer() bikin ICE candidate string palsu 'candidate:1...' — bukan SDP asli. | Ganti dengan jalur signaling SDP/ICE penuh + STUN/TURN relay. |
| **MAJOR** | multi-user | Seed hanya bila members kosong → tiap HP baru punya data dummy sendiri, bukan data komunitas bersama. | Butuh backend + akun server agar komunitas sinkron. |
| **MAJOR** | backup | allowBackup=false + tidak ada cloud backup/sync → ganti HP = data hilang permanen. | Tambah auto-backup terenkripsi atau export/import kas terenkripsi. |
| **MAJOR** | privacy | MIGRATION_12_13 DEFAULT kolom members berisi NIK/SIM/no HP dummy (3507123456780001) — data pribadi palsu masuk skema. | Ganti DEFAULT jadi '' dan jangan hardcode PII. |
| **MAJOR** | security | PIN hanya SHA-256(plain) di SharedPrefs, bukan PBKDF2/Keystore — klaim DevTools 'Keystore PIN & PBKDF2' bohong. | Ganti ke PBKDF2 + Android Keystore atau label jujur di DevTools. |
| **MINOR** | security | Room SQLite belum terenkripsi (SQLCipher tidak ada) — NIK/SIM/kas plain di file DB. | Enkripsi database atau minimal jelaskan risiko di docs. |
| **MAJOR** | database | Schema Room hilang ['1.json', '2.json', '3.json', '4.json', '8.json', '9.json'] (exportSchema=true tapi 1-4,8,9.json tidak commit) → verifikasi migrasi v1→v13 tidak lengkap. | Generate & commit schema hilang via Room export. |
| **MAJOR** | code | 1 TODO/FIXME tersisa (mis. DashboardHeader.kt: StatusChips(currentStatus = member.currentStatus, onStatusChange = { _, _ -> /* ). | Implementasikan sebelum klaim Selesai. |
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
- [DEFERRED] Integrasi Klaim Asuransi Mandiri & Santunan Cepat bagi driver korban kecelakaan di jalan raya.
- [DEFERRED] Rekonsiliasi CRDT — conflict-free multi-device tanpa locking.
- Discovery Peer via FCM Topic — Device menemukan peer terdekat otomatis lewat FCM topic + broadcast IP/port signaling.
- Adaptive Icon & Branding — Konversi logo ke adaptive icon (foreground/background) + splash screen production.
- Play Integrity & Anti-Tamper — Verifikasi integritas aplikasi, R8 aggressive obfuscation, dan stripping simbol sensitif.
- Release Signing & Distribusi — Keystore rilis aman, signing di CI, dan alur distribusi APK production.
- Arisan Digital & Dana Talangan — [DEFERRED — di luar scope rilis awal. Finansial komunitas sementara hanya iuran kas (Kas Transparan phase-1). Jangan dianggap blocker.] Arisan komunitas dan dana talangan darurat berbasis kas terverifikasi.
- Marketplace Kebutuhan Operasional — [DEFERRED — di luar scope rilis awal. Finansial komunitas sementara hanya iuran kas (Kas Transparan phase-1). Jangan dianggap blocker.] Oli, ban, helm, jas hujan langsung dari distributor resmi dengan diskon anggota.
- Klaim Asuransi & Santunan — Integrasi klaim asuransi mandiri dan santunan cepat bagi driver korban kecelakaan.
- Rekonsiliasi CRDT — Conflict-free replicated data types untuk sinkronisasi multi-device tanpa locking.
- Instrumented UI Tests — Tambah cakupan androidTest untuk alur kritis (login, SOS, kas).
- Detekt & KtLint di CI — Static analysis otomatis untuk kualitas kode selain batas 125 baris.
- Gemini Live & AI Assistant Production — Ganti placeholder AI dengan integrasi Gemini API production + rate limiting.
- Dokumentasi Publik — Situs dokumentasi (GitHub Pages) untuk pengurus & developer.

*Total fitur: 33 · 64% selesai.*
