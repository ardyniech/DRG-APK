package com.example.shared.atoms.cinematic.cyberojol

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DrgGrabGreenDark
import com.example.ui.theme.DrgGrabGreenPrimary

fun buildRider3DParts(): List<CyberOjolRenderPart> = listOf(
    // 1. Dual Seat
    CyberOjolRenderPart("rider_seat", Vec3D(0f, -6f, -45f), priority = 6) { pt, _, _ ->
        val w = 52f * pt.scaleFactor
        val h = 16f * pt.scaleFactor
        drawRoundRect(
            color = Color(0xFF0F172A),
            topLeft = Offset(pt.screenX - w * 0.5f, pt.screenY - h * 0.5f),
            size = Size(w, h),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(h * 0.4f, h * 0.4f)
        )
    },

    // 2. Rider Torso (Iconic DRG Green Flight Jacket)
    CyberOjolRenderPart("rider_torso", Vec3D(0f, -48f, -10f), priority = 7) { pt, _, _ ->
        val w = 62f * pt.scaleFactor
        val h = 58f * pt.scaleFactor
        drawRoundRect(
            brush = Brush.verticalGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark)),
            topLeft = Offset(pt.screenX - w * 0.5f, pt.screenY - h * 0.5f),
            size = Size(w, h),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.3f, h * 0.3f)
        )
        // Silver Reflective Safety Harness
        drawRect(
            color = Color(0xFFE2E8F0),
            topLeft = Offset(pt.screenX - w * 0.35f, pt.screenY - h * 0.1f),
            size = Size(w * 0.7f, 5f * pt.scaleFactor)
        )
    },

    // 3. Handlebars & Forward-Reaching Arms
    CyberOjolRenderPart("handlebars_arms", Vec3D(0f, -32f, 45f), priority = 8) { pt, _, _ ->
        val w = 72f * pt.scaleFactor
        val h = 12f * pt.scaleFactor
        drawRoundRect(
            color = DrgGrabGreenPrimary,
            topLeft = Offset(pt.screenX - w * 0.5f, pt.screenY - h * 0.5f),
            size = Size(w, h),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(h * 0.4f, h * 0.4f)
        )
    },

    // 4. Aerodynamic Helmet & Iridescent Visor
    CyberOjolRenderPart("rider_helmet", Vec3D(0f, -92f, 10f), priority = 9) { pt, _, _ ->
        val r = 24f * pt.scaleFactor
        drawCircle(color = Color(0xFF1E293B), center = Offset(pt.screenX, pt.screenY), radius = r)

        // Tinted Visor Facet
        val vw = 26f * pt.scaleFactor
        val vh = 12f * pt.scaleFactor
        drawRoundRect(
            color = Color(0xFF0F172A),
            topLeft = Offset(pt.screenX - vw * 0.5f, pt.screenY - vh * 0.4f),
            size = Size(vw, vh),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(vh * 0.3f, vh * 0.3f)
        )
        // Visor Glint Reflection
        drawRect(
            color = Color.White.copy(alpha = 0.65f),
            topLeft = Offset(pt.screenX - vw * 0.3f, pt.screenY - vh * 0.3f),
            size = Size(vw * 0.35f, 3f * pt.scaleFactor)
        )
    }
)
