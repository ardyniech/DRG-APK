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
    private var isApplying = false
    private var lastAppliedDateMemory = ""

    fun applyCommunityGpsPolicy(cur: DriverMember?) {
        if (cur == null || isApplying) return
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (currentHour in 6..17) {
            val todayStr = DateTimeUtils.formatCurrentDateTimeReadable().take(11)
            val lastDate = sharedPrefs?.getString("last_gps_auto_trigger_date", "") ?: ""
            if (lastDate != todayStr && lastAppliedDateMemory != todayStr && !cur.isLocationSharingConsent) {
                isApplying = true
                lastAppliedDateMemory = todayStr
                scope.launch {
                    runCatching {
                        repository.updateMember(cur.copy(isLocationSharingConsent = true))
                        sharedPrefs?.edit()?.putString("last_gps_auto_trigger_date", todayStr)?.commit()
                    }.onSuccess {
                        showToast("GPS Live diaktifkan otomatis (06.00 - 18.00)")
                    }.onFailure { error ->
                        showToast("Gagal menerapkan kebijakan GPS: ${error.localizedMessage}")
                    }
                    isApplying = false
                }
            }
        }
    }
}
