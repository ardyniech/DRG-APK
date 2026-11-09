package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DevToolsSsotShellTest {

    @Test
    fun testSharedPrimitivesExistInApk() {
        Class.forName("com.example.modules.devtools.DevTabShellKt")
        Class.forName("com.example.modules.devtools.DevSsotTabKt")
        Class.forName("com.example.shared.atoms.DrgIconTileBoxKt")
    }

    @Test
    fun testSsotManifestMinusMatchesAudit() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        val json = ctx.assets.open("about_manifest.json").bufferedReader().use { it.readText() }
        val root = JSONObject(json)
        assertTrue(root.has("minus"))
        assertTrue(root.has("minusSummary"))
        val summary = root.getJSONObject("minusSummary")
        val arr = root.getJSONArray("minus")
        assertEquals(summary.optInt("total"), arr.length())
        assertTrue(summary.optInt("total") > 0)
    }

    @Test
    fun testSsotBlockersHaveDetailAndFix() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        val json = ctx.assets.open("about_manifest.json").bufferedReader().use { it.readText() }
        val arr = JSONObject(json).getJSONArray("minus")
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            assertTrue(o.optString("detail").isNotBlank())
            assertTrue(o.optString("fix").isNotBlank() || o.optString("category").isNotBlank())
        }
    }
}
