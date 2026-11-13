package com.example.modules.members

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgGlassPrimaryButton
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.PoskoLocation
import com.example.shared.utils.PoskoContactLauncher
import com.example.shared.utils.PoskoCoordinationReason
import com.example.ui.theme.*

@Composable
fun PoskoContactOptionsModal(
    posko: PoskoLocation,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassModal(
        title = "Hubungi ${posko.name}",
        subtitle = "Pj: ${posko.coordinatorName} • ${posko.area}",
        onDismiss = onDismiss,
        modifier = modifier,
        icon = Icons.Default.Phone,
        accentColor = theme.primaryColor,
        footer = {
            DrgGlassPrimaryButton(
                text = "Panggil Pengurus via Telepon",
                icon = Icons.Default.Phone,
                onClick = { PoskoContactLauncher.openPoskoDialer(context, posko); onDismiss() }
            )
        }
    ) {
        Text("Pilih Template Koordinasi Posko:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)

        PoskoCoordinationReason.entries.forEach { reason ->
            DrgLuminousGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                backgroundColor = theme.pillBg.copy(alpha = 0.5f),
                borderHighlightColor = theme.glassBorderColor.copy(alpha = 0.7f),
                contentPadding = 11.dp,
                onClick = {
                    PoskoContactLauncher.openPoskoWhatsApp(context, posko, reason)
                    onDismiss()
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(17.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(reason.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                        Text(reason.template(posko), fontSize = 10.sp, color = DrgTextSecondary, maxLines = 1)
                    }
                }
            }
        }
    }
}
