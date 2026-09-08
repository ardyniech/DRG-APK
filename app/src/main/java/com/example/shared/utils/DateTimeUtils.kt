package com.example.shared.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {

    fun formatCurrentTimeHHmm(): String {
        return try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
            } else {
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            }
        } catch (e: Exception) {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        }
    }

    fun formatCurrentDateTimeReadable(): String {
        return try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))
            } else {
                SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
            }
        } catch (e: Exception) {
            SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
        }
    }
}
