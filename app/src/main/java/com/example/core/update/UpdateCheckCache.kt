package com.example.core.update

import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.TimeUnit

/**
 * Cache hasil cek update (TTL 6 jam) + lock unduhan, supaya tidak boros
 * internet dan tidak ada dua unduhan paralel (hemat kuota & tenaga).
 */
object UpdateCheckCache {

    private const val TTL_MS = 6L * 60 * 60 * 1000

    private var release: UpdateRelease? = null
    private var cachedAt: Long = 0L
    private var cachedForVersionCode: Int = -1

    /** Lock global untuk unduhan/apply (cegah paralel). */
    val downloadLock = Mutex()

    /** Rilis tersimpan kalau masih segar dan untuk versi terpasang yang sama. */
    fun cached(currentVersionCode: Int, now: Long = System.currentTimeMillis()): UpdateRelease? {
        val r = release ?: return null
        if (cachedForVersionCode != currentVersionCode) return null
        if (now - cachedAt > TTL_MS) return null
        return r
    }

    fun store(currentVersionCode: Int, r: UpdateRelease, now: Long = System.currentTimeMillis()) {
        release = r
        cachedAt = now
        cachedForVersionCode = currentVersionCode
    }

    fun invalidate() {
        release = null
        cachedAt = 0L
        cachedForVersionCode = -1
    }
}
