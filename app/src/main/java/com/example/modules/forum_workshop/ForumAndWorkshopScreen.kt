package com.example.modules.forum_workshop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.*
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgTextDark

@Composable
fun ForumAndWorkshopScreen(
    posts: List<ForumPost>,
    workshops: List<WorkshopPartner>,
    currentMemberId: String = "DRG-001",
    currentMember: DriverMember? = null,
    onCreatePost: (String, String, ForumCategory, PostType) -> Unit,
    onToggleLike: (String, Boolean) -> Unit,
    onDeletePost: (String) -> Unit = {},
    commentsMap: Map<String, List<ForumComment>> = emptyMap(),
    onAddComment: (String, String) -> Unit = { _, _ -> },
    onDeleteComment: (String) -> Unit = {},
    onCallWorkshop: (String) -> Unit,
    onRateWorkshop: (WorkshopPartner, Float, String, String) -> Unit = { _, _, _, _ -> },
    onBookingWorkshop: (WorkshopPartner, String) -> Unit = { _, _ -> },
    reviewsMap: Map<String, List<DriverReview>> = emptyMap(),
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    val accent = theme.primaryColor
    var selectedSubTab by remember { mutableIntStateOf(0) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var dialogInitialType by remember { mutableStateOf(PostType.UPDATE) }

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 14.dp)) {
        DrgLuminousGlassCard(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            shape = RoundedCornerShape(18.dp),
            backgroundColor = theme.glassBg,
            borderHighlightColor = theme.glassBorderColor,
            contentPadding = 4.dp
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val tabs = listOf(
                    "Forum Diskusi (${posts.size})" to Icons.Default.Forum,
                    "Bengkel Rekanan (${workshops.size})" to Icons.Default.Build
                )
                tabs.forEachIndexed { idx, (label, icon) ->
                    val active = selectedSubTab == idx
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (active) accent else Color.White.copy(alpha = 0.65f))
                            .border(1.dp, if (active) accent else Color.White, RoundedCornerShape(14.dp))
                            .bouncyClick { selectedSubTab = idx }
                            .padding(vertical = 9.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(icon, contentDescription = label, tint = if (active) Color.White else DrgTextDark, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else DrgTextDark)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedSubTab == 0) {
            ForumSection(
                posts = posts, currentMemberId = currentMemberId,
                onCreatePostClick = { type -> dialogInitialType = type; showCreateDialog = true },
                onToggleLike = onToggleLike, onDeletePost = onDeletePost,
                commentsMap = commentsMap, onAddComment = onAddComment, onDeleteComment = onDeleteComment
            )
        } else {
            WorkshopSection(
                workshops = workshops, onCallWorkshop = onCallWorkshop, currentMember = currentMember,
                onRateWorkshop = onRateWorkshop, onBookingWorkshop = onBookingWorkshop, reviewsMap = reviewsMap
            )
        }

        if (showCreateDialog) {
            CreatePostDialog(
                initialPostType = dialogInitialType,
                onDismiss = { showCreateDialog = false },
                onConfirm = { title, content, cat, type -> onCreatePost(title, content, cat, type) }
            )
        }
    }
}
