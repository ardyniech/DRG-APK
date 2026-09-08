# Modul Tanggap Darurat & Live Radar Pantauan (`modules/emergency` & `modules/radar`)

## Deskripsi
Modul inti untuk keselamatan pengemudi di jalan raya, dilengkapi dengan tombol panik darurat SOS terpusat, sensor simulasi deteksi tabrakan/jatuh keras, dan peta radar pemantau posisi armada terdekat berbasis OpenStreetMap.

## Fitur Utama
1. **Pusat Komando Darurat & Tombol SOS Hero (`SosHeroActionCard` & `EmergencyScreen`)**:
   - Tombol sentuh taktil ekstra besar (150dp diameter) dengan efek glow denyut nadi (*pulsating aura*) dan kontras visual tinggi (`#DC2626`).
   - Pemilihan kategori situasi cepat (*Mogok Mesin, Begal/Kriminal, Medis/Laka, Razia/Insiden*).
   - **Countdown Overlay (`SosCountdownOverlay`)**: Hitung mundur 5 detik disertai audio sirine 100 dB (`SosAlarmSoundManager`) dengan opsi pembatalan aman *"SAYA AMAN"* atau kirim instan *"KIRIM SOS SEKARANG"*.
   - **High-Visibility SOS Floating Action Button (`SosFloatingActionButton`)**: Tombol aksi melayang dengan animasi denyut aura (*pulsating halo*) di seluruh layar komunitas untuk memicu modal pelaporan darurat instan (`CommunityEmergencyModal`).
2. **Sensor Deteksi Benturan / Jatuh (`CrashDetectionService` & `CrashGuardQuickCard`)**:
   - Memantau ambang batas akselerasi dan deselerasi ekstrim untuk deteksi otomatis kecelakaan dan memicu hitung mundur darurat.
3. **Peta Radar Bebas API Key & Smart Cache Layer (OpenStreetMap / Canvas Layer)**:
   - Menampilkan posisi rekan pengemudi di sekitar.
   - Filter armada berdasarkan aplikator (*Gojek, Grab, Maxim, ShopeeFood, InDrive*).
   - Penanda titik posko/basecamp dan shelter aman.
   - **Room Tile Metadata & File System Cache (`MapTileDao` & `DRGCacheManager`)**: Menyimpan metadata tile peta (zoom, x, y, byte size, LRU access) serta status konfigurasi aplikasi (`AppStateDao`) untuk memangkas pemakaian kuota dan konsumsi baterai hingga 45%.
   - **Live Map Coordinate Visualizer (`LiveMapScreen` & `LiveMapCanvasView`)**: Visualisasi posisi real-time armada DRG terdekat berbasis koordinat presisi (lat, lng), kalkulasi jarak Haversine (km), pemilih radius jarak (1-10 km), serta kartu fokus koordinat driver.
