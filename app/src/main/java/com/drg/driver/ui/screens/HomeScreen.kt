package com.drg.driver.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.drg.driver.ui.components.PanicButton

@Composable
fun HomeScreen() {
    Scaffold(
        floatingActionButton = {
            PanicButton(onClick = { /* Trigger SOS Logic */ })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Text("Selamat Datang, Driver Riang Gembira", style = MaterialTheme.typography.headlineMedium)
            // Konten dashboard lainnya...
        }
    }
}