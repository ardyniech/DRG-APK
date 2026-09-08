package com.example.modules.services

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AiAssistantDialog(onDismiss: () -> Unit) {
    var messages by remember {
        mutableStateOf(
            listOf(ChatMessage("1", "AI", "Halo abang driver DRG Malang Raya! Ada yang bisa saya bantu pantau dari rute, info banjir, atau spot gacor hari ini? ☕🔥", false))
        )
    }
    var isTyping by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    fun handleQuestionClick(question: String) {
        if (isTyping) return
        val userMsg = ChatMessage(System.currentTimeMillis().toString(), "Driver", question, true)
        messages = messages + userMsg

        scope.launch {
            delay(100)
            listState.animateScrollToItem(messages.size - 1)
            isTyping = true
            delay(1500)
            val answer = AiAssistantKnowledge.getAnswerFor(question)
            val aiMsg = ChatMessage((System.currentTimeMillis() + 1).toString(), "AI", answer, false)
            messages = messages + aiMsg
            isTyping = false
            delay(100)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DrgSurface,
            modifier = Modifier.fillMaxWidth().padding(10.dp).border(1.dp, DrgAmberSecondary, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = Icons.Default.SmartToy, contentDescription = "Bot", tint = DrgAmberSecondary, modifier = Modifier.size(24.dp))
                    Text(text = "DRG AI Assistant", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                }

                HorizontalDivider(color = DrgTextMuted.copy(alpha = 0.2f))

                AiAssistantChatList(messages = messages, isTyping = isTyping, listState = listState)

                Text(text = "Pilih Pertanyaan Pantau:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextSecondary)

                Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                    AiAssistantKnowledge.presetQuestions.forEach { question ->
                        OutlinedButton(
                            onClick = { handleQuestionClick(question) },
                            enabled = !isTyping,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = question, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgAmberSecondary)
                        }
                    }
                }

                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), enabled = !isTyping) {
                    Text("Tutup Asisten", color = DrgTextSecondary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
