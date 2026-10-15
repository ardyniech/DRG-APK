# Tentang DRG Driver — Fitur & Progres

> Aplikasi komunitas driver ojek online Driver Riang Gembira (DRG)

**Progres pengembangan keseluruhan: 64%** (12 selesai · 13 dikerjakan · 8 direncanakan)

Versi `f159d3b` · commit `f159d3b` · 2026-10-15 · 15 modul · 369 file sumber · 25807 baris · 43 test suite

> Dokumen ini di-generate otomatis oleh `generate_about_manifest.py`. Status & persentase dihitung dari kode nyata, bukan ditulis manual.

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

## Backlog Berjalan

- P2P discovery via FCM topic — device menemukan peer terdekat
- Adaptive icon/logo — proses image attachment ke `app/src/main/res/drawable/`
- Database v13 explicit migrations complete — `MIGRATION_12_13` + `13.json` schema
- Fitur Arisan & Dana Talangan Darurat Berbasis Smart Contract / Kas Terverifikasi.
- Marketplace Kebutuhan Operasional (Oli, Ban, Helm, Jas Hujan) langsung dari distributor resmi dengan diskon anggota.
- Integrasi Klaim Asuransi Mandiri & Santunan Cepat bagi driver korban kecelakaan di jalan raya.
- Discovery Peer via FCM Topic — Device menemukan peer terdekat otomatis lewat FCM topic + broadcast IP/port signaling.
- Adaptive Icon & Branding — Konversi logo ke adaptive icon (foreground/background) + splash screen production.
- Play Integrity & Anti-Tamper — Verifikasi integritas aplikasi, R8 aggressive obfuscation, dan stripping simbol sensitif.
- Release Signing & Distribusi — Keystore rilis aman, signing di CI, dan alur distribusi APK production.
- Arisan Digital & Dana Talangan — Arisan komunitas dan dana talangan darurat berbasis kas terverifikasi.
- Marketplace Kebutuhan Operasional — Oli, ban, helm, jas hujan langsung dari distributor resmi dengan diskon anggota.
- Klaim Asuransi & Santunan — Integrasi klaim asuransi mandiri dan santunan cepat bagi driver korban kecelakaan.
- Rekonsiliasi CRDT — Conflict-free replicated data types untuk sinkronisasi multi-device tanpa locking.
- Instrumented UI Tests — Tambah cakupan androidTest untuk alur kritis (login, SOS, kas).
- Detekt & KtLint di CI — Static analysis otomatis untuk kualitas kode selain batas 125 baris.
- Gemini Live & AI Assistant Production — Ganti placeholder AI dengan integrasi Gemini API production + rate limiting.
- Dokumentasi Publik — Situs dokumentasi (GitHub Pages) untuk pengurus & developer.

*Total fitur: 33 · 64% selesai.*
