package com.example.modules.about.primitives

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgSurface
import com.example.ui.theme.DrgTextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text("Tentang Aplikasi", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DrgTextPrimary)
        },
        navigationIcon = {
            IconButton(onClick = onBack, modifier = Modifier.testTag("about_back")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DrgGreenPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = DrgSurface)
    )
}
