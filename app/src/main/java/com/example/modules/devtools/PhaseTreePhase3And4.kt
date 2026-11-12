package com.example.modules.devtools

object PhaseTreePhase3And4 {
    fun createPhase3(): DevPhase = DevPhase(
        phaseNumber = 3,
        title = "Hardware, Audio Intercom & Offline P2P",
        objective = "Komunikasi suara langsung tanpa kuota saat konvoi di blank-spot.",
        subPhases = listOf(
            DevSubPhase(
                id = "SUB-3.1",
                title = "P2P Walkie Talkie & Mesh Radar",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-3.1.1",
                        name = "PTT Walkie-Talkie (Audio Stream)",
                        status = PhaseTreeStatus.TERKENDALA_CACAT,
                        hasDefect = true,
                        whatIsIt = "Komunikasi push-to-talk antar-driver (saat ini simulasi UI).",
                        whyRequired = "Peringatan bahaya jalanan seketika saat konvoi.",
                        defectExplanation = "Belum ada transmisi audio nyata (capture/streaming/jaringan). Implementasi saat ini simulasi UI; butuh pipeline audio + transport/signaling."
                    )
                )
            ),
            DevSubPhase(
                id = "SUB-3.2",
                title = "Integrasi Bluetooth Intercom Helm",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-3.2.1",
                        name = "Koneksi GATT Bluetooth Headset Helm (PENDING DEV)",
                        status = PhaseTreeStatus.PENDING,
                        hasDefect = false,
                        whatIsIt = "Tombol pairing PTT fisik pada tombol stang / helm (ditunda).",
                        whyRequired = "Di luar scope rilis awal; intercom helm pending sesuai roadmap.",
                        defectExplanation = "DEFERRED/PENDING DEV per roadmap — bukan cacat in-scope."
                    )
                )
            )
        )
    )

    fun createPhase4(): DevPhase = DevPhase(
        phaseNumber = 4,
        title = "Emergency SOS & Telemetri Lapangan",
        objective = "Penyelamatan jiwa instan saat begal, laka lantas, atau intimidasi.",
        subPhases = listOf(
            DevSubPhase(
                id = "SUB-4.1",
                title = "Trigger Darurat & Sirene Taktis",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-4.1.1",
                        name = "Countdown 5 Detik & Haptic Pulsa SOS",
                        status = PhaseTreeStatus.LULUS_SEMPURNA,
                        hasDefect = false,
                        whatIsIt = "Penghitung mundur dengan getaran ritmik mencegah salah tekan.",
                        whyRequired = "Kepastian driver memicu sinyal darurat dalam kondisi panik."
                    )
                )
            ),
            DevSubPhase(
                id = "SUB-4.2",
                title = "Transmisi Sinyal Darurat Lapangan",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-4.2.1",
                        name = "Fallback SMS Darurat (Intent/Composer) Saat Offline",
                        status = PhaseTreeStatus.TERKENDALA_CACAT,
                        hasDefect = true,
                        whatIsIt = "Menyusun & membuka SMS darurat berisi koordinat GPS ke Satgas TRC (bukan kirim otomatis).",
                        whyRequired = "Membantu pelaporan saat sinyal data drop, jika SMS dapat dikirim.",
                        defectExplanation = "Tidak otomatis terkirim (membuka composer), butuh aksi user & pulsa/SMS aktif; gateway alternatif belum ada (DEFECT-03)."
                    ),
                    DevFeatureLeaf(
                        id = "F-4.2.2",
                        name = "Adaptive GPS Lock Power Management",
                        status = PhaseTreeStatus.DALAM_PENGERJAAN,
                        hasDefect = false,
                        whatIsIt = "Manajemen frekuensi/prioritas GPS adaptif (motion, baterai, profil hemat daya).",
                        whyRequired = "Mengurangi konsumsi baterai saat pelacakan armada/darurat.",
                        defectExplanation = "Sinkron lokasi adaptif/battery-aware sudah ada; geofencing/optimasi lanjutan & pengukuran dampak nyata masih bisa ditingkatkan."
                    )
                )
            )
        )
    )
}
