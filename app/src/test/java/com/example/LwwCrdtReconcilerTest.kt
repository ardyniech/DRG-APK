package com.example

import com.example.core.sync.CrdtRegister
import com.example.core.sync.LwwCrdtReconciler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LwwCrdtReconcilerTest {

    @Test
    fun `newer timestamp wins conflict deterministically`() {
        val older = CrdtRegister("Lokal Lama", updatedAtMillis = 1000L, nodeId = "devA")
        val newer = CrdtRegister("Cloud Baru", updatedAtMillis = 2000L, nodeId = "devB")
        assertEquals("Cloud Baru", LwwCrdtReconciler.merge(older, newer).value)
        assertEquals("Cloud Baru", LwwCrdtReconciler.merge(newer, older).value)
    }

    @Test
    fun `equal timestamp breaks ties deterministically by nodeId`() {
        val a = CrdtRegister("Node A", updatedAtMillis = 5000L, nodeId = "node-a")
        val b = CrdtRegister("Node B", updatedAtMillis = 5000L, nodeId = "node-b")
        assertEquals("Node B", LwwCrdtReconciler.merge(a, b).value)
        assertEquals("Node B", LwwCrdtReconciler.merge(b, a).value)
    }

    @Test
    fun `tombstone deletion removes entry from active values without resurrection`() {
        val local = mapOf("k1" to CrdtRegister("Aktif", 1000L, "dev1", deleted = false))
        val remote = mapOf("k1" to CrdtRegister("Terhapus", 3000L, "dev2", deleted = true))
        val merged = LwwCrdtReconciler.mergeMap(local, remote)
        val active = LwwCrdtReconciler.activeValues(merged)
        assertFalse(active.containsKey("k1"))
        assertTrue(merged.getValue("k1").deleted)
    }
}
