package com.example

import com.example.modules.services.AiAssistantKnowledge
import org.junit.Assert.*
import org.junit.Test

class AiAssistantKnowledgeTest {

    @Test
    fun testPresetQuestionsNotEmpty() {
        val questions = AiAssistantKnowledge.presetQuestions
        assertTrue(questions.isNotEmpty())
        assertEquals(4, questions.size)
    }

    @Test
    fun testGacorSpotAnswer() {
        val question = "Di mana spot paling gacor sore ini?"
        val answer = AiAssistantKnowledge.getAnswerFor(question)
        assertTrue(answer.contains("Info Gacor Sore-Malam"))
        assertTrue(answer.contains("Stasiun Malang"))
    }

    @Test
    fun testRouteSafetyAnswer() {
        val question = "Navigasi Rute Aman Karanglo Bebas Ranjau?"
        val answer = AiAssistantKnowledge.getAnswerFor(question)
        assertTrue(answer.contains("Asisten Rute Aman Karanglo"))
        assertTrue(answer.contains("Satgas DRG"))
    }

    @Test
    fun testPoskoRecommendationAnswer() {
        val question = "Posko rehat terdekat ada kopi & wifi?"
        val answer = AiAssistantKnowledge.getAnswerFor(question)
        assertTrue(answer.contains("Rekomendasi Posko Rehat"))
        assertTrue(answer.contains("Klojen"))
    }

    @Test
    fun testKasKeywordMatching() {
        val question = "Bagaimana aturan iuran kas bulanan?"
        val answer = AiAssistantKnowledge.getAnswerFor(question)
        assertTrue(answer.contains("Informasi Kas Komunitas"))
        assertTrue(answer.contains("20.000"))
    }

    @Test
    fun testSosKeywordMatching() {
        val question = "Apa yang harus dilakukan saat darurat begal?"
        val answer = AiAssistantKnowledge.getAnswerFor(question)
        assertTrue(answer.contains("Protokol Darurat SOS"))
        assertTrue(answer.contains("Satgas"))
    }

    @Test
    fun testPoinKeywordMatching() {
        val question = "Gimana cara dapat poin dan hadiah?"
        val answer = AiAssistantKnowledge.getAnswerFor(question)
        assertTrue(answer.contains("Program Poin & Gamifikasi"))
    }

    @Test
    fun testFallbackUnknownQuestion() {
        val question = "xyz123 random unhandled question"
        val answer = AiAssistantKnowledge.getAnswerFor(question)
        assertEquals("Ada lagi yang bisa saya bantu pantau dari rute, info posko, atau keamanan, rek?", answer)
    }
}
