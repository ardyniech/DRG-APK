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
