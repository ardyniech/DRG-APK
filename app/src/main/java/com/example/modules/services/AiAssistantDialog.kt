package com.example.modules.services

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(
    val id: String,
    val sender: String,
    val content: String,
    val isUser: Boolean
)

@Composable
fun AiAssistantDialog(
    onDismiss: () -> Unit
) {
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("1", "AI", "Halo abang driver DRG Malang Raya! Ada yang bisa saya bantu pantau dari rute, info banjir, atau spot gacor hari ini? ☕🔥", false)
            )
        )
    }

    var isTyping by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val presetQuestions = listOf(
        "Di mana spot paling gacor sore ini?",
        "Navigasi Rute Aman Karanglo Bebas Ranjau?",
        "Kuliner & tempat nongkrong legendaris Malang?",
        "Posko rehat terdekat ada kopi & wifi?"
    )

    fun handleQuestionClick(question: String) {
        if (isTyping) return

        val userMsg = ChatMessage(System.currentTimeMillis().toString(), "Driver", question, true)
        messages = messages + userMsg

        scope.launch {
            delay(100)
            listState.animateScrollToItem(messages.size - 1)
            isTyping = true

            // Simulate typing delay
            delay(1500)

            val answer = when (question) {
                "Di mana spot paling gacor sore ini?" -> {
                    "🔥 Info Gacor Sore-Malam Malang Raya:\n" +
                    "• Stasiun Malang (Kota Baru) & Alun-Alun: Banyak orderan wisatawan & warga lokal pulang kerja (lonjakan +45%).\n" +
                    "• Kampus UB/UM (Suhat): Potensi tinggi pesanan makanan (GrabFood) di sore/malam hari.\n" +
                    "• Silakan melipir pelan-pelan ke area penjemputan terdekat, rek!"
                }
                "Navigasi Rute Aman Karanglo Bebas Ranjau?" -> {
                    "📌 Asisten Rute Aman Karanglo - Singosari:\n" +
                    "• Perhatian rek! Area pertigaan Karanglo mengarah ke underpass rawan ranjau paku serbuk besi.\n" +
                    "• Rute Aman: Ambil lajur kanan (mendekati pembatas median jalan) atau lewat jalur alternatif Perumahan Karanglo Indah.\n" +
                    "• Satgas DRG rutin melakukan penyisiran ranjau paku magnetik setiap jam 08:00 & 16:00 WIB."
                }
                "Kuliner & tempat nongkrong legendaris Malang?" -> {
                    "🍲 Spot Kuliner & Cangkrukan Malang Legendaris:\n" +
                    "• Bakso President (Dekat rel kereta): Rasanya mantap, cocok buat rehat siang.\n" +
                    "• Cwie Mie Pojok (Pojok Alun-Alun): Klasik dan halal, porsi mengenyangkan.\n" +
                    "• Pos Ketan Legenda (Batu / Alun-alun): Buat ngangetin badan malam-malam.\n" +
                    "• STMJ Glintung (Jl. Letjend S. Parman): STMJ kuat penambah energi tarikan malem, rek!\n" +
                    "• Seluruh Posko DRG selalu sedia kopi tubruk gratis buat dulur kabeh!"
                }
                "Posko rehat terdekat ada kopi & wifi?" -> {
                    "☕ Rekomendasi Posko Rehat DRG Malang:\n" +
                    "• Posko Utama Klojen: Fasilitas lengkap (Kopi free, Wifi kenceng, charger hp & tempat selonjoran nyaman).\n" +
                    "• Penanggung Jawab: Bang Rian Pratama (Kordinator Lapangan)."
                }
                else -> "Ada lagi yang bisa saya bantu pantau, rek?"
            }

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
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(1.dp, DrgAmberSecondary, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "Bot",
                        tint = DrgAmberSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "DRG AI Assistant",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DrgTextPrimary
                    )
                }

                Divider(color = DrgTextMuted.copy(alpha = 0.2f))

                // Chat Scroll
                LazyColumn(
                    state = listState,
                    modifier = Modifier
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

                // Preset Question Chips
                Text(
                    text = "Pilih Pertanyaan Pantau:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgTextSecondary
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    presetQuestions.forEach { question ->
                        OutlinedButton(
                            onClick = { handleQuestionClick(question) },
                            enabled = !isTyping,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = question,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DrgAmberSecondary
                            )
                        }
                    }
                }

                // Dismiss Button
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isTyping
                ) {
                    Text("Tutup Asisten", color = DrgTextSecondary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
