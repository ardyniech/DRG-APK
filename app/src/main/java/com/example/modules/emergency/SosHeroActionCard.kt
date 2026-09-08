package com.example.modules.emergency

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.EmergencyType
import com.example.ui.theme.*

@Composable
fun SosHeroActionCard(
    selectedType: EmergencyType,
    onSelectType: (EmergencyType) -> Unit,
    onTriggerSos: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = DrgSurface,
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, DrgRedDanger.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
            .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = DrgRedDanger.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "PUSAT KOMANDO DARURAT (SOS)", fontSize = 14.sp, fontWeight = FontWeight.Black, color = DrgRedDanger, letterSpacing = 1.sp)
            SosPulsingCircleButton(onTriggerSos = onTriggerSos)
            Text(text = "Pilih Jenis Bantuan:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
            EmergencyTypeSelectorRow(selectedType = selectedType, onSelectType = onSelectType)
        }
    }
}
