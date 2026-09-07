package com.example.modules.forum_workshop

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.shared.models.ForumCategory
import com.example.shared.models.PostType
import com.example.ui.theme.*

@Composable
fun CreatePostDialog(
    initialPostType: PostType = PostType.UPDATE,
    onDismiss: () -> Unit,
    onConfirm: (String, String, ForumCategory, PostType) -> Unit
) {
    var postType by remember { mutableStateOf(initialPostType) }
    var selectedCat by remember {
        mutableStateOf(if (initialPostType == PostType.QUESTION) ForumCategory.TANYA_JAWAB else ForumCategory.INFO_JALUR)
    }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DrgSurface,
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = if (postType == PostType.QUESTION) "Ajukan Pertanyaan ke Rekan DRG" else "Bagikan Update / Info Jalur DRG",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgTextPrimary
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = postType == PostType.UPDATE,
                        onClick = {
                            postType = PostType.UPDATE
                            if (selectedCat == ForumCategory.TANYA_JAWAB) selectedCat = ForumCategory.INFO_JALUR
                        },
                        label = { Text("📢 Buat Update", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = postType == PostType.QUESTION,
                        onClick = {
                            postType = PostType.QUESTION
                            selectedCat = ForumCategory.TANYA_JAWAB
                        },
                        label = { Text("❓ Tanya Rekan", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ForumCategory.entries.forEach { cat ->
                        FilterChip(
                            selected = selectedCat == cat,
                            onClick = { selectedCat = cat },
                            label = { Text(cat.label, fontSize = 10.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (postType == PostType.QUESTION) "Judul Pertanyaan" else "Judul Info / Tips", fontSize = 11.sp) },
                    placeholder = { Text(if (postType == PostType.QUESTION) "Contoh: Mengapa tarikan motor berat pas tanjakan?" else "Contoh: Info banjir di Jl. Galunggung", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("create_post_title_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Detail Penjelasan Lengkap", fontSize = 11.sp) },
                    placeholder = { Text("Jelaskan secara rinci agar rekan driver lain mudah memahami...", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("create_post_content_input"),
                    maxLines = 4
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f).testTag("create_post_cancel_button")) {
                        Text("Batal", fontSize = 12.sp)
                    }
                    Button(
                        onClick = {
                            if (title.isNotBlank() && content.isNotBlank()) {
                                onConfirm(title.trim(), content.trim(), selectedCat, postType)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                        modifier = Modifier.weight(1f).testTag("create_post_submit_button")
                    ) {
                        Text("Kirim Post", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
