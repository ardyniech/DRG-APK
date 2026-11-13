package com.example.modules.forum_workshop

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.ForumCategory
import com.example.shared.models.PostType

@Composable
fun CreatePostDialog(
    initialPostType: PostType = PostType.UPDATE,
    onDismiss: () -> Unit,
    onConfirm: (String, String, ForumCategory, PostType) -> Unit
) {
    var postType by remember { mutableStateOf(initialPostType) }
    var selectedCat by remember { mutableStateOf(if (initialPostType == PostType.QUESTION) ForumCategory.TANYA_JAWAB else ForumCategory.INFO_JALUR) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassModal(
        title = if (postType == PostType.QUESTION) "Ajukan Pertanyaan ke Rekan DRG" else "Bagikan Update / Info Jalur DRG",
        subtitle = "Setiap kiriman langsung memicu notifikasi ke seluruh anggota",
        icon = Icons.Default.Forum,
        accentColor = theme.primaryColor,
        validationError = validationError,
        onDismiss = onDismiss,
        footer = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f).testTag("create_post_cancel_button"), shape = RoundedCornerShape(12.dp)) { Text("Batal", fontSize = 12.sp) }
                Button(
                    onClick = {
                        when {
                            title.isBlank() -> validationError = "Judul kiriman atau pertanyaan tidak boleh kosong."
                            content.isBlank() -> validationError = "Detail penjelasan wajib diisi agar rekan driver mudah memahami."
                            else -> { onConfirm(title.trim(), content.trim(), selectedCat, postType); onDismiss() }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).testTag("create_post_submit_button")
                ) { Text("Kirim Post", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = postType == PostType.UPDATE,
                onClick = { postType = PostType.UPDATE; if (selectedCat == ForumCategory.TANYA_JAWAB) selectedCat = ForumCategory.INFO_JALUR },
                label = { Text("📢 Buat Update", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = theme.primaryColor, selectedLabelColor = Color.White)
            )
            FilterChip(
                selected = postType == PostType.QUESTION,
                onClick = { postType = PostType.QUESTION; selectedCat = ForumCategory.TANYA_JAWAB },
                label = { Text("❓ Tanya Rekan", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = theme.primaryColor, selectedLabelColor = Color.White)
            )
        }
        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            ForumCategory.entries.forEach { cat ->
                FilterChip(
                    selected = selectedCat == cat, onClick = { selectedCat = cat },
                    label = { Text(cat.label, fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = theme.primaryColor, selectedLabelColor = Color.White)
                )
            }
        }
        OutlinedTextField(
            value = title, onValueChange = { title = it; validationError = null },
            label = { Text(if (postType == PostType.QUESTION) "Judul Pertanyaan" else "Judul Info / Tips", fontSize = 11.sp) },
            placeholder = { Text(if (postType == PostType.QUESTION) "Contoh: Mengapa tarikan motor berat pas tanjakan?" else "Contoh: Info banjir di Jl. Galunggung", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth().testTag("create_post_title_input"), singleLine = true, shape = RoundedCornerShape(12.dp)
        )
        OutlinedTextField(
            value = content, onValueChange = { content = it; validationError = null },
            label = { Text("Detail Penjelasan Lengkap", fontSize = 11.sp) },
            placeholder = { Text("Jelaskan secara rinci agar rekan driver lain mudah memahami...", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth().height(100.dp).testTag("create_post_content_input"), maxLines = 4, shape = RoundedCornerShape(12.dp)
        )
    }
}
