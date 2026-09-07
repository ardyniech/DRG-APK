# Rencana Detail 100 Item Implementasi DRG Malang Raya (Auth & Improvement)

## Fase 1: Perencanaan & Arsitektur Landing Page (Items 1-10)
1. Mendefinisikan rute dan state terpusat `AuthStage` (LANDING, LOGIN, REGISTER)
2. Membuat visualisasi representasi logo DRG menggunakan ikon bersayap
3. Mengatur skema warna Grab Green ramah mata (bebas dark theme & monolitik putih)
4. Membuat model data promosi interaktif `PromoData`
5. Mengatur rasio kontainer promosi agar adaptif di layar tablet maupun ponsel cerdas
6. Merancang navigasi transisi tanpa hambatan menggunakan `Crossfade`
7. Mengatur padding luar sebesar 24dp untuk keseimbangan tata letak
8. Mencegah cognitive overload dengan batasan maksimal satu CTA primer di landing page
9. Mengatur durasi pergantian otomatis promosi (auto-slide) selama 4000ms
10. Menyediakan slot testing komparatif `testTag` untuk keperluan otomasi pengujian

## Fase 2: Komponen Atomik UI Landing Page (Items 11-20)
11. Menghadirkan logo kustom "DRG" dengan perpaduan bayangan melengkung
12. Menambahkan teks judul utama dengan font bold `DrgTextDark`
13. Mengonfigurasi subjudul dinamis *"Kompak • Solid • Gacor di Malang Raya"*
14. Membuat kontainer promosi berlatar belakang putih dengan sudut membulat 20dp
15. Menyematkan emoji berukuran besar (42sp) untuk representasi visual yang kuat
16. Mendesain teks isi promosi berukuran 12sp dengan tinggi baris 18sp
17. Membuat indikator navigasi titik-titik (dot indicators) di bagian bawah promosi
18. Mengatur titik aktif agar melebar secara dinamis (16dp) sebagai penunjuk aktif
19. Membuat tombol primer "Masuk Anggota" dengan tinggi standar kenyamanan sentuh (52dp)
20. Membuat tombol sekunder "Daftar Driver Baru" dengan gaya transparan berbingkai (outlined)

## Fase 3: Visual & Efek Transisi Animasi (Items 21-30)
21. Menambahkan transisi fade-in saat pengguna membuka gerbang masuk aplikasi
22. Mengonfigurasi sensor haptic feedback mikro saat tombol CTA diketuk
23. Mengatur interpolasi spring dynamics untuk perubahan dot indicators
24. Menerapkan anti-aliasing penuh pada elemen border kartu promosi
25. Menghindari frame drop dengan membatasi recomposition pada deteksi interval
26. Mengatur transisi halus cross-module ke halaman dashboard utama setelah login sukses
27. Menyediakan transisi tombol kembali beranimasi responsif
28. Mengatur background gradient modern `DrgBackgroundGradient` sebagai fondasi visual
29. Menyertakan efek sentuhan ripple bawaan Material 3 di seluruh tombol interaktif
30. Melindungi dari tumpang tindih layar dengan pengaturan berat kontainer (`weight`) secara presisi

## Fase 4: Perancangan Form Login Interaktif (Items 31-40)
31. Mendesain form login dengan back button berikon `ArrowBack` bawaan material
32. Menambahkan pesan sambutan *"Selamat Datang"* di atas formulir masuk
33. Menyediakan kolom teks masukan `OutlinedTextField` untuk nomor HP atau KTA ID anggota
34. Menghubungkan form login ke daftar anggota aktif riil dari database (SeedData)
35. Merancang fitur "Quick Demo Login" berupa slider horizontal `LazyRow`
36. Menampilkan kartu saku anggota (mini member card) berukuran lebar tetap 130dp
37. Menandai kartu cepat dengan bingkai hijau menyala bila dipilih
38. Menyertakan haptic feedback bertipe `LongPress` ketika kartu cepat diketuk
39. Menambahkan penanganan pesan galat (error state) yang informatif dan awam
40. Memvalidasi ketersediaan input sebelum tombol "Masuk" dapat diproses

## Fase 5: Perancangan Form Registrasi (Malang Theme) (Items 41-50)
41. Membuat layout scrollable menggunakan `verticalScroll(rememberScrollState())`
42. Merancang judul "Daftar Anggota DRG" dengan penegas identitas Arema
43. Menyediakan kolom masukan nama lengkap pendaftar
44. Menyediakan kolom masukan nomor ponsel aktif berformat angka
45. Mendesain kolom masukan nomor pelat kendaraan khusus area Malang Raya
46. Merancang kolom tipe/model sepeda motor pendukung penarikan
47. Menyediakan pemilih wilayah operasional basis (drop-down menu)
48. Mendaftarkan 8 area Malang Raya (Klojen, Lowokwaru, Blimbing, Sukun, Batu, dll.)
49. Menampilkan icon pemicu menu jatuh (drop-down trailing icon)
50. Menyesuaikan tombol registrasi agar terkunci (disabled) sebelum semua field terisi

## Fase 6: Validasi Input & Validasi Pelat Nomor N Jawa Timur (Items 51-60)
51. Mengonfigurasi auto-uppercase otomatis pada isian pelat nomor kendaraan
52. Menambahkan validasi ketat awalan huruf pelat nomor Jawa Timur (`N`)
53. Membantu pengguna dengan prepending otomatis huruf "N " di awal input pelat nomor
54. Membatasi panjang isian nomor telepon agar sesuai standar telekomunikasi Indonesia
55. Memastikan isian nama lengkap tidak mengandung karakter aneh atau simbol bahaya
56. Mengubah status tombol kirim menjadi aktif hanya saat formulir valid penuh
57. Memberikan respon mikro-vibrasi saat formulir pendaftaran berhasil diserahkan
58. Menyediakan umpan balik visual instan (isError) pada text field jika validasi gagal
59. Menjaga state formulir tetap stabil saat terjadi rotasi layar (savedInstanceState aware)
60. Menyediakan pembersihan state input secara otomatis saat berpindah halaman

## Fase 7: State Management & Session Integration (Items 61-70)
61. Menambahkan state reaktif `isLoggedIn` di dalam kelas `DRGViewModel`
62. Menghubungkan fungsi login ke perpindahan state session `currentMemberId`
63. Membuat fungsi `logout` yang menghapus tanda login dan melempar pengguna ke landing
64. Mengintegrasikan proses pendaftaran dengan fungsi `registerNewDriver` di ViewModel
65. Menyimpan data pendaftaran baru langsung ke database lokal Room secara aman
66. Menampilkan notifikasi persetujuan pendaftaran (screening pending status) di panel Admin
67. Menampilkan nama anggota yang masuk di bar header atas secara langsung
68. Memperbarui KTA dinamis dengan data anggota baru yang berhasil login
69. Memastikan data radar live memperbarui lokasi driver sesuai ID anggota yang login
70. Melacak sesi aktif agar tidak terputus saat pengguna menutup tab emulator

## Fase 8: Onboarding & Progressive Disclosure (Just-in-Time) (Items 71-80)
71. Menyembunyikan menu-menu rumit bagi anggota baru yang berstatus belum diverifikasi
72. Menampilkan lencana status "Pending Screening" di beranda anggota baru
73. Menyediakan kartu instruksi "Langkah Selanjutnya" bagi pendaftar baru
74. Memperkenalkan tombol SOS Darurat secara bertahap untuk mencegah salah pencet
75. Membuka akses ke Koperasi dan Merch secara eksklusif bagi anggota yang sudah terverifikasi
76. Menampilkan analitik kas hanya setelah ada volume data transaksi terekam
77. Membatasi penulisan postingan di forum bagi anggota baru sebagai mitigasi spamming
78. Mengaktifkan radar pemantauan live secara bersyarat setelah persetujuan izin lokasi diberikan
79. Menyediakan lencana penghargaan junior rider sebagai insentif onboarding
80. Menyediakan panduan praktis pendaftaran lewat satu Call-to-Action utama

## Fase 9: Perbaikan Fitur Terkait (Ruang Improvement - Kas, PTT & AI) (Items 81-90)
81. Melokalisasi asisten AI agar mengenali kuliner dan tempat nongkrong di Malang
82. Meningkatkan sensitivitas tombol Push-to-Talk (PTT) radio komunikasi Malang
83. Memperbaiki pencatatan histori transaksi kas agar terhitung real-time dan transparan
84. Melokalisasi daftar bengkel rekanan servis motor di sepanjang jalan Kawi dan Suhat
85. Menambahkan asisten navigasi rute aman bebas ranjau paku di jalan Karanglo
86. Meningkatkan kinerja render loop peta radar agar hemat konsumsi daya baterai HP
87. Menyertakan sistem notifikasi pengumuman darurat bahaya banjir di jalan Galunggung Malang
88. Menambahkan fitur salin (copy-paste) cepat KTA ID dari profil anggota
89. Menyediakan fitur pencarian cepat posko terdekat dari koordinat posisi driver
90. Menyediakan riwayat transaksi koin loyalitas komunitas

## Fase 10: Pengujian, Audit Adversarial & Kompilasi Akhir (Items 91-100)
91. Menguji skenario login menggunakan nomor ponsel acak yang tidak terdaftar (harus gagal)
92. Menguji pendaftaran anggota baru tanpa isian pelat nomor (tombol harus non-aktif)
93. Menguji fungsionalitas tombol kembali di layar registrasi dan login
94. Memastikan tidak ada import duplikat atau library ilegal dalam file kotlin baru
95. Melakukan verifikasi linting berkas baru untuk mencegah kesalahan tata bahasa pemrograman
96. Menjalankan kompilasi proyek `:app:compileDebugKotlin` secara menyeluruh
97. Menguji aliran data login-logout berulang kali untuk memastikan stabilitas state
98. Mengaudit kebocoran memori pada loop animasi iklan promosi di landing page
99. Menguji kepatuhan ukuran file agar tidak ada berkas baru yang melampaui 125 baris kode
100. Mendokumentasikan dan mempublikasikan status kompilasi sukses kepada pengguna!

---

## Laporan Audit & Kemajuan Implementasi (Sesi 2)
- **Item 70 (Persistensi Sesi)**: [SELESAI] Terintegrasi via `SharedPreferences` terpusat yang diinjeksi ke dalam `DRGViewModel`.
- **Item 72 & 73 (Onboarding & Progressive Disclosure)**: [SELESAI] Menambahkan banner "Pendaftaran Dalam Screening" di beranda anggota baru dengan checklist onboarding Arema, kontak cepat satgas, serta tombol jalan pintas persetujuan instan demo penguji.
- **Item 81 (Lokalisasi Asisten AI)**: [SELESAI] Mengintegrasikan menu pertanyaan rekomendasi kuliner (Bakso President, Cwie Mie Pojok, STMJ Glintung) & cangkrukan arek Malang ke dalam `AiAssistantDialog`.
- **Item 82 (Radio PTT Malang)**: [SELESAI] Mengubah sistem Walkie-Talkie statis menjadi penyeleksi saluran Arema yang dinamis (Suhat, Klojen, Karanglo, Batu) dengan haptic getar yang responsif.
- **Status Kompilasi Akhir**: **SUKSES & SIAP DIUJI** (Lulus uji bangun Gradle).

---

## Laporan Audit & Kemajuan Implementasi (Sesi 3)
- **Pembersihan Wilayah Operasional**: [SELESAI] Menghapus total kolom isian, dialog edit, teks KTA, saringan pencarian, dan semua referensi "wilayah operasional" dari UI karena tidak logis untuk driver ojol harian yang bergerak bebas.
- **Item 88 (Salin Cepat KTA ID)**: [SELESAI] Menambahkan tombol klik/ketuk interaktif pada ID KTA digital untuk menyalin teks ID pengemudi secara instan ke clipboard ponsel dengan visual Toast yang ramah.
- **Item 85 (Asisten Rute Aman Karanglo)**: [SELESAI] Menyisipkan pertanyaan pemandu dan asisten navigasi rute alternatif bebas ranjau paku Karanglo di dalam `AiAssistantDialog`.
- **Uji Kelayakan Kompilasi**: **LULUS SEMPURNA** (Build Gradle sukses).

---

## Laporan Audit & Kemajuan Implementasi (Sesi 4)
- **Otoritas & Manajemen Peran (Pengurus)**: [SELESAI] Menambahkan panel khusus pengurus (`MemberManageDialog.kt`) untuk mengubah peran (Ketua, Waket, Sekretaris, Bendahara, Satgas, Dewan, Anggota) secara langsung, serta memperbarui status verifikasi (Aktif/Verified, Suspend) dengan feedback visual yang responsif.
- **Penyaringan Terfilter**: [SELESAI] Mendesain row filter chip di atas daftar pengemudi untuk memilah driver berdasarkan kriteria "Semua", "Pengurus", dan "Aktif Terverifikasi" guna memudahkan tata kelola data.
- **Ekspor Laporan Instan**: [SELESAI] Membuat generator laporan dinamis (`ExportReportDialog.kt`) yang menyajikan statistik ringkasan eksekutif dan tabel data anggota dalam format Markdown untuk disalin langsung ke WhatsApp/Notes, serta tombol cetak PDF interaktif.
- **Uji Kelayakan Kompilasi**: **LULUS SEMPURNA** (Build Gradle sukses).


