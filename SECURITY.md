# Kebijakan Keamanan — DRG Driver (Security Policy) 🛡️

## 🔒 Versi yang Didukung

Kami secara aktif memberikan pembaruan keamanan dan perbaikan celah untuk versi berikut:

| Versi | Didukung | Keterangan |
|---|:---:|---|
| `v1.0.x` |  | Rilis Stabil Utama (Room v5, Android 14/15) |
| `< v1.0` | ❌ | Versi Pengembangan Awal |

---

## 🚨 Pelaporan Kerentanan (Reporting a Vulnerability)

Keamanan dan privasi pengemudi di jalan raya adalah prioritas tertinggi kami. Jika Anda menemukan kerentanan keamanan (terutama terkait kebocoran koordinat darurat SOS, manipulasi transaksi kas, atau autentikasi KTA):

1. **JANGAN** membuat issue publik di GitHub.
2. Kirim laporan terperinci secara privat melalui email ke: `ardy.syafii@gmail.com`.
3. Cantumkan informasi:
   - Deskripsi kerentanan dan potensi dampaknya.
   - Langkah-langkah untuk mereproduksi celah (Proof of Concept).
   - Saran perbaikan (jika ada).

Kami akan merespons laporan dalam waktu **1x24 jam** dan berkoordinasi sebelum merilis security patch.

---

## 🛡️ Standar Arsitektur Keamanan DRG

- **TLS 1.3 / Enforced HTTPS**: Dikonfigurasi secara ketat pada `app/src/main/res/xml/network_security_config.xml` dengan mematikan `cleartextTraffic`.
- **Zero-Trust Local Storage**: Data sensitif disimpan secara terisolasi di database Room SQLite internal aplikasi (`allowBackup="false"`).
- **ProGuard & R8 Optimization**: Obfuscation dan stripping simbol sensitif pada build release (`app/proguard-rules.pro`).
- **No Hardcoded Secrets**: Secrets dan API keys diinjeksi saat build time via Secrets Gradle Plugin (`.env`).
