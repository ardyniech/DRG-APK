package com.example.modules.forum_workshop

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.shared.models.WorkshopPartner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkshopScreen(
    workshops: List<WorkshopPartner>,
    onCallWorkshop: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bengkel Rekanan") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        WorkshopSection(
            workshops = workshops,
            onCallWorkshop = onCallWorkshop,
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("workshop_screen")
        )
    }
}
