package com.example.modules.services

data class ChatMessage(
    val id: String,
    val sender: String,
    val content: String,
    val isUser: Boolean
)

object AiAssistantKnowledge {
    val presetQuestions = listOf(
        "Di mana spot paling gacor sore ini?",
        "Navigasi Rute Aman Karanglo Bebas Ranjau?",
        "Kuliner & tempat nongkrong legendaris Malang?",
        "Posko rehat terdekat ada kopi & wifi?"
    )

    fun getAnswerFor(question: String): String {
        return when (question) {
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
    }
}
