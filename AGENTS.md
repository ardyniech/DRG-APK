# DRG Driver — AI Coding Agent Architecture & Operational SOP

> **Status:** MANDATORY — All AI agents and developers MUST follow this document before making any code changes.
> **Project:** DRG Driver (Driver Riang Gembira) — Enterprise Android Application
> **Architecture:** Clean Architecture + MVI/UDF + Jetpack Compose Material 3
> **Database:** Room v12 (23 entities, schema files in `app/schemas/`)
> **Language:** Kotlin 2.2.10, Jetpack Compose, Coroutines, Flow

---

## ⚡ SYSTEM DIRECTIVE — NON-NEGOTIABLE RULES

### Rule 1: Code Structure & Modularity
- **Maximum 125 lines per file** for all UI, ViewModel, and adapter files. Hard enforcement via `.github/scripts/check_file_length.py` in CI.
- Escape hatch (200 lines max): `shared/models/`, `core/database/`, `modules/radar/primitives/RadarCanvasPainter.kt`.
- **NEVER** create a file exceeding 125 lines. If a file is over limit, split it into micro-primitives under the module's `primitives/` folder.
- **NEVER** use `vibe coding` — every line must be intentional, tested, and documented.
- **NEVER** hardcode colors, strings, or values. Use `res/values/colors.xml`, `res/values/strings.xml`, or `Drg*` brand tokens.

### Rule 2: Design System (Material 3)
- **Light Theme ONLY** (`#F8FAF8` background, `#FFFFFF` surface, `#00B14F` primary). **Dark theme is disabled in production** — `values-night/` exists but is not the target experience.
- Brand tokens: `DrgGreenPrimary (#00B14F)`, `DrgBackground (#F8FAF8)`, `DrgSurface (#FFFFFF)`, `DrgRedPanic (#DC2626)`, `DrgGoldReward (#F59E0B)`.
- **48x48dp minimum** touch target on all interactive elements.
- **Dynamic window insets** — always use `WindowInsets.navigationBars` and `WindowInsets.ime`.
- **NEVER** add dark theme support or `values-night` modifications unless explicitly authorized.

### Rule 3: Data Layer — Room Database
- **All data mutations** must go through Room Database with reactive `Flow`.
- **Non-blocking I/O** via `Dispatchers.IO` with Kotlin Coroutines.
- **AppDatabase v12** with 23 entities. Schema files: `app/schemas/com.example.core.database.AppDatabase/`.
- **NEVER** modify `AppDatabase.kt` entities without:
  1. Adding a new schema version JSON file.
  2. Adding explicit `Migration(from, to)` object in `AppDatabase.companion`.
  3. Updating `docs/DATABASE_MIGRATIONS.md`.
  4. Adding unit tests for the migration.
- **Explicit migrations required for v6→v12**: `MIGRATION_6_7`, `MIGRATION_7_8`, `MIGRATION_8_9`, `MIGRATION_9_10`, `MIGRATION_10_11`, `MIGRATION_11_12`. `fallbackToDestructiveMigration()` is ONLY for development — **NEVER use in production code**.

### Rule 4: Testing & Quality Assurance
- **Every new feature MUST have a unit test** in `app/src/test/java/com/example/`.
- **Test naming convention**: `FeatureNameTest.kt` (e.g., `SosEmergencyScreenTest.kt`, `RolePermissionRepositoryTest.kt`).
- **Minimum test coverage**: Each new public function must have at least one test case.
- **Run before every commit**: `./gradlew :app:testDebugUnitTest` AND `python3 .github/scripts/check_file_length.py`.
- **Test types**: Robolectric (JVM), Roborazzi (visual regression). No Espresso UI tests currently — but add them for new features.
- **Adversarial testing**: Every feature must handle bad input, null values, and offline states. See `AdversarialAuditScenariosTest.kt` for examples.

### Rule 5: CI/CD Pipeline Enforcement
- **GitHub Actions pipeline** (`.github/workflows/android_ci.yml`) enforces:
  1. Architecture quality check (`check_file_length.py`) — **hard fail** if >125 lines.
  2. Android Lint (`./gradlew :app:lintDebug`) — warnings captured.
  3. Robolectric tests (`./gradlew :app:testDebugUnitTest`) — **hard fail** if tests fail.
  4. Debug APK assembly.
- **NEVER** push code without verifying locally first. CI will reject non-compliant code.

---

## 🗂️ FILE STRUCTURE — WHERE TO PUT WHAT

```
app/src/main/java/com/example/
├── core/                          # Shared infrastructure
│   ├── database/                  # Room Database, DAOs, TypeConverters
│   │   ├── AppDatabase.kt         # v12, 23 entities, explicit migrations
│   │   └── dao/                   # 19 DAO files
│   ├── repository/                # Repository layer (Single source of truth)
│   │   ├── DRGRepository.kt
│   │   ├── RolePermissionRepository.kt
│   │   ├── PoskoCheckInRepository.kt
│   │   └── ...                    # AdminLog, Emergency, Gamification, etc.
│   ├── viewmodel/                 # Centralized MVI state management
│   │   ├── DRGViewModel.kt
│   │   ├── CashManagementViewModel.kt
│   │   └── coordinators/          # 8 Coordinator delegates
│   │       ├── EmergencyCoordinator.kt
│   │       ├── CommunityCoordinator.kt
│   │       ├── RadarCacheHelper.kt
│   │       └── ...
│   ├── shared/models/             # 25+ immutable data models & enums
│   ├── modules/                   # Feature modules (see below)
│   ├── cache/                     # DRGCacheManager, TileMetadataCacheEngine
│   ├── location/                  # BatteryAwareLocationSyncer, PoskoProximityDetector
│   ├── sensors/                   # CrashDetectionManager
│   ├── sync/                      # BackgroundSyncEngine, EmergencySmsFallbackHelper
│   ├── audio/                     # SosAlarmSoundManager, SosHapticManager, AudioRouteManager
│   ├── battery/                   # BatteryOptimizationManager
│   ├── firebase/                  # DRGFirebaseMessagingService
│   └── RoleManager.kt             # RBAC validation logic
├── shared/                        # Shared primitives
│   ├── atoms/                     # Atomic UI components (Badges, Buttons)
│   ├── models/                    # Shared immutable models
│   └── utils/                     # Utility functions (WhatsAppLauncher, etc.)
├── modules/                       # Feature modules — each is a feature area
│   ├── auth/                      # Landing, Login, Register
│   ├── dashboard/                 # Home, Quick Actions, Announcements
│   ├── emergency/                 # SOS, Crash Detection, Emergency UI
│   ├── radar/                     # Live Map, OSM, Hazard, Shelter
│   ├── forum_workshop/            # Forum Q&A, Workshop Partners
│   ├── treasury/                  # Kas Transparan, Financial Reports
│   ├── gamification/              # Leaderboard, Tasks, Rewards, Badges
│   ├── members/                   # Member Directory, Posko, RBAC
│   │   └── role_management/       # RoleFilterBar, RolePermissionScreen
│   ├── admin/                     # Admin Panel, Governance, Audit
│   ├── notifications/             # Settings, Preferences, Push
│   ├── profile/                   # KTA Digital, QR Code, Edit Profile
│   │   └── primitives/            # LoyaltyRank, KTA components
│   ├── community/                 # Community Hub
│   ├── services/                  # AI Assistant, Market, Koperasi
│   └── main/                      # Scaffold, Bottom Nav, Tab Content
└── ui/theme/                      # Custom Material 3 theme tokens
```

### Module Structure Rules
- Each module under `modules/` MUST have its own `primitives/` folder for micro-components.
- Module files: **< 125 lines**. Screen files: **< 125 lines**. ViewModel: **< 125 lines**.
- **NEVER** put business logic in UI files. Logic goes to `core/repository/` or `core/viewmodel/`.
- **NEVER** create a new module under `modules/` without updating `docs/modules/README.md`.

---

## 🔒 SECURITY PROTOCOLS

### Data Security
- **Network Security Config** (`app/src/main/res/xml/network_security_config.xml`): TLS 1.3 enforced, cleartext disabled, domain pinning for OSM/Firebase/Google.
- **Secrets**: API keys via `.env` + Secrets Gradle Plugin. **NEVER** commit `.env`, `secrets.properties`, or `google-services.json`.
- **ProGuard/R8**: Obfuscation enabled in release. Rules in `app/proguard-rules.pro`.
- **Database**: `allowBackup = false`, internal SQLite, data isolated per app.

### RBAC Security
- **RoleManager** (`core/RoleManager.kt`) validates all role changes.
- **Only Ketua** can appoint/demote Ketua.
- **Only Pengurus** (Wakil, Sekretaris, Bendahara, Satgas) can manage posko and view treasury.
- **All role changes** must be logged via `RoleAuditLogEntity` (immutable audit trail).
- **NEVER** bypass `RoleManager.validateRoleChange()`. Always validate before any role change.

### Emergency Security
- **SOS data** (GPS coordinates, emergency alerts) must be transmitted securely.
- **Emergency SMS fallback** (`EmergencySmsFallbackHelper`) must have valid phone numbers configured.
- **NEVER** log SOS coordinates to Logcat or debug outputs.

---

## 🧪 TESTING STANDARDS

### Test Requirements
- **Every new feature** requires at least one test file in `app/src/test/java/com/example/`.
- **Test naming**: `FeatureNameTest.kt`. Example: `SosHapticTactileTest.kt`, `PoskoCheckInRepositoryTest.kt`.
- **Test structure**: Use `@RunWith(RobolectricTestRunner::class)`, `@Config(sdk = [34])`.
- **Test content**: Arrange → Act → Assert pattern. Cover happy path, error path, and edge cases.
- **Adversarial tests**: Every feature must have negative input handling. See `AdversarialAuditScenariosTest.kt`.

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
# Run all unit tests
./gradlew :app:testDebugUnitTest

# Run specific test
./gradlew :app:testDebugUnitTest --tests "com.example.SosEmergencyScreenTest"

# Run architecture audit
python3 .github/scripts/check_file_length.py

# Run lint
./gradlew :app:lintDebug

# Build debug APK
./gradlew assembleDebug
```

---

## 🚫 PROHIBITED PATTERNS

| Pattern | Why | Alternative |
|---|---|---|
| `var` for state without `MutableStateFlow` | Breaks MVI/unidirectional flow | Use `MutableStateFlow` + `StateFlow` |
| Hardcoded colors (`Color.Red`, `Color.Blue`) | Violates design system | Use `DrgRedPanic`, `DrgGreenPrimary`, or `ColorResource` |
| Hardcoded strings | Breaks localization | Use `stringResource(R.string.xxx)` |
| `GlobalScope.launch` | Uncontrolled coroutine lifecycle | Use `viewModelScope` or `CoroutineScope(Dispatchers.IO)` |
| `fallbackToDestructiveMigration()` in production | **DESTROYS user data** on upgrade | Add explicit `Migration(from, to)` objects |
| `Log.d` / `Log.e` for sensitive data | Security leak | Use `android.util.Log` only for debug, never in production |
| Files > 125 lines | Breaks modularity | Split into `primitives/` folder |
| `TODO()` or `FIXME()` comments | Incomplete code | Implement before committing |
| `Thread.sleep()` or blocking I/O on main thread | ANR crash | Use `Dispatchers.IO` with Coroutines |
| Direct database access from UI | Breaks separation of concerns | Use Repository + ViewModel pattern |
| `Activity` context in ViewModel | Memory leak | Use `Application` context via `DRGApplication` |

---

## ✅ REQUIRED PATTERNS

### MVI/UDF Pattern
```kotlin
// State
data class ScreenState(
    val isLoading: Boolean = false,
    val data: List<Member> = emptyList(),
    val error: String? = null
)

// Event
sealed interface ScreenEvent {
    data class LoadData(val memberId: String) : ScreenEvent
    object RefreshData : ScreenEvent
}

// ViewModel
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
@Entity(
    tableName = "my_table",
    indices = [Index(value = ["foreignId"])]
)
data class MyEntity(
    @PrimaryKey val id: String,
    val foreignId: String,
    val name: String,
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

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        repository = MyRepository(db)
    }

    @After
    fun teardown() { db.close() }

    @Test
    fun `test happy path`() = runTest { ... }
}
```

---

## 📝 DOCUMENTATION STANDARDS

### Code Comments
- **Public functions**: Must have KDoc with `@param`, `@return`, `@throws`.
- **Private functions**: Single-line comment explaining intent.
- **Complex logic**: Block comment explaining the WHY, not the WHAT.
- **NEVER** comment out dead code — delete it.

### Commit Messages
- **Format**: `type(module): description` — e.g., `feat(emergency): add SOS haptic feedback`.
- **Types**: `feat`, `fix`, `refactor`, `build`, `chore`, `test`, `docs`.
- **NEVER** commit with vague messages like `update`, `fix bug`, `try`.

### Documentation Files
- New feature documentation goes to `docs/modules/` with format `MODULE_NAME.md`.
- Update `docs/DATABASE_MIGRATIONS.md` for any database change.
- Update `CHANGELOG.md` for every release-worthy change.
- Update `README.md` badges and feature lists when adding major features.

---

## 🔄 WORKFLOW — AI AGENT PROCEDURE

### Before Starting Work
1. **Read** `AGENTS.md`, `CONTRIBUTING.md`, `docs/DATABASE_MIGRATIONS.md`.
2. **Check** `.github/scripts/check_file_length.py` to understand the 125-line rule.
3. **Run** `python3 .github/scripts/check_file_length.py` to see current violations.
4. **Run** `./gradlew :app:testDebugUnitTest` to verify baseline tests pass.

### During Development
1. **Create** feature branch: `feat/descriptive-name` or `fix/descriptive-bug`.
2. **Write** tests FIRST (TDD pattern preferred).
3. **Implement** feature using MVI/UDF pattern.
4. **Verify** file line counts < 125.
5. **Run** `python3 .github/scripts/check_file_length.py`.
6. **Run** `./gradlew :app:testDebugUnitTest`.
7. **Update** `docs/DATABASE_MIGRATIONS.md` if database changed.
8. **Update** `CHANGELOG.md` if feature-worthy.

### Before Committing
1. **Run** full test suite: `./gradlew :app:testDebugUnitTest`.
2. **Run** architecture audit: `python3 .github/scripts/check_file_length.py`.
3. **Run** lint: `./gradlew :app:lintDebug`.
4. **Verify** no `TODO()`, `FIXME()`, `Log.d` with sensitive data.
5. **Verify** no hardcoded colors/strings.
6. **Commit** with descriptive message.

### After Pushing
1. **Verify** CI pipeline passes on GitHub Actions.
2. **Check** test reports uploaded.
3. **Verify** lint reports uploaded.
4. **Address** any CI failures immediately.

---

## 🎯 MODULE-SPECIFIC RULES

### Emergency Module
- SOS button **MUST** use `DrgRedPanic (#DC2626)` color.
- SOS countdown **MUST** have haptic feedback (`SosHapticManager`) and sound (`SosAlarmSoundManager`).
- Emergency data **MUST NOT** be logged or exposed in debug.
- All SOS-related tests **MUST** cover offline fallback (`EmergencySmsFallbackHelper`).

### Database Module
- **AppDatabase v12** — 23 entities. Schema version tracked.
- **Migrations must be explicit** — never rely on `fallbackToDestructiveMigration()` in production.
- Every entity **MUST** have `@Index` for query columns.
- Every DAO **MUST** return `Flow` for reactive updates.

### Repository Module
- Repositories **MUST** return `Flow` for reactive data.
- Suspend functions **MUST** use `withContext(Dispatchers.IO)`.
- **NEVER** expose `MutableStateFlow` externally. Use `StateFlow`.
- Repositories **MUST** handle `Result<T>` type for error handling.

### UI Module
- **Light theme ONLY**. Dark theme is not a feature.
- All colors from `res/values/colors.xml` brand tokens.
- All strings from `res/values/strings.xml`.
- Touch targets minimum 48x48dp.
- Dynamic insets handling mandatory.

### Profile/KTA Module
- **KTA Digital** must have QR Code verification.
- **Holographic watermark** (`KtaHolographicWatermark`) must be anti-forgery.
- **Profile data** must be encrypted at rest.

### Admin Module
- All admin actions **MUST** be logged via `RoleAuditLogEntity`.
- Role changes **MUST** go through `RoleManager.validateRoleChange()`.
- Admin dashboard **MUST** show `RoleAuditTrailCard`.

---

## 📞 COMMUNICATION PROTOCOLS

### Issue Reporting
- **Security issues**: Email `ardy.syafii@gmail.com` (from `SECURITY.md`). Do NOT use public GitHub issues.
- **Bug reports**: Use GitHub Issues with `bug_report.yml` template.
- **Feature requests**: Use GitHub Issues with `feature_request.yml` template.

### Pull Request Checklist
- [ ] Tests pass: `./gradlew :app:testDebugUnitTest`
- [ ] Architecture audit passes: `python3 .github/scripts/check_file_length.py`
- [ ] Lint passes: `./gradlew :app:lintDebug`
- [ ] All files < 125 lines
- [ ] No hardcoded colors/strings
- [ ] No TODO() or FIXME()
- [ ] Database migrations documented (if applicable)
- [ ] CHANGELOG.md updated (if applicable)
- [ ] README.md badges updated (if applicable)
- [ ] PR template completed with module checkboxes

---

## 🔧 TOOLING

### Required Tools
- **Android Studio** Hedgehog / Iguana / Jellyfish / Koala (2024.x+)
- **JDK 17** or **JDK 21** (Temurin/Corretto)
- **Gradle 8.11+** (via `./gradlew`)
- **Gradle Wrapper**: `./gradlew` (committed in repo)
- **Python 3** (for `check_file_length.py`)

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
- **Timeout**: 25 minutes per run
- **Concurrency**: Cancel in-progress runs on new push

---

## 📚 REFERENCE FILES

| File | Purpose |
|---|---|
| `SETUP.md` | Developer setup guide |
| `CONTRIBUTING.md` | Contribution workflow |
| `CODE_OF_CONDUCT.md` | Community standards |
| `SECURITY.md` | Security policy and vulnerability reporting |
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

> **⚠️ FINAL WARNING**: This document is MANDATORY. Any AI agent or developer who violates these rules will have their PR rejected by CI. The `check_file_length.py` script will **hard-fail** any PR with files exceeding 125 lines. The CI pipeline will **reject** any code with failing tests. **Read this document before writing any code.**
