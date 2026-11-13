package com.example.modules.forum_workshop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.LocalAppVisualTheme
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
    val theme = LocalAppVisualTheme.current
    var replyText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(theme.pillBg.copy(alpha = 0.35f))
            .border(0.8.dp, theme.glassBorderColor.copy(alpha = 0.55f), RoundedCornerShape(14.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Balasan & Diskusi (${comments.size})",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = DrgTextDark
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
                modifier = Modifier.weight(1f).testTag("forum_comment_input"),
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color(0xEBFFFFFF),
                    focusedBorderColor = theme.primaryColor,
                    unfocusedBorderColor = theme.glassBorderColor.copy(alpha = 0.6f)
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
                    .size(44.dp)
                    .background(if (replyText.isNotBlank()) theme.primaryColor else DrgOutline, CircleShape)
                    .testTag("forum_comment_send_btn")
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Kirim Balasan", tint = Color.White, modifier = Modifier.size(17.dp))
            }
        }
    }
}
