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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
            .padding(vertical = 32.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Campaign,
            contentDescription = "Forum Kosong",
            tint = DrgGreenPrimary.copy(alpha = 0.5f),
            modifier = Modifier.size(54.dp)
        )

        Text(
            text = if (isFiltered) "Tidak ada postingan yang sesuai filter" else "Belum Ada Diskusi atau Pertanyaan",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = DrgTextPrimary,
            textAlign = TextAlign.Center
        )

        Text(
            text = if (isFiltered) "Coba ganti kata kunci pencarian atau pilih kategori lain."
            else "Jadilah yang pertama berbagi info jalur terkini atau tanyakan kendala motor ke rekan ojol!",
            fontSize = 12.sp,
            color = DrgTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
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
