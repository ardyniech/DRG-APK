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
        val q = question.lowercase().trim()
        return when {
            q.contains("posko") || q.contains("rehat") || q.contains("istirahat") -> {
                "☕ Rekomendasi Posko Rehat DRG:\n" +
                "• Posko Utama Klojen: Wifi 100Mbps, kopi free, tempat charger & istirahat.\n" +
                "• Dapatkan bonus +15 Poin Loyalitas otomatis setiap check-in di posko!"
            }
            q.contains("gacor") || q.contains("spot") || q.contains("orderan") -> {
                "🔥 Info Gacor Sore-Malam Malang Raya:\n" +
                "• Stasiun Malang (Kota Baru) & Alun-Alun: Lonjakan penumpang +45%.\n" +
                "• Kawasan Suhat / Kampus UB: Ramai pesanan resto & makanan.\n" +
                "• Siapkan saldo & fisik prima, salam satu aspal, rek!"
            }
            q.contains("ranjau") || q.contains("karanglo") || q.contains("rute aman") -> {
                "📌 Asisten Rute Aman Karanglo - Singosari:\n" +
                "• Waspada pertigaan Karanglo mengarah underpass rawan serbuk ranjau paku.\n" +
                "• Ambil lajur kanan dekat median atau jalur alternatif Karanglo Indah.\n" +
                "• Satgas DRG rutin patroli magnetik pagi & sore."
            }
            q.contains("kuliner") || q.contains("nongkrong") || q.contains("makan") -> {
                "🍲 Spot Kuliner & Rehat Malang:\n" +
                "• Bakso President, Cwie Mie Pojok Alun-Alun, Pos Ketan Legenda, STMJ Glintung.\n" +
                "• Di setiap Posko DRG selalu tersedia kopi tubruk & air mineral gratis!"
            }
            q.contains("kas") || q.contains("iuran") || q.contains("saldo") -> {
                "💰 Informasi Kas Komunitas DRG:\n" +
                "• Iuran rutin bulanan Rp 20.000 dikelola transparan di menu Kas & Treasury.\n" +
                "• Dana dialokasikan untuk santunan kecelakaan, alat tambal ban, dan operasional posko."
            }
            q.contains("sos") || q.contains("darurat") || q.contains("begal") || q.contains("kecelakaan") -> {
                "🚨 Protokol Darurat SOS DRG:\n" +
                "• Tahan tombol merah SOS selama 3 detik untuk menyiarkan sinyal ke Satgas terdekat.\n" +
                "• Jika offline, sistem otomatis mengalihkan ke SMS Darurat berformat koordinat GPS."
            }
            q.contains("poin") || q.contains("badge") || q.contains("hadiah") || q.contains("kupon") -> {
                "🏆 Program Poin & Gamifikasi DRG:\n" +
                "• Raih poin dari Check-in Posko (+15), Bayar Kas (+25), dan Bantuan Darurat (+50).\n" +
                "• Tukarkan poin dengan voucher servis bengkel rekanan atau oli gratis di menu Poin!"
            }
            else -> "Ada lagi yang bisa saya bantu pantau dari rute, info posko, atau keamanan, rek?"
        }
    }
}
