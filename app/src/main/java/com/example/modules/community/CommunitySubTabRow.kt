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

    Surface(
        color = DrgSurface,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedSubTab,
            edgePadding = 12.dp,
            containerColor = DrgSurface,
            contentColor = Color.Transparent, // Hide default line indicator to use our pill background instead
            indicator = {}, // No default line indicator
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
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) DrgAmberContainer else Color.Transparent)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                modifier = Modifier.size(15.dp),
                                tint = if (isSelected) DrgAmberDark else DrgTextMuted
                            )
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) DrgAmberDark else DrgTextSecondary
                            )
                        }
                    }
                )
            }
        }
    }
}
