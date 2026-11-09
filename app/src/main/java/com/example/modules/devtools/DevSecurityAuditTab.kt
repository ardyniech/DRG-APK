package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
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
fun DevSecurityAuditTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    val context = LocalContext.current
    val auditItems = remember { SecurityAuditEngine.audit(context) }
    val allSecure = auditItems.all { it.isSecure }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
        if (headerContent != null) {
            item { headerContent() }
        }
        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = DrgGreenPrimary)
                        Text("Audit Keamanan Sistem & Enkripsi Data", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                    }
                    Text("Auto-audit integritas root OS, TLS 1.3 network security config, Android KeyStore, dan privasi koordinat SOS.", fontSize = 11.sp, color = DrgTextSecondary)
                }
            }
        }

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = if (allSecure) Color(0xFFE8F5E9) else Color(0xFFFFEBEE), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(if (allSecure) Icons.Default.CheckCircle else Icons.Default.Warning, contentDescription = null, tint = if (allSecure) Color(0xFF2E7D32) else Color.Red, modifier = Modifier.size(24.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(if (allSecure) "Status Keamanan: TINGGI & MEMENUHI STANDAR" else "Status Keamanan: PERINGATAN INTEGRITAS", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (allSecure) Color(0xFF1B5E20) else Color(0xFFC62828))
                        Text(if (allSecure) "Zero cleartext HTTP leaks, sandboxing SQLite Room aktif, PIN hashing salted." else "Ditemukan potensi celah keamanan lingkungan runtime.", fontSize = 10.sp, color = if (allSecure) Color(0xFF2E7D32) else Color(0xFFB71C1C))
                    }
                }
            }
        }

        items(auditItems) { item ->
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                            Text("Kategori: ${item.category}", fontSize = 10.sp, color = DrgTextSecondary)
                        }
                        Surface(shape = RoundedCornerShape(6.dp), color = if (item.isSecure) DrgGreenLight else Color(0xFFFFEBEE)) {
                            Text(if (item.isSecure) "MEMENUHI" else "PERINGATAN", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (item.isSecure) DrgGreenPrimary else Color.Red, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text("🛡️ ${item.statusText}", fontSize = 10.sp, color = if (item.isSecure) DrgTextPrimary else Color.Red, fontWeight = FontWeight.SemiBold)
                    item.riskDetail?.let { risk ->
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFFEBEE)) {
                            Text("⚠️ Risiko: $risk", fontSize = 10.sp, color = Color(0xFFC62828), modifier = Modifier.padding(6.dp))
                        }
                    }
                }
            }
        }
    }
}
