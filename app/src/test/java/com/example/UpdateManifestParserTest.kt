package com.example

import com.example.core.update.UpdateManifestParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Parser `update.json` harus toleran (field hilang → tidak crash) dan tidak
 * pernah memilih patch untuk base yang sha256-nya beda.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UpdateManifestParserTest {

    private val fullJson = """
        {
          "versionCode": 2,
          "versionName": "1.1",
          "apk": { "url": "https://x/app-debug.apk", "size": 77037592, "sha256": "aa11" },
          "patches": [
            { "fromVersionCode": 1, "fromSha256": "bb22",
              "url": "https://x/patch-1-to-2.patch", "size": 2512537, "sha256": "cc33" }
          ],
          "changelog": ["Tab SSOT", "Dedup UI"]
        }
    """.trimIndent()

    @Test
    fun parsesFullManifest() {
        val release = UpdateManifestParser.parse(fullJson)
        assertNotNull(release)
        requireNotNull(release)
        assertEquals(2, release.versionCode)
        assertEquals("1.1", release.versionName)
        assertEquals(77037592L, release.apkSizeBytes)
        assertEquals(1, release.patches.size)
        assertEquals(2, release.changelog.size)
    }

    @Test
    fun selectsPatchOnlyWhenVersionAndShaMatch() {
        val release = requireNotNull(UpdateManifestParser.parse(fullJson))
        assertNotNull(release.patchFor(1, "BB22"))
        assertNull("sha beda harus ditolak", release.patchFor(1, "deadbeef"))
        assertNull("versi beda harus ditolak", release.patchFor(9, "bb22"))
    }

    @Test
    fun toleratesMissingOptionalFields() {
        val minimal = """
            {"versionCode": 3, "apk": {"url": "https://x/a.apk", "sha256": "ff"}}
        """.trimIndent()
        val release = UpdateManifestParser.parse(minimal)
        assertNotNull(release)
        requireNotNull(release)
        assertEquals("?", release.versionName)
        assertTrue(release.patches.isEmpty())
        assertTrue(release.changelog.isEmpty())
    }

    @Test
    fun returnsNullForBrokenJson() {
        assertNull(UpdateManifestParser.parse("bukan json"))
        assertNull(UpdateManifestParser.parse("""{"versionCode": 2}"""))
    }
}
