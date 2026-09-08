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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DRGViewModel(val repository: DRGRepository, val sharedPrefs: SharedPreferences?, application: Application) : AndroidViewModel(application) {
    constructor(repo: DRGRepository, prefs: SharedPreferences?, ctx: Context? = null) :
        this(repo, prefs, (ctx?.applicationContext as? Application) ?: throw IllegalArgumentException("App context required"))

    private val _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage: StateFlow<String?> = _snackBarMessage.asStateFlow()
    fun showToast(msg: String) { _snackBarMessage.value = msg }
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
    fun setSearchQuery(q: String) { _searchQuery.value = q }

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
        val hasSeeded = sharedPrefs?.getBoolean("drg_has_seeded_v5", false) ?: false
        if (!hasSeeded) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.initializeSeedDataIfNeeded()
                sharedPrefs?.edit()?.putBoolean("drg_has_seeded_v5", true)?.apply()
            }
        }
        viewModelScope.launch {
            currentMember.filterNotNull().collect { member ->
                if (radarCoord.locationSyncer == null) {
                    radarCoord.locationSyncer = BatteryAwareLocationSyncer(application, viewModelScope) { lat, lng ->
                        repository.updateMemberLocation(member.id, lat, lng)
                    }.apply { setProfile(radarCoord.locationSyncProfile.value) }
                }
                radarCoord.applyCommunityGpsPolicy(member)
            }
        }
    }

    // Auth & Profile
    fun login(id: String) = profileCoord.login(id)
    fun logout() = profileCoord.logout()
    fun switchActiveMember(id: String) = profileCoord.switchActiveMember(id)
    fun switchRole(id: String) = switchActiveMember(id)
    fun registerNewDriver(n: String, p: String, pl: String, m: String, a: String) = profileCoord.registerNewDriver(n, p, pl, m, a)
    fun updateProfile(p: String, a: String, m: String, pl: String, u: String = "") = profileCoord.updateProfile(currentMember.value, p, a, m, pl, u)

    // Emergency & Crash
    fun triggerEmergency(t: EmergencyType, m: String, l: String) = emergencyCoord.triggerEmergency(currentMember.value, t, m, l)
    fun resolveEmergency(id: String) = emergencyCoord.resolveEmergency(id, currentMember.value)
    fun respondEmergency(id: String) = emergencyCoord.respondEmergency(id)
    fun respondToEmergency(id: String) = respondEmergency(id)
    fun toggleSosAlarmSound(e: Boolean) = emergencyCoord.toggleSosAlarmSound(e)
    fun testSosAlarmSound(f: (() -> Unit)? = null) = emergencyCoord.testSosAlarmSound(f)
    fun stopSosAlarmSound() = emergencyCoord.stopSosAlarmSound()
    fun toggleCrashGuard(e: Boolean) = emergencyCoord.toggleCrashGuard(e)
    fun setCrashSensitivity(s: CrashSensitivity) = emergencyCoord.setCrashSensitivity(s)
    fun simulateCrashImpact(g: Float = 3.2f) = emergencyCoord.simulateCrashImpact(g)
    fun dismissCrashCountdown() = emergencyCoord.dismissCrashCountdown()
    fun confirmCrashAutoSos(g: Float) {
        dismissCrashCountdown()
        triggerEmergency(EmergencyType.KECELAKAAN, "Auto-SOS: Crash Guard %.1fG".format(g), "Posisi: ${currentMember.value?.baseArea ?: "Malang"}")
    }

    // Governance, Community, Tasks & Radar
    fun approveMemberScreening(id: String, n: String = "Diverifikasi") = governanceCoord.approveMemberScreening(id, n)
    fun rejectMemberScreening(id: String, r: String) = governanceCoord.rejectMemberScreening(id, r)
    fun updateMemberRole(id: String, r: MemberRole) = governanceCoord.updateMemberRole(id, r, currentMember.value)
    fun updateMemberVerification(id: String, v: VerificationStatus) = governanceCoord.updateMemberVerification(id, v, currentMember.value)
    fun createPost(t: String, c: String, cat: ForumCategory, p: PostType = PostType.UPDATE) = communityCoord.createPost(currentMember.value, t, c, cat, p)
    fun addPost(t: String, c: String, cat: ForumCategory, p: PostType = PostType.UPDATE) = createPost(t, c, cat, p)
    fun deletePost(id: String) = communityCoord.deletePost(id)
    fun toggleLike(id: String, l: Boolean) = communityCoord.toggleLike(id, l)
    fun toggleLikePost(id: String, l: Boolean) = toggleLike(id, l)
    fun checkInEvent(id: String) = communityCoord.checkInEvent(currentMember.value, id)
    fun recordKopdarAttendance() = checkInEvent("EVT-KOPDAR-01")
    fun submitDriverReview(t: String, r: Float, tag: String, c: String) = communityCoord.submitDriverReview(currentMember.value, t, r, tag, c)
    fun addDriverReview(t: String, r: Int, c: String) = submitDriverReview(t, r.toFloat(), "Solidaritas", c)
    fun sendNotification(t: String, m: String, s: NotificationSeverity) = communityCoord.sendNotification(currentMember.value, t, m, s)
    fun awardPeerPoints(tId: String, tN: String, b: Boolean, r: String) = communityCoord.awardPeerPoints(currentMember.value, tId, tN, b, r)
    fun claimTask(id: String) = communityCoord.claimTask(currentMember.value, id)
    fun completeTask(id: String) = communityCoord.completeTask(currentMember.value, id)
    fun redeemReward(id: String) = communityCoord.redeemReward(currentMember.value, id)
    fun reportHazard(t: String, type: HazardType, l: String, d: String, lat: Double, lng: Double) = radarCoord.reportHazard(currentMember.value, t, type, l, d, lat, lng)
    fun confirmHazard(id: String) = radarCoord.confirmHazard(id)
    fun toggleLocationSharingConsent(c: Boolean) = radarCoord.toggleLocationSharingConsent(currentMember.value, c)
    fun saveNotificationPref(p: NotificationPreference) = radarCoord.saveNotificationPref(p)
    fun applyCommunityGpsPolicy() = radarCoord.applyCommunityGpsPolicy(currentMember.value)
    fun setMapCachePolicy(p: MapCachePolicy) = radarCoord.setMapCachePolicy(p)
    fun setLocationSyncProfile(p: LocationSyncPowerProfile) = radarCoord.setLocationSyncProfile(p)
    fun setPowerSaverMode(e: Boolean) = radarCoord.setPowerSaverMode(e)
    fun setDataSaverMode(e: Boolean) = radarCoord.setDataSaverMode(e)
    fun precacheMap(ctx: Context) = radarCoord.precacheMap(ctx, isPowerSaverMode.value)
    fun clearMapCache(ctx: Context) = radarCoord.clearMapCache(ctx)
    fun getCacheSizeDesc(ctx: Context) = radarCoord.getCacheSizeDesc(ctx)

    override fun onCleared() {
        super.onCleared()
        emergencyCoord.onCleared()
        radarCoord.locationSyncer?.stopSync()
    }
}
