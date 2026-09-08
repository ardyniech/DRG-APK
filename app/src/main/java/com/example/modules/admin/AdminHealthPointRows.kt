package com.example.modules.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun HealthPointRow(status: String, title: String, desc: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
            Text(status, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgGreenDark)
        }
        Text(desc, fontSize = 11.sp, color = DrgTextSecondary)
    }
}

@Composable
fun ImprovementStepRow(num: String, title: String, desc: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Box(
            modifier = Modifier.size(20.dp).background(DrgGreenContainer, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(num, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgGreenPrimary)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
            Text(desc, fontSize = 11.sp, color = DrgTextSecondary)
        }
    }
}
