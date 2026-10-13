package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.modules.about.AboutManifestRepository
import com.example.modules.about.FeatureStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AboutManifestTest {

    private val context: Context get() = ApplicationProvider.getApplicationContext()

    private fun loadManifest() = AboutManifestRepository.parse(
        context.assets.open("about_manifest.json").bufferedReader().use { it.readText() }
    )

    @Test
    fun `manifest asset ter-bundle dan valid`() {
        val manifest = loadManifest()
        assertEquals("DRG Driver", manifest.app.name)
        assertTrue(manifest.app.packageName.isNotBlank())
        assertTrue(manifest.app.philosophy.isNotEmpty())
        assertTrue(manifest.build.version.isNotBlank())
        assertTrue(manifest.build.commit.isNotBlank())
    }

    @Test
    fun `summary konsisten dan progres dalam rentang valid`() {
        val manifest = loadManifest()
        val summary = manifest.summary
        assertEquals(summary.totalFeatures, summary.done + summary.inProgress + summary.planned)
        assertTrue(summary.overallProgress in 0..100)
        assertTrue(summary.modules > 0)
        assertTrue(summary.totalSourceFiles > 0)
        assertTrue(summary.totalLoc > 0)
        assertTrue(summary.testSuites > 0)
    }

    @Test
    fun `fase dan fitur lengkap untuk tree fitur`() {
        val manifest = loadManifest()
        assertTrue(manifest.phases.isNotEmpty())
        manifest.phases.forEach { phase ->
            assertTrue(phase.title.isNotBlank())
            assertTrue(phase.progress in 0..100)
            assertTrue(phase.doneCount in 0..phase.featureCount)
        }
        assertTrue(manifest.features.isNotEmpty())
        assertTrue(manifest.plannedFeatures.isNotEmpty())
        (manifest.features + manifest.plannedFeatures).forEach { feature ->
            assertTrue(feature.name.isNotBlank())
            assertTrue(feature.progress in 0..100)
            assertTrue(feature.status in setOf(FeatureStatus.DONE, FeatureStatus.IN_PROGRESS, FeatureStatus.PLANNED))
        }
    }

    @Test
    fun `refinement dan backlog terdokumentasi`() {
        val manifest = loadManifest()
        assertTrue(manifest.refinements.isNotEmpty())
        manifest.refinements.forEach { assertTrue(it.title.isNotBlank()) }
        assertTrue(manifest.backlog.isNotEmpty())
        manifest.backlog.forEach { assertTrue(it.isNotBlank()) }
    }
}