# DRG-APK Continuation / Handoff

> Durable handoff trail for model/agent switches. Keep this file current at the end of every batch and at every stop point. Do not rely on chat memory.

## Current state
- **Last updated (UTC):** 2026-11-12
- **Repo/branch:** `projects/DRG-APK` — `main`
- **HEAD (pushed):** pending this batch commit (base: `a4f7032` test fix for honest APK inspector)
- **Working tree:** to be clean after commit/push
- **Latest CI (known):** `43216338139` **failure** (`ab0b581`, test compile ref to removed `LATEST_TARGET_VERSION_CODE`) → fixed by `a4f7032`; confirm latest run: `gh run list -R ardyniech/DRG-APK --limit 5`.

## Audit snapshot
- **Command:** `python3 .github/scripts/audit_minus.py`
- **Result:** **0 BLOCKER, 2 MAJOR, 2 MINOR** (total 4)
  - **MAJOR [multi-user]:** Seed lokal-only (butuh backend + akun server). *No fake sync.*
  - **MAJOR [database]:** Schema Room hilang `1,2,3,4,8,9.json` (export/commit valid saja; **jangan difabrikasi**).
  - **MINOR [security]:** SQLite belum terenkripsi/SQLCipher (risiko terdokumentasi di `README.md`).
  - **MINOR [test]:** `androidTest` minim (alur kritis login/SOS/kas).
- **Line cap:** PASS (473 files, <125 lines).

## Done (recent batches)
- Foundations/refactors, honest sync/P2P local-only, hex-token, CI fixes, handoff trail, ktlint warn-only CI.
- DevTools honesty: defects/bug hunter/score dynamic & scope-aware, Build/CI/CodeIntegrity/mesh/topology/APK/CPU/repo/git/UI-UX/phase/RCA/security probe limits dijujurkan.
- **`ab0b581`:** fix syntax `DevScoreBanner.kt`. **`a4f7032`:** update `DevToolsOutdatedApkCompatibilityTest` agar selaras perilaku `DynamicApkInspector` yang jujur (hapus ref `LATEST_TARGET_VERSION_CODE`).
- **This batch:** `DevMaturityTab` & `DevProgressTab` — label/teks diperjelas (audit terkurasi, progres jujur & scope-aware, PENDING/DEFERRED bukan cacat in-scope). SSOT regenerated.

## Next steps (ordered, toward production)
1. **Monitor CI** commit terbaru; jaga `main` hijau (prioritas).
2. **DevTools menyeluruh (lanjutkan):** tinjau sisa (DEFECT-01..03 narasi, teks tab/engine lain) untuk konsistensi fakta/probe atau UNKNOWN/heuristik.
3. **[MINOR test]** coverage feasible login/PIN, SOS, kas (CI-runnable/Robolectric bila memungkinkan).
4. **[MINOR security]** evaluasi/rencana SQLCipher (keputusan/L2 owner).
5. **Static analysis bertahap:** rapikan/tegakkan ktlint atau tambah Detekt tanpa memecah CI.
6. **Release/OTA:** validasi Delta OTA (`v1.10` release not found) saat aset tersedia.

## Blocked / needs decision (L2 or external)
- **[MAJOR][multi-user]** backend + akun server. *No fake sync.*
- **[MAJOR][database]** schema Room `1,2,3,4,8,9.json` hilang — export/commit valid saja.
- **Release `v1.10`:** `release not found`. **Keputusan owner (L2):** SQLCipher; production signing secrets.

## Scope reminders (production)
- Finansial rilis awal = **iuran Kas saja**. **Marketplace/Arisan = DEFERRED**. **Asuransi & Intercom Helm = PENDING DEV**.
- Jangan klaim production-ready jika ada gap kritis/CI gagal/perilaku belum terverifikasi.

## How to resume (for next AI/model)
1. Read this file, then `docs/ABOUT.md` + `app/src/main/assets/about_manifest.json`, `README.md`, `ROADMAP.md`, `.github/about/features.json`.
2. Run: `git status && git log --oneline -10`, `python3 .github/scripts/check_file_length.py`, `python3 .github/scripts/audit_minus.py`, `gh run list -R ardyniech/DRG-APK --limit 5`.
3. Pick next safe batch from "Next steps", implement + verify + commit/push, update this file and SSOT, report facts.
4. Follow skills: `drg-apk-to-production`, `auto-continue-workflow`, `parallel-dev-workflow`, `disciplined-dev-workflow`, `safe-change-workflow`.
