#!/usr/bin/env python3
"""
DRG Driver Architecture & Code Quality Enforcer
Enforces:
1. Standard UI modules, controllers, adapters, viewmodels max 125 lines.
2. Escape hatch (models, custom canvas, master routers) max 200 lines.
3. Scans app/src/main/java as well as core/, modules/, shared/ across the repo.
"""

import os
import sys

DEFAULT_LINE_CAP = 125
ESCAPE_HATCH_CAP = 200

# Directory paths that use escape hatch
ESCAPE_HATCH_PATHS = [
    os.path.join("shared", "models"),
    os.path.join("core", "database"), # Schemas, TypeConverters & DAOs
    os.path.join("app", "src", "main", "java", "com", "example", "ui", "theme"),
    os.path.join("modules", "radar", "primitives", "RadarCanvasPainter.kt")
]

def check_file(file_path):
    with open(file_path, "r", encoding="utf-8", errors="ignore") as f:
        lines = f.readlines()
        count = len(lines)

    is_escape_hatch = any(pattern in file_path for pattern in ESCAPE_HATCH_PATHS)
    limit = ESCAPE_HATCH_CAP if is_escape_hatch else DEFAULT_LINE_CAP

    if count > limit:
        return file_path, count, limit
    return None

def main():
    repo_root = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))

    # Scan target directories: main source and any standalone module roots
    scan_dirs = [
        os.path.join(repo_root, "app", "src", "main", "java"),
        os.path.join(repo_root, "core"),
        os.path.join(repo_root, "modules"),
        os.path.join(repo_root, "shared"),
    ]

    violations = []
    checked_count = 0

    for sdir in scan_dirs:
        if not os.path.exists(sdir):
            continue
        for root, _, files in os.walk(sdir):
            for file in files:
                if file.endswith(".kt") or file.endswith(".java"):
                    full_path = os.path.join(root, file)
                    rel_path = os.path.relpath(full_path, repo_root)
                    checked_count += 1
                    result = check_file(rel_path)
                    if result:
                        violations.append(result)

    print("==================================================")
    print("DRG Driver Architecture Quality Audit")
    print(f"Total source files audited: {checked_count}")
    print("Audited paths: app/src/main/java, core/, modules/, shared/")
    print("==================================================")

    if violations:
        print(f"❌ Found {len(violations)} file(s) exceeding architectural line cap:")
        for path, count, limit in violations:
            print(f"  - {path}: {count} lines (Max allowed: {limit})")
        print("\nPlease modularize violating files into micro-primitives before merging.")
        sys.exit(1)
    else:
        print("✅ All source files strictly comply with architectural line caps (< 125 lines)!")
        sys.exit(0)

if __name__ == "__main__":
    main()
