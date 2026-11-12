# DRG-APK Continuation / Handoff

> Durable handoff trail for model/agent switches. Keep this file current at the end of every batch and at every stop point. Do not rely on chat memory.

## Current state
- **Last updated (UTC):** 2026-11-12
- **Repo/branch:** `projects/DRG-APK` — `main`
- **HEAD (pushed):** pending this batch commit (base: `be373d9` devtools perf/repo/git)
- **Working tree:** to be clean after commit/push
- **Latest CI (known):** confirm with `gh run list -R ardyniech/DRG-APK --limit 5`.

## Audit snapshot
- **Command:** `python3 .github/scripts/audit_minus.py`
- **Result:** **0 BLOCKER, 2 MAJOR, 2 MINOR** (total 4)
  - **MAJOR [multi-user]:** Seed lokal-only (butuh backend + akun server). *No fake sync.*
  - **MAJOR [database]:** Schema Room hilang `1,2,3,4,8,9.json` (export/commit valid saja; **jangan difabrikasi**).
  - **MINOR [security]:** SQLite belum terenkripsi/SQLCipher (risiko terdokumentasi di `README.md`).
  - **MINOR [test]:** `androidTest` minim (alur kritis login/SOS/kas).
- **Line cap:** PASS (473 files, <125 lines).

## Done (recent batches)
- Foundations/refactors, honest sync/P2P local-only, hex-token refactors, CI fixes, handoff trail, ktlint warn-only CI.
- **`fe139e7`:** MultiUserGapScanner defects refreshed; DevBugHunterTab dynamic; DevScoreBanner actual penalty.
- **`d9dc51a`:** Asuransi/Intercom **PENDING/DEFERRED** (scope-aware) in scanner & PhaseTree; scoring progress from PhaseTree.
- **`1ac9ac0`:** Build/CiCd engines & tabs UNKNOWN (no fabricated metrics/PASS); CodeIntegrity stale smells fixed; NodeTopology mesh text honest.
- **`4207fa3`:** UiUxAudit heuristik/estimasi; PhaseTreePhase5 F-5.2.1/2 selaras; DEFECT-04 dihedging.
- **`be373d9`:** DevicePerformance CPU% UNKNOWN (probe suhu/termal/thread nyata); DevRepoHealth/DevGitVersion hapus klaim statis palsu.
- **This batch (DevTools honesty part 6 — security & topology):**
  - `SecurityAuditEngine`: item sandbox DB & privasi SOS tidak lagi `isSecure=true` tanpa probe kuat — ditandai risiko/belum terverifikasi penuh (selaras MINOR SQLCipher & DEFECT-03); root/cleartext/keystore tetap probe nyata.
  - `NodeTopologyScanner` + `DevNodeTopologyTab`: status node dilabeli **konseptual/konfigurasi** (bukan koneksi live terprobe), badge "AKTIF LOKAL"/"TIDAK AKTIF" dengan penjelasan jujur.
  - SSOT regenerated.

## Next steps (ordered, toward production)
1. **Monitor CI** commit ter-push; jaga `main` hijau.
2. **DevTools menyeluruh (lanjutkan):** audit & jujurkan sisa scanner/tab/engine (probe nyata atau UNKNOWN/heuristik):
   - `ProductionDefectScanner` DEFECT-01..03: tinjau keakuratan narasi vs kondisi aktual.
   - `DeviceAdvisorEngine`/`DevDeviceAdvisorTab` (sebagian probe nyata: RAM/baterai/suhu/optimasi — verifikasi), `DynamicApkInspector`, `DynamicDbIntrospector`, `RuntimeCapabilityProber` (+ `DevApkSkewCard`), `RealtimeMemoryProbe`/`DevMemoryTab`, `RealtimeNetworkProbe`/`DevServerTab`, `EarlySymptomDetector`/`RootCause*`/`DevRootCauseTab`, `DevSecurityAuditTab` teks konsisten, `DevCodeIntegrityTab`, `DevMaturityTab`/`DevProgressTab` konsistensi & scope-aware.
3. **[MINOR test]** coverage feasible login/PIN, SOS, kas (CI-runnable/Robolectric bila memungkinkan).
4. **[MINOR security]** evaluasi/rencana SQLCipher (keputusan/L2 owner).
5. **Static analysis bertahap:** rapikan/tegakkan ktlint atau tambah Detekt tanpa memecah CI.
6. **Release/OTA:** validasi Delta OTA (`v1.10` release not found) saat aset tersedia.

## Blocked / needs decision (L2 or external)
- **[MAJOR][multi-user]** backend + akun server. *No fake sync.*
- **[MAJOR][database]** schema Room `1,2,3,4,8,9.json` hilang — export/commit valid saja.
- **Release `v1.10`:** `release not found`.
- **Keputusan owner (L2):** SQLCipher/migrasi enkripsi DB; production signing secrets.

## Scope reminders (production)
- Finansial rilis awal = **iuran Kas saja**. **Marketplace/Arisan = DEFERRED**. **Asuransi & Intercom Helm = PENDING DEV**.
- Jangan klaim production-ready jika ada gap kritis/CI gagal/perilaku belum terverifikasi.

## How to resume (for next AI/model)
1. Read this file, then `docs/ABOUT.md` + `app/src/main/assets/about_manifest.json`, `README.md`, `ROADMAP.md`, `.github/about/features.json`.
2. Run: `git status && git log --oneline -10`, `python3 .github/scripts/check_file_length.py`, `python3 .github/scripts/audit_minus.py`, `gh run list -R ardyniech/DRG-APK --limit 5`.
3. Pick next safe batch from "Next steps", implement + verify + commit/push, update this file and SSOT, report facts.
4. Follow skills: `drg-apk-to-production`, `auto-continue-workflow`, `parallel-dev-workflow`, `disciplined-dev-workflow`, `safe-change-workflow`.
