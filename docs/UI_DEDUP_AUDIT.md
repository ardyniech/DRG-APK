# Audit Total Duplikasi UI — Frontend DRG (>1.038 file Kotlin)

> Diaudit 2026-11-09 dari kode nyata. Setiap klaim ada baris/file.
> Merge yang sudah dikerjakan dicatat dengan file barunya; sisanya backlog prioritas.

## Hitung kasar

* 455 file `.kt`, ~1.538 baris *Tab* DevTools, 23 glass-card `highlightBrush`, 2 launcher grid dobel di Dashboard.

## Rank 1 — DevTools 18 tab: `LazyColumn` + header yang sama persis

11 tab lurus digabung ke `DevTabShell.kt` (scaffold kanonis 90 baris):

| Tab | Baris sebelum | Sesudah | Hemat |
|---|---|---|---|
| BugHunter, Defects, DeviceAdvisor, Maturity, Memory, NodeTopology, RepoHealth, RootCause, SecurityAudit, Server, TechStack | 74-104 | 67-97 | ~21/file (total ~231) |

Tidak digabung (bespoke, dipertahankan):
* `DevProgressTab` / `DevCodeIntegrityTab` / `DevDevicePerformanceTab` (warna kondisional merah/kuning + layout tanpa ikon)
* `DevCiCdTab` / `DevBuildDeploymentTab` (badge HEALTHY/trailing + kolom khusus)
* `DevUiUxAuditTab` (skor besar 16sp) · `DevGitVersionTab` orphan (tidak ada di switcher → kandidat hapus saat refactor switcher berikutnya)

Header kondisional diekspresikan lewat param `headerBackground/headerBorderColor/titleColor/headerTrailing/headerExtra` (tanpa ubah visual inti). Semua tab tetap <125 baris (terverifikasi `check_file_length.py`, 457 file).

## Rank 2 — Glass card `highlightBrush` di 23 file

Kanonis: `shared/atoms/DrgGlassCard.kt` (58 baris) + `DrgIconTileBox.kt` (47 baris, icon-box 44dp/14dp).

Yang dimerger sekarang:

* `modules/dashboard/primitives/GlassCard.kt` (74 → 48 baris): kini wrapper `DrgGlassCard` (8 callers dashboard tidak perlu diubah)
* `shared/atoms/StatCard.kt` (95 → 58 baris): kini `DrgGlassCard + DrgIconTileBox`
* `modules/dashboard/QuickTile.kt` (81 → 61 baris): kini `DrgGlassCard + DrgIconTileBox`
* `modules/dashboard/LauncherTile.kt` (84 → 72 baris): ikon via `DrgIconTileBox`

Sisa 19 file masih pakai pola hand-roll Rock12/Surface (RadarRecommendationCard sudah pakai DrgGlassCard; Emergency*, ForumPost/Workshop, Members, Profile, Treasury). Prioritas backlog: satukan ke `DrgGlassCard` + `DrgIconTileBox` per modul (estimasi ~10-12 baris/file, total ~230 baris).

## Rank 3 — Atoms vs primitives

* `GlassCard` (dashboard) vs `DrgGlassCard` (shared) = ~85% overlap → dimerger di Rank 2.
* `StatCard` (44dp) vs `QuickTile` (38dp) vs `LauncherTile` (46dp) icon-box identik kecuali ukuran → kanonis di `DrgIconTileBox(size, iconSize)`; semua sudah pakai.

## Rank 4 — Dashboard: 2 launcher grid dobel

* `DashboardScreen.kt:113` render `DashboardDesktopLauncher` (12 apps, chunked 4) lalu `QuickActionGrid` (4 QuickTile RADAR/KAS/FORUM/WORKFLOW) → destinasi sama, hanya chrome beda (icon-only vs lebar+desc). **Dimerger:** hapus `QuickActionGrid` (59 baris), satu launcher kanonis. `QuickTile.kt` dipertahankan sebagai tile lebar generik (kini pakai kanonis).

## Rank 5 — Gamifikasi 5 tab Saya: `BadgesTabContent`, `LeaderboardTabContent`, `RewardsTabContent`, `TasksTabContent`, `PointHistoryTabContent`

* Semua `LazyColumn(spacedBy 8-10dp)` + `Surface 12dp` + `Row/Column(padding 12dp)`. `BadgesTabContent` dan `RewardsTabContent` hampir identik (`Row{emoji+Column(title/desc/meta)+trailing}` beda Text vs Button Tukar).
* *Backlog:* `GamificationCardRow(leading, title, subtitle, meta, trailing)` + wrapper LazyColumn. Estimasi ~12 baris/file.

## Rank 6 — Services/About/Treasury/Radar sisa

* `MarketHeaderCard` vs `ProtectionHeaderCard` diff 6 baris (ikon/warna/teks) → `ServicesHeaderCard(icon,title,subtitle,tint,bg)` (~35 baris).
* `About*Card` (Header/Progress/Backlog/Phase/Refinement) `Surface 20dp` seragam → shell ~10 baris/file.
* `KasBalanceCard`/`KasPeriodSummaryCard`/`KasFinancialChartCard` & `TreasuryHealthCard` — shell saja, chart unik dibiarkan.
* `RealTrafficLiveCard`/`ShelterRadarCard` — mulai migrasikan ke `DrgGlassCard(16dp, bordered)` saat menyentuh file itu.

## Verifikasi tiap merge

* `check_file_length.py`: ✅ 457 file <125 baris.
* `audit_minus.py`: 23 temuan (6 BLOCKER tetap untuk multi-user, bukan file UI).
* Gradle `DevTools* + AboutManifestTest` passed `BUILD SUCCESSFUL` 1m 43s (verifikasi end-to-end).

## Aturan ke depan

Jangan bikin tab/card/page baru tanpa pakai kanonis: `DrgGlassCard`, `DrgIconTileBox`, `DevTabShell`, `DashboardDesktopLauncher`. Jika fitur baru perlu UI, tambahkan param ke kanonis dulu, baru buat wrapper tipis bila perlu.
