package com.example.modules.devtools

object PhaseTreePhase1 {
    fun createPhase1(): DevPhase = DevPhase(
        phaseNumber = 1,
        title = "Fondasi Arsitektur, Keamanan & RBAC",
        objective = "Memastikan persistensi lokal kokoh dan hak wewenang terlindungi.",
        subPhases = listOf(
            DevSubPhase(
                id = "SUB-1.1",
                title = "Room v14 Storage & Multi-Entity",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-1.1.1",
                        name = "26 Tabel Database (Room v14) & Migrasi Eksplisit",
                        status = PhaseTreeStatus.LULUS_SEMPURNA,
                        hasDefect = false,
                        whatIsIt = "Skema lokal SQLite Room v14 (26 tabel) untuk seluruh modul data.",
                        whyRequired = "Data driver, posko, kas, dan forum tersimpan aman di internal."
                    ),
                    DevFeatureLeaf(
                        id = "F-1.1.2",
                        name = "UDF Flow & ViewModel MVI Architecture",
                        status = PhaseTreeStatus.LULUS_SEMPURNA,
                        hasDefect = false,
                        whatIsIt = "StateFlow reactive pipeline dengan isolasi total modul.",
                        whyRequired = "Mencegah race condition dan recomposition lag pada layar driver."
                    )
                )
            ),
            DevSubPhase(
                id = "SUB-1.2",
                title = "Wewenang Role & Keamanan Sesi",
                features = listOf(
                    DevFeatureLeaf(
                        id = "F-1.2.1",
                        name = "Audit Log Mutasi Role (RoleAuditLogEntity)",
                        status = PhaseTreeStatus.LULUS_SEMPURNA,
                        hasDefect = false,
                        whatIsIt = "Pencatatan riwayat pergantian jabatan kepengurusan komunitas.",
                        whyRequired = "Transparansi pertanggungjawaban penunjukan pengurus posko."
                    ),
                    DevFeatureLeaf(
                        id = "F-1.2.2",
                        name = "Validasi Wewenang Edge Token Signature",
                        status = PhaseTreeStatus.TERKENDALA_CACAT,
                        hasDefect = true,
                        whatIsIt = "Verifikasi tanda tangan kriptografi peran admin di serverless edge.",
                        whyRequired = "Cegah manipulasi lokal tabel role SQLite oleh perangkat root.",
                        defectExplanation = "Validasi role sudah terpusat di RoleManager.validateRoleChange() (dipakai RolePermissionRepository & UI), namun tanpa edge signature/backend, manipulasi SQLite lokal oleh perangkat root tetap mungkin. Butuh backend custom claims (DEFECT-01)."
                    )
                )
            )
        )
    )
}
