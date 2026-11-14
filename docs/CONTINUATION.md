# DRG-APK Continuation / Handoff

> Durable handoff trail for model/agent switches. Keep this file current at the end of every batch and at every stop point. Do not rely on chat memory.

## Current state
- **Last updated (UTC):** 2026-11-14
- **Repo/branch:** `projects/DRG-APK` — `main`
- **HEAD (pushed):** `c9e5483` + batch "revert unfinished firestore profile experiment, restore DRGApplication.kt"
- **Working tree:** clean (no orphan files, DRGApplication.kt restored, audit trail updated).
- **Latest CI:** last known clean build `c9e5483`; `check_file_length.py` PASS; `audit_minus.py` → 0 BLOCKER, 2 MAJOR, 3 MINOR (same as baseline).

## Audit snapshot
- **Command:** `python3 .github/scripts/audit_minus.py`
- **Result:** **0 BLOCKER, 2 MAJOR, 3 MINOR** (total 5)
  - **MAJOR [multi-user]:** Seed lokal-only (butuh backend + akun server). *No fake sync.*
  - **MAJOR [database]:** Schema Room hilang `1,2,3,4,8,9.json` (export/commit valid saja; **jangan difabrikasi**).
  - **MINOR [security]:** SQLite belum terenkripsi/SQLCipher (risiko terdokumentasi di `README.md`).
  - **MINOR [code]:** Hardcode `Color(0x...)` di 1 file (palet tema `AppThemeModels.kt` — tabel definisi palet; komponen sudah bersih).
  - **MINOR [test]:** `androidTest` minim (alur kritis login/SOS/kas belum ter-cover instrumented).
- **Line cap:** PASS (`check_file_length.py`, semua <125 lines).

## DevTools metric truth (2026-11-14)
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

