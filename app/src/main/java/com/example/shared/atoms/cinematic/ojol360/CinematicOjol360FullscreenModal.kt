package com.example.shared.atoms.cinematic.ojol360

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGlassSurfaceStrong
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark

@Composable
fun CinematicOjol360FullscreenModal(
    state: CinematicOjol360State,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = state.isFullscreen,
        enter = fadeIn() + scaleIn(initialScale = 0.88f),
        exit = fadeOut() + scaleOut(targetScale = 0.88f),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .testTag("cinematic_ojol_fullscreen_modal")
        ) {
            CinematicOjol360Viewport(
                state = state,
                modifier = Modifier.fillMaxSize(),
                showHud = true
            )

            // Top exit button
            IconButton(
                onClick = { state.setFullscreenMode(false) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 20.dp)
                    .background(DrgGlassSurfaceStrong, RoundedCornerShape(12.dp))
                    .testTag("btn_close_fullscreen")
            ) {
                Icon(Icons.Default.Close, contentDescription = "Tutup Fullscreen", tint = DrgTextDark)
            }

            // Bottom Floating Quick Auth Action
            Button(
                onClick = {
                    state.setFullscreenMode(false)
                    onLoginClick()
                },
                colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp)
                    .height(48.dp)
                    .testTag("btn_fullscreen_quick_login")
            ) {
                Icon(Icons.Default.Login, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Mulai Narik • Masuk Driver", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
            }
        }
    }
}
