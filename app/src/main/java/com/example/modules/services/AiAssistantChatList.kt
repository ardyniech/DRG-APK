package com.example.modules.services

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AiAssistantChatList(
    messages: List<ChatMessage>,
    isTyping: Boolean,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(DrgBackground, RoundedCornerShape(12.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(messages) { msg ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = if (msg.isUser) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                Surface(
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = if (msg.isUser) 12.dp else 0.dp,
                        bottomEnd = if (msg.isUser) 0.dp else 12.dp
                    ),
                    color = if (msg.isUser) DrgAmberSecondary else DrgSurface,
                    modifier = Modifier
                        .widthIn(max = 220.dp)
                        .border(
                            1.dp,
                            if (msg.isUser) Color.Transparent else DrgTextMuted.copy(alpha = 0.15f),
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    Text(
                        text = msg.content,
                        fontSize = 11.sp,
                        color = if (msg.isUser) Color.Black else DrgTextPrimary,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }

        if (isTyping) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DrgSurface,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(
                            text = "AI sedang mengetik...",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = DrgAmberSecondary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
