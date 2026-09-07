# DRG Driver — AI Coding Agent Architecture & Operational SOP

## System Directive & Core Architecture Rules

1. **Simplicity is King & Modular Isolation**:
   - Batas maksimal **125 baris per file** untuk file UI, ViewModel, dan adapter.
   - Gunakan pendekatan micro-primitives di folder modul masing-masing.
   - Dilarang keras melakukan *vibe coding*, mutasi state tanpa aturan, atau nilai hardcoded.

2. **Design System & Aesthetics**:
   - **Haram Dark Theme**: Menggunakan tema terang, segar, dan bersih (`#F8FAF8` background, `#FFFFFF` surface cards, `#00B14F` primary brand).
   - Dynamic window insets handling dengan `WindowInsets.navigationBars` dan `WindowInsets.ime`.
   - Minimum target sentuh 48x48dp pada setiap tombol dan elemen interaktif.

3. **Data Layer & Room Database**:
   - Seluruh mutasi data menggunakan Room Database dengan versioning yang sinkron (`AppDatabase.kt`).
   - Terapkan `fallbackToDestructiveMigration()` saat fase prototipe/pengembangan.
   - Non-blocking I/O via Kotlin Coroutines `Dispatchers.IO` dan Flow reaktif.

4. **Testing & Quality Assurance**:
   - Setiap fitur baru harus divalidasi dengan unit test Robolectric di bawah `app/src/test/java/com/example/`.
   - Jalankan `compile_applet` atau `gradle :app:testDebugUnitTest` sebelum menyelesaikan turn.
