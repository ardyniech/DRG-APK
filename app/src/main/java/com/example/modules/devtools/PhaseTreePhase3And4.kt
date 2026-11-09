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
                        name = "Audio Multicast & PTT Voice Stream",
                        status = PhaseTreeStatus.TERKENDALA_CACAT,
                        hasDefect = true,
                        whatIsIt = "Transmisi suara push-to-talk ad-hoc antar-driver.",
                        whyRequired = "Peringatan bahaya jalanan seketika saat konvoi malam hari.",
                        defectExplanation = "Terbatas satu subnet WiFi posko; butuh STUN/TURN relay publik."
                    )
                )
            ),
            DevSubPhase(
                id = "SUB-3.2",
                title = "Integrasi Bluetooth Intercom Helm",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-3.2.1",
                        name = "Koneksi GATT Bluetooth Headset Helm",
                        status = PhaseTreeStatus.TERKENDALA_CACAT,
                        hasDefect = true,
                        whatIsIt = "Tombol pairing PTT fisik pada tombol stang / helm.",
                        whyRequired = "Driver tidak perlu melepas tangan dari stang motor untuk bicara.",
                        defectExplanation = "Tombol pairing masih dummy loopback; perlu RFCOMM binding."
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
                        name = "Fallback SMS Otomatis Saat Blank Spot",
                        status = PhaseTreeStatus.TERKENDALA_CACAT,
                        hasDefect = true,
                        whatIsIt = "SMS darurat berisi koordinat GPS ke Satgas TRC terdekat.",
                        whyRequired = "Tetap terhubung saat sinyal paket data seluler drop 0 bar.",
                        defectExplanation = "Gagal bila saldo pulsa prabayar driver Rp 0; butuh gateway WA."
                    ),
                    DevFeatureLeaf(
                        id = "F-4.2.2",
                        name = "Adaptive GPS Lock Power Management",
                        status = PhaseTreeStatus.TERKENDALA_CACAT,
                        hasDefect = true,
                        whatIsIt = "Manajemen frekuensi lock GPS saat pelacakan armada.",
                        whyRequired = "Mencegah HP driver kehabisan baterai sebelum bantuan tiba.",
                        defectExplanation = "Polling 1 detik menguras 12%/jam; butuh adaptive geofence."
                    )
                )
            )
        )
    )
}
