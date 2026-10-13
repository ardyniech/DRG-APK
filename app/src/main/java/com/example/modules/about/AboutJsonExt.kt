package com.example.modules.about

import org.json.JSONArray
import org.json.JSONObject

internal fun JSONArray?.toStringList(): List<String> {
    if (this == null) return emptyList()
    return (0 until length()).mapNotNull { optString(it).takeIf { s -> s.isNotEmpty() } }
}

internal inline fun <T> JSONArray?.toList(mapper: (JSONObject) -> T): List<T> {
    if (this == null) return emptyList()
    return (0 until length()).mapNotNull { i -> optJSONObject(i)?.let(mapper) }
}

internal fun JSONObject.toFeature(): AboutFeature = AboutFeature(
    id = optString("id"),
    name = optString("name"),
    description = optString("description"),
    phase = optString("phase"),
    status = optString("status"),
    progress = optInt("progress")
)
