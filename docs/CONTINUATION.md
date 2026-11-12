# DRG-APK Continuation / Handoff

> Durable handoff trail for model/agent switches. Keep this file current at the end of every batch and at every stop point. Do not rely on chat memory.

## Current state
- **Last updated (UTC):** 2026-11-12
- **Repo/branch:** `projects/DRG-APK` — `main`
- **HEAD (pushed):** pending this batch commit (base: `fe139e7` devtools honesty part 1)
- **Working tree:** to be clean after commit/push
- **Latest CI (known):** `43131829544` **success** (`a744c01`). Runs for recent pushes — confirm with `gh run list -R ardyniech/DRG-APK --limit 5`.

## Audit snapshot
- **Command:** `python3 .github/scripts/audit_minus.py`
- **Result:** **0 BLOCKER, 2 MAJOR, 2 MINOR** (total 4)
  - **MAJOR [multi-user]:** Seed lokal-only → tiap HP data dummy sendiri, bukan data komunitas bersama. (Butuh backend + akun server, sinkron nyata.)
  - **MAJOR [database]:** Schema Room hilang `1,2,3,4,8,9.json` (exportSchema=true tapi tidak commit). (Generate & commit schema valid; **jangan difabrikasi**.)
  - **MINOR [security]:** Room SQLite belum terenkripsi (SQLCipher) — NIK/SIM/kas plain; risiko data-at-rest terdokumentasi di `README.md`.
  - **MINOR [test]:** `androidTest` cuma 1 file contoh → alur kritis login/SOS/kas belum ter-cover instrumented.
- **Line cap:** `python3 .github/scripts/check_file_length.py` → PASS (473 files, all < 125 lines).

## Done (recent batches)
- **OTA/CI/release & refactors (earlier):** delta OTA, signing consistency, honest sync/P2P local-only, audit BLOCKERs cleared, hex-token refactors, SSOT refreshes.
- **`9a47730`+`a744c01`:** fixed `RiderFairingCowl` compile/imports; audit hex excludes `ui/theme/`. CI green.
- **`f35aca3`:** `docs/CONTINUATION.md` handoff trail. Skills: `auto-continue-workflow`, `drg-apk-to-production` (with mandatory trail).
- **`5c10408`:** ktlint (CLI, warn-only/non-blocking) step in `.github/workflows/android_ci.yml`; cleared audit MINOR `[ci]`.
- **`fe139e7` (DevTools honesty part 1):** `MultiUserGapScanner` defects refreshed to reality (sync/signaling honest/local-only gaps, schema missing, Asuransi/Intercom pending/deferred); `DevBugHunterTab` dynamic from scanners/score; `DevScoreBanner` shows actual computed penalty.
- **This batch (DevTools honesty part 2 — scope/progress/scoring):**
  - `DeadButtonShallowScanner`: Asuransi & Intercom Helm ditandai **PENDING/DEFERRED** (out-of-scope rilis awal) — tidak lagi dihitung sebagai dead button/shallow.
  - `PhaseTreePhase1And2/3And4`: F-2.2.2 (Klaim Asuransi) & F-3.2.1 (Intercom Helm GATT) diubah ke **PENDING, hasDefect=false** (deferred, bukan cacat in-scope). F-3.1.1 PTT/P2P tetap TERKENDALA_CACAT (limitasi nyata).
  - `DevScoringEngine`: `progressPenaltiesCount` kini dihitung dari PhaseTree (defect + in-progress) alih-alih hardcode 1.
  - SSOT diregenerasi. Progres/skor DevTools sekarang lebih jujur & scope-aware (deferred tidak menurunkan progres in-scope secara menyesatkan).

## Next steps (ordered, toward production)
1. **Monitor CI** untuk commit ter-push; jaga `main` hijau. Perbaiki regresi dulu.
2. **DevTools menyeluruh (lanjutkan — masih banyak):** buat semua scanner/tab/engine jujur & selaras SSOT/scope (hapus hardcode/fake, tampilkan "unknown/unavailable/tidak terprobe" bila tidak bisa diverifikasi):
   - Validasi & perbarui `ProductionDefectScanner` DEFECT-01..04 (RBAC client-side, P2P Walkie subnet, SMS fallback pulsa, GPS polling baterai) vs kondisi aktual.
   - Tinjau `PhaseTreePhase5` (F-5.2.2 sync worker local-only/honest tapi belum cloud upload — status/deskripsi) dan metrik progres agar konsisten.
   - Audit scanner/engine: `BuildDeploymentEngine`, `CiCdStatusScanner`, `CodeIntegrityAuditor`, `NodeTopologyScanner`, `SecurityAuditEngine`, `ServerOverloadMonitor`, `UiUxAuditEngine`, `DeviceAdvisorEngine`, `DevicePerformanceEngine`, `DynamicApkInspector`, `DynamicDbIntrospector`, `RuntimeCapabilityProber`, `RealtimeMemoryProbe`, `RealtimeNetworkProbe`, `EarlySymptomDetector`, `RootCause*` — ganti nilai bohong/hardcode dengan probe nyata atau label jujur.
   - Pastikan tab UI (Maturity/Progress/Defects/BugHunter/SSOT/RCA/Memory/Server/CI-CD/Build/Security/Node/dll.) menampilkan data konsisten & tidak menyesatkan.
3. **[MINOR test]** coverage feasible alur kritis login/PIN, SOS, kas (CI-runnable/Robolectric bila memungkinkan).
4. **[MINOR security]** evaluasi/rencana SQLCipher/enkripsi DB (keputusan/L2 owner) dengan risiko terdokumentasi.
5. **Static analysis bertahap:** rapikan/tegakkan ktlint atau tambah Detekt tanpa memecah CI.
6. **Release/OTA:** validasi Delta OTA (`v1.10` release not found) saat aset tersedia.

## Blocked / needs decision (L2 or external)
- **[MAJOR][multi-user]** backend + akun server untuk sinkron nyata. *No fake sync.*
- **[MAJOR][database]** schema Room `1,2,3,4,8,9.json` hilang — export/commit valid saja, **jangan difabrikasi**.
- **Release `v1.10`:** `release not found`.
- **Keputusan owner (potensial L2):** strategi SQLCipher/migrasi enkripsi DB; production signing secrets. Aksi L2 = ask-first.

## Scope reminders (production)
- Finansial rilis awal = **iuran Kas saja**. **Marketplace/Arisan = DEFERRED**. **Asuransi & Intercom Helm = PENDING DEV** (tandai pending/deferred, bukan cacat in-scope menyesatkan).
- Jangan klaim production-ready jika ada gap kritis/CI gagal/perilaku belum terverifikasi.

## How to resume (for next AI/model)
1. Read this file, then `docs/ABOUT.md` + `app/src/main/assets/about_manifest.json`, `README.md`, `ROADMAP.md`, `.github/about/features.json`.
2. Run: `git status && git log --oneline -10`, `python3 .github/scripts/check_file_length.py`, `python3 .github/scripts/audit_minus.py`, `gh run list -R ardyniech/DRG-APK --limit 5`.
3. Pick next safe batch from "Next steps" (respecting blocked/L2), implement + verify + commit/push, update this file and SSOT as needed, report facts.
4. Follow skills: `drg-apk-to-production`, `auto-continue-workflow`, `parallel-dev-workflow`, `disciplined-dev-workflow`, `safe-change-workflow`.
