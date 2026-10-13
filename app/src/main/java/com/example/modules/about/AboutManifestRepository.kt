package com.example.modules.about

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

object AboutManifestRepository {
    private const val ASSET_NAME = "about_manifest.json"

    suspend fun load(context: Context): Result<AboutManifest> = withContext(Dispatchers.IO) {
        runCatching {
            val json = context.assets.open(ASSET_NAME).bufferedReader().use { it.readText() }
            parse(json)
        }
    }

    fun parse(json: String): AboutManifest {
        val root = JSONObject(json)
        val app = root.getJSONObject("app")
        val build = root.getJSONObject("build")
        val summary = root.getJSONObject("summary")
        return AboutManifest(
            app = AboutApp(
                name = app.optString("name"),
                tagline = app.optString("tagline"),
                packageName = app.optString("package"),
                audience = app.optString("audience"),
                philosophy = app.optJSONArray("philosophy").toStringList()
            ),
            build = AboutBuild(
                version = build.optString("version"),
                commit = build.optString("commit"),
                commitDate = build.optString("commitDate")
            ),
            summary = AboutSummary(
                overallProgress = summary.optInt("overallProgress"),
                totalFeatures = summary.optInt("totalFeatures"),
                done = summary.optInt("done"),
                inProgress = summary.optInt("inProgress"),
                planned = summary.optInt("planned"),
                modules = summary.optInt("modules"),
                totalSourceFiles = summary.optInt("totalSourceFiles"),
                totalLoc = summary.optInt("totalLoc"),
                testSuites = summary.optInt("testSuites")
            ),
            phases = root.optJSONArray("phases").toList { o ->
                AboutPhase(
                    id = o.optString("id"), title = o.optString("title"), target = o.optString("target"),
                    state = o.optString("state"), summary = o.optString("summary"), progress = o.optInt("progress"),
                    featureCount = o.optInt("featureCount"), doneCount = o.optInt("doneCount")
                )
            },
            features = root.optJSONArray("features").toList { it.toFeature() },
            plannedFeatures = root.optJSONArray("plannedFeatures").toList { it.toFeature() },
            refinements = root.optJSONArray("refinements").toList { o ->
                AboutRefinement(
                    id = o.optString("id"), title = o.optString("title"), detail = o.optString("detail"),
                    phase = o.optString("phase"), state = o.optString("state")
                )
            },
            backlog = root.optJSONArray("backlog").toStringList()
        )
    }
}
