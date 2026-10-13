package com.example.modules.members.primitives

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.core.cache.PoskoTilePrefetchManager
import com.example.core.cache.TileMetadataCacheEngine
import com.example.core.database.AppDatabase
import com.example.shared.models.PoskoLocation
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun PoskoOfflineMapButton(
    posko: PoskoLocation,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isChecking by remember { mutableStateOf(false) }
    var isCached by remember { mutableStateOf(false) }

    LaunchedEffect(posko.id) {
        val db = AppDatabase.getInstance(context)
        val engine = TileMetadataCacheEngine(db.mapTileDao(), db.appStateDao())
        val manager = PoskoTilePrefetchManager(engine)
        isCached = manager.isPoskoCachedOffline(posko.id)
    }

    IconButton(
        onClick = {
            if (isChecking || isCached) {
                Toast.makeText(context, "Peta offline radius 1 KM untuk ${posko.name} sudah tersimpan", Toast.LENGTH_SHORT).show()
                return@IconButton
            }
            scope.launch {
                isChecking = true
                val db = AppDatabase.getInstance(context)
                val engine = TileMetadataCacheEngine(db.mapTileDao(), db.appStateDao())
                val manager = PoskoTilePrefetchManager(engine)
                val result = manager.prefetchTilesForPosko(posko)
                isChecking = false
                result.onSuccess { res ->
                    isCached = true
                    Toast.makeText(
                        context,
                        "Berhasil unduh ${res.totalTilesCached} tile peta offline ${posko.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                }.onFailure {
                    Toast.makeText(context, "Gagal mengunduh peta offline: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        },
        modifier = modifier.size(36.dp)
    ) {
        if (isChecking) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                color = DrgGreenPrimary,
                modifier = Modifier.size(16.dp)
            )
        } else if (isCached) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Peta Offline Tersimpan",
                tint = DrgGreenPrimary,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Icon(
                Icons.Default.CloudDownload,
                contentDescription = "Unduh Peta Offline",
                tint = DrgTextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
