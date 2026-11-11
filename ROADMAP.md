# Peta Jalan Pengembangan (Roadmap) — DRG Driver 🗺️🚀

Dokumen ini memetakan visi, target rilis, dan prioritas pengembangan ekosistem **DRG Driver**.
Kondisi real per 2026-11-09 ada di [README — Kondisi Real](README.md#️-kondisi-real-saat-ini-jujur-2026-11-09--baca-ini-dulu) dan otomatis di [docs/ABOUT.md](docs/ABOUT.md).

> **Keputusan scope finansial (2026-11-09):** Finansial komunitas sementara *hanya iuran kas* (Kas Transparan). Marketplace & arisan adalah **[DEFERRED] — di luar scope rilis awal, bukan blocker.** Jangan jadikan target Q1 2027; evaluasi ulang setelah sinkron multi-user stabil.

---

## 🎯 Fase 1 — Fondasi Komunitas & Darurat (Selesai - v1.0.0)
- [x] Arsitektur Clean MVI + Jetpack Compose Material 3 (< 125 baris per file).
- [x] Persistensi Local-First dengan Room Database v13 (Flow reaktif, 23 entitas).
- [x] Tombol Darurat Panic SOS & Sensor Deteksi Tabrakan Akselerometer.
- [x] Radar Pantauan Peta OpenStreetMap (OSM) dengan LRU Tile Caching.
- [x] **Iuran Kas (finansial wajib rilis awal):** Laporan Keuangan Kas Transparan (Kas Masuk, Kas Keluar, Saldo Bersih).
- [x] Forum Diskusi Internal, Update Jalur & Komentar Berantai.
- [x] KTA Digital QR Code & Direktori Anggota Terverifikasi.
- [x] Standar CI/CD GitHub Actions & Keamanan Jaringan TLS 1.3.
- [ ] Room schema `1-4,8,9.json` hilang — generate & commit (detail: `audit_minus.py` category `database`).

---

## ⚡ Fase 2 — Komunikasi P2P Serverless (Q4 2026) — BELUM multi-user
- [x] Modul `core/p2p/` — WebRTC DataChannel, STUN/TURN (Google free), FCM signaling (kerangka)
- [x] Self-host server per device (port 8080) — setiap Android device = server
- [x] PendingSyncQueue fallback — P2P gagal → simpan lokal, retry saat koneksi balik
- [x] 10 file P2P + 3 test files + `docs/P2P_SERVERLESS_ARCHITECTURE.md`
- [ ] P2P discovery via FCM topic — device menemukan peer terdekat (BAWEL: `audit_minus.py` BLOCKER `p2p`; signaling sekarang cuma `Log.d`)
- [ ] Backend multi-user + sinkron nyata — uncomment `firestore`/`auth`, isi `google-services.json`, ganti `BackgroundSyncEngine` dari "hapus antrean lokal" ke upload sungguhan (BAWEL: BLOCKER `firebase`/`fake-sync`)
- [ ] Adaptive icon/logo — proses image attachment ke `app/src/main/res/drawable/`
- [ ] Release signing & distribusi AAB — CI sekarang hanya `assembleDebug` (BAWEL: MAJOR `ci`)

---

## ⏸️ Fase 3 — Finansial & Kemitraan [DEFERRED — di luar scope rilis awal]
> Bukan blocker. Fokus rilis awal tetap **iuran kas saja**. Rincian bawel ada di `audit_minus.py` NOTE `planning`.
- [ ] [DEFERRED] Fitur Arisan & Dana Talangan Darurat Berbasis Smart Contract / Kas Terverifikasi.
- [ ] [DEFERRED] Marketplace Kebutuhan Operasional (Oli, Ban, Helm, Jas Hujan) langsung dari distributor resmi dengan diskon anggota.
- [ ] [DEFERRED / PENDING DEV] **Fitur Asuransi** — Integrasi Klaim Asuransi Mandiri & Santunan Cepat bagi driver korban kecelakaan di jalan raya. (Pengembangan ditunda/pending.)
- [ ] [DEFERRED] Rekonsiliasi CRDT — conflict-free multi-device tanpa locking.

## ⏸️ Fitur Lain — Pending Development
- [ ] [PENDING DEV] **Intercom Helm (Bluetooth/GATT, PTT Radio)** — integrasi pairing & audio intercom helm. Pengembangan ditunda/pending sampai prioritas inti stabil.
