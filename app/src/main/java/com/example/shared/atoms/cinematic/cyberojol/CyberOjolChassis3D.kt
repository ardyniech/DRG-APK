package com.example.shared.atoms.cinematic.cyberojol

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DrgGrabGreenDark
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgHeadlightWarm
import com.example.ui.theme.DrgRedPanic

fun buildChassis3DParts(): List<CyberOjolRenderPart> = listOf(
    // 1. Rear Wheel Assembly
    CyberOjolRenderPart("rear_wheel", Vec3D(0f, 65f, -130f), priority = 1) { pt, _, _ ->
        val r = 32f * pt.scaleFactor
        drawCircle(color = Color(0xFF11171A), center = Offset(pt.screenX, pt.screenY), radius = r)
        drawCircle(color = Color(0xFF475569), center = Offset(pt.screenX, pt.screenY), radius = r * 0.52f)
        drawCircle(color = Color(0xFF1E293B), center = Offset(pt.screenX, pt.screenY), radius = r * 0.22f)
    },

    // 2. Front Wheel Assembly
    CyberOjolRenderPart("front_wheel", Vec3D(0f, 65f, 130f), priority = 1) { pt, _, _ ->
        val r = 32f * pt.scaleFactor
        drawCircle(color = Color(0xFF11171A), center = Offset(pt.screenX, pt.screenY), radius = r)
        drawCircle(color = Color(0xFF475569), center = Offset(pt.screenX, pt.screenY), radius = r * 0.52f)
        drawCircle(color = Color(0xFF1E293B), center = Offset(pt.screenX, pt.screenY), radius = r * 0.22f)
    },

    // 3. Engine Block & Transmission Case
    CyberOjolRenderPart("engine_block", Vec3D(0f, 32f, 0f), priority = 2) { pt, _, _ ->
        val s = 48f * pt.scaleFactor
        drawRoundRect(
            color = Color(0xFF1E293B),
            topLeft = Offset(pt.screenX - s * 0.6f, pt.screenY - s * 0.4f),
            size = Size(s * 1.2f, s * 0.8f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(s * 0.2f, s * 0.2f)
        )
    },

    // 4. Exhaust Canister (Right side)
    CyberOjolRenderPart("exhaust_pipe", Vec3D(36f, 42f, -85f), priority = 3) { pt, _, _ ->
        val w = 55f * pt.scaleFactor
        val h = 14f * pt.scaleFactor
        drawRoundRect(
            color = Color(0xFF64748B),
            topLeft = Offset(pt.screenX - w * 0.5f, pt.screenY - h * 0.5f),
            size = Size(w, h),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(h * 0.4f, h * 0.4f)
        )
    },

    // 5. Fairing & Fuel Tank (DRG Cyber Green)
    CyberOjolRenderPart("fuel_tank_fairing", Vec3D(0f, -8f, 25f), priority = 4) { pt, _, _ ->
        val w = 68f * pt.scaleFactor
        val h = 34f * pt.scaleFactor
        drawRoundRect(
            brush = Brush.horizontalGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark)),
            topLeft = Offset(pt.screenX - w * 0.5f, pt.screenY - h * 0.5f),
            size = Size(w, h),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.25f, h * 0.35f)
        )
    },

    // 6. Dual Projector Headlights
    CyberOjolRenderPart("headlights", Vec3D(0f, 2f, 135f), priority = 5) { pt, _, _ ->
        val r = 8f * pt.scaleFactor
        drawCircle(color = DrgHeadlightWarm, center = Offset(pt.screenX - r * 1.6f, pt.screenY), radius = r)
        drawCircle(color = DrgHeadlightWarm, center = Offset(pt.screenX + r * 1.6f, pt.screenY), radius = r)
        drawCircle(color = Color.White, center = Offset(pt.screenX - r * 1.6f, pt.screenY), radius = r * 0.5f)
        drawCircle(color = Color.White, center = Offset(pt.screenX + r * 1.6f, pt.screenY), radius = r * 0.5f)
    },

    // 7. Taillight Bar
    CyberOjolRenderPart("taillight", Vec3D(0f, 6f, -135f), priority = 5) { pt, _, _ ->
        val w = 36f * pt.scaleFactor
        val h = 10f * pt.scaleFactor
        drawRoundRect(
            color = DrgRedPanic,
            topLeft = Offset(pt.screenX - w * 0.5f, pt.screenY - h * 0.5f),
            size = Size(w, h),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(h * 0.3f, h * 0.3f)
        )
    }
)
