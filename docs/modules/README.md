# Dokumentasi Modul DRG Driver 📚

Direktori ini berisi dokumentasi teknis mendalam untuk setiap modul arsitektural dalam aplikasi **DRG Driver**:

---

## 📑 Daftar Dokumentasi Modul

1. **[🚨 Emergency & Radar (`EMERGENCY_RADAR.md`)](EMERGENCY_RADAR.md)**
   - Algoritma sensor deteksi jatuh / tabrakan keras.
   - Tombol darurat SOS, koordinasi Tim Reaksi Cepat (TRC), dan peta live radar.

2. **[💬 Forum & Bengkel Rekanan (`FORUM_WORKSHOP.md`)](FORUM_WORKSHOP.md)**
   - Arsitektur diskusi *Tanya Rekan (Q&A)* vs *Update Jalur*.
   - Persistensi komentar berantai Room Database dan kontak bengkel rekanan.

3. **[💰 Treasury & Gamifikasi (`TREASURY_GAMIFICATION.md`)](TREASURY_GAMIFICATION.md)**
   - Transparansi pembukuan kas komunitas DRG.
   - Poin solidaritas driver, misi harian, dan reward voucher.

4. **[👤 Autentikasi, Profil & KTA Digital (`AUTH_PROFILE.md`)](AUTH_PROFILE.md)**
   - Login PIN 6 digit, validasi plat nomor polisi, QR Code KTA digital.

5. **[👥 Direktori Anggota & Panel Pengurus (`MEMBERS_ADMIN.md`)](MEMBERS_ADMIN.md)**
   - Manajemen peran anggota (*Ketua, Wakil, Satgas, Bendahara, Anggota*), screening calon anggota, dan audit logs.

6. **[🗺️ Tile Caching & Efisiensi Peta (`RADAR_MAP_CACHE.md`)](RADAR_MAP_CACHE.md)**
   - Manajemen cache multi-level tile OpenStreetMap, LRU disk eviction, penghematan baterai dan kuota.

7. **[🔗 P2P Serverless Architecture (`P2P_SERVERLESS_ARCHITECTURE.md`)](P2P_SERVERLESS_ARCHITECTURE.md)**
   - Setiap Android device = server. Komunikasi P2P via internet, nol server tengah, nol cost.
   - WebRTC DataChannel, STUN/TURN (Google free), FCM signaling, PendingSyncQueue fallback.
   - Arsitektur self-hosted untuk ojol di jalan tanpa harus kumpul di satu tempat.

8. **[🔄 Update Delta OTA (`UPDATE_DELTA.md`)](/docs/UPDATE_DELTA.md)**
   - Update minor tanpa unduh APK 73 MB: manifest `update.json` (≈1 KB) + patch BSDIFF40.
   - Engine `core/update/` (bspatch murni JVM, tanpa NDK), UI di Profil → "Pembaruan Aplikasi".
   - Rilis lewat `.github/workflows/release_delta.yml` (tag `v*`). Kanal preview, bukan produksi.

9. **[ℹ️ Halaman Tentang & Roadmap Otomatis (`ABOUT.md`)](/ABOUT.md)**
   - Tree fitur (tersedia / dibangun / rencana) dan progres pengembangan jujur.
   - Manifest digenerate dari kode sumber oleh `.github/scripts/generate_about_manifest.py`, diverifikasi CI.
   - In-app: `modules/about/` (AboutScreen) dibuka dari Profil → Tentang Aplikasi.
