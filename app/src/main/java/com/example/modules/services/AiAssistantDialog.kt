package com.example.modules.services

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.firebase.AiStreamChunk
import com.example.core.firebase.DrgAiAssistantEngine
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AiAssistantDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val theme = LocalAppVisualTheme.current
    var messages by remember {
        mutableStateOf(
            listOf(ChatMessage("1", "AI", "Halo abang driver DRG! Tanya apa saja soal rute Malang, posko rehat, atau keamanan berkendara.", false))
        )
    }
    var promptInput by remember { mutableStateOf("") }
    var isStreaming by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    fun submitQuestion(question: String) {
        val clean = question.trim()
        if (clean.isEmpty() || isStreaming) return
        promptInput = ""
        val userId = "u_${System.currentTimeMillis()}"
        val botId = "ai_${System.currentTimeMillis() + 1}"
        messages = messages + ChatMessage(userId, "Driver", clean, true) + ChatMessage(botId, "AI", "...", false)
        scope.launch {
            isStreaming = true
            listState.animateScrollToItem((messages.size - 1).coerceAtLeast(0))
            DrgAiAssistantEngine.streamAnswer(context, clean).collect { chunk ->
                val text = when (chunk) {
                    is AiStreamChunk.Delta -> chunk.accumulated
                    is AiStreamChunk.Completed -> chunk.finalText
                    is AiStreamChunk.Error -> "⚠️ ${chunk.message}"
                }
                messages = messages.map { if (it.id == botId) it.copy(content = text) else it }
            }
            isStreaming = false
            listState.animateScrollToItem((messages.size - 1).coerceAtLeast(0))
        }
    }

    DrgLuminousGlassModal(
        title = "DRG Asisten Rute (Gemini)",
        subtitle = "Firebase AI Logic • Streaming Real-time",
        onDismiss = { if (!isStreaming) onDismiss() },
        icon = Icons.Default.SmartToy,
        accentColor = DrgAmberSecondary,
        footer = {
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth().testTag("ai_close_button"), enabled = !isStreaming) {
                Text("Tutup Asisten", color = DrgTextSecondary, fontWeight = FontWeight.Bold)
            }
        }
    ) {
        AiAssistantChatList(messages = messages, isTyping = isStreaming, listState = listState)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            items(AiAssistantKnowledge.presetQuestions) { question ->
                OutlinedButton(
                    onClick = { submitQuestion(question) },
                    enabled = !isStreaming,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, theme.primaryColor.copy(alpha = 0.35f)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(question, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor, maxLines = 1)
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = promptInput,
                onValueChange = { promptInput = it },
                placeholder = { Text("Tanya rute, bengkel, atau cuaca...", fontSize = 11.sp) },
                singleLine = true,
                enabled = !isStreaming,
                modifier = Modifier.weight(1f).testTag("ai_prompt_input")
            )
            IconButton(
                onClick = { submitQuestion(promptInput) },
                enabled = !isStreaming && promptInput.isNotBlank(),
                modifier = Modifier.size(48.dp).testTag("ai_send_button")
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Kirim pertanyaan AI", tint = theme.primaryColor)
            }
        }
    }
}
