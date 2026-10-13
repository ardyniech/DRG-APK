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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    val cardShape = RoundedCornerShape(26.dp)
    val highlightBrush = Brush.linearGradient(
        colors = listOf(
            Color.White,
            Color.White.copy(alpha = 0.5f),
            DrgRedDanger.copy(alpha = 0.35f)
        )
    )

    Surface(
        shape = cardShape,
        color = Color.White.copy(alpha = 0.88f),
        tonalElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, cardShape, ambientColor = DrgRedDanger.copy(alpha = 0.22f), spotColor = DrgRedDanger.copy(alpha = 0.18f))
            .border(1.5.dp, highlightBrush, cardShape)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
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
