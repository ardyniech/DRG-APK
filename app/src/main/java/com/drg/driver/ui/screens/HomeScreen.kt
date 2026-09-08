package com.drg.driver.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drg.driver.ui.components.PanicButton

@Composable
fun HomeScreen(
    onTriggerEmergency: () -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            PanicButton(onClick = onTriggerEmergency)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Selamat Datang, Driver Riang Gembira",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Komunitas Driver Ojol Malang Raya — Aman, Solid, Transparan.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
