package com.example.modules.forum_workshop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgAmberWarning
import com.example.ui.theme.DrgTextMuted

@Composable
fun ForumActionBanner(
    onCreateUpdateClick: () -> Unit,
    onAskQuestionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    val accent = theme.primaryColor

    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 12.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ruang Diskusi & Pantauan Dulur Ojol", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = accent)
                Text("Sinkron Lokal DB", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = DrgTextMuted)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onCreateUpdateClick,
                    colors = ButtonDefaults.buttonColors(containerColor = accent),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).height(42.dp).testTag("button_create_update")
                ) {
                    Icon(Icons.Default.Campaign, contentDescription = "Update", modifier = Modifier.size(15.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Update Info", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                }

                OutlinedButton(
                    onClick = onAskQuestionClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgAmberWarning),
                    modifier = Modifier.weight(1f).height(42.dp).testTag("button_ask_question")
                ) {
                    Icon(Icons.Default.HelpOutline, contentDescription = "Tanya", modifier = Modifier.size(15.dp), tint = DrgAmberWarning)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tanya Driver", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}
