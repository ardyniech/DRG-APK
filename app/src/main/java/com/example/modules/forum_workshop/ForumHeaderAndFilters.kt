package com.example.modules.forum_workshop

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.ForumCategory
import com.example.shared.models.PostType
import com.example.ui.theme.*

@Composable
fun ForumHeaderAndFilters(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: ForumCategory?,
    onSelectCategory: (ForumCategory?) -> Unit,
    selectedPostType: PostType?,
    onSelectPostType: (PostType?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Cari update jalur atau pertanyaan...", fontSize = 12.sp, color = DrgTextMuted) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Cari", tint = DrgTextMuted, modifier = Modifier.size(18.dp)) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Hapus Pencarian", tint = DrgTextMuted, modifier = Modifier.size(18.dp))
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DrgSurface,
                unfocusedContainerColor = DrgSurface,
                focusedBorderColor = DrgGreenPrimary,
                unfocusedBorderColor = DrgOutline
            ),
            modifier = Modifier.fillMaxWidth().testTag("forum_search_input")
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = selectedCategory == null && selectedPostType == null,
                onClick = {
                    onSelectCategory(null)
                    onSelectPostType(null)
                },
                label = { Text("Semua", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DrgGreenPrimary.copy(alpha = 0.15f),
                    selectedLabelColor = DrgGreenPrimary
                )
            )

            FilterChip(
                selected = selectedPostType == PostType.QUESTION,
                onClick = {
                    onSelectPostType(if (selectedPostType == PostType.QUESTION) null else PostType.QUESTION)
                },
                label = { Text("❓ Tanya Rekan", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DrgAmberWarning.copy(alpha = 0.2f),
                    selectedLabelColor = DrgAmberWarning
                )
            )

            FilterChip(
                selected = selectedPostType == PostType.UPDATE,
                onClick = {
                    onSelectPostType(if (selectedPostType == PostType.UPDATE) null else PostType.UPDATE)
                },
                label = { Text("📢 Update Terkini", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DrgBlueInfo.copy(alpha = 0.18f),
                    selectedLabelColor = DrgBlueInfo
                )
            )

            ForumCategory.entries.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        onSelectCategory(if (selectedCategory == category) null else category)
                    },
                    label = { Text(category.label, fontSize = 11.sp) }
                )
            }
        }
    }
}
