package com.example.modules.forum_workshop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.ForumComment
import com.example.ui.theme.*

@Composable
fun ForumCommentSection(
    comments: List<ForumComment>,
    currentMemberId: String,
    onAddComment: (String) -> Unit,
    onDeleteComment: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var replyText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DrgSurfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Balasan & Diskusi (${comments.size})",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = DrgTextPrimary
        )

        if (comments.isEmpty()) {
            Text(
                text = "Belum ada balasan. Jadilah yang pertama menanggapi!",
                fontSize = 11.sp,
                color = DrgTextMuted,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        } else {
            comments.forEach { comment ->
                ForumCommentItemRow(
                    comment = comment,
                    canDelete = comment.authorId == currentMemberId,
                    onDelete = { onDeleteComment(comment.id) }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OutlinedTextField(
                value = replyText,
                onValueChange = { replyText = it },
                placeholder = { Text("Tulis balasan atau saran...", fontSize = 11.sp) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("forum_comment_input"),
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            IconButton(
                onClick = {
                    if (replyText.isNotBlank()) {
                        onAddComment(replyText.trim())
                        replyText = ""
                    }
                },
                enabled = replyText.isNotBlank(),
                modifier = Modifier
                    .size(40.dp)
                    .background(if (replyText.isNotBlank()) DrgGreenPrimary else DrgOutline, CircleShape)
                    .testTag("forum_comment_send_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Kirim Balasan",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ForumCommentItemRow(
    comment: ForumComment,
    canDelete: Boolean,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = comment.authorName, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                Text(text = "• ${comment.authorRole.shortName}", fontSize = 9.sp, color = DrgGreenPrimary)
                Text(text = "• ${comment.timeAgo}", fontSize = 9.sp, color = DrgTextMuted)
            }
            Text(text = comment.content, fontSize = 11.sp, color = DrgTextSecondary)
        }
        if (canDelete) {
            IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Hapus", tint = DrgRedDanger, modifier = Modifier.size(14.dp))
            }
        }
    }
}
