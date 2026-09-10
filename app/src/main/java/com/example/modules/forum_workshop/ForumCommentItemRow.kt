package com.example.modules.forum_workshop

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.ForumComment
import com.example.ui.theme.*

@Composable
fun ForumCommentItemRow(
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
