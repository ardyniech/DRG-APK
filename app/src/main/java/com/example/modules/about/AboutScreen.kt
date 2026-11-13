package com.example.modules.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.modules.about.primitives.*
import com.example.shared.atoms.LocalAppVisualTheme

@Composable
fun AboutScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    val state by rememberAboutUiState(context)
    val theme = LocalAppVisualTheme.current

    Column(modifier = modifier.fillMaxSize()) {
        AboutTopBar(onBack = onBack)
        Box(Modifier.weight(1f).fillMaxWidth()) {
            when (val current = state) {
                is AboutUiState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center), color = theme.primaryColor)
                is AboutUiState.Error ->
                    AboutErrorState(current.message, Modifier.align(Alignment.Center))
                is AboutUiState.Ready -> AboutReadyContent(current.manifest, contentPadding)
            }
        }
    }
}

@Composable
private fun AboutReadyContent(manifest: AboutManifest, contentPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("about_content"),
        contentPadding = PaddingValues(
            start = 12.dp,
            end = 12.dp,
            top = 8.dp,
            bottom = contentPadding.calculateBottomPadding() + 24.dp
        ),
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
