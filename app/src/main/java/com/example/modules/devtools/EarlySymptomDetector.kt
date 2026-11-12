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
                id = "RCA-PEND-01",
                title = "Pending/Deferred: Klaim Asuransi",
                preFailureSymptom = "Fitur asuransi belum diimplementasikan end-to-end (sesuai roadmap: PENDING/DEFERRED).",
                sourceFile = "modules/services/* (roadmap)",
                sourceFunction = "N/A (scope rilis awal: iuran Kas saja)",
                rootCauseTechnical = "Integrasi upload klaim & API asuransi ditunda; bukan defect in-scope rilis awal.",
                severity = SymptomSeverity.PREVENTIVE,
                actionableFix = "Tandai jelas sebagai PENDING/DEFERRED; kerjakan setelah prioritas inti produksi stabil."
            )
        )

        findings.add(
            RootCauseFinding(
                id = "RCA-PEND-02",
                title = "Pending/Deferred: Intercom Helm (Bluetooth/GATT)",
                preFailureSymptom = "Integrasi pairing/audio intercom helm belum diimplementasikan (PENDING DEV).",
                sourceFile = "modules/hardware/* (roadmap)",
                sourceFunction = "N/A (scope rilis awal: iuran Kas saja)",
                rootCauseTechnical = "Koneksi GATT/RFCOMM & PTT helm ditunda sesuai roadmap.",
                severity = SymptomSeverity.PREVENTIVE,
                actionableFix = "Tandai jelas sebagai PENDING/DEFERRED; implementasi saat prioritas inti stabil."
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
