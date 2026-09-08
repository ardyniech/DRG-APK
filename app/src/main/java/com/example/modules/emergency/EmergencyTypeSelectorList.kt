package com.example.modules.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.EmergencyType
import com.example.ui.theme.*

@Composable
fun EmergencyTypeSelectorList(
    selectedType: EmergencyType,
    onSelectType: (EmergencyType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        EmergencyType.values().forEach { type ->
            val isSelected = selectedType == type
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) DrgRedContainer else DrgSurfaceVariant.copy(alpha = 0.5f))
                    .border(
                        1.dp,
                        if (isSelected) DrgRedDanger else DrgOutline.copy(alpha = 0.5f),
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { onSelectType(type) }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onSelectType(type) },
                    colors = RadioButtonDefaults.colors(selectedColor = DrgRedDanger)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = type.label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) DrgRedDanger else DrgTextPrimary
                    )
                    Text(
                        text = "Prioritas Siaga: ${type.severity}",
                        fontSize = 10.sp,
                        color = DrgTextSecondary
                    )
                }
            }
        }
    }
}
