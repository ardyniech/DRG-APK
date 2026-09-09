package com.example.core.viewmodel.coordinators

import android.content.SharedPreferences
import com.example.core.repository.DRGRepository
import com.example.shared.models.DriverMember
import com.example.shared.utils.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

class GpsPolicyHelper(
    private val repository: DRGRepository,
    private val sharedPrefs: SharedPreferences?,
    private val scope: CoroutineScope,
    private val showToast: (String) -> Unit
) {
    fun applyCommunityGpsPolicy(cur: DriverMember?) {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (currentHour in 6..17) {
            val todayStr = DateTimeUtils.formatCurrentDateTimeReadable().take(11)
            val lastDate = sharedPrefs?.getString("last_gps_auto_trigger_date", "") ?: ""
            if (lastDate != todayStr && cur != null && !cur.isLocationSharingConsent) {
                scope.launch {
                    runCatching {
                        repository.updateMember(cur.copy(isLocationSharingConsent = true))
                        sharedPrefs?.edit()?.putString("last_gps_auto_trigger_date", todayStr)?.apply()
                    }.onSuccess {
                        showToast("GPS Live diaktifkan otomatis (06.00 - 18.00)")
                    }.onFailure { error ->
                        showToast("Gagal menerapkan kebijakan GPS: ${error.localizedMessage}")
                    }
                }
            }
        }
    }
}
