package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.core.firebase.AiStreamChunk
import com.example.core.firebase.DrgAiAssistantEngine
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DrgAiAssistantEngineTest {

    @Test
    fun streamAnswer_rejectsBlankPromptWithUserFriendlyError() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val chunks = DrgAiAssistantEngine.streamAnswer(context, "   ").toList()
        assertTrue(chunks.size == 1 && chunks.first() is AiStreamChunk.Error)
        val err = chunks.first() as AiStreamChunk.Error
        assertTrue(err.message.contains("pertanyaan", ignoreCase = true))
    }

    @Test
    fun streamAnswer_rejectsEmptyOrNewlineOnlyPromptSafely() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val chunks = DrgAiAssistantEngine.streamAnswer(context, "\n\t  \n").toList()
        assertTrue(chunks.size == 1 && chunks.first() is AiStreamChunk.Error)
        if (FirebaseApp.getApps(context).isEmpty()) {
            val uninit = DrgAiAssistantEngine.streamAnswer(context, "Info jalur aman Malang").toList()
            assertTrue(uninit.size == 1 && uninit.first() is AiStreamChunk.Error)
        }
    }
}
