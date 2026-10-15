package com.example

import android.content.Context
import android.content.ContextWrapper
import androidx.test.core.app.ApplicationProvider
import com.example.shared.utils.DoubleBackExitHandler
import com.example.shared.utils.findActivity
import com.example.shared.utils.moveAppToBackground
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DoubleBackExitHandlerTest {

    @Test
    fun `first back press records timestamp and triggers onFirstBack without exiting`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        var exitCalled = false
        var firstBackCalled = false

        val handler = DoubleBackExitHandler(
            context = context,
            timeoutMillis = 2000L,
            onExit = { exitCalled = true },
            onFirstBack = { firstBackCalled = true }
        )

        val result = handler.handleBackPress(currentTime = 1000L)

        assertFalse("First back press must not exit", result)
        assertFalse("Exit callback must not be invoked on first press", exitCalled)
        assertTrue("First back callback must be invoked", firstBackCalled)
        assertEquals(1000L, handler.lastBackPressTime)
    }

    @Test
    fun `second back press within timeout triggers onExit and returns true`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        var exitCalled = false
        var firstBackCount = 0

        val handler = DoubleBackExitHandler(
            context = context,
            timeoutMillis = 2000L,
            onExit = { exitCalled = true },
            onFirstBack = { firstBackCount++ }
        )

        handler.handleBackPress(currentTime = 1000L)
        val result = handler.handleBackPress(currentTime = 2500L) // 1500ms diff <= 2000ms

        assertTrue("Second back press within timeout must exit", result)
        assertTrue("Exit callback must be invoked", exitCalled)
        assertEquals("First back callback only called once", 1, firstBackCount)
    }

    @Test
    fun `second back press after timeout resets timestamp and does not exit`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        var exitCalled = false
        var firstBackCount = 0

        val handler = DoubleBackExitHandler(
            context = context,
            timeoutMillis = 2000L,
            onExit = { exitCalled = true },
            onFirstBack = { firstBackCount++ }
        )

        handler.handleBackPress(currentTime = 1000L)
        val result = handler.handleBackPress(currentTime = 3500L) // 2500ms diff > 2000ms

        assertFalse("Second back press after timeout must not exit", result)
        assertFalse("Exit callback must not be invoked", exitCalled)
        assertEquals("First back callback called again", 2, firstBackCount)
        assertEquals(3500L, handler.lastBackPressTime)
    }

    @Test
    fun `reset clears lastBackPressTime preventing unintended exit on tab switch`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        var exitCalled = false

        val handler = DoubleBackExitHandler(
            context = context,
            timeoutMillis = 2000L,
            onExit = { exitCalled = true }
        )

        handler.handleBackPress(currentTime = 1000L)
        handler.reset()
        assertEquals(0L, handler.lastBackPressTime)

        // Pressing back again within timeout of original press should now act as first press
        val result = handler.handleBackPress(currentTime = 1500L)
        assertFalse("After reset, press must be treated as first press", result)
        assertFalse(exitCalled)
        assertEquals(1500L, handler.lastBackPressTime)
    }

    @Test
    fun `findActivity and moveAppToBackground gracefully handle application context and deep wrapper`() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        val wrapper1 = ContextWrapper(appContext)
        val wrapper2 = ContextWrapper(wrapper1)

        // Context is not an Activity, must return null and false without throwing
        assertNull("Application Context has no Activity", wrapper2.findActivity())
        assertFalse("moveAppToBackground should return false safely", wrapper2.moveAppToBackground())
    }
}
