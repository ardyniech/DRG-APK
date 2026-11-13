package com.example.modules.services

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AiAssistantDialog(onDismiss: () -> Unit) {
    val theme = LocalAppVisualTheme.current
    var messages by remember {
        mutableStateOf(
            listOf(ChatMessage("1", "AI", "Halo abang driver DRG Malang Raya! Ada yang bisa saya bantu pantau dari rute, info banjir, atau spot gacor hari ini?", false))
        )
    }
    var isTyping by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    fun handleQuestionClick(question: String) {
        if (isTyping) return
        messages = messages + ChatMessage(System.currentTimeMillis().toString(), "Driver", question, true)
        scope.launch {
            delay(100)
            listState.animateScrollToItem(messages.size - 1)
            isTyping = true
            delay(1500)
            val answer = AiAssistantKnowledge.getAnswerFor(question)
            messages = messages + ChatMessage((System.currentTimeMillis() + 1).toString(), "AI", answer, false)
            isTyping = false
            delay(100)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    DrgLuminousGlassModal(
        title = "DRG Asisten Rute",
        subtitle = "Panduan Cepat Rute, Cuaca & Spot Malang",
        onDismiss = { if (!isTyping) onDismiss() },
        icon = Icons.Default.SmartToy,
        accentColor = DrgAmberSecondary,
        footer = {
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), enabled = !isTyping) {
                Text("Tutup Asisten", color = DrgTextSecondary, fontWeight = FontWeight.Bold)
            }
        }
    ) {
        AiAssistantChatList(messages = messages, isTyping = isTyping, listState = listState)

        Text("Pilih Pertanyaan Pantau:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)

        Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            AiAssistantKnowledge.presetQuestions.forEach { question ->
                OutlinedButton(
                    onClick = { handleQuestionClick(question) },
                    enabled = !isTyping,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, theme.primaryColor.copy(alpha = 0.35f)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(question, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                }
            }
        }
    }
}
