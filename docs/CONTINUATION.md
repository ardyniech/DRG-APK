# DRG-APK Continuation / Handoff

> Durable handoff trail for model/agent switches. Keep this file current at the end of every batch and at every stop point. Do not rely on chat memory.

## Current state
- **Last updated (UTC):** 2026-11-13
- **Repo/branch:** `projects/DRG-APK` — `main`
- **HEAD (pushed):** `c1b3512` (docs CONTINUATION engine event-sync)
- **Working tree:** dirty — batch "DevTools honesty (metrik terpisah)" siap commit (lihat di bawah).
- **Latest CI:** deprioritized per owner (preview via aigoogle.studio); last known issues fixed in `ab0b581`/`a4f7032`. Confirm if needed: `gh run list -R ardyniech/DRG-APK --limit 5`.

## Audit snapshot
- **Command:** `python3 .github/scripts/audit_minus.py`
- **Result:** **0 BLOCKER, 2 MAJOR, 3 MINOR** (total 5)
  - **MAJOR [multi-user]:** Seed lokal-only (butuh backend + akun server). *No fake sync.*
  - **MAJOR [database]:** Schema Room hilang `1,2,3,4,8,9.json` (export/commit valid saja; **jangan difabrikasi**).
  - **MINOR [security]:** SQLite belum terenkripsi/SQLCipher (risiko terdokumentasi di `README.md`).
  - **MINOR [code]:** hardcoded `Color(0x...)` di 44 file.
  - **MINOR [test]:** `androidTest` minim (alur kritis login/SOS/kas).
- **Line cap:** PASS (`check_file_length.py`, semua <125 lines).

## DevTools metric truth (2026-11-13)
- Angka besar di header DevTools **bukan** progres fitur. Itu `ProjectHealthScore.finalScore`:
  `95 − (6 cacat produksi × 2) − (5 gap progres × 2) = 73` → **Grade B (heuristik)**.
- **Progres Fitur (PhaseTree, tab Tree Progress):** `LULUS ÷ fitur in-scope` = **9/17 = 52.9%**
  (total 19 · in-scope 17 · lulus 9 · cacat 3 · dikerjakan 5 · deferred 2).
- `DEFERRED` (Asuransi `F-2.2.2`, Intercom Helm `F-3.2.1`) dikecualikan dari denominator progres.
- `progressPenaltiesCount` kini dinamis dari `PhaseTreeScanner.getMetrics().progressGapCount` (bukan hardcoded 1).

## Done (recent batches)
- Foundations/refactors, honest sync/P2P local-only, hex-token, CI fixes, handoff trail, ktlint warn-only CI.
- DevTools honesty: defects/bug hunter/score dynamic & scope-aware, Build/CI/CodeIntegrity/mesh/topology/APK/CPU/repo/git/UI-UX/phase/RCA/security probe limits dijujurkan.
- **`ab0b581`:** fix syntax `DevScoreBanner.kt`. **`a4f7032`:** update `DevToolsOutdatedApkCompatibilityTest` agar selaras `DynamicApkInspector` jujur (hapus ref `LATEST_TARGET_VERSION_CODE`).
- **`dc641c6`:** `DevMaturityTab` & `DevProgressTab` — label/teks diperjelas (audit terkurasi, progres jujur & scope-aware).
- **`bc835b6`/`a542cb9`:** F-4.2.2 & DEFECT-04 selaras implementasi aktual (`BatteryAwareLocationSyncer` adaptif gerak/baterai/profil). SSOT/CONTINUATION updated.
- **`d16a22c`/`f255aaa`:** SMS fallback honesty (selaras implementasi composer/intent, bukan kirim otomatis) — F-4.2.1, DEFECT-03, probe/tab/UX/knowledge diperbarui; `FeatureMaturity.LULUS` diperjelas. SSOT regenerated.
- **`dd7bda2`/`7ae3cc5`:** tambah `LoginAuthValidatorTest` (login via driverId/plat/No.HP ternormalisasi & penolakan user tak terdaftar, Robolectric).
- **`36b3290`/`b62af35`:** PTT/Walkie-Talkie dijujurkan sebagai simulasi UI (bukan transmisi audio nyata) — dialog/DEFECT-02/F-3.1.1/thumbnail/card/capability probe diperbarui. SSOT regenerated.
- **`5a0bcd5`/`48318d2`:** `TreasuryHealthCard` dijujurkan dari klaim statis/absolut menjadi ringkasan netral + catatan sinkronisasi (lokal-first).
- **`226074e`/`3fb8bd0`:** `NodeTopologyScanner` NODE-MESH-01 dijujurkan (mesh/signaling & audio belum end-to-end). DEFECT-11 dipisahkan dari isu PTT radio. SSOT regenerated.
- **`b430076`/`ec9aef6`:** `RuntimeCapabilityProber` label modul diperjelas (PTT simulasi, audit/topologi heuristik/konseptual). `DevServerTab` membedakan ping host sebagai konektivitas, bukan status backend app.
- **`80f7601`/`45581f1`:** F-2.1.1 (Kas) dijujurkan — hapus klaim "Realtime" berlebihan, tekankan pencatatan di aplikasi (lokal-first). SSOT regenerated.
- **`e0efb4b`/`dda617c`:** tambah kasus `EmergencySmsFallbackHelperTest` (fallback hotline default saat nomor kosong/tidak valid, Robolectric).
- **This batch (fitur Event komunitas, lokal-first):** tambah manajemen event (Kopdar rutin tiap 3 bulan, Anniversary, dll) dengan panitia sendiri per event: **susunan panitia, dana sumber, dana keluar, LPJ, dan pihak terkait/terlibat** (jelas & transparan). Implementasi: Room **v13→v14** (migrasi 13_14) + entitas/tabel `community_events`, `event_committee_members`, `event_funding`, `event_lpj_reports`, `event_stakeholders`; DAO, repository, coordinator, ViewModel/extension; UI tab **Event** di Komunitas (list, detail, form tambah/edit/hapus per bagian). DevTools DB introspector & topologi diselaraskan (target v14). README/SSOT diperbarui (v14, 28 entitas). *Catatan: data lokal-first; tidak ada sinkron cloud.*

- **This batch (DevTools honesty — metrik terpisah, terverifikasi):**
  - **Root cause "81%":** `DevToolsGlassHeader`/`DevScoreBanner` menampilkan `ProjectHealthScore.finalScore`, bukan progres fitur. Sekarang dilabeli **"Skor Kesehatan"** + catatan eksplisit "BUKAN progres fitur".
  - **PhaseTree scope-aware:** `PhaseTreeStatus.DEFERRED` baru; `DevPhase`/`DevSubPhase` punya `inScopeFeatures`, `deferredFeatures`, `completionPercentage` (lulus ÷ in-scope); `PhaseTreeScanner.getMetrics(): PhaseTreeMetrics`.
  - **Tab Tree Progress:** menampilkan total/in-scope/lulus/cacat/dikerjakan/deferred + **Progres in-scope 52.9%** + disclaimer metrik kurasi (bukan runtime/build/produksi).
  - **Scope:** `F-2.2.2` (Asuransi) & `F-3.2.1` (Intercom Helm) → `DEFERRED`; event `F-2.3.1` (engine) & `F-2.3.2` (UI) → `DALAM_PENGERJAAN`.
  - **`PhaseTreePhase1And2.kt` dipecah** → `PhaseTreePhase1.kt` + `PhaseTreePhase2.kt` (patuh <125 baris).
  - **Fakta basi dibersihkan:** Room v13/23 → **v14/26** di `CodeIntegrityAuditor`, `MultiUserGapScanner`, `DevServerTab`, `NodeTopologyScanner`, `DevGitVersionTab`, `DevTechStackTab`, `DevRepoHealthTab`, `SecurityAuditEngine`; klaim "Firebase Firestore aktif" → "belum aktif penuh; local-first".
  - **Build blocker (bukan test):** KSP gagal karena konverter ganda `EventPhaseStage`/`CommitteeTaskStatus` di `Converters.kt` + `EventConverters.kt` → duplikat di `Converters.kt` dihapus (satu konversi per tipe, sumber tunggal `EventConverters.kt`).
  - **Repo/DAO drift:** `CommunityEventRepository` memakai nama param lama (`progressPercent`, `isResolved`) → diselaraskan ke DAO (`progress`, resolveFeedback 2 arg); test `CommunityEventTransparencyTest` disesuaikan.
  - **SSOT/README:** `features.json` Room v14/26, refinement `room-v13-verified` → `room-v14-verified` (**pending**); README badge v14/26 tabel + 65 suite + 532 file; ROADMAP v14/26; `DATABASE_MIGRATIONS.md` (v13→v14); P2P doc v14; `audit_minus.py` false-positive badge "32" diperbaiki + rentang schema v14.
  - **Verifikasi:** `:app:compileDebugKotlin` **BUILD SUCCESSFUL**; test `DevToolsPhaseTreeAndUiUxTest`, `DevToolsMaturityScoringTest`, `AboutManifestTest`, `CommunityEventTransparencyTest` **lulus**; `check_file_length.py` PASS; `audit_minus.py` 0 BLOCKER/2 MAJOR/3 MINOR. SSOT regenerated (64%, 5 temuan).
- **This batch (sinkronisasi UI ke dashboard — Kas + DevTools):**
  - **Acuan:** `DashboardScreen` + `AppVisualTheme` + `DrgLuminousGlassCard`/`DrgGlassCard` (shell `DrgAppContent` sudah mengecat `DrgLuminousAtmosphere` global, jadi kartu modul wajib glass/theme-aware, bukan putih opaque).
  - **Treasury (Kas = finansial rilis awal):** `KasBalanceCard`, `KasTransactionCard`, `KasFinancialChartCard` ditulis ulang memakai `DrgLuminousGlassCard` + token tema (`glassBg`, `glassBorderColor`, `primaryColor`); judul seksi `TreasuryScreen` pakai `LocalAppVisualTheme.current.primaryColor`.
  - **Kejujuran chart kas:** deret bulanan fabrikasi dihapus; bar kini proporsional terhadap `totalIncome`/`totalExpense` yang benar-benar tercatat + badge "Data Tercatat".
  - **Copy jujur:** "Real-Time" → "Tercatat"; subtitle dialog → "data tercatat di aplikasi".
  - **DevTools (21 `Surface(color = Color.White)` di 19 file):** token baru `DevGlassTokens.kt` (`devGlassBg()`, `devGlassBorder()`, `devAccent()`, `devPillBg()`); `DevTabShell`/`DevScoreBanner` tidak lagi hardcode putih/abu; border `DrgOutline` → `devGlassBorder()`.
  - **Verifikasi:** `:app:compileDebugKotlin` **BUILD SUCCESSFUL**; test `DevToolsPhaseTreeAndUiUxTest`, `DevToolsMaturityScoringTest`, `AboutManifestTest`, `CommunityEventTransparencyTest`, `KasTransparanComponentTest`, `TreasuryAuditComputationTest`, `DashboardGlassmorphismTest` **lulus**; `check_file_length.py` PASS.
- **`450a061` (UI revamp besar + implementasi event paralel, incomplete):** revamp UI bertahap (acuan dashboard/luminous, nav/tema baru, banyak layar dimodernisasi) + path event baru (`modules/events`, `EVENTS` tab, model task/feedback/readiness) yang engine-nya belum nyambung.
- **This batch (sinkronisasi engine, fokus engine):** rekonsiliasi ke **satu model event** sesuai schema 14.json & UI baru (`CommunityEventEntity`, `EventCommitteeTaskEntity`, `EventMemberFeedbackEntity`): perbaiki `EventConverters`, `CommunityEventDao`, `AppDatabase` (entity registry), `MIGRATION_13_14`, wiring `CommunityEventRepository`/`DRGRepository`, `EventCoordinator` (create/stage/task/progress/feedback/resolve), `DRGViewModel` (`communityEvents`/`committeeTasks`/`eventFeedback`) agar `EventsTabDelegate`/`CommunityEventsScreen` punya kontrak lengkap. Hapus duplikasi path event lama saya (`modules/community/events/*`, `EventManagementRepository`, `DRGEventViewModelExtensions`, model `CommunityEvent/EventCommitteeMember/EventFunding/EventLpjReport/EventStakeholder`) & lepas sub-tab Event dari `CommunityHub` (Event kini top-level). `DynamicDbIntrospector`/README diselaraskan (26 tabel v14). *Catatan:* model event aktif kini task+budget+readiness+feedback (berbeda dari request awal dana sumber/keluar, LPJ & stakeholder sebagai entitas terpisah — perlu keputusan/konsolidasi produk bila ingin eksplisit). Build Gradle/UI runtime belum diverifikasi di sini.

## Next steps (ordered, toward production)
1. **DevTools menyeluruh (lanjutkan):** tinjau sisa temuan (DEFECT-01..02 narasi vs aktual, teks tab/engine lain, konsistensi Progress/Maturity/Defects/SSOT) — perbaiki feasible in-app; fakta/probe nyata atau UNKNOWN/heuristik.
2. **Event (lanjutan):** verifikasi runtime UI `EVENTS` + readiness setelah revamp; bila sudah stabil, naikkan `F-2.3.1`/`F-2.3.2` dari `DALAM_PENGERJAAN` → `LULUS_SEMPURNA` **hanya setelah** build+test lolos. Jangan tambah ke `features.json` sebelum itu (generator akan menandai DONE/100% prematur).
2. **[MINOR test]** coverage feasible login/PIN, SOS, kas (CI-runnable/Robolectric bila memungkinkan).
3. **[MINOR security]** evaluasi/rencana SQLCipher (keputusan/L2 owner).
4. **Static analysis bertahap:** rapikan/tegakkan ktlint atau tambah Detekt tanpa memecah CI.
5. **Release/OTA:** validasi Delta OTA (`v1.10` release not found) saat aset tersedia.

## Blocked / needs decision (L2 or external)
- **[MAJOR][multi-user]** backend + akun server. *No fake sync.*
- **[MAJOR][database]** schema Room `1,2,3,4,8,9.json` hilang — export/commit valid saja.
- **Release `v1.10`:** `release not found`. **Keputusan owner (L2):** SQLCipher; production signing secrets.

## Scope reminders (production)
- Finansial rilis awal = **iuran Kas saja**. **Marketplace/Arisan = DEFERRED**. **Asuransi & Intercom Helm = DEFERRED** (dikecualikan dari denominator progres PhaseTree, bukan cacat in-scope).
- Jangan klaim production-ready jika ada gap kritis/CI gagal/perilaku belum terverifikasi.

## How to resume (for next AI/model)
1. Read this file, then `docs/ABOUT.md` + `app/src/main/assets/about_manifest.json`, `README.md`, `ROADMAP.md`, `.github/about/features.json`.
2. Run: `git status && git log --oneline -10`, `python3 .github/scripts/check_file_length.py`, `python3 .github/scripts/audit_minus.py`, `gh run list -R ardyniech/DRG-APK --limit 5`.
3. Pick next safe batch from "Next steps", implement + verify + commit/push, update this file and SSOT, report facts.
4. Follow skills: `drg-apk-to-production`, `auto-continue-workflow`, `parallel-dev-workflow`, `disciplined-dev-workflow`, `safe-change-workflow`.
