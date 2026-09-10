# Panduan Kontribusi — DRG Driver (Driver Riang Gembira) 🤝

Terima kasih atas minat Anda untuk berkontribusi pada proyek **DRG Driver**! Dokumen ini memberikan pedoman lengkap bagi kontributor untuk memastikan kualitas kode, arsitektur, dan keamanan sistem tetap berada pada standar produksi tertinggi.

---

## 🏛️ Prinsip Arsitektur Utama

1. **Simplicity is King & Batas Baris Kode**:
   - Maksimal **125 baris per file** untuk seluruh file UI (`modules/`), ViewModel, dan adapter.
   - Pecah komponen besar menjadi micro-primitives di folder modul masing-masing.
   - Pisahkan model domain ke dalam `shared/models/`.

2. **Design System & Estetika (Material 3)**:
   - **Haram Dark Theme**: Gunakan tema terang yang segar dan ramah pengemudi siang hari (`#F8FAF8` background, `#FFFFFF` cards, `#00B14F` primary brand).
   - Dynamic Inset Handling wajib mendukung `WindowInsets.navigationBars` dan `WindowInsets.ime`.
   - Minimum target sentuh **48x48dp** pada semua elemen interaktif.

3. **Local-First & Room Database**:
   - Semua mutasi data harus dioperasikan secara lokal terlebih dahulu melalui Room Database (`AppDatabase.kt`) dengan reactive Flow.
   - Non-blocking I/O via Kotlin Coroutines pada `Dispatchers.IO`.

4. **Zero-Tolerance Quality Assurance**:
   - Setiap fitur baru **wajib** disertai unit test Robolectric di `app/src/test/java/com/example/`.
   - Pastikan seluruh test suite lolos (`gradle :app:testDebugUnitTest`) sebelum mengajukan Pull Request.

---

## 🛠️ Alur Kerja Kontribusi (Workflow)

1. **Fork & Clone Repositori**:
   ```bash
   git clone https://github.com/ardyniech/DRG-APK.git
   cd DRG-APK
   ```

2. **Buat Feature Branch**:
   Gunakan format penamaan branch yang jelas:
   - `feat/nama-fitur` (untuk fitur baru)
   - `fix/deskripsi-bug` (untuk perbaikan bug)
   - `refactor/nama-modul` (untuk refaktor kode)

3. **Pengembangan & Pengujian**:
   Jalankan kompilasi dan unit test lokal:
   ```bash
   gradle :app:testDebugUnitTest
   ```

4. **Commit & Push**:
   Tulis commit message yang deskriptif:
   ```bash
   git commit -m "feat(forum): tambahkan fitur balasan komentar dengan Room persistence"
   ```

5. **Ajukan Pull Request (PR)**:
   Jelaskan konteks perubahan, modul yang terdampak, dan sertakan bukti screenshot atau hasil unit test.

---

## 🔒 Kebijakan Keamanan & Laporan Kerentanan

Jika Anda menemukan celah keamanan (terutama terkait SOS darurat, transparansi kas, atau data privasi anggota):
- **Jangan** laporkan melalui issue publik.
- Kirim email langsung ke pengurus komunitas atau buat private security advisory.

---

## 📜 Kode Etik

Dengan berpartisipasi dalam proyek ini, Anda setuju untuk mematuhi [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md).
