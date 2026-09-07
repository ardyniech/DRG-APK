# PLAN DEVELOPMENT & ROADMAP APLIKASI DRG (DRIVER RIANG GEMBIRA)

## 📌 VERSI 1: PONDASI SISTEM SOLID & ARSITEKTUR KANONIK (CORE MVP)
Versi 1 berfokus pada fondasi teknis yang kuat, performa lokal *local-first*, dan isolasi modul yang ketat tanpa "vibe coding".

### 1. Core Architecture & Local-First Engine
- **MVVM + Clean Architecture**: Pemisahan total antara UI Composable, ViewModel StateFlow, Repository, dan Room Database.
- **Room SQLite Local Storage**: Sinkronisasi data lokal tanpa ketergantungan penuh ke cloud.
- **Vibrant M3 Design Tokens**: Penggunaan token warna dinamis (Emerald, Amber, Indigo, Panic Red) tanpa hardcoded hex di composable.

### 2. Multi-Role RBAC System (7 Peranan Komunitas)
- **Ketua / Waket**: Hak akses administrasi penuh & siaran pengumuman.
- **Sekretaris**: Screening pendaftaran & verifikasi anggota baru.
- **Bendahara**: Pencatatan kas & upload laporan keuangan.
- **Satgas**: Penanganan sinyal darurat SOS & navigasi patroli.
- **Dewan Etika**: Pengawasan kode etik, peninjauan rating, & sanksi.
- **Penasihat**: Akses audit read-only.
- **Anggota Biasa**: Penggunaan fitur harian (Radar, SOS, Forum, KTA).

### 3. Fitur Utama Versi 1
- **Radar GPS Basic Canvas**: Pemantauan lokasi driver dan posko basecamp.
- **Darurat SOS 1-Tap**: Pemicu sinyal mogok, kecelakaan, atau begal.
- **Kas Transparan**: Transparansi keuangan kas dan dana darurat.
- **KTA Digital & Profil**: Kartu Tanda Anggota dengan status verifikasi resmi.
- **Forum & Bengkel Rekanan**: Diskusi teknis otomotif dan diskon servis.

---

## 🚀 VERSI 2: GAMIFIKASI LANJUTAN, NOTIFIKASI PUSH & INTERACTIVE HAZARD RADAR

Versi 2 memperkaya keterlibatan anggota (community engagement), keselamatan di jalanan, dan gamifikasi terukur.

### 1. Sistem Gamifikasi Terukur & Weighted Peer-Gifting
- **System Lencana (Badges Collection)**:
  - *Pejuang Kopdar* (Hadir 5x berturut-turut)
  - *Pahlawan Aspal* (Merespon >3 panggilan SOS Satgas)
  - *Donatur Setia Kas* (Lunas iuran kas 6 bulan)
  - *Suhu Mekanik Forum* (Jawaban solusi terbanyak di forum)
  - *Eksekutor Tugas DRG* (Selesaikan 3 tugas komunitas)

- **Bobot Skor Poin Berdasarkan Role Pemberi (Role-Weighted Peer Points)**:
  - **Dewan Etika**: Bobot **+5 Poin**
  - **Ketua / Wakil Ketua**: Bobot **+4 Poin**
  - **Pengurus (Sekretaris, Bendahara, Satgas, Penasihat)**: Bobot **+3 Poin**
  - **Korban / Beneficiary Direct**: Bobot **+2 Poin** *(Driver yang merasakan langsung bantuan/pertolongan dari driver lain)*
  - **Anggota Biasa**: Bobot **+1 Poin**

- **Tugas Komunitas (Community Tasks)**:
  - Tugas khusus yang diterbitkan pengurus (Patroli Jalur Rawan, Kerja Bakti Posko, Distro Rompi).
  - Menyelesaikan tugas mendapatkan **Poin Khusus (XP)** otomatis.

- **Katalog Tukar Poin (Rewards Store)**:
  - Penukaran Poin Loyalitas dengan keuntungan komunitas (Voucher Bensin Pertalite 25rb, Bebas Iuran Kas 1 Bulan, Stiker Metalik DRG, Diskon Servis 25% Bengkel Rekanan).

- **Papan Peringkat (Leaderboard)**:
  - Pemeringkatan driver paling aktif & berkontribusi secara real-time.

### 2. Sistem Push Notification & Preference Matrix
- **Pengaturan Preferensi Per-User (Notification Matrix)**:
  - Toggle terpisah untuk *Peringatan Keselamatan Real-time SOS*, *Pengingat Kopdar Bulanan*, *Pembaruan Kas Komunitas*, *Aktivitas Forum*, dan *Apresiasi Poin Rekan*.
- **Pusat Notifikasi Komunitas**:
  - Halaman terpusat melihat semua pengumuman, riwayat peringatan, dan pemberitahuan poin.

### 3. Peta Interaktif Lanjutan & Area Rawan (Hazard Layer)
- **Visualisasi Satelit Minimalis Berbasis Canvas**:
  - Grid jalanan kontras tinggi, ring jarak radar, dan gelombang sweep radar 60fps.
- **Layer Area Rawan (Hazard Map)**:
  - Penandaan titik bahaya di jalan (*Jalur Rawan Begal*, *Genangan Banjir*, *Jalan Berlubang Parah*, *Macet Total/Penyekatan*).
  - Mekanisme konfirmasi & upvote bahaya dari sesama driver.
- **Privacy Consent Toggle (Bagi Lokasi Live)**:
  - Tombol persetujuan (*Consent Switch*) pada Radar. Saat nonaktif, lokasi driver tersembunyi dari radar demi privasi pengguna.

---

## 🔍 AUDIT MATRIX & SKENARIO PENGUJIAN NEGATIF
1. **Bad Input / Malformed Data**: Menangani input kosong / format tidak valid pada dialog penandaan bahaya & pemberian poin.
2. **Cross-Module Failure**: Proteksi saat DAO atau database gagal mengembalikan data.
3. **UI Dead-End**: Menghindari infinite loading dan memberikan pesan error / snackbar transparan kepada pengguna.
