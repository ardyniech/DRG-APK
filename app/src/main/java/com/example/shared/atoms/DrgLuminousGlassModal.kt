package com.example.shared.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*

/**
 * Standardized 1-DNA Luminous Frosted Glass Modal Chrome used by every record form and dialog.
 */
@Composable
fun DrgLuminousGlassModal(
    title: String,
    subtitle: String? = null,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    accentColor: Color? = null,
    maxHeightFraction: Float = 0.88f,
    scrollable: Boolean = true,
    validationError: String? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val theme = LocalAppVisualTheme.current
    val resolvedAccent = accentColor ?: theme.primaryColor
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0x660F172A)).padding(horizontal = 16.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            DrgLuminousGlassCard(
                modifier = modifier.fillMaxWidth().widthIn(max = 540.dp).heightIn(max = 680.dp).wrapContentHeight(),
                shape = RoundedCornerShape(26.dp),
                backgroundColor = Color(0xF5FFFFFF),
                borderHighlightColor = accentColor?.copy(alpha = 0.72f) ?: theme.glassBorderColor,
                contentPadding = 18.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            if (icon != null) {
                                Box(
                                    Modifier.size(36.dp).clip(CircleShape).background(resolvedAccent.copy(alpha = 0.14f)).border(1.dp, resolvedAccent.copy(alpha = 0.35f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) { Icon(icon, null, tint = resolvedAccent, modifier = Modifier.size(19.dp)) }
                            }
                            Column(Modifier.weight(1f)) {
                                Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                                if (!subtitle.isNullOrBlank()) Text(subtitle, fontSize = 11.sp, color = DrgTextSecondary)
                            }
                        }
                        IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp).testTag("modal_close_button")) {
                            Icon(Icons.Default.Close, contentDescription = "Tutup", tint = DrgTextDark, modifier = Modifier.size(20.dp))
                        }
                    }
                    HorizontalDivider(Modifier.padding(vertical = 8.dp), color = theme.glassBorderColor.copy(alpha = 0.45f))
                    val bodyMod = Modifier.fillMaxWidth().weight(1f, fill = false).let { if (scrollable) it.verticalScroll(rememberScrollState()) else it }
                    Column(modifier = bodyMod, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (!validationError.isNullOrBlank()) DrgFormValidationBanner(message = validationError)
                        content()
                    }
                    if (footer != null) {
                        Spacer(Modifier.height(10.dp))
                        Column(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp), content = footer)
                    }
                }
            }
        }
    }
}

@Composable
fun DrgFormValidationBanner(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = DrgRedPanic,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DrgRedPanic.copy(alpha = 0.12f))
            .border(1.dp, DrgRedPanic.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp)
            .testTag("form_validation_error")
    )
}
