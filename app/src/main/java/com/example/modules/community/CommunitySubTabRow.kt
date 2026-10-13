package com.example.modules.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgGlassCard
import com.example.ui.theme.*

@Composable
fun CommunitySubTabRow(
    selectedSubTab: Int,
    onSelectSubTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val subTabs = listOf(
        "Kas DRG" to Icons.Default.AccountBalanceWallet,
        "Forum" to Icons.Default.Forum,
        "Misi & Poin" to Icons.Default.EmojiEvents,
        "Anggota" to Icons.Default.People,
        "Layanan & Koperasi" to Icons.Default.ShoppingBag
    )

    DrgGlassCard(
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White.copy(alpha = 0.88f),
        elevation = 4.dp
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedSubTab,
            edgePadding = 8.dp,
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            indicator = {},
            divider = {}
        ) {
            subTabs.forEachIndexed { index, (title, icon) ->
                val isSelected = selectedSubTab == index
                Tab(
                    selected = isSelected,
                    onClick = { onSelectSubTab(index) },
                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp),
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) DrgGrabGreenContainer else Color.Transparent)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                modifier = Modifier.size(15.dp),
                                tint = if (isSelected) DrgGrabGreenDark else DrgTextMuted
                            )
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) DrgGrabGreenDark else DrgTextSecondary
                            )
                        }
                    }
                )
            }
        }
    }
}
