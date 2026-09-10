package com.example.modules.forum_workshop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.*
import com.example.ui.theme.DrgBackground
import com.example.ui.theme.DrgGreenPrimary

@Composable
fun ForumAndWorkshopScreen(
    posts: List<ForumPost>,
    workshops: List<WorkshopPartner>,
    currentMemberId: String = "DRG-001",
    onCreatePost: (String, String, ForumCategory, PostType) -> Unit,
    onToggleLike: (String, Boolean) -> Unit,
    onDeletePost: (String) -> Unit = {},
    commentsMap: Map<String, List<ForumComment>> = emptyMap(),
    onAddComment: (String, String) -> Unit = { _, _ -> },
    onDeleteComment: (String) -> Unit = {},
    onCallWorkshop: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var dialogInitialType by remember { mutableStateOf(PostType.UPDATE) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(horizontal = 16.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = DrgBackground,
            contentColor = DrgGreenPrimary,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = { Text("Forum Diskusi Internal (${posts.size})", fontSize = 12.sp) },
                icon = { Icon(Icons.Default.Forum, contentDescription = "Forum", modifier = Modifier.size(16.dp)) }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = { Text("Bengkel Rekanan (${workshops.size})", fontSize = 12.sp) },
                icon = { Icon(Icons.Default.Build, contentDescription = "Bengkel", modifier = Modifier.size(16.dp)) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedSubTab == 0) {
            ForumSection(
                posts = posts,
                currentMemberId = currentMemberId,
                onCreatePostClick = { type ->
                    dialogInitialType = type
                    showCreateDialog = true
                },
                onToggleLike = onToggleLike,
                onDeletePost = onDeletePost,
                commentsMap = commentsMap,
                onAddComment = onAddComment,
                onDeleteComment = onDeleteComment
            )
        } else {
            WorkshopSection(
                workshops = workshops,
                onCallWorkshop = onCallWorkshop
            )
        }

        if (showCreateDialog) {
            CreatePostDialog(
                initialPostType = dialogInitialType,
                onDismiss = { showCreateDialog = false },
                onConfirm = { title, content, cat, type ->
                    onCreatePost(title, content, cat, type)
                }
            )
        }
    }
}
