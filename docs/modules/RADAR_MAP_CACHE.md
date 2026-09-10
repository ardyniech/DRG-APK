# Modul Radar Peta & Disk Cache LRU 🗺️⚡

## 📌 Ringkasan
Menyediakan visualisasi peta live pantauan rekan ojol, posko basecamp, dan radius armada menggunakan engine OpenStreetMap (OSM) tanpa biaya API berbayar serta efisiensi konsumsi kuota & baterai maksimal.

## 🏛️ Arsitektur Cache LRU Multi-Level
- **Level 1 (In-Memory)**: Bitmap LRU cache berkecepatan tinggi untuk rendering 60 FPS.
- **Level 2 (Room Metadata & Disk Storage)**: Tabel `map_tile_metadata` menyimpan index zoom level, koordinat X/Y, ukuran byte, timestamp akses, dan frekuensi hit.
- **Kebijakan Eviction**: Jika ukuran cache disk mendekati ambang batas, algoritma Least-Recently-Used (LRU) secara otomatis menghapus tile tertua.

## 🧪 Validasi Pengujian
- Teruji di `MapTileCacheAndStateTest.kt` dan `CacheAndBatteryTest.kt`.
