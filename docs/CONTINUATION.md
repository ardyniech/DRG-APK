# DRG-APK Continuation / Handoff

> Durable handoff trail for model/agent switches. Keep this file current at the end of every batch and at every stop point. Do not rely on chat memory.

## Current state
- **Last updated (UTC):** 2026-11-12
- **Repo/branch:** `projects/DRG-APK` — `main`
- **HEAD (pushed):** pending this batch commit (base: `d9dc51a` devtools scope/progress/scoring)
- **Working tree:** to be clean after commit/push
- **Latest CI (known):** `43131829544` **success** (`a744c01`). Confirm recent runs: `gh run list -R ardyniech/DRG-APK --limit 5`.

## Audit snapshot
- **Command:** `python3 .github/scripts/audit_minus.py`
- **Result:** **0 BLOCKER, 2 MAJOR, 2 MINOR** (total 4)
  - **MAJOR [multi-user]:** Seed lokal-only → data dummy per-HP, bukan komunitas bersama. (Butuh backend + akun server.)
  - **MAJOR [database]:** Schema Room hilang `1,2,3,4,8,9.json`. (Export/commit valid; **jangan difabrikasi**.)
  - **MINOR [security]:** SQLite belum terenkripsi/SQLCipher (risiko data-at-rest terdokumentasi di `README.md`).
  - **MINOR [test]:** `androidTest` minim (alur kritis login/SOS/kas belum ter-cover instrumented).
- **Line cap:** `check_file_length.py` → PASS (473 files, <125 lines).

## Done (recent batches)
- **Foundations/refactors (earlier):** delta OTA, signing, honest sync/P2P local-only, audit BLOCKERs cleared, hex-token refactors, CI compile fixes, SSOT refreshes.
- **`f35aca3`:** `docs/CONTINUATION.md` trail. Skills: `auto-continue-workflow`, `drg-apk-to-production`.
- **`5c10408`:** ktlint CLI warn-only/non-blocking in CI (cleared MINOR `[ci]`).
- **`fe139e7` (DevTools honesty part 1):** `MultiUserGapScanner` defects refreshed; `DevBugHunterTab` dynamic; `DevScoreBanner` actual penalty.
- **`d9dc51a` (DevTools honesty part 2):** Asuransi/Intercom Helm marked **PENDING/DEFERRED** (out-of-scope) in `DeadButtonShallowScanner` & PhaseTree (F-2.2.2, F-3.2.1); `DevScoringEngine` progress penalty derived from PhaseTree (defect+in-progress).
- **This batch (DevTools honesty part 3 — build/CI/integrity/topology):**
  - `BuildDeploymentEngine`: removed fabricated metrics (hash/coverage/suites/duration/failures) → returns UNKNOWN/-1, `isPipelineHealthy=false` (unknown≠healthy).
  - `CiCdStatusScanner`: pipeline definition only with UNKNOWN runtime statuses; steps updated to reflect actual workflow (incl. audit/manifest/ktlint); no fake PASS claims.
  - `DevBuildDeploymentTab` / `DevCiCdTab`: UI handles UNKNOWN (no HEALTHY/PASS green lies).
  - `CodeIntegrityAuditor`: replaced stale SMELL-01/02 (GlobalScope in BackgroundSyncEngine, Divider in BugHunterTab) with honest heuristic/info items.
  - `NodeTopologyScanner`: NODE-MESH-01 text updated (local queue/kerangka ada, signaling/discovery end-to-end belum fungsional).
  - SSOT regenerated.

## Next steps (ordered, toward production)
1. **Monitor CI** untuk commit ter-push; jaga `main` hijau. Perbaiki regresi dulu.
2. **DevTools menyeluruh (lanjutkan):** jujurkan & selaraskan sisa scanner/tab/engine (hapus hardcode/fake, label "unknown/tidak terprobe" bila perlu):
   - `ProductionDefectScanner` DEFECT-01..04: validasi vs kondisi aktual dan perbarui jika stale.
   - `PhaseTreePhase5` (F-5.2.2 sync worker: honest local-only queue, cloud upload belum) — pastikan deskripsi/status konsisten.
   - `UiUxAuditEngine`, `SecurityAuditEngine` (isSecure hardcode/probe?), `ServerOverloadMonitor` (sudah mostly honest), `DeviceAdvisorEngine`, `DevicePerformanceEngine`, `DynamicApkInspector`, `DynamicDbIntrospector`, `RuntimeCapabilityProber`, `RealtimeMemoryProbe`, `RealtimeNetworkProbe`, `EarlySymptomDetector`, `RootCause*`, `Dev*Tab` terkait — hilangkan skor/data fabrikasi, tampilkan fakta/probe nyata atau UNKNOWN.
   - Pastikan Progress/Maturity/Defects/BugHunter/SSOT/RCA konsisten dan tidak menyesatkan (progres scope-aware).
3. **[MINOR test]** coverage feasible login/PIN, SOS, kas (CI-runnable/Robolectric bila memungkinkan).
4. **[MINOR security]** evaluasi/rencana SQLCipher (keputusan/L2 owner); risiko tetap terdokumentasi.
5. **Static analysis bertahap:** rapikan/tegakkan ktlint atau tambah Detekt tanpa memecah CI.
6. **Release/OTA:** validasi Delta OTA (`v1.10` release not found) saat aset tersedia.

## Blocked / needs decision (L2 or external)
- **[MAJOR][multi-user]** backend + akun server untuk sinkron nyata. *No fake sync.*
- **[MAJOR][database]** schema Room `1,2,3,4,8,9.json` hilang — export/commit valid saja, **jangan difabrikasi**.
- **Release `v1.10`:** `release not found`.
- **Keputusan owner (potensial L2):** SQLCipher/migrasi enkripsi DB; production signing secrets. Aksi L2 = ask-first.

## Scope reminders (production)
- Finansial rilis awal = **iuran Kas saja**. **Marketplace/Arisan = DEFERRED**. **Asuransi & Intercom Helm = PENDING DEV**.
- Jangan klaim production-ready jika ada gap kritis/CI gagal/perilaku belum terverifikasi.

## How to resume (for next AI/model)
1. Read this file, then `docs/ABOUT.md` + `app/src/main/assets/about_manifest.json`, `README.md`, `ROADMAP.md`, `.github/about/features.json`.
2. Run: `git status && git log --oneline -10`, `python3 .github/scripts/check_file_length.py`, `python3 .github/scripts/audit_minus.py`, `gh run list -R ardyniech/DRG-APK --limit 5`.
3. Pick next safe batch from "Next steps", implement + verify + commit/push, update this file and SSOT, report facts.
4. Follow skills: `drg-apk-to-production`, `auto-continue-workflow`, `parallel-dev-workflow`, `disciplined-dev-workflow`, `safe-change-workflow`.
