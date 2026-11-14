# DRG-APK Continuation / Handoff

> Durable handoff trail for model/agent switches. Keep this file current at the end of every batch and at every stop point. Do not rely on chat memory.

## Current state
- **Last updated (UTC):** 2026-11-13
- **Repo/branch:** `projects/DRG-APK` — `main`
- **HEAD (pushed):** `4fd22c9` + batch "tokenisasi permukaan glass" (lihat Done).
- **Working tree:** clean (batch tokenisasi glass sudah di-commit di batch yang sama dengan file ini).
- **Latest CI:** deprioritized per owner (preview via aigoogle.studio); last known issues fixed in `ab0b581`/`a4f7032`. Confirm if needed: `gh run list -R ardyniech/DRG-APK --limit 5`.

## Audit snapshot
- **Command:** `python3 .github/scripts/audit_minus.py`
- **Result:** **0 BLOCKER, 2 MAJOR, 3 MINOR** (total 5)
  - **MAJOR [multi-user]:** Seed lokal-only (butuh backend + akun server). *No fake sync.*
  - **MAJOR [database]:** Schema Room hilang `1,2,3,4,8,9.json` (export/commit valid saja; **jangan difabrikasi**).
  - **MINOR [security]:** SQLite belum terenkripsi/SQLCipher (risiko terdokumentasi di `README.md`).
  - **MINOR [code]:** hardcoded `Color(0x...)` di 1 file (palet tema `AppThemeModels.kt` — tabel definisi palet; komponen sudah bersih).
  - **MINOR [test]:** `androidTest` minim (alur kritis login/SOS/kas).
- **Line cap:** PASS (`check_file_length.py`, semua <125 lines).

## DevTools metric truth (2026-11-13)
- Angka besar di header DevTools **bukan** progres fitur. Itu `ProjectHealthScore.finalScore`:
  `95 − (6 cacat produksi × 2) − (5 gap progres × 2) = 73` → **Grade B (heuristik)**.
- **Progres Fitur (PhaseTree, tab Tree Progress):** `LULUS ÷ fitur in-scope` (dihitung runtime oleh `PhaseTreeScanner`). Fitur deferred (Asuransi `F-2.2.2`, Intercom Helm `F-3.2.1`) **dihapus** dari roadmap — rilis awal hanya iuran Kas; total in-scope turun 2.
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
  - **Scope:** event `F-2.3.1` (engine) & `F-2.3.2` (UI) → `DALAM_PENGERJAAN`. (Asuransi/Intercom Helm dihapus dari roadmap.)
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
- **This batch (sinkronisasi UI ke dashboard — modul lain, `11482bc` lanjutan):**
  - **Kartu opaque → glass bertema** (13 file): `forum_workshop/{WorkshopCard,ForumPostCard,ForumListRowItem}`, `members/{PoskoCard,DriverMemberCard}`, `profile/ProfileKtaCard`, `profile/primitives/CommunityActivityPointsList`, `radar/primitives/LiveSharingDriversRow`, `services/AiAssistantChatList`, `community/CommunitySubTabRow`, `auth/LandingFeatureThumbnailCard`, `dashboard/{DriverProfileOverviewCard,DriverProgressiveOnboardingCard}`. Semua kini memakai `DrgGlassCard`/`DrgLuminousGlassCard` (default `theme.glassBg` + border tema), bukan `Surface(color = Color.White)`.
  - **Dashboard (acuan) dirapikan internal:** `WeatherHeroCard`, `QuickTile`, `CommunityLiveStatusCard`, `DashboardDesktopLauncher`, `RecentCommunityActivitiesSection` tidak lagi hardcode putih — pakai `LocalAppVisualTheme.current.glassBg`.
  - **Root opaque dihapus:** `CommunityHubScreen` tidak lagi mengecat `DrgBackgroundGradient` (atmosfer global tema terlihat, sama seperti dashboard).
  - **Hasil pindai:** **0** container `Surface/Card(color = Color.White)` tersisa di seluruh `modules/`.
  - **Temuan (belum diubah):** kode mati UI — `treasury/TreasuryScreen.kt`, `treasury/primitives/KasBalanceCard.kt`, `treasury/TreasuryHealthCard.kt`, `forum_workshop/WorkshopScreen.kt`, `radar/DriverRadarScreen.kt`, `radar/LiveMapScreen.kt` (tanpa pemanggil; UI Kas yang live = `KasTransparanComponent` via `TransparencyReportScreen`). Penghapusan menunggu keputusan produk.
  - **Verifikasi:** `:app:compileDebugKotlin` **BUILD SUCCESSFUL**; 10 test kelas lulus (`DashboardGlassmorphismTest`, `ForumPersistenceTest`, `PoskoManagementTest`, `PoskoProximityAndWatermarkTest`, `KasTransparanComponentTest`, `TreasuryAuditComputationTest`, `GamificationAndLoyaltyTest`, `LandingFeatureAutoScrollTest`, `ProgressiveOnboardingLogicTest`, `DriverContactInteractionTest`); `check_file_length.py` PASS; `audit_minus.py` 0 BLOCKER / 2 MAJOR / 3 MINOR.
- **This batch (tokenisasi permukaan glass — `4fd22c9` lanjutan):**
  - **Token baru di `ui/theme/Color.kt`:** `DrgGlassSurfaceSoft` (80%), `DrgGlassSurface` (86%), `DrgGlassSurfaceStrong` (92%), `DrgGlassSurfaceBright` (95%), `DrgScrimOverlay` — menggantikan literal `Color(0xCC/DC/DB/EB/EC/E8/F2/F5/FAFFFFFF)` dan `Color(0x660F172A)`.
  - **26 file dikonversi** dari hex mentah ke token bernama (termasuk 5 file yang sebelumnya hanya punya import tema eksplisit: `GlassCard.kt`, `LandingAuthMenuDialog.kt`, `EventCommitteeTaskAddCard.kt`, `LiveMapControlsFabColumn.kt`, `DrgLuminousGlassCard.kt`).
  - **Dampak audit:** temuan `[code] hardcoded hex Color(0x...)` turun **42 → 22 file** (sisa = palet aksen semantik amber/blue/violet/indigo + file definisi token; sengaja tidak diganti mekanis).
  - **Verifikasi:** `:app:compileDebugKotlin` **BUILD SUCCESSFUL**; test `DashboardGlassmorphismTest`, `ForumPersistenceTest`, `KasTransparanComponentTest`, `DevToolsPhaseTreeAndUiUxTest`, `NotificationSettingsTest` **lulus**; `check_file_length.py` PASS.
- **This batch (token aksen eksak — `0bb0bf1` lanjutan):**
  - **16 file** mengganti literal yang **nilainya persis sama** dengan token yang sudah ada: `Color(0xFF0284C7)`→`DrgBlueAccent`, `0xFF7C3AED`→`DrgPurpleAccent`, `0xFFF1F5F9`→`DrgSlateBgSoftAlt`, `0xFFD97706`→`DrgWarningText`, `0xFFB91C1C`→`DrgRedPanicDark`, `0xFF991B1B`→`DrgRedPanicDarker`. Tidak ada perubahan warna/visual — hanya penamaan.
  - **Dampak audit:** temuan `[code] hardcoded hex` **22 → 10 file** (3 di antaranya file definisi token yang memang menyimpan hex).
  - **Verifikasi:** `:app:compileDebugKotlin` **BUILD SUCCESSFUL**; `check_file_length.py` PASS; 6 test kelas **lulus** (`DashboardDesktopLauncherTest`, `DashboardGlassmorphismTest`, `CommunityEventTransparencyTest`, `DevToolsPhaseTreeAndUiUxTest`, `DevToolsMaturityScoringTest`, `SosFabAndModalTest`).
- **This batch (palet aksen + tint glass — `d179f66` lanjutan):**
  - **10 file non-definisi-token** ditokenisasi (termasuk `AppThemeModels.kt` untuk nilai yang punya token eksak): `SquircleLauncherItems`, `EventTransparencyGlassCard`, `DevApkSkewCard`, `DrgLuminousGlassCard`, `DrgButtons`, `DrgBottomNavSatellite`, `DevProgressTab`, `GlassCard`, `CockpitHeaderSosButton`, `AppThemeModels`.
  - **Token baru di `Color.kt`:** `DrgPinkAccent`, `DrgSkyAccent`, `DrgYellowAccent`, `DrgIndigoVioletAccent`, `DrgGlassBorderGreenLight`, `DrgGlassBorderBlueLight`, `DrgGlassTintBlueSoft`, `DrgGlassTintRedSoft`, `DrgDisabledGradientStart`, `DrgDisabledGradientEnd`.
  - **Sisa literal (`AppThemeModels.kt`, 19 nilai)** adalah palet per-varian tema (skyTop/skyMid/orb/glassBg per tema) — ini tabel definisi palet, bukan magic number di komponen. Audit sekarang **1 file** (`[code] hardcoded hex`).
  - **Verifikasi:** `:app:compileDebugKotlin` **BUILD SUCCESSFUL**; `check_file_length.py` PASS; 6 test kelas **lulus** (`DashboardDesktopLauncherTest`, `DashboardGlassmorphismTest`, `SosFabAndModalTest`, `DevToolsPhaseTreeAndUiUxTest`, `DevToolsMaturityScoringTest`, `CommunityEventTransparencyTest`).
- **This batch (perbaikan test stale `DevToolsMultiUserGapTest`):**
  - `extraDefects()` kini 7 (DEFECT-05..11) dan total 11 (4+7): ekspektasi test diperbarui (8→7, 12→11, CONCEPT_FLAW 6→5), kata kunci judul diselaraskan ("SYNCED"→"Cloud", "Firestore"→"Firebase", "P2P Signaling"→"P2P"), dan DEFECT-12 (tidak ada) dihapus dari `containsAll`.
  - **Verifikasi:** `:app:testDebugUnitTest --tests DevToolsMultiUserGapTest` **BUILD SUCCESSFUL**; `check_file_length.py` PASS.
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
- Finansial rilis awal = **iuran Kas saja**. **Marketplace = DEFERRED**. Asuransi & Intercom Helm **dihapus** dari roadmap (rilis awal hanya iuran Kas). (dikecualikan dari denominator progres PhaseTree, bukan cacat in-scope).
- Jangan klaim production-ready jika ada gap kritis/CI gagal/perilaku belum terverifikasi.

## How to resume (for next AI/model)
1. Read this file, then `docs/ABOUT.md` + `app/src/main/assets/about_manifest.json`, `README.md`, `ROADMAP.md`, `.github/about/features.json`.
2. Run: `git status && git log --oneline -10`, `python3 .github/scripts/check_file_length.py`, `python3 .github/scripts/audit_minus.py`, `gh run list -R ardyniech/DRG-APK --limit 5`.
3. Pick next safe batch from "Next steps", implement + verify + commit/push, update this file and SSOT, report facts.
4. Follow skills: `drg-apk-to-production`, `auto-continue-workflow`, `parallel-dev-workflow`, `disciplined-dev-workflow`, `safe-change-workflow`.
