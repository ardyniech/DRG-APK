#!/usr/bin/env python3
"""
DRG Driver — About Manifest Generator
=====================================
Single source of truth for the in-app "Tentang Aplikasi" page and docs/ABOUT.md.

It reads the *intent* declarations from .github/about/features.json and derives
every status / progress value by inspecting the REAL repository (module files,
screen composables, unit tests, TODO/FIXME markers, ROADMAP checkboxes, git).

This means progress is honest and self-updating: nobody edits a percentage by
hand. Ship the code and the number moves.

Outputs (both overwritten):
  - app/src/main/assets/about_manifest.json   (consumed by the Android app)
  - docs/ABOUT.md                             (human readable)
"""

import json
import os
import re
import subprocess
import sys

REPO = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
CONFIG = os.path.join(REPO, ".github", "about", "features.json")
MAIN_JAVA = os.path.join(REPO, "app", "src", "main", "java")
TEST_DIR = os.path.join(REPO, "app", "src", "test", "java")
OUT_JSON = os.path.join(REPO, "app", "src", "main", "assets", "about_manifest.json")
OUT_MD = os.path.join(REPO, "docs", "ABOUT.md")

STATUS_PLANNED = "PLANNED"
STATUS_IN_PROGRESS = "IN_PROGRESS"
STATUS_DONE = "DONE"

STATUS_LABEL = {STATUS_DONE: "Selesai", STATUS_IN_PROGRESS: "Sedang Dibangun", STATUS_PLANNED: "Direncanakan"}


def sh(*args):
    try:
        return subprocess.check_output(args, cwd=REPO, stderr=subprocess.DEVNULL).decode().strip()
    except Exception:
        return ""


def read_json(path):
    with open(path, "r", encoding="utf-8") as fh:
        return json.load(fh)


def read_lines(path):
    with open(path, "r", encoding="utf-8", errors="ignore") as fh:
        return fh.readlines()


def collect_source_files(root):
    out = []
    if not os.path.exists(root):
        return out
    for dirpath, _, files in os.walk(root):
        for name in files:
            if name.endswith((".kt", ".java")):
                out.append(os.path.join(dirpath, name))
    return out


def test_suite_names():
    names = []
    if os.path.exists(TEST_DIR):
        for dirpath, _, files in os.walk(TEST_DIR):
            for name in files:
                if name.endswith("Test.kt"):
                    names.append(name[:-3])
    return names


def module_stats(rel_path):
    path = os.path.join(REPO, rel_path)
    if os.path.isfile(path):
        files = [path]
    elif os.path.isdir(path):
        files = collect_source_files(path)
    else:
        return None
    loc = screens = todos = 0
    for f in files:
        lines = read_lines(f)
        loc += len(lines)
        if os.path.basename(f).endswith("Screen.kt"):
            screens += 1
        for ln in lines:
            if "TODO" in ln or "FIXME" in ln:
                todos += 1
    return {"exists": True, "files": len(files), "loc": loc, "screens": screens, "todos": todos}


def matched_tests(keywords, all_tests):
    hits = []
    for kw in keywords or []:
        for t in all_tests:
            if kw.lower() in t.lower():
                hits.append(t)
    return sorted(set(hits))


def compute_feature(feat, all_tests):
    stats = module_stats(feat.get("modulePath", "")) if feat.get("modulePath") else None
    hits = matched_tests(feat.get("testKeywords"), all_tests)
    if not stats:
        return {"status": STATUS_PLANNED, "progress": 0, "files": 0, "loc": 0, "todos": 0, "tests": []}
    progress = 40
    if stats["screens"] > 0:
        progress += 30
    if hits:
        progress += 25
    if stats["files"] > 1:
        progress += 5
    progress -= min(stats["todos"] * 3, 15)
    if stats["screens"] > 0 and hits and stats["todos"] == 0:
        status, progress = STATUS_DONE, 100
    else:
        status = STATUS_IN_PROGRESS
        progress = max(5, min(progress, 95))
    return {"status": status, "progress": progress, "files": stats["files"],
            "loc": stats["loc"], "todos": stats["todos"], "tests": hits}


def roadmap_backlog():
    items = []
    path = os.path.join(REPO, "ROADMAP.md")
    if not os.path.exists(path):
        return items
    for ln in read_lines(path):
        m = re.match(r"\s*[-*]\s*\[\s\]\s*(.+)", ln)
        if m:
            items.append(m.group(1).strip())
    return items


def main():
    cfg = read_json(CONFIG)
    all_tests = test_suite_names()
    files = collect_source_files(MAIN_JAVA)

    features = []
    for feat in cfg["features"]:
        result = compute_feature(feat, all_tests)
        features.append({
            "id": feat["id"], "name": feat["name"], "description": feat["description"],
            "phase": feat["phase"], **result,
        })
    features.sort(key=lambda f: (f["phase"], f["id"]))

    planned = [{"id": p["id"], "name": p["name"], "description": p["description"],
                "phase": p["phase"], "status": STATUS_PLANNED, "progress": 0} for p in cfg.get("plannedFeatures", [])]
    refinements = cfg.get("refinements", [])

    phases = []
    for phase in cfg["phases"]:
        members = [f for f in features if f["phase"] == phase["id"]] + \
                  [p for p in planned if p["phase"] == phase["id"]]
        progress = round(sum(m["progress"] for m in members) / len(members)) if members else 0
        done = sum(1 for m in members if m.get("status") == STATUS_DONE)
        phases.append({**phase, "progress": progress, "featureCount": len(members), "doneCount": done})

    all_items = features + planned
    overall = round(sum(i["progress"] for i in all_items) / len(all_items)) if all_items else 0
    summary = {
        "overallProgress": overall,
        "totalFeatures": len(all_items),
        "done": sum(1 for i in features if i["status"] == STATUS_DONE),
        "inProgress": sum(1 for i in features if i["status"] == STATUS_IN_PROGRESS),
        "planned": sum(1 for i in all_items if i["status"] == STATUS_PLANNED),
        "modules": len([d for d in os.listdir(os.path.join(MAIN_JAVA, "com", "example", "modules"))
                        if os.path.isdir(os.path.join(MAIN_JAVA, "com", "example", "modules", d))])
        if os.path.isdir(os.path.join(MAIN_JAVA, "com", "example", "modules")) else 0,
        "totalSourceFiles": len(files),
        "totalLoc": sum(len(read_lines(f)) for f in files),
        "testSuites": len(all_tests),
    }

    backlog = roadmap_backlog()
    backlog += [f"{p['name']} — {p['description']}" for p in planned]
    backlog += [f"{r['title']} — {r['detail']}" for r in refinements if r.get("state") != "done"]

    manifest = {
        "schemaVersion": cfg["schemaVersion"],
        "app": cfg["app"],
        "build": {
            "version": sh("git", "describe", "--tags", "--always") or "dev",
            "commit": sh("git", "rev-parse", "--short", "HEAD"),
            "commitDate": sh("git", "show", "-s", "--format=%cs", "HEAD"),
            "generatedBy": "generate_about_manifest.py",
        },
        "summary": summary,
        "phases": phases,
        "features": features,
        "plannedFeatures": planned,
        "refinements": refinements,
        "backlog": backlog,
    }

    os.makedirs(os.path.dirname(OUT_JSON), exist_ok=True)
    with open(OUT_JSON, "w", encoding="utf-8") as fh:
        json.dump(manifest, fh, indent=2, ensure_ascii=False)
        fh.write("\n")
    os.makedirs(os.path.dirname(OUT_MD), exist_ok=True)
    with open(OUT_MD, "w", encoding="utf-8") as fh:
        fh.write(render_markdown(manifest))

    print(f"✅ About manifest generated: overall {overall}% "
          f"({summary['done']} done / {summary['inProgress']} in-progress / {summary['planned']} planned)")
    print(f"   → {os.path.relpath(OUT_JSON, REPO)}")
    print(f"   → {os.path.relpath(OUT_MD, REPO)}")


def render_markdown(m):
    s, b = m["summary"], m["build"]
    out = [f"# Tentang {m['app']['name']} — Fitur & Progres", ""]
    out.append(f"> {m['app']['tagline']}")
    out.append("")
    out.append(f"**Progres pengembangan keseluruhan: {s['overallProgress']}%** "
               f"({s['done']} selesai · {s['inProgress']} dikerjakan · {s['planned']} direncanakan)")
    out.append("")
    out.append(f"Versi `{b['version']}` · commit `{b['commit']}` · {b['commitDate']} · "
               f"{s['modules']} modul · {s['totalSourceFiles']} file sumber · {s['totalLoc']} baris · {s['testSuites']} test suite")
    out.append("")
    out.append("> Dokumen ini di-generate otomatis oleh `generate_about_manifest.py`. "
               "Status & persentase dihitung dari kode nyata, bukan ditulis manual.")
    out.append("")
    out.append("## Fase Pengembangan")
    out.append("")
    for p in m["phases"]:
        out.append(f"### {p['title']} — {p['progress']}% ({p['target']})")
        out.append(f"{p['summary']}")
        out.append("")
        out.append("| Fitur | Status | Progres |")
        out.append("|---|---|---|")
        for f in [x for x in m["features"] if x["phase"] == p["id"]]:
            out.append(f"| {f['name']} | {STATUS_LABEL[f['status']]} | {f['progress']}% |")
        for f in [x for x in m["plannedFeatures"] if x["phase"] == p["id"]]:
            out.append(f"| {f['name']} | {STATUS_LABEL[STATUS_PLANNED]} | 0% |")
        out.append("")
    out.append("## Rencana Penyempurnaan")
    out.append("")
    for r in m["refinements"]:
        out.append(f"- **{r['title']}** ({r['state']}) — {r['detail']}")
    out.append("")
    out.append("## Backlog Berjalan")
    out.append("")
    for item in m["backlog"]:
        out.append(f"- {item}")
    out.append("")
    out.append(f"*Total fitur: {s['totalFeatures']} · {s['overallProgress']}% selesai.*")
    out.append("")
    return "\n".join(out)


if __name__ == "__main__":
    sys.exit(main())
