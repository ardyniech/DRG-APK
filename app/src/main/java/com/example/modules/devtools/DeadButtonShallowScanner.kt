package com.example.modules.devtools

object DeadButtonShallowScanner {
    fun getAuditedFeatures(): List<FeatureAuditItem> = listOf(
        FeatureAuditItem(
            name = "Emergency SOS & Tactical Haptics",
            module = "Emergency",
            stage = FeatureStage.LULUS,
            codeLineCount = 118
        ),
        FeatureAuditItem(
            name = "Kas Transparan & Export Text Share",
            module = "Treasury",
            stage = FeatureStage.LULUS,
            codeLineCount = 118
        ),
        FeatureAuditItem(
            name = "Bengkel Rekanan & Rating 1-5 Star",
            module = "Forum & Bengkel",
            stage = FeatureStage.LULUS,
            codeLineCount = 108
        ),
        FeatureAuditItem(
            name = "Field Workflow Realistic Simulation",
            module = "Workflow",
            stage = FeatureStage.LULUS,
            codeLineCount = 106
        ),
        FeatureAuditItem(
            name = "Posko Radar & Driver Proximity",
            module = "Radar",
            stage = FeatureStage.LULUS,
            codeLineCount = 115
        ),
        FeatureAuditItem(
            name = "P2P Walkie Talkie Voice Fallback",
            module = "Services",
            stage = FeatureStage.MATURE,
            hasDeadButtons = false,
            codeLineCount = 112
        ),
        FeatureAuditItem(
            name = "Insurance Claim Instant Settlement",
            module = "Services",
            stage = FeatureStage.ANAK,
            hasDeadButtons = true,
            isShallow = true,
            deadButtonDetails = "Tombol 'Upload Berkas RS' belum terhubung ke storage backend multipart",
            shallowReason = "Hanya dialog konfirmasi snackbar, belum integrasi ke API asuransi pihak ketiga",
            codeLineCount = 98
        ),
        FeatureAuditItem(
            name = "Helmet GATT Bluetooth Intercom",
            module = "Hardware",
            stage = FeatureStage.BENIH,
            hasDeadButtons = true,
            isShallow = true,
            deadButtonDetails = "Tombol 'Pairing Sena 50S' masih mock listener",
            shallowReason = "Baru berupa interface GATT, belum terkoneksi ke BluetoothAdapter fisik",
            codeLineCount = 85
        )
    )

    fun countDeadButtons(): Int = getAuditedFeatures().count { it.hasDeadButtons }
    fun countShallowFeatures(): Int = getAuditedFeatures().count { it.isShallow }
}
