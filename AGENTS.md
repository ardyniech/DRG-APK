# DRG Driver — AI Coding Agent Architecture & Operational SOP (v3.0)

> **MANDATE:** Dilarang keras "vibe coding" (perbaikan asal jalan, file monolitik, state mutation tanpa aturan, hardcoded value, atau status sukses palsu).
> **Core Philosophy:** **"Simplicity is King"** & **"Build One and Forget"** — Isolasi total, performa kelas atas, zero future regressions, batas modul sangat ketat.
> **Project:** DRG Driver (Driver Riang Gembira) — Enterprise Android Application
> **Architecture:** Clean Architecture + MVI/UDF + Jetpack Compose Material 3
> **Database:** Room v12 (23 entities, schema files in `app/schemas/`)
> **Language:** Kotlin 2.2.10, Jetpack Compose, Coroutines, Flow

---

## 0. AGENT PERSONA & PROACTIVE OPERATIONAL PROTOCOL

1. **Elite Technical Architect Persona:** Bertindak sebagai Principal Engineer. Selalu proaktif merencanakan peningkatan fungsional dan estetika tanpa menunggu disuapi oleh user.
2. **Adversarial Audit & Skepticism (Zero-Tolerance):** Haram menyatakan kode "selesai/normal" tanpa bukti pengujian. Setiap penyelesaian tugas WAJIB disertai audit 3 skenario negatif:
   - *Bad Input / Malformed Data:* Skenario data null, kosong, atau tipe salah.
   - *Cross-Module Failure:* Skenario event bus/dispatcher gagal.
   - *UI Dead-End:* Skenario user terjebak di infinite loading / tombol mati saat koneksi terputus.
3. **Execution Transparency:** Di setiap akhir respon, laporkan secara rinci:
   - Daftar file yang diubah/dibuat beserta jumlah barisnya.
   - Item pekerjaan yang belum selesai (Pending Todo Backlog).

---

## 1. ABSOLUTE CODE CONSTRAINTS & ISOLATION

*   **File Length Cap (Batas Baris Kode):**
    - **Default Cap:** Maksimal **125 baris per file** untuk seluruh UI components, logic, controllers, dan storage adapters.
    - **Escape Hatch (Maksimal 200 baris):** HANYA diizinkan untuk:
      1. Custom Canvas Painters & AGSL/Fragment Shaders.
      2. Immutable Data Models & Code-generated Serializers.
      3. Master Centralized Router / App Config Entrypoints.
    - **Trigger Action:** Jika file standar menyentuh baris 126, AI **WAJIB STOP** dan merestrukturisasi kode ke sub-komponen, custom hooks, atau utility helper sebelum melanjutkan.
*   **No Magic Numbers / Inline Styling:** Dilarang hardcode hex color, ukuran pixel mentah, atau durasi animasi. Wajib mengonsumsi `DesignTokens` atau `Theme` terpusat.
*   **Strict Unidirectional Data Flow (UDF):** UI Layer hanya memancarkan event dan mengonsumsi immutable state. Business logic harus terpisah total dari UI.
*   **Zero Cross-Module Imports:** Modul dilarang mengimpor internal file milik modul lain. Komunikasi antarmodul HANYA melalui `core/dispatcher` (Event Bus) atau `shared/models/` untuk data read-only.
*   **Single Responsibility:** Tepat satu tanggung jawab per file.

---

## 2. ADAPTIVE LAYOUT & EDGE-TO-EDGE FRAMEWORK

*   **Edge-to-Edge Execution:** Konten wajib digambar di bawah status bar dan navigation bar secara seamless menggunakan `WindowInsetsCompat` / native `EdgeToEdge` APIs.
*   **Dynamic Inset Handling:** Adjust margin/padding secara otomatis terhadap `WindowInsets.ime` (keyboard) dan `WindowInsets.navigationBars` agar tidak tumpang tindih.
*   **Multi-Device Adaptability:** Gunakan breakpoint `WindowSizeClass`. Layout harus responsif dari HP layar kecil, layar lipat (foldable/multi-window aware), hingga tablet.
*   **Touch Target & Padding:** Target sentuh minimal 48x48dp (Android) / 44x44pt (iOS) dengan hit-slop tambahan. Padding internal rapat 8–12dp/pt/px (sesuai unit platform).

---

## 3. HIGH-TECH VISUALS & GESTURE MOTION ENGINE

*   **High-End Visual Polish:** Manfaatkan custom canvas renderers dan AGSL/Fragment shaders untuk efek glow, glassmorphism, depth elevations, dan blurred micro-surfaces.
*   **Physics-Based Motion:** Semua animasi UI dan transisi wajib menggunakan spring dynamics (stiffness, damping) pada tingkat 60fps/120fps. Avoid rigid linear animations.
*   **Seamless Navigation:** Integrasikan gesture navigation bawaan Android (Predictive Back Gesture) dan `SharedElementTransitions` antar-halaman.
*   **Rich Haptics Engine:** Injeksi mikro-getaran (haptic feedback) pada setiap perubahan state penting dan interaksi tombol.

---

## 4. SERVERLESS ECOSYSTEM & LOCAL-FIRST RECONCILIATION

*   **Local-First Architecture:** Mutasi state di lokal terlebih dahulu (Room / Isar / SQLite) dengan optimistic UI updates, lalu sinkronisasi ke server di background.
*   **Conflict-Free Sync (CRDTs):** Terapkan algoritma CRDTs atau event-driven WebSockets untuk rekonsiliasi data multi-user tanpa database locking.
*   **Non-Blocking UI Thread:** Semua operasi I/O, database query, dan komputasi berat WAJIB berjalan di background thread (Isolates/Coroutines/Workers).
*   **Zero-Allocation Render Loops:** Minimalisir instansiasi objek di dalam loop animasi/render pass untuk mencegah Garbage Collection (GC) frame drop.
*   **Low RAM & Battery:** Aplikasi HARUS dioptimalkan untuk konsumsi RAM rendah dan baterai efisien. Setiap fitur harus mempertimbangkan dampak performa.

---

## 5. HUB-AND-SPOKE MASTER CONTROL & SECURITY ENGINE

*   **Remote Configuration & Feature Toggles:** Fitur, paywall monetisasi (RevenueCat/Custom), dan perilaku aplikasi dikendalikan penuh dari Master Hub serverless via Edge Functions tanpa update APK.
*   **Serverless RBAC & Zero-Trust:** Manajemen hak akses (Role-Based Access Control) berbasis ephemeral tokens yang diverifikasi di Edge Server.
*   **Hardware Enclave & Secrets:** DILARANG hardcode API key, secret, atau IV statis. Gunakan Android Keystore / iOS Keychain. IV/nonce enkripsi simetris wajib acak per operasi.
*   **App Integrity & Anti-Reverse Engineering:** Terapkan Play Integrity API check, R8/ProGuard aggressive code obfuscation, dan stripping simbol sensitif.

---

## 6. CANONICAL DIRECTORY STRUCTURE

```
project_root/
├── core/
│   ├── main.[ext]                 # Entry point (< 125 lines)
│   ├── loader.[ext]               # Module registry (1 line per module)
│   ├── dispatcher.[ext]           # Event bus
│   ├── integration_points.[ext]   # Slot resmi integrasi UI (Dashboard/Nav/Settings)
│   └── config.[ext]
├── shared/
│   ├── atoms/                     # Atomic Design System (base UI components)
│   ├── models/                    # Domain model SHARED lintas modul
│   └── utils/
├── modules/
│   └── [module_name]/
│       ├── primitives/            # UI Micro-components
│       ├── logic/                 # Pure functions & state handlers
│       ├── storage/               # DB/API Adapters (menggunakan shared/models)
│       ├── tests/                 # Unit tests untuk modul ini
│       └── index.[ext]            # PUBLIC API GATEWAY TUNGGAL untuk modul
├── tests/
└── docs/modules/                  # Concise README per modul
```

### Module Structure Rules
- Setiap module di `modules/` HARUS punya `primitives/`, `logic/`, `storage/`, `tests/`, dan `index.[ext]`.
- Module files: **< 125 lines**. Screen files: **< 125 lines**. ViewModel: **< 125 lines**.
- **NEVER** put business logic di UI files. Logic ke `core/repository/` atau `core/viewmodel/`.
- **NEVER** create module baru tanpa update `docs/modules/README.md`.
- **NEVER** cross-module import. Komunikasi via `core/dispatcher` atau `shared/models/`.

---

## 7. ERROR HANDLING & ANTI-FABRIKASI

*   **Log Format:** Wajib: `[Module:<Name>] Error in <function>: <message>`
*   **User-Facing Error:** Dilarang menampilkan raw stack trace/error 500 ke pengguna. Terjemahkan ke bahasa awam di layer `primitives/` (Jelaskan: *Apa yang terjadi* + *Langkah solusi user*).
*   **Kejujuran Fungsional (Anti-Fabrikasi):** DILARANG keras memalsukan status sukses. Wajib bedakan state:
    1. **Sukses Nyata** (terkirim ke server tujuan).
    2. **Fallback Lokal** (terpanjar di perangkat, menunggu koneksi).
    3. **Gagal** (disertai alasan transparan).
*   **Status sukses palsu = HARD VIOLATION.** Contoh: fitur backup mengembalikan "Sukses" padahal data belum terkirim ke cloud.

---

## 8. PHASED EXECUTION PROTOCOL

Saat menerima tugas pengkodean, AI WAJIB mengeksekusi secara berurutan:

1.  **Phase 1 — Schema & Interfaces:** Deklarasikan immutable models di `shared/models/` dan kontrak gateway di `index.[ext]`.
2.  **Phase 2 — Structural Framing:** Bangun layout skeleton Edge-to-Edge dan dynamic inset handling tanpa styling berlebihan.
3.  **Phase 3 — UI & Shader Injection:** Injeksi design tokens, animasi fisika, AGSL shaders, dan rich haptics.
4.  **Phase 4 — Testing & Adversarial Audit:** Tulis unit test (1 happy path + 1 error path) dan jalankan 3 skenario audit negatif sebelum melaporkan hasil.

---

## 9. UX/UI PHILOSOPHY — ZERO COGNITIVE OVERLOAD

### Cognitive Overload Elimination
- **Masalah:** Pengguna baru langsung churn (meninggalkan aplikasi) dalam 5 menit pertama karena disuguhi puluhan menu dan tombol yang belum dipahami.
- **Solusi:** **"Just-in-Time Onboarding"** — fitur baru hanya diperkenalkan (revealed) tepat pada saat pengguna benar-benar membutuhkannya atau sudah siap menggunakannya.

### Prinsip Desain
1. **Zero Learning Curve di Awal:** Layar pertama sangat intuitif (biasanya hanya satu CTA utama).
2. **Gamification Organik:** Sensasi "naik level" atau progres dirasakan pengguna. Semakin sering dipakai, aplikasi terasa semakin kaya — bukan dari awal sudah sok tahu.
3. **Progressive Disclosure (UI/UX) + Event/State-Driven Feature Activation (Business Logic):** Conditional Rendering, Empty State Management, dan Feature Flagging based on Data Volume.
4. **Maintenance & State Management yang Rapi:** Lazy loading komponen atau modular plugin — modul berat tidak perlu di-render sebelum datanya ada.

### AESTHETICS — HARD RULES
- **HARAM DARK THEME.** Tapi juga bukan white monolitik. **BE CREATIVE** — gunakan brand tokens (`DrgGreenPrimary`, `DrgBackground`, `DrgSurface`, `DrgRedPanic`, `DrgGoldReward`) untuk tampilan yang segar, tinggi kontras, dan ramah pengemudi siang hari.
- **Consistency:** Desain UI HARUS konsisten di semua page. Tidak boleh ada desain yang berbeda-beda per halaman.
- **Pikiran estetika UI/UX JANGAN kerasa seperti vibe coding.** Setiap elemen harus disengaja dan bermakna.
- **Proaktif memikirkan sampai hal sekecil apapun** utk menghadirkan pengalaman kenyamanan bagi user — ini sangat penting dan fatal jika terlewat.
- **Ini lebih efisien utk menghargai waktu user** — build sebagai production-grade, bukan prototype.

### Proactive Building
- **Proaktif membangun beberapa fitur utk mendukung fitur yang lain** agar kerasa lebih solid, tidak berkesan vibe coding dan developer yang malas.
- **Buat app sebagai tools agar memudahkan user itu intinya.**

---

## 10. FOCUS RULES (When to Do What)

*   **Jika hasil audit dilaporkan** → **FOCUS UTK PERBAIKAN**. JANGAN menambah fitur/modul baru.
*   **Jika mengerjakan fitur/modul baru** → **KERJAKAN SAMPAI TUNTAS**, dari A sampai Z. JANGAN scaffold atau gimmick.
*   **Laporkan yg belum dikerjakan**, atau ada ruang improvement — **prioritas go deep** fitur yang sudah ada di setiap respon.
*   **Production-grade, bukan prototype.** Selalu thinking improvement performa dan coding quality sebelum touch code.

---

## 11. PERFORMANCE & LOW-RAM OPTIMIZATION

*   **Apps must low RAM consumption and battery efficient** (Android).
*   **Always think what better performance and coding quality for production grade** before touching the code.
*   **Optimalkan:**
    - Map tile caching LRU minimalisir data & baterai.
    - Background sync hemat baterai (`BatteryAwareSyncScheduler`, `BatteryOptimizationManager`).
    - Image loading via Coil dengan placeholder & caching.
    - Avoid unnecessary recomposition di Compose (`remember`, `derivedStateOf`, `stable` classes).
    - Minimalisir alokasi objek di loop render (`Zero-Allocation Render Loops`).

---

## 12. TESTING & ADVERSARIAL AUDIT

### Test Requirements
- **Every new feature MUST have a unit test** in `app/src/test/java/com/example/`.
- **Test naming**: `FeatureNameTest.kt`.
- **Test structure**: Use `@RunWith(RobolectricTestRunner::class)`, `@Config(sdk = [34])`.
- **Adversarial testing**: Setiap fitur wajib audit 3 skenario negatif:
    1. *Bad Input / Malformed Data* (null, empty, wrong type).
    2. *Cross-Module Failure* (event bus/dispatcher gagal).
    3. *UI Dead-End* (infinite loading / tombol mati saat offline).

### Current Test Suite (27 suites, 1879 lines)
```
AdversarialAuditScenariosTest, AudioRouteManagerTest, AuthAndProfileSecurityTest,
CacheAndBatteryTest, CrashDetectionTest, EmergencySmsFallbackTest, EmergencyTrcDispatchTest,
ExampleRobolectricTest, ExampleUnitTest, ForumPersistenceTest, FreeMapAndTrafficTest,
GamificationAndLoyaltyTest, GreetingScreenshotTest, KasTransparanComponentTest,
LiveDriverMapTest, MapTileCacheAndStateTest, MemberRolePermissionDatabaseTest,
PoskoCheckInRepositoryTest, PoskoProximityAndWatermarkTest, ProgressiveOnboardingLogicTest,
RoleManagementAndSecurityTest, RolePermissionRepositoryTest, SosEmergencyScreenTest,
SosFabAndModalTest, SosHapticTactileTest, TreasuryAuditComputationTest, ViewModelRefactorAuditTest
```

### Test Commands
```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:testDebugUnitTest --tests "com.example.SosEmergencyScreenTest"
python3 .github/scripts/check_file_length.py
./gradlew :app:lintDebug
./gradlew assembleDebug
```

---

## 13. SECURITY PROTOCOLS

### Data Security
- **Network Security Config** (`app/src/main/res/xml/network_security_config.xml`): TLS 1.3 enforced, cleartext disabled, domain pinning.
- **Secrets**: API keys via `.env` + Secrets Gradle Plugin. **NEVER** commit `.env`, `secrets.properties`, atau `google-services.json`.
- **ProGuard/R8**: Obfuscation enabled in release. Rules in `app/proguard-rules.pro`.
- **Database**: `allowBackup = false`, internal SQLite, data isolated.

### RBAC Security
- **RoleManager** (`core/RoleManager.kt`) validates ALL role changes.
- **Only Ketua** can appoint/demote Ketua.
- **Only Pengurus** (Wakil, Sekretaris, Bendahara, Satgas) can manage posko dan view treasury.
- **All role changes** MUST logged via `RoleAuditLogEntity` (immutable audit trail).
- **NEVER** bypass `RoleManager.validateRoleChange()`.

### Emergency Security
- **SOS data** (GPS coordinates, emergency alerts) MUST be transmitted securely.
- **Emergency SMS fallback** (`EmergencySmsFallbackHelper`) MUST have valid phone numbers.
- **NEVER** log SOS coordinates to Logcat atau debug outputs.

---

## 14. CI/CD PIPELINE ENFORCEMENT

- **GitHub Actions** (`.github/workflows/android_ci.yml`) enforces:
    1. Architecture quality check (`check_file_length.py`) — **hard fail** jika >125 lines.
    2. Android Lint (`./gradlew :app:lintDebug`) — warnings captured.
    3. Robolectric tests (`./gradlew :app:testDebugUnitTest`) — **hard fail** jika tests gagal.
    4. Debug APK assembly.
- **NEVER** push code tanpa verifikasi lokal pertama. CI akan reject non-compliant code.
- **Concurrency**: Cancel in-progress runs pada new push.
- **Timeout**: 25 menit per run.

---

## 15. PROHIBITED PATTERNS

| Pattern | Why | Alternative |
|---|---|---|
| `var` untuk state tanpa `MutableStateFlow` | Breaks MVI/unidirectional flow | `MutableStateFlow` + `StateFlow` |
| Hardcoded colors (`Color.Red`, dll) | Violates design system | `DrgRedPanic`, `DrgGreenPrimary`, dll |
| Hardcoded strings | Breaks localization | `stringResource(R.string.xxx)` |
| Hardcode hex color / pixel / duration | Violates DesignTokens | Theme / DesignTokens |
| `GlobalScope.launch` | Uncontrolled coroutine lifecycle | `viewModelScope` / `CoroutineScope(Dispatchers.IO)` |
| `fallbackToDestructiveMigration()` di production | **DESTROYS user data** | Explicit `Migration(from, to)` |
| `Log.d` / `Log.e` untuk data sensitif | Security leak | Hanya debug, bukan production |
| Files > 125 baris | Breaks modularity | Split ke `primitives/` |
| `TODO()` / `FIXME()` comments | Incomplete code | Implement sebelum commit |
| `Thread.sleep()` / blocking I/O di main thread | ANR crash | `Dispatchers.IO` + Coroutines |
| Direct database access dari UI | Breaks separation of concerns | Repository + ViewModel |
| `Activity` context di ViewModel | Memory leak | `Application` context via `DRGApplication` |
| Cross-module imports | Breaks isolation | `core/dispatcher` atau `shared/models/` |
| Dark theme | **HARD RULE — HARAM** | Light theme dengan brand tokens |
| Status sukses palsu | Anti-fabrikasi | Bedakan: Sukses Nyata / Fallback / Gagal |
| Scaffold / gimmick fitur | Tidak production-grade | Kerjakan sampai tuntas A-z |
| Vibe coding | Tidak intentional | Setiap line disengaja, diuji, didokumentasikan |

---

## 16. REQUIRED PATTERNS

### MVI/UDF Pattern
```kotlin
data class ScreenState(val isLoading: Boolean = false, val data: List<Member> = emptyList(), val error: String? = null)
sealed interface ScreenEvent { data class LoadData(val memberId: String) : ScreenEvent; object RefreshData : ScreenEvent }
class ScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(ScreenState())
    val state: StateFlow<ScreenState> = _state.asStateFlow()
    fun onEvent(event: ScreenEvent) { ... }
}
```

### Repository Pattern
```kotlin
class MyRepository(private val db: AppDatabase) {
    val allItems: Flow<List<MyEntity>> = db.myDao().getAll()
    suspend fun insert(item: MyEntity) = withContext(Dispatchers.IO) { db.myDao().insert(item) }
}
```

### DAO Pattern
```kotlin
@Dao
interface MyDao {
    @Query("SELECT * FROM my_table ORDER BY timestamp DESC")
    fun getAll(): Flow<List<MyEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MyEntity)
}
```

### Entity Pattern
```kotlin
@Entity(tableName = "my_table", indices = [Index(value = ["foreignId"])])
data class MyEntity(
    @PrimaryKey val id: String, val foreignId: String, val name: String,
    val timestamp: Long = System.currentTimeMillis()
)
```

### Test Pattern
```kotlin
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MyFeatureTest {
    private lateinit var db: AppDatabase
    private lateinit var repository: MyRepository
    @Before fun setup() { db = Room.inMemoryDatabaseBuilder(...).build(); repository = MyRepository(db) }
    @After fun teardown() { db.close() }
    @Test fun `test happy path`() = runTest { ... }
}
```

---

## 17. DOCUMENTATION STANDARDS

- **Public functions**: KDoc dengan `@param`, `@return`, `@throws`.
- **Private functions**: Single-line comment menjelaskan intent.
- **Complex logic**: Block comment menjelaskan WHY, bukan WHAT.
- **Commit format**: `type(module): description` — e.g., `feat(emergency): add SOS haptic feedback`.
- **NEVER** commit dengan pesan vague seperti `update`, `fix bug`, `try`.
- **New feature docs** → `docs/modules/MODULE_NAME.md`.
- **Database change** → update `docs/DATABASE_MIGRATIONS.md`.
- **Release-worthy change** → update `CHANGELOG.md`.
- **Major feature added** → update `README.md` badges dan feature lists.

---

## 18. WORKFLOW — AI AGENT PROCEDURE

### Before Starting Work
1. **Read** `AGENTS.md`, `CONTRIBUTING.md`, `docs/DATABASE_MIGRATIONS.md`.
2. **Check** `.github/scripts/check_file_length.py` untuk understand 125-line rule.
3. **Run** `python3 .github/scripts/check_file_length.py` untuk lihat current violations.
4. **Run** `./gradlew :app:testDebugUnitTest` untuk verify baseline tests pass.
5. **Analyze** fitur yang sudah ada dan identifikasi ruang improvement.

### During Development
1. **Phase 1**: Deklarasikan models di `shared/models/` dan kontrak di `index.[ext]`.
2. **Phase 2**: Bangun layout skeleton Edge-to-Edge + dynamic insets.
3. **Phase 3**: Injeksi design tokens, animasi fisika, shaders, haptics.
4. **Phase 4**: Tulis unit test + adversarial audit (3 skenario negatif).
5. **Verify** file line counts < 125 baris.
6. **Run** `python3 .github/scripts/check_file_length.py`.
7. **Run** `./gradlew :app:testDebugUnitTest`.
8. **Update** `docs/DATABASE_MIGRATIONS.md` jika database berubah.
9. **Update** `CHANGELOG.md` jika feature-worthy.

### Before Committing
1. **Run** `./gradlew :app:testDebugUnitTest`.
2. **Run** `python3 .github/scripts/check_file_length.py`.
3. **Run** `./gradlew :app:lintDebug`.
4. **Verify** no `TODO()`, `FIXME()`, `Log.d` sensitif.
5. **Verify** no hardcoded colors/strings.
6. **Verify** no cross-module imports.
7. **Commit** dengan descriptive message.

### After Pushing
1. **Verify** CI pipeline passes di GitHub Actions.
2. **Check** test reports & lint reports uploaded.
3. **Address** CI failures segera.
4. **Laporkan** item yang belum selesai (Pending Todo Backlog).

---

## 19. MODULE-SPECIFIC RULES

### Emergency Module
- SOS button **MUST** gunakan `DrgRedPanic (#DC2626)`.
- SOS countdown **MUST** punya haptic (`SosHapticManager`) dan suara (`SosAlarmSoundManager`).
- Emergency data **MUST NOT** di-log atau diekspos di debug.
- Semua SOS tests **MUST** cover offline fallback (`EmergencySmsFallbackHelper`).

### Database Module
- **AppDatabase v12** — 23 entities. Schema version tracked.
- **Migrations must be explicit** — never `fallbackToDestructiveMigration()` di production.
- Every entity **MUST** punya `@Index` untuk query columns.
- Every DAO **MUST** return `Flow` untuk reactive updates.
- **Tambahkan `MIGRATION_6_7` sampai `MIGRATION_11_12`** — ini critical, data user akan hilang tanpa explicit migration.

### Repository Module
- Repositories **MUST** return `Flow` untuk reactive data.
- Suspend functions **MUST** pakai `withContext(Dispatchers.IO)`.
- **NEVER** expose `MutableStateFlow` secara eksternal. Gunakan `StateFlow`.
- Repositories **MUST** handle `Result<T>` untuk error handling.

### UI Module
- **Light theme ONLY**. Dark theme **HARAM**.
- Semua warna dari `res/values/colors.xml` brand tokens.
- Semua strings dari `res/values/strings.xml`.
- Touch targets minimum 48x48dp.
- Dynamic insets handling mandatory.
- **Consistency desain** di semua page — HARUS seragam.

### Profile/KTA Module
- **KTA Digital** MUST punya QR Code verification.
- **Holographic watermark** (`KtaHolographicWatermark`) MUST anti-forgery.
- **Profile data** MUST encrypted at rest.

### Admin Module
- Semua admin actions **MUST** di-log via `RoleAuditLogEntity`.
- Role changes **MUST** lewat `RoleManager.validateRoleChange()`.
- Admin dashboard **MUST** tunjukkan `RoleAuditTrailCard`.

---

## 20. TOOLING

### Required Tools
- **Android Studio** Hedgehog / Iguana / Jellyfish / Koala (2024.x+)
- **JDK 17** atau **JDK 21** (Temurin/Corretto)
- **Gradle 8.11+** (via `./gradlew`)
- **Gradle Wrapper**: `./gradlew` (committed di repo)
- **Python 3** (untuk `check_file_length.py`)

### Build Commands
```bash
./gradlew :app:testDebugUnitTest          # All unit tests
./gradlew :app:lintDebug                  # Lint check
./gradlew assembleDebug                   # Build debug APK
python3 .github/scripts/check_file_length.py  # Architecture audit
```

### CI Pipeline
- **GitHub Actions**: `.github/workflows/android_ci.yml`
- **Steps**: Checkout → JDK 17 → Gradle Setup → Architecture Audit → Lint → Tests → APK Assembly → Upload Artifacts
- **Timeout**: 25 menit per run
- **Concurrency**: Cancel in-progress runs pada new push

---

## 21. REFERENCE FILES

| File | Purpose |
|---|---|
| `SETUP.md` | Developer setup guide |
| `CONTRIBUTING.md` | Contribution workflow |
| `CODE_OF_CONDUCT.md` | Community standards |
| `SECURITY.md` | Security policy & vulnerability reporting |
| `CHANGELOG.md` | Release history |
| `ROADMAP.md` | Project roadmap (Phases 1-3) |
| `docs/DATABASE_MIGRATIONS.md` | Room database migration history v1-v12 |
| `docs/modules/README.md` | Module documentation index |
| `.github/scripts/check_file_length.py` | Architecture quality enforcer |
| `.github/workflows/android_ci.yml` | CI/CD pipeline definition |
| `app/proguard-rules.pro` | R8/ProGuard rules |
| `app/src/main/res/xml/network_security_config.xml` | Network security config |
| `app/google-services.json.example` | Firebase configuration template |

---

## 22. FOCUS AREAS — WHAT NEEDS IMPROVEMENT

Prioritaskan **go deep** pada fitur yang sudah ada:

### 🔴 Critical (Data Loss Risk)
- [ ] Tambahkan explicit migrations `MIGRATION_6_7` s/d `MIGRATION_11_12` di `AppDatabase.kt`
- [ ] Generate dan commit `app/schemas/.../12.json` schema file
- [ ] Verifikasi `google-services.json` — Firebase features belum aktif

### 🟠 High (Production Readiness)
- [ ] Fix `values-night/themes.xml` parent theme → `Material.Dark.NoActionBar` (atau hapus dark theme)
- [ ] Setup release signing di CI pipeline
- [ ] Tambah `lintOptions { abortOnError true }` di `build.gradle.kts`
- [ ] Verify AI Assistant (`AiAssistant*`) — API integration mungkin placeholder
- [ ] Update `SETUP.md` — test count masih 18, sekarang 27

### 🟡 Medium (UX & Polish)
- [ ] Tambah theme toggle UI di settings (meskipun dark theme HARM, user perlu opsi)
- [ ] Tambah instrumented UI tests (`androidTest/`)
- [ ] Add detekt static analysis ke CI
- [ ] Deploy documentation site (Docusaurus/GitHub Pages)
- [ ] Standardize Gradle version (local `gradle-wrapper.properties` vs CI `8.11.1`)
- [ ] Update `metadata.json` — artifacts dari AI Studio generation, perlu update/hapus

### 🟢 Low (Nice to Have)
- [ ] Tambah auto-changelog tool
- [ ] Verifikasi `FUNDING.yml` links (Saweria/Kitabisa) active
- [ ] Tambah conventional commit auto-changelog
- [ ] Dark theme colors test di berbagai device/AMOLED (jika suatu hari diaktifkan)

---

> **⚠️ FINAL DIRECTIVE**: Dokumen ini adalah MANDATORY. Setiap AI agent atau developer yang melanggar aturan akan memiliki PR ditolak oleh CI. `check_file_length.py` akan **hard-fail** jika ada file >125 baris. CI pipeline akan **reject** jika tests gagal. **Baca dokumen ini sebelum menulis kode apapun.**
>
> **Pikiran terakhir**: Apps must low RAM consumption and battery efficient. Always think production-grade, bukan prototype. Proaktif sampai hal sekecil apapun utk menghadirkan kenyamanan user — ini sangat penting dan fatal jika terlewat. Konsistensi desain UI di semua page. Haram dark theme, tapi juga bukan white monolitik — BE CREATIVE.
