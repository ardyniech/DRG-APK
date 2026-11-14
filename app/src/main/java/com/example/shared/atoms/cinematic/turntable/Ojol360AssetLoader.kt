package com.example.shared.atoms.cinematic.turntable

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

object Ojol360AssetLoader {
    private val frameCache = mutableMapOf<Int, ImageBitmap>()
    private var backgroundCache: ImageBitmap? = null

    fun loadFrame(context: Context, index: Int): ImageBitmap? {
        val safeIndex = (index % 16).let { if (it < 0) it + 16 else it }
        frameCache[safeIndex]?.let { return it }

        return try {
            val fileName = "cinematic/360/ojol_frame_%02d.png".format(safeIndex)
            context.assets.open(fileName).use { stream ->
                val bmp = BitmapFactory.decodeStream(stream)
                val imageBmp = bmp.asImageBitmap()
                frameCache[safeIndex] = imageBmp
                imageBmp
            }
        } catch (_: Exception) {
            null
        }
    }

    fun loadBackground(context: Context): ImageBitmap? {
        backgroundCache?.let { return it }
        return try {
            context.assets.open("cinematic/malang_highway_bg.jpg").use { stream ->
                val bmp = BitmapFactory.decodeStream(stream)
                val imageBmp = bmp.asImageBitmap()
                backgroundCache = imageBmp
                imageBmp
            }
        } catch (_: Exception) {
            null
        }
    }

    fun clearCache() {
        frameCache.clear()
        backgroundCache = null
    }
}
