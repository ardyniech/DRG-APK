# DRG-APK Continuation / Handoff

> Durable handoff trail for model/agent switches. Keep this file current at the end of every batch and at every stop point. Do not rely on chat memory.

## Current state
- **Last updated (UTC):** 2026-11-12
- **Repo/branch:** `projects/DRG-APK` — `main`
- **HEAD (pushed):** pending this batch commit (base: `5c10408` ci ktlint warn-only + handoff)
- **Working tree:** to be clean after commit/push
- **Latest CI (known):** `43131829544` **success** (`a744c01`, 2026-11-12T01:03:48Z). Runs for `f35aca3`/`5c10408` may be in progress/completed — confirm with `gh run list -R ardyniech/DRG-APK --limit 5`.

## Audit snapshot
- **Command:** `python3 .github/scripts/audit_minus.py`
- **Result:** **0 BLOCKER, 2 MAJOR, 2 MINOR** (total 4)
  - **MAJOR [multi-user]:** Seed hanya bila members kosong → tiap HP baru punya data dummy sendiri, bukan data komunitas bersama. (Fix: backend + akun server, sinkron nyata.)
  - **MAJOR [database]:** Schema Room hilang `1.json,2.json,3.json,4.json,8.json,9.json` (exportSchema=true tapi tidak commit) → verifikasi migrasi v1→v13 tidak lengkap. (Fix: generate & commit schema hilang via Room export valid; **jangan difabrikasi**.)
  - **MINOR [security]:** Room SQLite belum terenkripsi (SQLCipher tidak ada) — NIK/SIM/kas plain di file DB. Risiko data-at-rest sudah terdokumentasi di `README.md`; keputusan/implementasi enkripsi masih terbuka.
  - **MINOR [test]:** `androidTest` cuma 1 file contoh → alur kritis login/SOS/kas belum ter-cover instrumented.
- **Line cap:** `python3 .github/scripts/check_file_length.py` → PASS (473 files, all < 125 lines).

## Done (recent batches)
- **OTA/CI/release foundations (earlier):** delta OTA (BSDIFF40, `release_delta.yml`), signing consistency, honest sync/P2P local-only, README/test count & SSOT refreshes.
- **Hex-token refactor batches (pushed):** auth/profile/treasury/dashboard/about/emergency/radar/workflow/cinematic rider tokenized; splits for <125 lines; SSOT refreshed.
- **`9a47730` + `a744c01`:** fixed CI compile break (`RiderFairingCowl`) and excluded `ui/theme/` from hex audit; CI green.
- **`f35aca3`:** added `docs/CONTINUATION.md` handoff trail.
- **`5c10408`:** added **ktlint (CLI, warn-only/non-blocking)** in `.github/workflows/android_ci.yml`; cleared audit MINOR `[ci]`.
- **This batch (DevTools honesty — part 1):**
  - `modules/devtools/MultiUserGapScanner.kt`: updated extra defects (DEFECT-05/06/07/08/09/10/11) to reflect current reality (sync/signaling now honest/local-only but no real server upload/discovery; schema missing 1-4,8,9; Asuransi/Intercom marked pending/deferred per scope; removed stale "fake SYNCED"/log-only/hex-count claims).
  - `modules/devtools/DevBugHunterTab.kt`: replaced hardcoded fake summary/findings with **dynamic** rendering from `ProjectHealthScore`, `DeadButtonShallowScanner`, and `ProductionDefectScanner`.
  - `modules/devtools/DevScoreBanner.kt`: removed hardcoded "Penalti -20%" badge; shows actual computed penalty/score breakdown.
  - SSOT regenerated. (No audit_minus finding count change.)

## Next steps (ordered, toward production)
1. **Monitor CI** for pushed commits; keep `main` green. Fix regressions first.
2. **DevTools menyeluruh (lanjutkan):** audit semua scanner/tab/engine agar jujur & selaras SSOT/scope (banyak masih hardcoded/stale):
   - `DevScoringEngine` (`progressPenaltiesCount` hardcoded=1, formula/grade), `DeadButtonShallowScanner` (daftar fitur/stage/dead/shallow termasuk Asuransi/Intercom harus ditandai PENDING/DEFERRED sesuai roadmap), `PhaseTreePhase1-5` & metrik/progres ("64%" dsb) agar tidak menandai fitur deferred out-of-scope sebagai cacat menyesatkan.
   - `ProductionDefectScanner` DEFECT-01..04: validasi terhadap kondisi aktual (P2P/SMS/GPS/RBAC) dan perbarui jika stale.
   - Scanner/engine lain: `BuildDeploymentEngine`, `CiCdStatusScanner`, `CodeIntegrityAuditor`, `NodeTopologyScanner`, `SecurityAuditEngine`, `ServerOverloadMonitor`, `UiUxAuditEngine`, `Device*`, `Realtime*`, `RootCause*` — hilangkan data hardcode/fake, tampilkan status "unknown/unavailable" jika tidak bisa diprobe.
   - `DevSsotTab` sudah baca `about_manifest.json` minus (bagus) — pastikan konsisten; `DevProgress/Defects/Maturity/BugHunter` saling konsisten.
3. **[MINOR test]** tambah coverage feasible untuk alur kritis login/PIN, SOS, kas (CI-runnable/Robolectric bila memungkinkan).
4. **[MINOR security]** evaluasi/rencana SQLCipher atau enkripsi DB (keputusan/L2 owner) dengan risiko tetap terdokumentasi.
5. **Static analysis bertahap:** rapikan/tegakkan ktlint atau tambah Detekt tanpa memecah CI.
6. **Release/OTA:** validasi Delta OTA (`v1.10` release not found saat ini) saat aset tersedia.

## Blocked / needs decision (L2 or external)
- **[MAJOR][multi-user]** butuh backend + akun server untuk sinkron nyata. *No fake sync.*
- **[MAJOR][database]** schema Room `1,2,3,4,8,9.json` hilang — export/commit valid saja, **jangan difabrikasi**.
- **Release `v1.10`:** `release not found`.
- **Keputusan owner (potensial L2):** strategi SQLCipher/migrasi enkripsi DB; production signing secrets. Aksi L2 = ask-first.

## Scope reminders (production)
- Finansial rilis awal = **iuran Kas saja**. **Marketplace/Arisan = DEFERRED**. **Asuransi & Intercom Helm = PENDING DEV** (tandai pending/deferred, bukan "cacat in-scope" yang menyesatkan).
- Jangan klaim production-ready jika ada gap kritis/CI gagal/perilaku belum terverifikasi.

## How to resume (for next AI/model)
1. Read this file, then `docs/ABOUT.md` + `app/src/main/assets/about_manifest.json`, `README.md`, `ROADMAP.md`, `.github/about/features.json`.
2. Run: `git status && git log --oneline -10`, `python3 .github/scripts/check_file_length.py`, `python3 .github/scripts/audit_minus.py`, `gh run list -R ardyniech/DRG-APK --limit 5`.
3. Pick next safe batch from "Next steps" (respecting blocked/L2), implement + verify + commit/push, update this file and SSOT as needed, report facts.
4. Follow skills: `drg-apk-to-production`, `auto-continue-workflow`, `parallel-dev-workflow`, `disciplined-dev-workflow`, `safe-change-workflow`.
