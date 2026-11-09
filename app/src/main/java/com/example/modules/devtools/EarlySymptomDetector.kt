package com.example.modules.devtools

import android.content.Context

object EarlySymptomDetector {
    fun diagnose(context: Context): List<RootCauseFinding> {
        val findings = mutableListOf<RootCauseFinding>()
        val mem = RealtimeMemoryProbe.inspect(context)

        mem.earlySymptomWarning?.let { warning ->
            findings.add(
                RootCauseFinding(
                    id = "RCA-MEM-01",
                    title = "Tekanan Memori Runtime Terdeteksi",
                    preFailureSymptom = warning,
                    sourceFile = "core/cache/MapTileCacheManager.kt",
                    sourceFunction = "trimMemoryCache()",
                    rootCauseTechnical = "Alokasi bitmap tile peta belum agresif melepaskan memory saat lowMemory signal dari OS.",
                    severity = if (mem.isSystemLowMemory) SymptomSeverity.CRITICAL else SymptomSeverity.PREVENTIVE,
                    actionableFix = "Terapkan ComponentCallbacks2.onTrimMemory(TRIM_MEMORY_RUNNING_CRITICAL) untuk evict LRU cache seketika."
                )
            )
        }

        findings.add(
            RootCauseFinding(
                id = "RCA-DEAD-01",
                title = "Dead Button: Upload Berkas Klaim Asuransi",
                preFailureSymptom = "Pengemudi menekan 'Klaim Asuransi' namun bukti foto laka/kuitansi RS tidak terkirim ke storage.",
                sourceFile = "modules/services/DriverServicesScreen.kt",
                sourceFunction = "onClaimInsurance()",
                rootCauseTechnical = "Handler tombol hanya memanggil snackbar lokal tanpa multipart OkHttp/Retrofit upload pipeline ke cloud storage.",
                severity = SymptomSeverity.CRITICAL,
                actionableFix = "Hubungkan URI hasil PhotoPicker ke Repository upload worker dengan retry policy saat offline."
            )
        )

        findings.add(
            RootCauseFinding(
                id = "RCA-DEAD-02",
                title = "Dead Button: Pairing Intercom Helm GATT",
                preFailureSymptom = "Klik 'Hubungkan Helm Sena' di dashboard tidak merespons pada Bluetooth headset fisik driver.",
                sourceFile = "modules/hardware/HelmetIntercomMonitor.kt",
                sourceFunction = "connectGattDevice()",
                rootCauseTechnical = "UUID service GATT masih menggunakan dummy loopback tanpa BluetoothAdapter.getRemoteDevice() binding.",
                severity = SymptomSeverity.WARNING,
                actionableFix = "Inject Android BluetoothSocket RFCOMM channel untuk profile HSP/HFP sebelum pairing dialog ditutup."
            )
        )

        findings.add(
            RootCauseFinding(
                id = "RCA-UI-01",
                title = "Potensi Recomposition Hitch di Kas Transparan",
                preFailureSymptom = "Scroll pada daftar 50+ transaksi kas terasa micro-stutter saat filter periode diubah.",
                sourceFile = "modules/treasury/primitives/KasTransparanComponent.kt",
                sourceFunction = "filteredList calculation",
                rootCauseTechnical = "Kalkulasi filter transaksi menggunakan remember reguler tanpa derivedStateOf sehingga recompose berulang saat scroll.",
                severity = SymptomSeverity.PREVENTIVE,
                actionableFix = "Bungkus filter logic dengan remember(transactions, selectedFilter) { derivedStateOf { ... } }."
            )
        )

        return findings
    }
}
