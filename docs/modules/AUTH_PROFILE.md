# Modul Autentikasi, Profil & KTA Digital 👤📱

## 📌 Ringkasan
Modul ini mengelola proses masuk akun anggota komunitas DRG, identitas digital pengemudi, validasi kendaraan, dan KTA (Kartu Tanda Anggota) berbasis QR Code.

## 🏛️ Komponen Kunci
- `AuthScreen.kt` & `AuthFormPrimitives.kt`: Form autentikasi PIN cepat, pemilihan platform ojol utama, dan input nomor plat motor.
- `ProfileScreen.kt`: Tampilan identitas lengkap pengemudi dengan badge kepengurusan.
- `KtaCardPrimitive.kt`: Tampilan visual kartu keanggotaan DRG dengan QR Code dinamis untuk validasi saat Kopdar atau klaim diskon bengkel rekanan.

## 🔒 Kebijakan Keamanan
- Verifikasi status akun (`PENDING`, `VERIFIED`, `REJECTED`) dikendalikan oleh pengurus komunitas.
- Data kontak darurat disimpan di database lokal SQLite terenkripsi.
