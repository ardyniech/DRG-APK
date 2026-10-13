package com.example.modules.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.modules.about.primitives.AboutBacklogCard
import com.example.modules.about.primitives.AboutErrorState
import com.example.modules.about.primitives.AboutFooterCard
import com.example.modules.about.primitives.AboutHeaderCard
import com.example.modules.about.primitives.AboutPhaseCard
import com.example.modules.about.primitives.AboutProgressCard
import com.example.modules.about.primitives.AboutRefinementCard
import com.example.modules.about.primitives.AboutStatGrid
import com.example.modules.about.primitives.AboutTopBar
import com.example.modules.about.primitives.SectionTitle
import com.example.ui.theme.DrgBackground
import com.example.ui.theme.DrgBackgroundGradient
import com.example.ui.theme.DrgGreenPrimary

@Composable
fun AboutScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val state by rememberAboutUiState(context)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DrgBackground,
        topBar = { AboutTopBar(onBack = onBack) }
    ) { padding ->
        Box(Modifier.fillMaxSize().background(DrgBackgroundGradient).padding(padding)) {
            when (val current = state) {
                is AboutUiState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center), color = DrgGreenPrimary)
                is AboutUiState.Error ->
                    AboutErrorState(current.message, Modifier.align(Alignment.Center))
                is AboutUiState.Ready -> AboutReadyContent(current.manifest)
            }
        }
    }
}

@Composable
private fun AboutReadyContent(manifest: AboutManifest) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("about_content"),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { AboutHeaderCard(app = manifest.app, build = manifest.build) }
        item { AboutProgressCard(summary = manifest.summary) }
        item { AboutStatGrid(summary = manifest.summary) }
        item { SectionTitle("Peta Fitur & Fase") }
        items(manifest.phases) { phase ->
            AboutPhaseCard(
                phase = phase,
                features = manifest.features.filter { it.phase == phase.id },
                plannedFeatures = manifest.plannedFeatures.filter { it.phase == phase.id }
            )
        }
        item { AboutRefinementCard(refinements = manifest.refinements) }
        item { AboutBacklogCard(items = manifest.backlog) }
        item { AboutFooterCard(build = manifest.build) }
        item { Spacer(Modifier.height(6.dp)) }
    }
}
