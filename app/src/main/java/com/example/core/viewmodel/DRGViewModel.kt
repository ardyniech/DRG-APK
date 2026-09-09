package com.example.core.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.battery.BatteryOptimizationManager
import com.example.core.cache.*
import com.example.core.location.BatteryAwareLocationSyncer
import com.example.core.repository.DRGRepository
import com.example.core.sync.BackgroundSyncEngine
import com.example.core.viewmodel.coordinators.*
import com.example.shared.models.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DRGViewModel(
    val repository: DRGRepository,
    val sharedPrefs: SharedPreferences?,
    application: Application
) : AndroidViewModel(application) {

    private val _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage: StateFlow<String?> = _snackBarMessage.asStateFlow()
    fun showToast(message: String) { _snackBarMessage.value = message }
    fun clearSnackBar() { _snackBarMessage.value = null }

    val batteryManager = BatteryOptimizationManager(sharedPrefs)
    val syncEngine = BackgroundSyncEngine(viewModelScope)

    internal val profileCoord = ProfileSessionCoordinator(repository, sharedPrefs, viewModelScope) { showToast(it) }
    internal val emergencyCoord = EmergencyCoordinator(repository, sharedPrefs, application, viewModelScope, syncEngine) { showToast(it) }
    internal val communityCoord = CommunityCoordinator(repository, viewModelScope, syncEngine) { showToast(it) }

    val members = repository.allMembers.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val allMembers: StateFlow<List<DriverMember>> get() = members
    internal val governanceCoord = GovernanceCoordinator(repository, viewModelScope, members) { showToast(it) }
    internal val radarCoord = RadarSafetyCoordinator(repository, sharedPrefs, viewModelScope, batteryManager, syncEngine) { showToast(it) }

    val currentMemberId = profileCoord.currentMemberId
    val isLoggedIn = profileCoord.isLoggedIn
    val currentMember = combine(members, currentMemberId) { list, id -> list.find { it.id == id } ?: list.firstOrNull() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _selectedTab = MutableStateFlow(MainNavTab.DASHBOARD)
    val selectedTab: StateFlow<MainNavTab> = _selectedTab.asStateFlow()
    val currentTab: StateFlow<MainNavTab> get() = selectedTab
    fun selectTab(tab: MainNavTab) { _selectedTab.value = tab }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    fun setSearchQuery(query: String) { _searchQuery.value = query }

    private val whileSubscribed = SharingStarted.WhileSubscribed(5000L)
    val activeAlerts = repository.activeAlerts.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val poskoList = repository.allPosko.stateIn(viewModelScope, whileSubscribed, emptyList())
    val notifications = repository.allNotifications.stateIn(viewModelScope, whileSubscribed, emptyList())
    val badges = repository.allBadges.stateIn(viewModelScope, whileSubscribed, emptyList())
    val tasks = repository.allTasks.stateIn(viewModelScope, whileSubscribed, emptyList())
    val rewards = repository.allRewards.stateIn(viewModelScope, whileSubscribed, emptyList())
    val hazards = repository.allHazards.stateIn(viewModelScope, whileSubscribed, emptyList())
    val pointTransactions = repository.allPointTransactions.stateIn(viewModelScope, whileSubscribed, emptyList())
    val posts = repository.allPosts.stateIn(viewModelScope, whileSubscribed, emptyList())
    val workshops = repository.allWorkshops.stateIn(viewModelScope, whileSubscribed, emptyList())
    val adminLogs = governanceCoord.adminLogs
    val isSosAlarmSoundEnabled = emergencyCoord.isSosAlarmSoundEnabled
    val isSosAlarmEnabled get() = isSosAlarmSoundEnabled
    val isCrashGuardEnabled = emergencyCoord.isCrashGuardEnabled
    val crashSensitivity = emergencyCoord.crashSensitivity
    val crashDetectedEvent = emergencyCoord.crashDetectedEvent
    val mapCachePolicy = radarCoord.mapCachePolicy
    val locationSyncProfile = radarCoord.locationSyncProfile
    val isPowerSaverMode = batteryManager.isPowerSaverMode
    val isDataSaverMode = batteryManager.isDataSaverMode
    val dataSavedBytes = batteryManager.savedDataBytes

    init {
        viewModelScope.launch {
            combine(currentMember, isLoggedIn) { member, loggedIn ->
                if (loggedIn) member else null
            }.filterNotNull().collect { member ->
                if (radarCoord.locationSyncer == null) {
                    radarCoord.locationSyncer = BatteryAwareLocationSyncer(application, viewModelScope) { lat, lng ->
                        repository.updateMemberLocation(member.id, lat, lng)
                    }.apply { setProfile(radarCoord.locationSyncProfile.value) }
                }
                radarCoord.applyCommunityGpsPolicy(member)
            }
        }

        viewModelScope.launch {
            var knownAlertIds = emptySet<String>()
            activeAlerts.collect { alerts ->
                val newAlerts = alerts.filter { it.id !in knownAlertIds }
                if (knownAlertIds.isNotEmpty() && newAlerts.isNotEmpty()) {
                    newAlerts.forEach { alert ->
                        com.example.shared.utils.NotificationService.sendSystemNotification(
                            context = application,
                            title = "🚨 SOS DARURAT: ${alert.driverName}",
                            message = "${alert.type.label}: ${alert.message} di ${alert.locationName}"
                        )
                    }
                }
                knownAlertIds = alerts.map { it.id }.toSet()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        emergencyCoord.onCleared()
        radarCoord.locationSyncer?.stopSync()
    }
}
