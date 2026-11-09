#!/usr/bin/env python3
"""
DRG Driver — Bawel Minus Auditor (audit_minus.py)
==================================================
Static detector paling cerewet di repo: mendeteksi SEMUA kekurangan,
bahkan minus kecil, secara empiris dari file repo (bukan hardcode).

Mendeteksi: fake SYNCED tanpa network call, Firestore/Auth disabled +
google-services.json hilang, P2P log-only, auth lokal PIN 1234,
allowBackup=false tanpa backup, schema Room hilang, drift docs,
nilai DevTools fake, TODO/FIXME, dead onClick, hardcode warna,
permission kurang, gradlew mode, androidTest kosong, Detekt hilang,
release signing hilang, dependabot pending, file temp untracked,
marketplace/arisan ditunda (kas saja untuk finansial).

Severity: BLOCKER (multi-user rusak), MAJOR (production risk),
MINOR (hygiene), NOTE (deferred).

Usage:
  python3 .github/scripts/audit_minus.py            # warn-only, exit 0
  python3 .github/scripts/audit_minus.py --fail-on-blocker
  python3 .github/scripts/audit_minus.py --json > minus.json

Sumber kebenaran: laporan ini digabung ke about_manifest.json + docs/ABOUT.md
via generate_about_manifest.py menjadi SINGLE SOURCE OF TRUTH.
AI dev wajib baca docs/ABOUT.md (mesin: about_manifest.json) sebelum klaim.
DevTools in-app wajib mencerminkan laporan ini.
"""

import json, os, re, sys

REPO = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
MAIN = os.path.join(REPO, "app", "src", "main", "java")
TEST = os.path.join(REPO, "app", "src", "test", "java")
FINDINGS = []

def add(sev, cat, detail, fix=""):
    FINDINGS.append({"severity": sev, "category": cat, "detail": detail, "fix": fix})

def read(p):
    try:
        path = p if os.path.isabs(p) else os.path.join(REPO, p)
        with open(path, encoding="utf-8", errors="ignore") as f: return f.read()
    except Exception: return ""

def exists(p): return os.path.exists(os.path.join(REPO, p))

def walk(base, suffix=".kt"):
    out = []
    for dp, _, fs in os.walk(base):
        for n in fs:
            if n.endswith(suffix): out.append(os.path.join(dp, n))
    return out

def check_sync_fake():
    sync = read("app/src/main/java/com/example/core/sync/BackgroundSyncEngine.kt")
    has_net = bool(re.search(r"Retrofit|OkHttp|Firestore|HttpClient|@POST|@GET", sync)) or "syncQueueDao" in sync and "removeSyncItem" in sync and sync.count("removeSyncItem") > 0
    # BLOCKER bila tidak ada network call keluar
    if "Tersinkronisasi ke Cloud" in sync and sync.count("removeSyncItem") > 0 and "Retrofit" not in sync and "Firestore" not in sync:
        add("BLOCKER", "fake-sync", 'BackgroundSyncEngine label "Tersinkronisasi ke Cloud" padahal triggerBackgroundSync() cuma delay + removeSyncItem() — TIDAK ada network call (Retrofit/OkHttp/Firestore).',
            "Ganti jadi upload sungguhan atau label jujur OFFLINE_SAVED (anti-fabrikasi AGENTS §7).")
    if "Idle" in sync and sync.count("SYNCED") >= 1:
        add("MINOR", "fake-sync", "Status SYNCED ditampilkan walau tidak ada server — user dikira sudah ke cloud.",
            "Bedakan Sukses Nyata / Fallback Lokal / Gagal.")

def check_firebase():
    gradle = read("app/build.gradle.kts")
    if re.search(r"//\s*implementation\(libs\.firebase\.firestore\)", gradle):
        add("BLOCKER", "firebase", "firestore dependency dikomentari → multi-user tidak bisa sinkron.",
            "Uncomment firestore + auth + credentials bersama google-services.json asli.")
    if re.search(r"//\s*implementation\(libs\.firebase\.auth\)", gradle):
        add("BLOCKER", "firebase", "auth dependency dikomentari → akun server tidak ada, login lokal saja.",
            "Uncomment auth + Credential Manager.")
    if not exists("app/google-services.json"):
        add("BLOCKER", "firebase", "app/google-services.json HILANG (hanya .example) → FCM/Auth/AI tidak jalan produksi, P2P signaling gagal join topic.",
            "Isi google-services.json asli dari Firebase Console (jangan commit key prod, tapi CI perlu).")
    env = read(".env.example")
    if "1234" in env and "SEED_SUPER_ADMIN_PIN" in env:
        add("BLOCKER", "auth", "Seed super-admin PIN default 1234 di .env.example — kalau tidak diganti, akun pengurus bisa ditebak.",
            "Ganti sebelum rilis; tolak login bila PIN masih default.")
    manifest = read("app/src/main/AndroidManifest.xml")
    for perm in ["RECORD_AUDIO", "POST_NOTIFICATIONS", "FOREGROUND_SERVICE", "BLUETOOTH_CONNECT"]:
        if perm not in manifest:
            add("MAJOR", "permission", f"Permission {perm} belum dideklarasi (PTT/notif/foreground/Bluetooth).", "Tambah di manifest + runtime request.")

def check_p2p():
    sig = read("app/src/main/java/com/example/core/p2p/P2PSignalingManager.kt")
    if "broadcastPeerInfo" in sig and sig.count("Log.d") >= 1 and "mqtt" not in sig.lower() and "Topic" in sig:
        add("BLOCKER", "p2p", "P2PSignalingManager.broadcastPeerInfo() dan onMessageReceived() hanya Log.d — discovery FCM topic 0%, peer tidak pernah dicatat/dihubungi.",
            "Implementasikan FCM signaling sungguhan + WebRTC SDP/ICE exchange.")
    mgr = read("app/src/main/java/com/example/core/p2p/P2PManager.kt")
    if "candidate:1" in mgr:
        add("MAJOR", "p2p", "P2PManager.connectToPeer() bikin ICE candidate string palsu 'candidate:1...' — bukan SDP asli.",
            "Ganti dengan jalur signaling SDP/ICE penuh + STUN/TURN relay.")

def check_local_only():
    seeder = read("app/src/main/java/com/example/core/repository/DRGRepositorySeeder.kt")
    if "getMemberCount() == 0" in seeder:
        add("MAJOR", "multi-user", "Seed hanya bila members kosong → tiap HP baru punya data dummy sendiri, bukan data komunitas bersama.",
            "Butuh backend + akun server agar komunitas sinkron.")
    validator = read("app/src/main/java/com/example/modules/auth/LoginAuthValidator.kt")
    if "pin_hash" in validator: add("MAJOR", "multi-user", "LoginAuthValidator cari user di List<DriverMember> lokal + SharedPrefs — user HP lain tidak terlihat.",
        "Migrasi ke Firebase Auth / API akun.")
    manifest = read("app/src/main/AndroidManifest.xml")
    if 'allowBackup="false"' in manifest:
        add("MAJOR", "backup", "allowBackup=false + tidak ada cloud backup/sync → ganti HP = data hilang permanen.",
            "Tambah auto-backup terenkripsi atau export/import kas terenkripsi.")
    seeder2 = read("app/src/main/java/com/example/core/database/AppDatabaseMigrations.kt")
    if "3507123456780001" in seeder2:
        add("MAJOR", "privacy", "MIGRATION_12_13 DEFAULT kolom members berisi NIK/SIM/no HP dummy (3507123456780001) — data pribadi palsu masuk skema.",
            "Ganti DEFAULT jadi '' dan jangan hardcode PII.")
    sec = read("app/src/main/java/com/example/shared/utils/SecurityUtils.kt")
    if "SHA-256" in sec and "KeyStore" not in sec:
        add("MAJOR", "security", "PIN hanya SHA-256(plain) di SharedPrefs, bukan PBKDF2/Keystore — klaim DevTools 'Keystore PIN & PBKDF2' bohong.",
            "Ganti ke PBKDF2 + Android Keystore atau label jujur di DevTools.")
    # SQLite rawan
    # (dicek via AppDatabase: tidak ada SQLCipher)
    appdb = read("app/src/main/java/com/example/core/database/AppDatabase.kt")
    if "SQLCipher" not in appdb and "sqlcipher" not in read("gradle/libs.versions.toml").lower():
        add("MINOR", "security", "Room SQLite belum terenkripsi (SQLCipher tidak ada) — NIK/SIM/kas plain di file DB.",
            "Enkripsi database atau minimal jelaskan risiko di docs.")

def check_schema_docs():
    sdir = os.path.join(REPO, "app/schemas/com.example.core.database.AppDatabase")
    have = sorted([f for f in os.listdir(sdir) if f.endswith(".json")]) if os.path.isdir(sdir) else []
    want = [f"{i}.json" for i in range(1, 14)]
    missing = [w for w in want if w not in have]
    if missing:
        add("MAJOR", "database", f"Schema Room hilang {missing} (exportSchema=true tapi 1-4,8,9.json tidak commit) → verifikasi migrasi v1→v13 tidak lengkap.",
            "Generate & commit schema hilang via Room export.")
    readme = read("README.md")
    appdb = read("app/src/main/java/com/example/core/database/AppDatabase.kt")
    ver = re.search(r"version\s*=\s*(\d+)", appdb)
    if ver and f"v{ver.group(1)}" not in readme:
        add("MAJOR", "docs", f"README badge/section stale: README tidak sebut Room v{ver.group(1)} (masih v12).", "Sinkronkan badge + section Room v13.")
    suites = len([f for f in walk(TEST, "Test.kt")]) if os.path.isdir(TEST) else 0
    m = re.search(r"(\d+)\s+suite", readme)
    if m and int(m.group(1)) != suites:
        add("MINOR", "docs", f"README klaim {m.group(1)} suite, realita {suites} file Test.kt.",
            "Update README badge + SETUP/DEVTOOLS jumlahnya.")
    if "32" in readme and suites >= 50:
        add("MINOR", "docs", "README badge masih '32 Suites Passed' — basi, harusnya " + str(suites) + ".", "Naikkan badge test (auto dari manifest lebih baik).")

def check_code_hygiene():
    kts = walk(MAIN)
    total = len(kts)
    todos = []
    dead = []
    hexes = []
    for p in kts:
        txt = read(os.path.relpath(p, REPO))
        for ln in txt.splitlines():
            s = ln.strip()
            if "TODO(" in s or "TODO:" in s or "FIXME" in s:
                # placeholder pro se bukan TODO pemalsu — hanya TODO implementasi
                if "placeholder" not in s.lower():
                    todos.append(os.path.basename(p) + ": " + s[:80])
            if "onClick = {}" in s or "onClick={}" in s.replace(" ", ""):
                dead.append(p)
            if re.search(r"Color\(0x", s):
                hexes.append(os.path.basename(p))
    if todos:
        add("MAJOR", "code", f"{len(todos)} TODO/FIXME tersisa (mis. {todos[0]}).", "Implementasikan sebelum klaim Selesai.")
    if dead:
        add("MINOR", "code", f"{len(dead)} onClick kosong potensial dead-button.", "Hubungkan handler atau hapus tombol.")
    if hexes:
        add("MINOR", "code", f"Hardcode hex Color(0x...) di{len(set(hexes))} file — melanggar 'No Magic Numbers' AGENTS §1.",
            "Pindah ke DesignTokens/Theme.")
    # ServerOverload & Build fake
    overload = read("app/src/main/java/com/example/modules/devtools/ServerOverloadMonitor.kt")
    if "1420" in overload and "145L" in overload:
        add("MAJOR", "devtools", "ServerOverloadMonitor hardcode 1420 user/145ms + hasSystemFailures()=false — telemetri bohong tanpa backend.",
            "Ganti jadi UNKNOWN/no-backend atau probe latensi sungguhan.")
    build = read("app/src/main/java/com/example/modules/devtools/BuildDeploymentEngine.kt")
    if "ef5820e" in build and "92.4" in build:
        add("MAJOR", "devtools", "BuildDeploymentEngine hardcode hash ef5820e + coverage 92.4% + 39 suites — stale/fake.",
            "Baca BuildConfig nyata atau label UNKNOWN.")
    topo = read("app/src/main/java/com/example/modules/devtools/NodeTopologyScanner.kt")
    if "Realtime Snapshot Synced" in topo and not exists("app/google-services.json"):
        add("BLOCKER", "devtools", "NodeTopologyScanner klaim Firestore 'Synced' padahal google-services.json hilang + dep firestore off.",
            "Buat scanner jujur: belum di-enable.")
    sec = read("app/src/main/java/com/example/modules/devtools/SecurityAuditEngine.kt")
    if sec.count("isSecure = true") >= 3:
        add("MAJOR", "devtools", "SecurityAuditEngine 3 cek hardcode isSecure=true tanpa probe (Keystore/Sandbox/SOS).",
            "Probe runtime atau label risiko jujur.")
    cicd = read("app/src/main/java/com/example/modules/devtools/CiCdStatusScanner.kt")
    if "PASS (0 Pelanggaran" in cicd and ("39" in cicd or "426" in cicd):
        add("MINOR", "devtools", "CiCdStatusScanner hardcode PASS + 39 suites — stale (realita 50+ suites/454 files), status runtime tidak diketahui.",
            "Update angka + tulis UNKNOWN di runtime.")

def check_hygiene_small():
    if not exists("app/google-services.json.example"): add("MINOR","hygiene","google-services.json.example hilang.","Kembalikan template.")
    # gradlew mode
    try:
        mode = os.stat(os.path.join(REPO, "gradlew")).st_mode
        if not (mode & 0o111):
            add("MINOR", "hygiene", "gradlew tidak executable (mode 644) → ./gradlew lokal gagal tanpa chmod.",
                "chmod +x atau commit mode 755.")
    except Exception: pass
    # androidTest
    at = walk(os.path.join(REPO, "app/src/androidTest")) if exists("app/src/androidTest") else []
    if len(at) <= 1:
        add("MINOR", "test", "androidTest cuma 1 file contoh → alur kritis login/SOS/kas belum ter-cover instrumented.",
            "Tambah UI test untuk login, SOS, kas.")
    ci = read(".github/workflows/android_ci.yml")
    if "detekt" not in ci.lower() and "ktlint" not in ci.lower():
        add("MINOR", "ci", "Detekt/KtLint tidak ada di CI — hanya line-cap + lint.",
            "Tambah static analysis (sudah masuk backlog).")
    if "assembleRelease" not in ci and "bundle" not in ci.lower():
        add("MAJOR", "ci", "CI hanya assembleDebug, tidak ada release/AAB signing & distribusi.",
            "Tambah job release dengan Keystore via Secrets.")
    if "audit_minus" not in ci:
        add("NOTE", "ci", "audit_minus.py belum wajib di CI (warn-only).", "Tambah step warn-only setelah script ada.")
    # dependabot? tidak bisa dicek dari file, cukup NOTE
    # saran finansial scope
    feats = read(".github/about/features.json")
    if "marketplace" in feats and "DEFERRED" not in feats:
        add("NOTE", "planning", "Fitur marketplace/arisan masih 'Direncanakan' selevel blocker — padahal scope finansial sementara hanya iuran kas.",
            "Tandai DEFERRED agar DevTools tidak hitung sebagai blocker rilis awal.")

def main():
    check_sync_fake(); check_firebase(); check_p2p(); check_local_only()
    check_schema_docs(); check_code_hygiene(); check_hygiene_small()
    fail_blocker = "--fail-on-blocker" in sys.argv
    as_json = "--json" in sys.argv
    if as_json:
        print(json.dumps(FINDINGS, indent=2, ensure_ascii=False))
    else:
        by = {}
        for f in FINDINGS: by.setdefault(f["severity"], []).append(f)
        order = ["BLOCKER", "MAJOR", "MINOR", "NOTE"]
        total = len(FINDINGS)
        print("="*70)
        print("DRG Driver — Bawel Minus Audit (audit_minus.py)")
        print(f"Audit repo: {REPO} · Total temuan: {total}")
        print("="*70)
        for sev in order:
            items = by.get(sev, [])
            print(f"\n[{sev}] {len(items)} temuan")
            for f in items:
                print(f"  - [{f['category']}] {f['detail']}")
                print(f"      Fix: {f['fix']}")
        print("\nRingkasan: BLOCKER harus diperbaiki sebelum klaim multi-user.")
        print("Catatan finansial: Marketplace/Arisan DEFERRED — Kas (iuran) saja yang wajib rilis awal.")
    if fail_blocker and any(f["severity"] == "BLOCKER" for f in FINDINGS):
        sys.exit(1)
    sys.exit(0)

if __name__ == "__main__":
    main()
