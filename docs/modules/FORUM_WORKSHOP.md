# Modul Forum Diskusi & Bengkel Rekanan (`modules/forum_workshop`)

## Deskripsi
Modul ini menyediakan ruang komunikasi, pantauan jalur lalu lintas real-time, tanya jawab kendala motor antar-pengemudi ojol, serta direktori bengkel rekanan DRG dengan potongan harga khusus.

## Fitur Utama
1. **Pemisahan Tipe Postingan (`PostType`)**:
   - `UPDATE`: Berbagi pantauan jalur, razia, pohon tumbang, atau info banjir.
   - `QUESTION`: Ruang tanya jawab dan konsultasi teknis kendaraan ke rekan ojol.
2. **Kategori Filter (`ForumCategory`)**:
   - Tanya Driver / Q&A
   - Info Jalur & Pantauan
   - Tips & Rawat Motor
   - Info Spot & Jam Ramai
   - Sapa Rekan
3. **Persistensi Lokal Room Database (`ForumDao`)**:
   - Operasi penambahan post, toggle suka, dan penghapusan post pemilik.
4. **Direktori Bengkel Rekanan (`WorkshopSection`)**:
   - List bengkel mitra terverifikasi, jam operasional, diskon jasa servis, dan tombol panggil cepat.

## Struktur Berkas
- `ForumAndWorkshopScreen.kt`: Sub-tab controller (Forum vs Bengkel).
- `ForumSection.kt`: Daftar postingan forum interaktif.
- `ForumActionBanner.kt`: Tombol aksi pembuatan update / pertanyaan.
- `ForumHeaderAndFilters.kt`: Kolom pencarian dan chip filter kategori.
- `ForumPostCard.kt` & `ForumPostCardHeader.kt`: Komponen kartu postingan.
- `CreatePostDialog.kt`: Modal dialog input postingan.
- `ForumEmptyState.kt`: Tampilan ramah ketika data kosong atau filter nihil.
- `WorkshopSection.kt`: Daftar bengkel rekanan resmi.
