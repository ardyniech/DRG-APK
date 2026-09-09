package com.example.core.viewmodel

import android.app.Application
import android.content.Context
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

    private val profileCoord = ProfileSessionCoordinator(repository, sharedPrefs, viewModelScope) { showToast(it) }
    private val emergencyCoord = EmergencyCoordinator(repository, sharedPrefs, application, viewModelScope, syncEngine) { showToast(it) }
    private val communityCoord = CommunityCoordinator(repository, viewModelScope, syncEngine) { showToast(it) }

    val members = repository.allMembers.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val allMembers: StateFlow<List<DriverMember>> get() = members
    private val governanceCoord = GovernanceCoordinator(repository, viewModelScope, members) { showToast(it) }
    private val radarCoord = RadarSafetyCoordinator(repository, sharedPrefs, viewModelScope, batteryManager, syncEngine) { showToast(it) }

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

        // Real-Time System Notification Observer for Emergency SOS Alerts
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

    // Auth & Profile
    fun login(memberId: String) = profileCoord.login(memberId)
    fun logout() = profileCoord.logout()
    fun switchActiveMember(memberId: String) = profileCoord.switchActiveMember(memberId)
    fun switchRole(memberId: String) = switchActiveMember(memberId)
    fun registerNewDriver(name: String, phone: String, plate: String, motorcycle: String, area: String, pin: String) =
        profileCoord.registerNewDriver(name, phone, plate, motorcycle, area, pin)
    fun updateProfile(phone: String, area: String, motorcycle: String, plate: String, photoUrl: String = "") =
        profileCoord.updateProfile(currentMember.value, phone, area, motorcycle, plate, photoUrl)

    // Emergency & Crash
    fun triggerEmergency(type: EmergencyType, message: String, location: String) =
        emergencyCoord.triggerEmergency(currentMember.value, type, message, location)
    fun resolveEmergency(alertId: String) = emergencyCoord.resolveEmergency(alertId, currentMember.value)
    fun respondEmergency(alertId: String) = emergencyCoord.respondEmergency(alertId)
    fun respondToEmergency(alertId: String) = respondEmergency(alertId)
    fun toggleSosAlarmSound(enabled: Boolean) = emergencyCoord.toggleSosAlarmSound(enabled)
    fun testSosAlarmSound(onFinish: (() -> Unit)? = null) = emergencyCoord.testSosAlarmSound(onFinish)
    fun stopSosAlarmSound() = emergencyCoord.stopSosAlarmSound()
    fun toggleCrashGuard(enabled: Boolean) = emergencyCoord.toggleCrashGuard(enabled)
    fun setCrashSensitivity(sensitivity: CrashSensitivity) = emergencyCoord.setCrashSensitivity(sensitivity)
    fun simulateCrashImpact(gForce: Float = 3.2f) = emergencyCoord.simulateCrashImpact(gForce)
    fun dismissCrashCountdown() = emergencyCoord.dismissCrashCountdown()
    fun confirmCrashAutoSos(gForce: Float) {
        dismissCrashCountdown()
        triggerEmergency(EmergencyType.KECELAKAAN, "Auto-SOS: Crash Guard %.1fG".format(gForce), "Posisi: ${currentMember.value?.baseArea ?: "Malang"}")
    }

    // Governance & Community
    fun approveMemberScreening(memberId: String, note: String = "Diverifikasi") = governanceCoord.approveMemberScreening(memberId, note)
    fun rejectMemberScreening(memberId: String, reason: String) = governanceCoord.rejectMemberScreening(memberId, reason)
    fun updateMemberRole(memberId: String, role: MemberRole) = governanceCoord.updateMemberRole(memberId, role, currentMember.value)
    fun updateMemberVerification(memberId: String, status: VerificationStatus) = governanceCoord.updateMemberVerification(memberId, status, currentMember.value)
    fun createPost(title: String, content: String, category: ForumCategory, postType: PostType = PostType.UPDATE) =
        communityCoord.createPost(currentMember.value, title, content, category, postType)
    fun addPost(title: String, content: String, category: ForumCategory, postType: PostType = PostType.UPDATE) =
        createPost(title, content, category, postType)
    fun deletePost(postId: String) = communityCoord.deletePost(postId)
    fun toggleLike(postId: String, isLiked: Boolean) = communityCoord.toggleLike(postId, isLiked)
    fun toggleLikePost(postId: String, isLiked: Boolean) = toggleLike(postId, isLiked)
    fun checkInEvent(eventId: String) = communityCoord.checkInEvent(currentMember.value, eventId)
    fun recordKopdarAttendance() = checkInEvent("EVT-KOPDAR-01")
    fun submitDriverReview(targetDriverId: String, rating: Float, tag: String, comment: String) =
        communityCoord.submitDriverReview(currentMember.value, targetDriverId, rating, tag, comment)
    fun addDriverReview(targetDriverId: String, rating: Int, comment: String) = submitDriverReview(targetDriverId, rating.toFloat(), "Solidaritas", comment)
    fun sendNotification(title: String, message: String, severity: NotificationSeverity) =
        communityCoord.sendNotification(currentMember.value, title, message, severity)
    fun awardPeerPoints(targetId: String, targetName: String, isBeneficiary: Boolean, reason: String) =
        communityCoord.awardPeerPoints(currentMember.value, targetId, targetName, isBeneficiary, reason)
    fun claimTask(taskId: String) = communityCoord.claimTask(currentMember.value, taskId)
    fun completeTask(taskId: String) = communityCoord.completeTask(currentMember.value, taskId)
    fun redeemReward(rewardId: String) = communityCoord.redeemReward(currentMember.value, rewardId)

    // Radar & Battery
    fun reportHazard(title: String, type: HazardType, location: String, description: String, lat: Double, lng: Double) =
        radarCoord.reportHazard(currentMember.value, title, type, location, description, lat, lng)
    fun confirmHazard(hazardId: String) = radarCoord.confirmHazard(hazardId)
    fun toggleLocationSharingConsent(consent: Boolean) = radarCoord.toggleLocationSharingConsent(currentMember.value, consent)
    fun saveNotificationPref(preference: NotificationPreference) = radarCoord.saveNotificationPref(preference)
    fun applyCommunityGpsPolicy() = radarCoord.applyCommunityGpsPolicy(currentMember.value)
    fun setMapCachePolicy(policy: MapCachePolicy) = radarCoord.setMapCachePolicy(policy)
    fun setLocationSyncProfile(profile: LocationSyncPowerProfile) = radarCoord.setLocationSyncProfile(profile)
    fun setPowerSaverMode(enabled: Boolean) = radarCoord.setPowerSaverMode(enabled)
    fun setDataSaverMode(enabled: Boolean) = radarCoord.setDataSaverMode(enabled)
    fun precacheMap(context: Context) = radarCoord.precacheMap(context, isPowerSaverMode.value)
    fun clearMapCache(context: Context) = radarCoord.clearMapCache(context)
    fun getCacheSizeDesc(context: Context) = radarCoord.getCacheSizeDesc(context)

    override fun onCleared() {
        super.onCleared()
        emergencyCoord.onCleared()
        radarCoord.locationSyncer?.stopSync()
    }
}
