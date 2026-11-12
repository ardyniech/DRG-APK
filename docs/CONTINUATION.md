# DRG-APK Continuation / Handoff

> Durable handoff trail for model/agent switches. Keep this file current at the end of every batch and at every stop point. Do not rely on chat memory.

## Current state
- **Last updated (UTC):** 2026-11-12
- **Repo/branch:** `projects/DRG-APK` — `main`
- **HEAD (pushed):** pending this batch commit (base: `1ac9ac0` devtools build/CI/integrity/topology)
- **Working tree:** to be clean after commit/push
- **Latest CI (known):** `43131829544` **success** (`a744c01`). Confirm recent: `gh run list -R ardyniech/DRG-APK --limit 5`.

## Audit snapshot
- **Command:** `python3 .github/scripts/audit_minus.py`
- **Result:** **0 BLOCKER, 2 MAJOR, 2 MINOR** (total 4)
  - **MAJOR [multi-user]:** Seed lokal-only (butuh backend + akun server). *No fake sync.*
  - **MAJOR [database]:** Schema Room hilang `1,2,3,4,8,9.json` (export/commit valid saja; **jangan difabrikasi**).
  - **MINOR [security]:** SQLite belum terenkripsi/SQLCipher (risiko data-at-rest terdokumentasi di `README.md`).
  - **MINOR [test]:** `androidTest` minim (alur kritis login/SOS/kas).
- **Line cap:** `check_file_length.py` → PASS (473 files, <125 lines).

## Done (recent batches)
- Foundations/refactors: delta OTA, honest sync/P2P local-only, audit BLOCKERs cleared, hex-token refactors, CI compile fixes, SSOT refreshes.
- `f35aca3`: handoff trail. Skills: `auto-continue-workflow`, `drg-apk-to-production`.
- `5c10408`: ktlint CLI warn-only in CI (cleared MINOR `[ci]`).
- `fe139e7`: DevTools defects refreshed (MultiUserGapScanner), BugHunter dynamic, ScoreBanner actual penalty.
- `d9dc51a`: scope-aware progress/scoring (Asuransi/Intercom PENDING/DEFERRED in scanner & PhaseTree; progress penalty derived from PhaseTree).
- `1ac9ac0`: BuildDeploymentEngine/CiCdStatusScanner no fabricated runtime metrics (UNKNOWN), DevBuild/CI tabs honest, CodeIntegrityAuditor stale smells fixed, NodeTopology mesh text updated.
- **This batch (DevTools honesty part 4 — UI/UX, defects, phase):**
  - `UiUxAuditEngine` + `DevUiUxAuditTab`: converted from fabricated "real score/audit" to **heuristik/checklist** with estimasi scores and disclaimer (bukan probe runtime).
  - `PhaseTreePhase5`: F-5.2.1 set to DALAM_PENGERJAAN (DevTools audit makin jujur, belum "lulus sempurna"); F-5.2.2 deskripsi/status diselaraskan (queue lokal jujur, cloud upload belum).
  - `ProductionDefectScanner`: DEFECT-04 diubah jadi klaim risiko baterai yang jujur/terukur (bukan angka spesifik tanpa verifikasi).
  - SSOT regenerated.

## Next steps (ordered, toward production)
1. **Monitor CI** commit ter-push; jaga `main` hijau. Perbaiki regresi dulu.
2. **DevTools menyeluruh (lanjutkan):** jujurkan sisa scanner/tab/engine (hapus hardcode/fake, gunakan probe nyata atau label UNKNOWN/heuristik):
   - `ProductionDefectScanner` DEFECT-01..03: tinjau keakuratan vs kondisi aktual; perjelas bila asumsi.
   - `SecurityAuditEngine`: beberapa cek `isSecure=true` dengan narasi risiko — pastikan tidak mengklaim "aman" melebihi probe nyata.
   - Scanner/engine lain: `DeviceAdvisorEngine`, `DevicePerformanceEngine`, `DynamicApkInspector`, `DynamicDbIntrospector`, `RuntimeCapabilityProber`, `RealtimeMemoryProbe`, `RealtimeNetworkProbe`, `EarlySymptomDetector`, `RootCause*`, `DevDevice*/DevMemory*/DevServer*/DevNodeTopology*/DevSecurityAudit*/DevRootCause*` tabs — audit & perbaiki nilai statis/fabrikasi.
   - Pastikan konsistensi Progress/Maturity/Defects/BugHunter/SSOT/RCA (progres scope-aware, tanpa fake).
3. **[MINOR test]** coverage feasible login/PIN, SOS, kas (CI-runnable/Robolectric bila memungkinkan).
4. **[MINOR security]** evaluasi/rencana SQLCipher (keputusan/L2 owner); risiko terdokumentasi.
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
