package com.example.core.cache

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.ImageRequest
import okhttp3.*
import java.io.File
import java.util.concurrent.TimeUnit

object DRGCacheManager {
    private var imageLoader: ImageLoader? = null
    private const val DISK_CACHE_DIR = "drg_map_tile_cache"
    private const val DEFAULT_MAX_DISK_CACHE_BYTES = 100L * 1024L * 1024L // 100 MB

    var cachePolicy: MapCachePolicy = MapCachePolicy.CACHE_FIRST
    var isOfflineModeForced: Boolean = false

    fun getImageLoader(context: Context): ImageLoader {
        return imageLoader ?: synchronized(this) {
            imageLoader ?: buildImageLoader(context.applicationContext).also { imageLoader = it }
        }
    }

    private fun buildImageLoader(appContext: Context): ImageLoader {
        val cacheDir = File(appContext.cacheDir, DISK_CACHE_DIR)
        val httpCache = Cache(cacheDir, DEFAULT_MAX_DISK_CACHE_BYTES)

        val tileCacheInterceptor = Interceptor { chain ->
            var request = chain.request().newBuilder()
                .header("User-Agent", "DRGDriverCommunityApp/1.0 (contact@drgdriver.org)")
                .build()

            // If offline mode is forced or policy is OFFLINE_ONLY, force offline cache
            if (isOfflineModeForced || cachePolicy == MapCachePolicy.OFFLINE_ONLY) {
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            }

            val response = chain.proceed(request)
            response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=604800, max-stale=2592000")
                .build()
        }

        val okHttpClient = OkHttpClient.Builder()
            .cache(httpCache)
            .addNetworkInterceptor(tileCacheInterceptor)
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        return ImageLoader.Builder(appContext)
            .okHttpClient(okHttpClient)
            .memoryCache {
                MemoryCache.Builder(appContext)
                    .maxSizePercent(0.25)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(File(appContext.cacheDir, "drg_coil_disk_cache"))
                    .maxSizeBytes(DEFAULT_MAX_DISK_CACHE_BYTES)
                    .build()
            }
            .respectCacheHeaders(false)
            .build()
    }

    fun getCacheSizeBytes(context: Context): Long {
        val dir = File(context.cacheDir, DISK_CACHE_DIR)
        val coilDir = File(context.cacheDir, "drg_coil_disk_cache")
        return getFolderSize(dir) + getFolderSize(coilDir)
    }

    private fun getFolderSize(dir: File): Long {
        if (!dir.exists()) return 0L
        var result = 0L
        dir.listFiles()?.forEach { file ->
            result += if (file.isDirectory) getFolderSize(file) else file.length()
        }
        return result
    }

    fun clearCache(context: Context) {
        val dir = File(context.cacheDir, DISK_CACHE_DIR)
        val coilDir = File(context.cacheDir, "drg_coil_disk_cache")
        dir.deleteRecursively()
        coilDir.deleteRecursively()
        imageLoader?.memoryCache?.clear()
    }
}
