package com.example.modules.forum_workshop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ForumEmptyState(
    isFiltered: Boolean,
    onCreatePostClick: () -> Unit,
    onAskQuestionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        com.example.shared.atoms.EmptyStateOrganicGraphic(
            title = if (isFiltered) "Tidak ada postingan yang sesuai filter" else "Belum Ada Diskusi atau Pertanyaan",
            message = if (isFiltered) "Coba ganti kata kunci pencarian atau pilih kategori lain."
                      else "Jadilah yang pertama berbagi info jalur terkini atau tanyakan kendala motor ke rekan ojol!"
        )
        
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = onAskQuestionClick,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.HelpOutline, contentDescription = "Tanya", modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tanya Driver", fontSize = 11.sp)
            }
            Button(
                onClick = onCreatePostClick,
                colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Campaign, contentDescription = "Update", modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Bagikan Update", fontSize = 11.sp)
            }
        }
    }
}
