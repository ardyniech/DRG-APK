package com.example.modules.auth

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.firebase.GoogleAuthManager
import com.example.core.firebase.GoogleAuthOutcome
import com.example.shared.atoms.LocalAppVisualTheme
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

@Composable
fun GoogleSignInButton(
    onSignedIn: (FirebaseUser) -> Unit,
    onAuthError: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Masuk dengan Google (Terverifikasi)"
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val theme = LocalAppVisualTheme.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Button(
        onClick = {
            if (isLoading) return@Button
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            val activity = context.findActivity()
            if (activity == null) {
                onAuthError("Activity tidak tersedia untuk menampilkan dialog Google Sign-In.")
                return@Button
            }
            isLoading = true
            scope.launch {
                when (val outcome = GoogleAuthManager.signInWithGoogle(activity)) {
                    is GoogleAuthOutcome.Success -> {
                        isLoading = false
                        onSignedIn(outcome.user)
                    }
                    GoogleAuthOutcome.Cancelled -> {
                        isLoading = false
                    }
                    is GoogleAuthOutcome.Error -> {
                        isLoading = false
                        onAuthError(outcome.message)
                    }
                }
            }
        },
        enabled = !isLoading,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp)
            .testTag("google_sign_in_button")
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
            Spacer(Modifier.width(10.dp))
            Text("Menghubungkan Akun Google...", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        } else {
            Icon(Icons.Default.AccountCircle, contentDescription = "Google Sign-In", modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
