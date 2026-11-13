package com.example.modules.emergency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.EmergencyType
import com.example.ui.theme.DrgRedDanger
import com.example.ui.theme.DrgTextPrimary

@Composable
fun SosHeroActionCard(
    selectedType: EmergencyType,
    onSelectType: (EmergencyType) -> Unit,
    onTriggerSos: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = DrgRedDanger.copy(alpha = 0.55f),
        contentPadding = 20.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "PUSAT KOMANDO DARURAT (SOS)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = DrgRedDanger,
                letterSpacing = 1.sp
            )
            SosPulsingCircleButton(onTriggerSos = onTriggerSos)
            Text(
                text = "Pilih Jenis Bantuan:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DrgTextPrimary
            )
            EmergencyTypeSelectorRow(selectedType = selectedType, onSelectType = onSelectType)
        }
    }
}
