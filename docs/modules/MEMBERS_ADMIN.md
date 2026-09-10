# Modul Direktori Anggota & Panel Pengurus (Admin) 👥⚙️

## 📌 Ringkasan
Modul ini memfasilitasi pencarian direktori rekan pengemudi di jalan raya serta menyediakan instrumen tata kelola dan screening calon anggota bagi pengurus komunitas DRG.

## 🏛️ Komponen Kunci
- `MembersDirectorySection.kt`: Direktori pencarian anggota dengan filter platform ojol, status aktif, dan nomor lambung DRG.
- `AdminScreeningSection.kt`: Antarmuka persetujuan calon pendaftar anggota baru DRG.
- `AdminAuditLogsSection.kt`: Catatan audit rekam jejak mutasi hak akses dan persetujuan pengurus.

## 🛡️ Aturan Otorisasi (RBAC)
- Hak akses tindakan administratif dibatasi hanya untuk peran `KETUA`, `WAKIL_KETUA`, `SEKRETARIS`, `BENDAHARA`, dan `SATGAS`.
