package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.modules.auth.LoginAuthValidator
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import com.example.shared.utils.SecurityUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LoginAuthValidatorTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun member(id: String = "M1", driverId: String = "D1", phone: String = "0812") =
        DriverMember(
            id = id, name = "Test", driverId = driverId, phone = phone,
            role = MemberRole.ANGGOTA, motorcyclePlate = "AB1234CD",
            motorcycleModel = "Honda"
        )

    @Test
    fun loginBaruAutoStorePin() {
        val m = member()
        val res = LoginAuthValidator.authenticate(context, listOf(m), m.id, "4321")
        assertTrue("login sukses", res.first)
        assertEquals(m.id, res.second)
        // tersimpan v2
        val stored = SecurityUtils.getStoredPinHash(context, m.id)!!
        assertTrue("format v2", stored.startsWith("v2:"))
        assertTrue("verify", SecurityUtils.verifyPin(context, "4321", stored))
    }

    @Test
    fun loginPinBenarLulus() {
        val m = member()
        SecurityUtils.storePinHash(context, m.id, "9999")
        val res = LoginAuthValidator.authenticate(context, listOf(m), m.id, "9999")
        assertTrue(res.first)
    }

    @Test
    fun loginPinSalahDitolak() {
        val m = member()
        SecurityUtils.storePinHash(context, m.id, "9999")
        val res = LoginAuthValidator.authenticate(context, listOf(m), m.id, "0000")
        assertFalse(res.first)
    }

    @Test
    fun migrasiLegacySha256KeV2() {
        val m = member()
        // simpan legacy (v1) manual
        val legacy = "v1:" + sha256Hex("1234")
        context.getSharedPreferences("drg_security_prefs", Context.MODE_PRIVATE).edit()
            .putString("pin_hash_${m.id}", legacy).apply()
        val res = LoginAuthValidator.authenticate(context, listOf(m), m.id, "1234")
        assertTrue("login legacy sukses", res.first)
        val now = SecurityUtils.getStoredPinHash(context, m.id)!!
        assertTrue("ter-migrasi ke v2: $now", now.startsWith("v2:"))
        // needsMigration mengikuti mode: di Robolectric Keystore tidak tersedia → "sw" → true.
        // Pada perangkat nyata (Keystore ada) → "ks" → false.
    }

    @Test
    fun pinSalahTidakMigrasi() {
        val m = member()
        val legacy = "v1:" + sha256Hex("1234")
        context.getSharedPreferences("drg_security_prefs", Context.MODE_PRIVATE).edit()
            .putString("pin_hash_${m.id}", legacy).apply()
        val res = LoginAuthValidator.authenticate(context, listOf(m), m.id, "9999")
        assertFalse(res.first)
        val still = SecurityUtils.getStoredPinHash(context, m.id)!!
        assertTrue("tetap legacy", still.startsWith("v1:"))
    }

    private fun sha256Hex(input: String): String {
        val md = java.security.MessageDigest.getInstance("SHA-256")
        return md.digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
