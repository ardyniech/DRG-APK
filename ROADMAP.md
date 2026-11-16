# Peta Jalan Pengembangan (Roadmap) — DRG Driver 🗺️🚀

Dokumen ini memetakan visi, target rilis, dan prioritas pengembangan ekosistem **DRG Driver**.
Kondisi real otomatis tersinkron ke [docs/ABOUT.md](docs/ABOUT.md) (Single Source of Truth).

> **Keputusan scope finansial:** Finansial komunitas sementara *hanya iuran kas* (Kas Transparan). Marketplace & arisan adalah **[DEFERRED] — di luar scope rilis awal, bukan blocker.**

---

## 🎯 Fase 1 — Fondasi Komunitas & Darurat (Selesai - v1.0.0)
- [x] Arsitektur Clean MVI + Jetpack Compose Material 3 (< 125 baris per file).
- [x] Persistensi Local-First dengan Room Database v14 (Flow reaktif, 26 tabel, schema JSON `1..14` lengkap).
- [x] Tombol Darurat Panic SOS & Sensor Deteksi Tabrakan Akselerometer.
- [x] Radar Pantauan Peta OpenStreetMap (OSM) dengan LRU Tile Caching.
- [x] **Iuran Kas (finansial wajib rilis awal):** Laporan Keuangan Kas Transparan (Kas Masuk, Kas Keluar, Saldo Bersih).
- [x] Forum Diskusi Internal, Update Jalur & Komentar Berantai.
- [x] KTA Digital QR Code & Direktori Anggota Terverifikasi.
- [x] Standar CI/CD GitHub Actions & Keamanan Jaringan TLS 1.3.

---

## ⚡ Fase 2 — Komunikasi P2P Serverless & Cloud Production (Selesai - v1.1.0)
- [x] Modul `core/p2p/` — WebRTC DataChannel, STUN/TURN, `P2PSignalingManager` (FCM topic + Firestore) & `P2PSelfHostServer` TCP/HTTP listener.
- [x] Rekonsiliasi `LwwCrdtReconciler` + `BackgroundSyncEngine` terhubung ke Cloud Firestore 2-way sync (`FirestoreCloudSyncMapper`).
- [x] Firebase Auth (Google Credential Manager), App Check, Security Rules zero-trust, dan Firebase AI Logic (`DrgAiAssistantEngine`).
- [x] Runtime Anti-Tamper & Integrity (`AppIntegrityVerifier`) + R8 ProGuard release hardening.
- [x] Adaptive Icon & PNG 5 densitas (`mdpi`–`xxxhdpi` square + circular round).
- [x] Pipeline CI/CD & Delta OTA Release Signing (`android_ci.yml` + `release_delta.yml`).

---

## ⏸️ Fase 3 — Finansial & Kemitraan [DEFERRED — di luar scope rilis awal]
> Bukan blocker. Fokus rilis awal tetap **iuran kas saja**. Rincian bawel ada di `audit_minus.py` NOTE `planning`.
- [ ] [DEFERRED] Fitur Arisan & Dana Talangan Darurat Berbasis Smart Contract / Kas Terverifikasi.
- [ ] [DEFERRED] Marketplace Kebutuhan Operasional (Oli, Ban, Helm, Jas Hujan) langsung dari distributor resmi dengan diskon anggota.
- [ ] [DEFERRED / PENDING DEV] **Fitur Asuransi** — Integrasi Klaim Asuransi Mandiri & Santunan Cepat bagi driver korban kecelakaan di jalan raya. (Pengembangan ditunda/pending.)

## ⏸️ Fitur Lain — Opsional / Pending Development
- [ ] [PENDING DEV] **Intercom Helm (Bluetooth/GATT, PTT Radio)** — integrasi pairing & audio intercom helm lanjutan.
