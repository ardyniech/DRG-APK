# Dokumentasi Migrasi Database Room — DRG Driver 🗄️

Dokumen ini menjelaskan evolusi skema Room Database internal aplikasi **DRG Driver** (`drg_community.db`), strategi migrasi, dan riwayat versi dari `v1` hingga `v10`.

---

## 🏛️ Arsitektur & Strategi Migrasi

Aplikasi menggunakan **Room Database** dengan `exportSchema = true` (disimpan di `app/schemas/com.example.core.database.AppDatabase/`).

### Kebijakan Migrasi:
1. **Fase Pengembangan & Prototipe**:
   - Dikonfigurasi dengan `.fallbackToDestructiveMigration()`.
   - Menjamin perangkat penguji tidak mengalami SQLite Crash / `IllegalStateException` saat ada penambahan kolom atau entitas baru selama iterasi cepat.
2. **Fase Produksi Stabil**:
   - Menggunakan objek `Migration(from, to)` eksplisit yang didaftarkan pada builder `addMigrations(...)`.
   - Seluruh mutasi struktur tabel wajib menyertakan skrip DDL SQL (`ALTER TABLE`, `CREATE TABLE IF NOT EXISTS`, atau pembuatan index baru).

---

## 📜 Riwayat Versi Skema (Version History)

### **v1 — Skema Inti (Core Schema)**
- `members`: Profil pengemudi, status keanggotaan, dan role kepengurusan.
- `emergency_alerts`: Notifikasi SOS darurat dan titik koordinat GPS.
- `posko_locations`: Titik basecamp dan posko istirahat komunitas.
- `kas_transactions`: Transaksi kas komunitas (pemasukan & pengeluaran).
- `forum_posts`: Postingan forum diskusi dan pembaruan jalur.
- `workshop_partners`: Direktori bengkel rekanan dan diskon servis.

### **v2 — v4: Gamifikasi & Preferensi Notifikasi**
- **v2**: Menambahkan tabel `point_transactions` untuk pencatatan riwayat pemberian XP/poin loyalitas driver.
- **v3**: Menambahkan tabel `notification_preferences` untuk pengaturan notifikasi audio sirine SOS, filter notifikasi begal, kecelakaan, dan kas.
- **v4**: Menambahkan tabel `map_tile_cache` untuk metadata caching disk tile OpenStreetMap offline.

### **v5 — v6: Pengaturan & Antrean Sinkronisasi (Offline First)**
- **v5**: Menambahkan tabel `app_state_settings` untuk key-value storage pengaturan aplikasi lokal.
- **v6**: Menambahkan tabel `pending_sync_queue` untuk pola rekonsiliasi data *Local-First* (menyimpan payload JSON saat offline untuk dikirim ulang saat online).

### **v7 — v9: Audit Pengurus & Diskusi Berantai**
- **v7**: Menambahkan tabel `admin_logs` untuk audit trail tindakan pengurus (screening anggota, perubahan hak akses).
- **v8 & v9**: Optimasi indeks pencarian pada `emergency_alerts(isActive, timestamp)` dan `kas_transactions(type, timestamp)`.

### **v10: Diskusi Berantai & Optimasi Komentar**
- Menambahkan entitas `forum_comments` untuk mendukung komentar berantai (*threaded discussions*) pada postingan forum.
- Menambahkan indeks sekunder pada `forum_comments(postId)` untuk query relasi yang cepat.
- Penyelarasan relasi cascade dan pengujian integritas via Robolectric.

### **v11 — Skema Terkini (Current Release: Role-Based Access Control & Security)**
- Menambahkan entitas `member_role_permissions` untuk mengelola hak akses granular (kelola kas, verifikasi driver, siaran SOS, kelola posko) dan status verifikasi akun.
- Menambahkan entitas `role_audit_logs` untuk audit trail kepengurusan (pencatatan promosi/demosi jabatan, alasan, dan pembuat aksi).
- Indeks sekunder pada `member_role_permissions(role, verificationStatus)` dan `role_audit_logs(targetMemberId, timestamp)` untuk query berkinerja tinggi.
