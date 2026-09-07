package com.example.modules.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun VehicleInfoCard(
    member: DriverMember?,
    onEditClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DrgSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DrgGreenContainer, RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Detail Kendaraan & Kontak",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = DrgTextPrimary
            )
            Text(
                text = "Motor: ${member?.motorcycleModel ?: "-"}",
                fontSize = 13.sp,
                color = DrgTextSecondary
            )
            Text(
                text = "Nomor Plat: ${member?.motorcyclePlate ?: "-"}",
                fontSize = 13.sp,
                color = DrgTextSecondary
            )
            Text(
                text = "Nomor Telepon: ${member?.phone ?: "-"}",
                fontSize = 13.sp,
                color = DrgTextSecondary
            )

            Button(
                onClick = onEditClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DrgGreenPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Perbarui Data Profil & Kendaraan",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
