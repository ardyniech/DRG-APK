package com.example.modules.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgPurpleAccent
import com.example.ui.theme.DrgBlueAccent
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.AppVisualTheme
import com.example.shared.models.MemberRole
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary

private data class ProfileLauncherSpec(
    val title: String,
    val icon: ImageVector,
    val tint: Color,
    val tag: String,
    val highlighted: Boolean = false,
    val onClick: () -> Unit
)

/**
 * Squircle Launcher Dock on Profile matching Dashboard's SquircleAppDock DNA.
 * Provides prominent Settings Hub entry alongside role-gated Admin & SuperAdmin DevTools launchers.
 */
@Composable
fun ProfileLauncherDeck(
    role: MemberRole?,
    visualTheme: AppVisualTheme,
    onOpenSettings: () -> Unit,
    onEditBiodata: () -> Unit,
    onOpenAdmin: () -> Unit,
    onOpenDevTools: () -> Unit,
    modifier: Modifier = Modifier
) {
    val entries = buildList {
        add(ProfileLauncherSpec("Pengaturan", Icons.Filled.Tune, visualTheme.primaryColor, "btn_open_settings_launcher", true, onOpenSettings))
        add(ProfileLauncherSpec("Biodata", Icons.Filled.Edit, visualTheme.secondaryColor, "btn_edit_profile_launcher", false, onEditBiodata))
        if (role == MemberRole.SUPER_ADMIN || role?.isLeadership == true) {
            add(ProfileLauncherSpec("Admin", Icons.Filled.AdminPanelSettings, DrgBlueAccent, "btn_admin_governance", false, onOpenAdmin))
        }
        if (role == MemberRole.SUPER_ADMIN) {
            add(ProfileLauncherSpec("Dev Tools", Icons.Filled.Terminal, DrgPurpleAccent, "btn_superadmin_dev_tools", false, onOpenDevTools))
        }
    }

    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 14.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Pusat Kontrol & Pengaturan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                Text("Akses Cepat", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = visualTheme.primaryColor)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                entries.forEach { item ->
                    val sqShape = RoundedCornerShape(18.dp)
                    val bg = if (item.highlighted) {
                        Brush.verticalGradient(listOf(item.tint.copy(alpha = 0.25f), visualTheme.pillBg))
                    } else {
                        Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.85f), item.tint.copy(alpha = 0.12f)))
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .bouncyClick { item.onClick() }
                            .testTag(item.tag)
                            .padding(vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(sqShape)
                                .background(bg)
                                .border(1.4.dp, if (item.highlighted) item.tint.copy(alpha = 0.65f) else Color.White, sqShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(item.icon, contentDescription = item.title, tint = item.tint, modifier = Modifier.size(23.dp))
                        }
                        Text(
                            text = item.title,
                            fontSize = 10.sp,
                            fontWeight = if (item.highlighted) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (item.highlighted) item.tint else DrgTextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
