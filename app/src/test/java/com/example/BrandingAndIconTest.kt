package com.example

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BrandingAndIconTest {

    @Test
    fun `custom adaptive icon and launcher drawables resolve without error`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val launcherId = context.resources.getIdentifier("ic_launcher", "mipmap", context.packageName)
        val roundId = context.resources.getIdentifier("ic_launcher_round", "mipmap", context.packageName)
        val logoId = context.resources.getIdentifier("drg_app_icon", "drawable", context.packageName)
        assertTrue("ic_launcher mipmap harus tersedia", launcherId != 0)
        assertTrue("ic_launcher_round mipmap harus tersedia", roundId != 0)
        assertTrue("drg_app_icon drawable harus tersedia", logoId != 0)
        assertNotNull(ContextCompat.getDrawable(context, logoId))
    }
}
