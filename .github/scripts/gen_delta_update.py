#!/usr/bin/env python3
"""Generate aset OTA delta: patch BSDIFF40 + update.json untuk GitHub Releases.

Patch dibuat dari APK rilis sebelumnya ke APK baru. HP hanya mengunduh patch
(biasanya 1-4% ukuran APK), menerapkannya ke APK terpasang, lalu hasilnya
byte-identik dengan APK yang ditandatangani CI (signature tetap valid).

Butuh: pip install bsdiff4

Contoh:
  python3 .github/scripts/gen_delta_update.py \\
    --new-apk dist/app-debug.apk \\
    --old-apk prev/app-debug.apk --old-version-code 1 \\
    --version-code 2 --version-name 1.1 \\
    --base-url https://github.com/ardyniech/DRG-APK/releases/download/v1.1 \\
    --out-dir dist \\
    --changelog "Tab SSOT DevTools" --changelog "Dedup UI"
"""

import argparse
import hashlib
import json
import os
import sys


def sha256_file(path):
    digest = hashlib.sha256()
    with open(path, "rb") as handle:
        for chunk in iter(lambda: handle.read(1 << 20), b""):
            digest.update(chunk)
    return digest.hexdigest()


def human_mb(size):
    return "%.2f MB" % (size / 1048576.0)


def main():
    parser = argparse.ArgumentParser(description="Generate delta OTA assets for DRG-APK")
    parser.add_argument("--new-apk", required=True)
    parser.add_argument("--old-apk", default=None)
    parser.add_argument("--old-version-code", type=int, default=None)
    parser.add_argument("--version-code", type=int, required=True)
    parser.add_argument("--version-name", required=True)
    parser.add_argument("--base-url", required=True, help="URL folder aset rilis (tanpa slash akhir)")
    parser.add_argument("--apk-name", default="app-debug.apk", help="Nama file APK di rilis")
    parser.add_argument("--out-dir", default="dist")
    parser.add_argument("--changelog", action="append", default=[])
    args = parser.parse_args()

    if not os.path.isfile(args.new_apk):
        print("ERROR: APK baru tidak ada: %s" % args.new_apk, file=sys.stderr)
        return 2

    os.makedirs(args.out_dir, exist_ok=True)
    new_size = os.path.getsize(args.new_apk)
    new_sha = sha256_file(args.new_apk)
    base_url = args.base_url.rstrip("/")
    patches = []

    if args.old_apk and args.old_version_code is not None and os.path.isfile(args.old_apk):
        try:
            import bsdiff4
        except ImportError:
            print("ERROR: bsdiff4 belum terpasang (pip install bsdiff4)", file=sys.stderr)
            return 3
        print("Membaca APK lama & baru…")
        with open(args.old_apk, "rb") as handle:
            old_bytes = handle.read()
        with open(args.new_apk, "rb") as handle:
            new_bytes = handle.read()
        print("Membuat patch bsdiff (bisa 1-3 menit untuk APK 70 MB)…")
        patch_bytes = bsdiff4.diff(old_bytes, new_bytes)
        patch_name = "patch-%d-to-%d.patch" % (args.old_version_code, args.version_code)
        patch_path = os.path.join(args.out_dir, patch_name)
        with open(patch_path, "wb") as handle:
            handle.write(patch_bytes)
        patch_size = len(patch_bytes)
        print("Patch: %s (%s = %.2f%% dari APK)" % (
            patch_name, human_mb(patch_size), 100.0 * patch_size / max(new_size, 1)))
        patches.append({
            "fromVersionCode": args.old_version_code,
            "fromSha256": sha256_file(args.old_apk),
            "url": "%s/%s" % (base_url, patch_name),
            "size": patch_size,
            "sha256": hashlib.sha256(patch_bytes).hexdigest(),
        })
    else:
        print("Tanpa APK lama: update.json hanya berisi APK penuh (tanpa patch).")

    manifest = {
        "versionCode": args.version_code,
        "versionName": args.version_name,
        "apk": {
            "url": "%s/%s" % (base_url, args.apk_name),
            "size": new_size,
            "sha256": new_sha,
        },
        "patches": patches,
        "changelog": args.changelog,
    }
    manifest_path = os.path.join(args.out_dir, "update.json")
    with open(manifest_path, "w", encoding="utf-8") as handle:
        json.dump(manifest, handle, indent=2, ensure_ascii=False)
        handle.write("\n")
    print("update.json: %s (APK penuh %s)" % (manifest_path, human_mb(new_size)))
    return 0


if __name__ == "__main__":
    sys.exit(main())
