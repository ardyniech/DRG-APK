# DRG-APK Continuation / Handoff

> Durable handoff trail for model/agent switches. Keep this file current at the end of every batch and at every stop point. Do not rely on chat memory.

## Current state
- **Last updated (UTC):** 2026-11-12
- **Repo/branch:** `projects/DRG-APK` — `main`
- **HEAD (pushed):** `a744c01` — `chore(ssot): refresh manifest after audit fix` (also `9a47730 fix: resolve RiderFairingCowl token imports and exclude theme defs from hex audit`)
- **Working tree:** clean at time of this handoff
- **Latest CI:** `43131829544` **success** (DRG Driver Android CI/CD, `a744c01`, 8m2s, 2026-11-12T01:03:48Z). Prior failures (`43077741682` etc.) were due to compile error in `RiderFairingCowl` (unresolved token imports), fixed in `9a47730`.

## Audit snapshot
- **Command:** `python3 .github/scripts/audit_minus.py`
- **Result:** **0 BLOCKER, 2 MAJOR, 3 MINOR** (total 5)
  - **MAJOR [multi-user]:** Seed hanya bila members kosong → tiap HP baru punya data dummy sendiri, bukan data komunitas bersama. (Fix: backend + akun server, sinkron nyata.)
  - **MAJOR [database]:** Schema Room hilang `1.json,2.json,3.json,4.json,8.json,9.json` (exportSchema=true tapi tidak commit) → verifikasi migrasi v1→v13 tidak lengkap. (Fix: generate & commit schema hilang via Room export valid; **jangan difabrikasi**.)
  - **MINOR [security]:** Room SQLite belum terenkripsi (SQLCipher tidak ada) — NIK/SIM/kas plain di file DB. Risiko data-at-rest sudah terdokumentasi di `README.md`; keputusan/implementasi enkripsi masih terbuka.
  - **MINOR [test]:** `androidTest` cuma 1 file contoh → alur kritis login/SOS/kas belum ter-cover instrumented.
  - **MINOR [ci]:** Detekt/KtLint tidak ada di CI — hanya line-cap + lint.
- **Line cap:** `python3 .github/scripts/check_file_length.py` → PASS (473 files, all < 125 lines).

## Done (recent batches)
- **OTA/CI/release foundations (earlier):** delta OTA (BSDIFF40, `release_delta.yml` tag `v*`, APK+patch+`update.json` sha256), signing consistency work (debug-signed fallback/prod-capable workflow), audit BLOCKERs cleared (honest sync/P2P/local-only behavior), README/test count & SSOT refreshes.
- **Hex-token refactor batches (pushed):** `b28d9d1`..`0a181a5`, `7391ec6`..`b852295`/`1a8b55f` (devtools/profile/treasury/dashboard/about/emergency/auth/radar/shared/cinematic/workflow); `DrgAtmosphericGlow` split (<125 lines).
- **`b852295` + `1a8b55f`:** tokenized auth/profile/radar hex colors + SSOT refresh.
- **`be1bb58` + `3712f68`:** tokenized workflow & cinematic rider hex colors; `MultiUserGapScanner` text adjusted (avoid hex heuristic) + SSOT refresh.
- **`9a47730` + `a744c01`:** fixed CI compile break (missing `RiderFairingCowl` token imports) and updated `audit_minus.py` to exclude `ui/theme/` token definitions from hex hardcode check; SSOT refreshed. CI green.
- **Skills (host-level, global):** created/updated `~/.config/opencode/skills/auto-continue-workflow/SKILL.md` and `~/.config/opencode/skills/drg-apk-to-production/SKILL.md` (continuous dev to production + mandatory trail/handoff).

## Next steps (ordered, toward production)
1. **Monitor CI** for any new pushes; keep `main` green. Fix regressions/compile breaks first.
2. **Static analysis gate (MINOR [ci]):** integrate Detekt/KtLint into Gradle/CI in a safe, incremental/non-breaking way (assess existing violations, add config, consider warn/report-only first step then tighten) — verify line-cap/audit/tests still pass.
3. **Instrumented/UI tests (MINOR [test]):** add feasible coverage for critical flows (login/PIN, SOS, kas) — note: emulator/device may not be available on host; prefer CI-runnable or clearly scoped additions.
4. **Security hardening (MINOR [security]):** evaluate/plan SQLCipher or equivalent DB encryption for NIK/SIM/kas (design/impact decision; may be L2 if it affects dependencies/migration/signing) while keeping risk honestly documented.
5. **Release/OTA validation:** when available, verify `v1.10` (and future) Delta OTA release assets/status/signature consistency (`gh run list/view`, `gh release view v1.10 -R ardyniech/DRG-APK`), `APK+patch+update.json` sha256, and updater path.
6. **Continue audit-driven hygiene/refactors** in batches (no fake states, <125 lines, no cross-module imports), regenerating SSOT when audit-visible facts change.

## Blocked / needs decision (L2 or external)
- **[MAJOR][multi-user] Backend & sinkron nyata:** butuh backend + akun server; `google-services`/auth/firestore & `BackgroundSyncEngine` upload sungguhan belum dapat diselesaikan lokal-only. *Do not fake sync.*
- **[MAJOR][database] Schema Room `1,2,3,4,8,9.json` hilang:** perlu export/commit schema valid dari sumber tepercaya/build environment yang sesuai; **tidak boleh difabrikasi**.
- **Release `v1.10`:** `gh release view v1.10 -R ardyniech/DRG-APK` → `release not found` (tidak dapat divalidasi saat ini).
- **Keputusan owner (potensial L2):** strategi SQLCipher/migrasi enkripsi DB; production signing keystore/secrets untuk rilis produksi (workflow mendukung debug-signed fallback). Aksi network/DNS/firewall, reboot, data deletion, package install/upgrade, exposure secrets = **ask-first**.

## Scope reminders (production)
- Finansial rilis awal = **iuran Kas saja**. **Marketplace/Arisan = DEFERRED** (bukan blocker). **Asuransi & Intercom Helm = PENDING DEV**.
- Multi-user hanya klaim bila sinkron nyata ada. Jangan klaim production-ready jika ada gap kritis/CI gagal/perilaku belum terverifikasi.

## How to resume (for next AI/model)
1. Read this file, then `docs/ABOUT.md` + `app/src/main/assets/about_manifest.json`, `README.md`, `ROADMAP.md`, `.github/about/features.json`.
2. Run: `git status && git log --oneline -10`, `python3 .github/scripts/check_file_length.py`, `python3 .github/scripts/audit_minus.py`, `gh run list -R ardyniech/DRG-APK --limit 5`.
3. Pick next safe batch from "Next steps" (respecting blocked/L2), implement + verify + commit/push, update this file and SSOT as needed, report facts.
4. Follow skills/workflows: `drg-apk-to-production`, `auto-continue-workflow`, `parallel-dev-workflow`, `disciplined-dev-workflow`, `safe-change-workflow`.
