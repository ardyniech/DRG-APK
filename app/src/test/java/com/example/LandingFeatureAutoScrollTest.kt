package com.example

import com.example.modules.auth.AppFeatureRepository
import org.junit.Assert.*
import org.junit.Test

class LandingFeatureAutoScrollTest {

    @Test
    fun testAllFeaturesRepositoryNotEmpty() {
        val features = AppFeatureRepository.allFeatures
        assertTrue(features.isNotEmpty())
        assertEquals(8, features.size)
    }

    @Test
    fun testFeaturePropertiesValid() {
        val features = AppFeatureRepository.allFeatures
        val radar = features.first { it.id == "radar" }
        assertEquals("Radar Pantau & Live Map", radar.title)
        assertEquals("Peta & Kopdar", radar.category)
        assertEquals("Live GPS", radar.badgeText)

        val sos = features.first { it.id == "sos" }
        assertEquals("Tombol SOS & Tim Reaksi Cepat", sos.title)
        assertEquals("Siaga 24/7", sos.badgeText)
    }
}
