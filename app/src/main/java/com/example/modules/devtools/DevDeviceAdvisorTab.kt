package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DevDeviceAdvisorTab() {
    val context = LocalContext.current
    val adviceList = remember { DeviceAdvisorEngine.evaluateDevice(context) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = DrgGreenPrimary)
                        Text("Saran Optimasi Perangkat Pengemudi", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                    }
                    Text("Auto-analisis kondisi fisik HP driver (RAM, baterai, suhu, background whitelist) untuk efisiensi maksimum di jalan.", fontSize = 11.sp, color = DrgTextSecondary)
                }
            }
        }

        if (adviceList.isEmpty()) {
            item {
                Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFE8F5E9), modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("✅ Perangkat pengemudi dalam kondisi optimal (RAM lapang, baterai aman, background siap).", fontSize = 11.sp, color = Color(0xFF1B5E20), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        } else {
            items(adviceList) { item ->
                Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(item.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (item.isCritical) Color(0xFFC62828) else DrgTextPrimary, modifier = Modifier.weight(1f))
                            Surface(shape = RoundedCornerShape(6.dp), color = if (item.isCritical) Color(0xFFFFEBEE) else Color(0xFFFFF8E1)) {
                                Text(item.priority, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (item.isCritical) Color(0xFFC62828) else Color(0xFFF57F17), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("📊 Kondisi Riil: ${item.currentDeviceMetric}", fontSize = 10.sp, color = Color(0xFF0369A1), fontWeight = FontWeight.SemiBold)
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFF0FDF4)) {
                            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.TipsAndUpdates, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(16.dp).padding(top = 1.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("💡 Solusi Praktis Ojol: ${item.practicalOjolAdvice}", fontSize = 10.sp, color = DrgTextPrimary, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}
