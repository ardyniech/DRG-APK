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

### **v11: Role-Based Access Control & Security**
- **Entitas `member_role_permissions`**:
  - Kolom: `memberId` (Primary Key, String), `role` (Enum MemberRole), `assignedByMemberId` (String), `assignedByMemberName` (String), `assignedTimestamp` (Long), `canManageKas` (Boolean), `canVerifyDrivers` (Boolean), `canBroadcastSos` (Boolean), `canManagePosko` (Boolean), `verificationStatus` (Enum VerificationStatus), `notes` (String).
  - Indeks Sekunder: `Index(value = ["role"])`, `Index(value = ["verificationStatus"])`.
  - Fungsi: Mengisolasi hak akses per-driver secara granular, memisahkan hak administratif spesifik dari peran dasar tanpa perlu mutasi entitas profil.
- **Entitas `role_audit_logs`**:
  - Kolom: `id` (Primary Key, String), `targetMemberId` (String), `targetMemberName` (String), `previousRole` (String), `newRole` (String), `actionByMemberId` (String), `actionByMemberName` (String), `reason` (String), `timestamp` (Long).
  - Indeks Sekunder: `Index(value = ["targetMemberId"])`, `Index(value = ["timestamp"])`.
  - Fungsi: Audit trail tak terhapus (*immutable audit log*) untuk merekam setiap promosi, demosi, dan perubahan izin kepengurusan komunitas.

### **v12 — Skema Terkini (Current Release: Posko Check-In Event Logger)**
- **Entitas `posko_check_ins`**:
  - Kolom: `id` (Primary Key, String), `poskoId` (String), `poskoName` (String), `memberId` (String), `memberName` (String), `driverPlate` (String), `checkInTimestamp` (Long), `pointsAwarded` (Int), `checkInMethod` (String).
  - Indeks Sekunder: `Index(value = ["poskoId"])`, `Index(value = ["memberId"])`, `Index(value = ["checkInTimestamp"])`.
  - Fungsi: Pencatatan riwayat singgah/check-in driver di Posko Komunitas untuk integrasi poin loyalitas dan kehadiran kopdar.
- **Strategi Migrasi DDL (v11 -> v12)**:
  - `CREATE TABLE IF NOT EXISTS posko_check_ins (...)` dengan indeks `index_posko_check_ins_poskoId`, `index_posko_check_ins_memberId`, dan `index_posko_check_ins_checkInTimestamp`.
  - Pengujian tervalidasi 100% via suite Robolectric `PoskoCheckInRepositoryTest`.
