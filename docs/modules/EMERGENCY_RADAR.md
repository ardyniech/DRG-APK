# Modul Tanggap Darurat & Live Radar Pantauan (`modules/emergency` & `modules/radar`)

## Deskripsi
Modul inti untuk keselamatan pengemudi di jalan raya, dilengkapi dengan tombol panik darurat SOS terpusat, sensor simulasi deteksi tabrakan/jatuh keras, dan peta radar pemantau posisi armada terdekat berbasis OpenStreetMap.

## Fitur Utama
1. **Tombol Panik SOS**:
   - Memicu status darurat siaga tinggi dengan koordinat GPS real-time.
   - Mengirimkan siaga darurat ke seluruh anggota dan satgas terdekat.
2. **Sensor Deteksi Benturan / Jatuh (`CrashDetectionService`)**:
   - Memantau ambang batas akselerasi dan deselerasi ekstrim untuk deteksi otomatis kecelakaan.
3. **Peta Radar Bebas API Key (OpenStreetMap / Canvas Layer)**:
   - Menampilkan posisi rekan pengemudi di sekitar.
   - Filter armada berdasarkan aplikator (*Gojek, Grab, Maxim, ShopeeFood, InDrive*).
   - Penanda titik posko/basecamp dan shelter aman.
