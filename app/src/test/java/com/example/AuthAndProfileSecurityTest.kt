package com.example

import com.example.shared.models.MemberRole
import org.junit.Assert.*
import org.junit.Test

class AuthAndProfileSecurityTest {

    @Test
    fun testPinValidationSecurity() {
        val validPin = "123456"
        val shortPin = "1234"
        val letterPin = "12345A"
        val emptyPin = ""

        assertTrue(validPin.length == 6 && validPin.all { it.isDigit() })
        assertFalse(shortPin.length == 6 && shortPin.all { it.isDigit() })
        assertFalse(letterPin.length == 6 && letterPin.all { it.isDigit() })
        assertFalse(emptyPin.length == 6 && emptyPin.all { it.isDigit() })
    }

    @Test
    fun testPolicePlateNumberNormalization() {
        val rawInput = " b  1234  abc "
        val normalized = rawInput.trim().replace("\\s+".toRegex(), " ").uppercase()
        assertEquals("B 1234 ABC", normalized)

        val plateRegex = "^[A-Z]{1,2}\\s[0-9]{1,4}\\s[A-Z]{1,3}$".toRegex()
        assertTrue(normalized.matches(plateRegex))
        assertFalse("1234 INVALID".matches(plateRegex))
    }

    @Test
    fun testRoleBasedAdminScreeningAccess() {
        val ketua = MemberRole.KETUA
        val wakil = MemberRole.WAKIL_KETUA
        val satgas = MemberRole.SATGAS
        val bendahara = MemberRole.BENDAHARA
        val sekretaris = MemberRole.SEKRETARIS
        val dewanEtika = MemberRole.DEWAN_ETIKA
        val anggota = MemberRole.ANGGOTA

        assertTrue(ketua.isLeadership)
        assertTrue(wakil.isLeadership)
        assertTrue(satgas.isLeadership)
        assertTrue(bendahara.isLeadership)
        assertTrue(sekretaris.isLeadership)
        assertTrue(dewanEtika.isLeadership)
        assertFalse(anggota.isLeadership)
    }

    @Test
    fun testKtaQrCodePayloadIntegrity() {
        val memberId = "DRG-JKT-042"
        val role = MemberRole.SATGAS
        val platformName = "GRAB"
        val checksum = (memberId.hashCode() + role.ordinal).toString(16)

        val payload = "DRG:$memberId:$platformName:${role.name}:$checksum"
        assertTrue(payload.startsWith("DRG:"))
        assertTrue(payload.contains("DRG-JKT-042"))
        assertTrue(payload.contains("GRAB"))
        assertTrue(payload.contains("SATGAS"))
    }
}
