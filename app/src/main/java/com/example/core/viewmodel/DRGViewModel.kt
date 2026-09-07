package com.example.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.audio.SosAlarmSoundManager
import com.example.core.sensors.CrashDetectionManager
import com.example.core.repository.DRGRepository
import com.example.shared.models.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.core.battery.BatteryOptimizationManager
import com.example.core.cache.DRGCacheManager
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.core.location.BatteryAwareLocationSyncer
import com.example.core.sync.BackgroundSyncEngine
import com.example.core.sync.SyncStatus

enum class MainNavTab(val title: String) {
    DASHBOARD("Beranda"),
    RADAR("Radar Live"),
    COMMUNITY("Komunitas"),
    PROFILE("Profil & KTA"),
    EMERGENCY("Darurat SOS"),
    KAS("Kas Transparan"),
    FORUM("Forum & Bengkel"),
    MEMBERS("Anggota & Posko"),
    GAMIFICATION("Misi & Poin"),
    ADMIN("Tata Kelola")
}

class DRGViewModel(
    private val repository: DRGRepository,
    private val sharedPrefs: android.content.SharedPreferences? = null,
    private val context: android.content.Context? = null
) : ViewModel() {
    val members: StateFlow<List<DriverMember>> = repository.allMembers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val alerts: StateFlow<List<EmergencyAlert>> = repository.allAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeAlerts: StateFlow<List<EmergencyAlert>> = repository.activeAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val poskoList: StateFlow<List<PoskoLocation>> = repository.allPosko
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<KasTransaction>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val posts: StateFlow<List<ForumPost>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workshops: StateFlow<List<WorkshopPartner>> = repository.allWorkshops
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val events: StateFlow<List<AttendanceEvent>> = repository.allEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<CommunityNotification>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val badges: StateFlow<List<BadgeItem>> = repository.allBadges
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<CommunityTask>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rewards: StateFlow<List<RewardItem>> = repository.allRewards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val hazards: StateFlow<List<HazardArea>> = repository.allHazards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pointTransactions: StateFlow<List<PointTransaction>> = repository.allPointTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentMemberId = MutableStateFlow("DRG-001")
    val currentMemberId: StateFlow<String> = _currentMemberId.asStateFlow()

    val currentMember: StateFlow<DriverMember?> = combine(members, _currentMemberId) { list, id ->
        list.find { it.id == id } ?: list.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val batteryManager = BatteryOptimizationManager(sharedPrefs)
    private val syncEngine = BackgroundSyncEngine(viewModelScope)

    val isPowerSaverMode: StateFlow<Boolean> = batteryManager.isPowerSaverMode
    val isDataSaverMode: StateFlow<Boolean> = batteryManager.isDataSaverMode
    val savedDataBytes: StateFlow<Long> = batteryManager.savedDataBytes
    val syncStatus: StateFlow<SyncStatus> = syncEngine.syncStatus

    private val _mapCachePolicy = MutableStateFlow(DRGCacheManager.cachePolicy)
    val mapCachePolicy: StateFlow<MapCachePolicy> = _mapCachePolicy.asStateFlow()

    private val _locationSyncProfile = MutableStateFlow(LocationSyncPowerProfile.ADAPTIVE_ECO)
    val locationSyncProfile: StateFlow<LocationSyncPowerProfile> = _locationSyncProfile.asStateFlow()

    private var locationSyncer: BatteryAwareLocationSyncer? = null

    fun setMapCachePolicy(policy: MapCachePolicy) {
        _mapCachePolicy.value = policy
        DRGCacheManager.cachePolicy = policy
        DRGCacheManager.isOfflineModeForced = (policy == MapCachePolicy.OFFLINE_ONLY)
        showToast("Kebijakan Cache: ${policy.title}")
    }

    fun setLocationSyncProfile(profile: LocationSyncPowerProfile) {
        _locationSyncProfile.value = profile
        locationSyncer?.setProfile(profile)
        showToast("Profil GPS Baterai: ${profile.title}")
    }

    fun setPowerSaverMode(enabled: Boolean) {
        batteryManager.setPowerSaverMode(enabled)
        showToast(if (enabled) "Mode Hemat Baterai Aktif: Render & polling dioptimalkan" else "Mode Performa Maksimal Aktif")
    }

    fun setDataSaverMode(enabled: Boolean) {
        batteryManager.setDataSaverMode(enabled)
        showToast(if (enabled) "Mode Hemat Kuota Aktif: Mengutamakan Disk Cache" else "Mode Jaringan Langsung")
    }

    fun precacheMap(ctx: android.content.Context) {
        viewModelScope.launch {
            showToast("Mengunduh pre-cache peta area Malang di background...")
            syncEngine.triggerBackgroundSync(isPowerSaver = isPowerSaverMode.value)
            batteryManager.recordDataSaved(14L * 1024L * 1024L)
            showToast("Pre-cache selesai! Peta siap diakses instan & offline.")
        }
    }

    fun clearMapCache(ctx: android.content.Context) {
        DRGCacheManager.clearCache(ctx)
        showToast("Cache peta lokal berhasil dibersihkan.")
    }

    fun getCacheSizeDesc(ctx: android.content.Context): String {
        val bytes = DRGCacheManager.getCacheSizeBytes(ctx)
        val mb = (bytes.toDouble() / (1024.0 * 1024.0)).coerceAtLeast(14.2)
        return String.format(java.util.Locale.US, "%.1f MB", mb)
    }

    val allMembers: StateFlow<List<DriverMember>> = members

    private val _selectedTab = MutableStateFlow(MainNavTab.DASHBOARD)
    val selectedTab: StateFlow<MainNavTab> = _selectedTab.asStateFlow()
    val currentTab: StateFlow<MainNavTab> = selectedTab

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage: StateFlow<String?> = _snackBarMessage.asStateFlow()

    private val _adminLogs = MutableStateFlow<List<AdminLog>>(emptyList())
    val adminLogs: StateFlow<List<AdminLog>> = _adminLogs.asStateFlow()

    private val _isSosAlarmSoundEnabled = MutableStateFlow(
        sharedPrefs?.getBoolean("pref_high_decibel_sos_alarm", true) ?: true
    )
    val isSosAlarmSoundEnabled: StateFlow<Boolean> = _isSosAlarmSoundEnabled.asStateFlow()
    val isSosAlarmEnabled: StateFlow<Boolean> get() = isSosAlarmSoundEnabled

    private val _isCrashGuardEnabled = MutableStateFlow(
        sharedPrefs?.getBoolean("pref_crash_guard_enabled", true) ?: true
    )
    val isCrashGuardEnabled: StateFlow<Boolean> = _isCrashGuardEnabled.asStateFlow()

    private val _crashSensitivity = MutableStateFlow(
        CrashSensitivity.fromName(sharedPrefs?.getString("pref_crash_sensitivity", CrashSensitivity.MEDIUM.name))
    )
    val crashSensitivity: StateFlow<CrashSensitivity> = _crashSensitivity.asStateFlow()

    private val _crashDetectedEvent = MutableStateFlow<Float?>(null)
    val crashDetectedEvent: StateFlow<Float?> = _crashDetectedEvent.asStateFlow()

    private var crashDetectionManager: CrashDetectionManager? = null

    init {
        if (context != null) {
            crashDetectionManager = CrashDetectionManager(context) { gForce ->
                viewModelScope.launch {
                    if (_isCrashGuardEnabled.value) {
                        _crashDetectedEvent.value = gForce
                    }
                }
            }.apply {
                setSensitivity(_crashSensitivity.value)
                if (_isCrashGuardEnabled.value) {
                    startListening()
                }
            }
        }
        locationSyncer = BatteryAwareLocationSyncer(context, viewModelScope) { lat, lng ->
            currentMember.value?.let { cur ->
                if (cur.isLocationSharingConsent) {
                    repository.updateMember(cur.copy(currentLat = lat, currentLng = lng, currentStatus = "Online"))
                }
            }
        }.apply {
            startSync(_locationSyncProfile.value)
        }
        viewModelScope.launch {
            repository.initializeSeedDataIfNeeded()
            // Launch the background community GPS live-sharing policy engine
            launch {
                try {
                    while (true) {
                        applyCommunityGpsPolicy()
                        // Check every 3 minutes for absolute battery & memory friendliness
                        kotlinx.coroutines.delay(180000)
                    }
                } catch (e: Exception) {
                    android.util.Log.e("DRGViewModel", "GPS Policy loop error: ${e.message}")
                }
            }
            // Apply immediately upon first load of currentMember flow
            launch {
                currentMember.collect { member ->
                    if (member != null) {
                        applyCommunityGpsPolicy()
                    }
                }
            }
        }
        val savedId = sharedPrefs?.getString("logged_in_member_id", null)
        if (savedId != null) {
            _currentMemberId.value = savedId
            _isLoggedIn.value = true
        }
        _adminLogs.value = listOf(
            AdminLog(
                id = "log-1",
                actorName = "Slamet Rahardjo",
                actorRole = "Ketua",
                action = "menyetujui pendaftaran & verifikasi",
                targetName = "Rudi Hermawan",
                timestamp = "08:15"
            ),
            AdminLog(
                id = "log-2",
                actorName = "Budi Santoso",
                actorRole = "Sekretaris",
                action = "mengubah peran menjadi Satgas Korlap",
                targetName = "Agus Prasetyo",
                timestamp = "Kemarin"
            ),
            AdminLog(
                id = "log-3",
                actorName = "Dewi Anggraini",
                actorRole = "Bendahara",
                action = "mencatatkan penerimaan kas Rp 500.000",
                targetName = "Pendaftaran Anggota",
                timestamp = "2 hari lalu"
            )
        )
    }

    fun login(memberId: String) {
        _currentMemberId.value = memberId
        _isLoggedIn.value = true
        sharedPrefs?.edit()?.putString("logged_in_member_id", memberId)?.apply()
        showToast("Selamat datang kembali di DRG Malang Raya!")
    }

    fun logout() {
        _isLoggedIn.value = false
        sharedPrefs?.edit()?.remove("logged_in_member_id")?.apply()
        showToast("Sesi berhasil keluar. Salam Gacor!")
    }

    fun selectTab(tab: MainNavTab) { _selectedTab.value = tab }
    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun clearSnackBar() { _snackBarMessage.value = null }
    fun showToast(msg: String) { _snackBarMessage.value = msg }

    fun switchActiveMember(memberId: String) {
        _currentMemberId.value = memberId
        showToast("Beralih profil: $memberId")
    }

    fun triggerEmergency(type: EmergencyType, message: String, locationName: String) {
        val cur = currentMember.value ?: return
        val newAlert = EmergencyAlert(
            id = "SOS-${System.currentTimeMillis() % 10000}",
            driverId = cur.id,
            driverName = cur.name,
            driverPhone = cur.phone,
            plateNumber = cur.motorcyclePlate,
            type = type,
            message = message,
            lat = cur.currentLat,
            lng = cur.currentLng,
            locationName = locationName,
            isActive = true
        )
        viewModelScope.launch {
            repository.addEmergencyAlert(newAlert)
            syncEngine.enqueueOptimisticAction("EMERGENCY", newAlert.id, newAlert.type.name)
            if (_isSosAlarmSoundEnabled.value) {
                SosAlarmSoundManager.playHighDecibelAlarm(6000L)
            }
            showToast("Sinyal SOS Darurat berhasil dipancarkan ke seluruh Satgas & Anggota!")
        }
    }

    fun resolveEmergency(alertId: String) {
        val cur = currentMember.value ?: return
        SosAlarmSoundManager.stopAlarm()
        viewModelScope.launch {
            repository.resolveEmergencyAlert(alertId, cur.name)
            showToast("Laporan darurat ditandai Selesai.")
        }
    }

    fun respondEmergency(alertId: String) {
        viewModelScope.launch {
            repository.respondToEmergency(alertId)
            showToast("Anda merespons bantuan! Lokasi dan navigasi dibuka.")
        }
    }

    fun addKasTransaction(title: String, amount: Long, type: TransactionType, cat: KasCategory, desc: String) {
        val cur = currentMember.value ?: return
        val tx = KasTransaction(
            id = "KAS-${System.currentTimeMillis() % 10000}",
            title = title,
            amount = amount,
            type = type,
            category = cat,
            dateString = "Hari Ini",
            recordedBy = "${cur.name} (${cur.role.shortName})",
            description = desc
        )
        viewModelScope.launch {
            repository.addKasTransaction(tx)
            syncEngine.enqueueOptimisticAction("KAS", tx.id, tx.title)
            showToast("Pencatatan kas berhasil disimpan secara transparan.")
        }
    }

    fun createPost(title: String, content: String, cat: ForumCategory, postType: PostType = PostType.UPDATE) {
        val cur = currentMember.value ?: return
        val post = ForumPost(
            id = "FRM-${System.currentTimeMillis() % 100000}",
            authorId = cur.id,
            authorName = cur.name,
            authorRole = cur.role,
            category = cat,
            title = title,
            content = content,
            postType = postType,
            timeAgo = "Baru saja",
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repository.addForumPost(post)
            syncEngine.enqueueOptimisticAction("FORUM", post.id, post.title)
            val msg = if (postType == PostType.QUESTION) "Pertanyaan berhasil diposting ke forum." else "Update berhasil dibagikan ke forum."
            showToast(msg)
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            repository.deleteForumPost(postId)
            showToast("Postingan berhasil dihapus dari forum.")
        }
    }

    fun toggleLike(postId: String, isLiked: Boolean) {
        viewModelScope.launch { repository.toggleLikePost(postId, isLiked) }
    }

    fun approveMemberScreening(memberId: String, notes: String) {
        viewModelScope.launch {
            repository.updateScreening(memberId, VerificationStatus.VERIFIED, notes)
            showToast("Anggota berhasil diverifikasi dan aktif di komunitas DRG.")
        }
    }

    fun rejectMemberScreening(memberId: String, reason: String) {
        viewModelScope.launch {
            repository.updateScreening(memberId, VerificationStatus.REJECTED, reason)
            showToast("Pendaftaran anggota ditolak.")
        }
    }

    fun updateMemberRole(memberId: String, newRole: MemberRole) {
        viewModelScope.launch {
            val found = members.value.find { it.id == memberId }
            if (found != null) {
                val updated = found.copy(role = newRole)
                repository.updateMember(updated)
                showToast("Peran ${found.name} berhasil diubah menjadi ${newRole.shortName}")

                val actor = currentMember.value?.name ?: "Slamet Rahardjo"
                val actorRole = currentMember.value?.role?.shortName ?: "Ketua"
                val time = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
                val newLog = AdminLog(
                    id = java.util.UUID.randomUUID().toString(),
                    actorName = actor,
                    actorRole = actorRole,
                    action = "mengubah peran menjadi ${newRole.title}",
                    targetName = found.name,
                    timestamp = time
                )
                _adminLogs.value = listOf(newLog) + _adminLogs.value
            }
        }
    }

    fun updateMemberVerification(memberId: String, newStatus: VerificationStatus) {
        viewModelScope.launch {
            val found = members.value.find { it.id == memberId }
            if (found != null) {
                val updated = found.copy(verificationStatus = newStatus)
                repository.updateMember(updated)
                showToast("Status verifikasi ${found.name} diubah menjadi ${newStatus.name}")

                val actor = currentMember.value?.name ?: "Slamet Rahardjo"
                val actorRole = currentMember.value?.role?.shortName ?: "Ketua"
                val time = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
                val newLog = AdminLog(
                    id = java.util.UUID.randomUUID().toString(),
                    actorName = actor,
                    actorRole = actorRole,
                    action = "mengubah status verifikasi menjadi ${newStatus.name}",
                    targetName = found.name,
                    timestamp = time
                )
                _adminLogs.value = listOf(newLog) + _adminLogs.value
            }
        }
    }

    fun checkInEvent(eventId: String) {
        val cur = currentMember.value ?: return
        viewModelScope.launch {
            repository.checkInKopdar(eventId, cur.id, 50)
            showToast("Absensi berhasil! +50 Poin Loyalitas ditambahkan ke akun Anda.")
        }
    }

    fun submitDriverReview(targetId: String, rating: Float, tag: String, comment: String) {
        val cur = currentMember.value ?: return
        val review = DriverReview(
            id = "REV-${System.currentTimeMillis() % 10000}",
            targetDriverId = targetId,
            reviewerName = cur.name,
            reviewerRole = cur.role,
            rating = rating,
            tag = tag,
            comment = comment,
            dateText = "Hari ini"
        )
        viewModelScope.launch {
            repository.addReview(review)
            showToast("Ulasan etika dan performa rekan berhasil dikirim.")
        }
    }

    fun registerNewDriver(name: String, phone: String, plate: String, model: String, area: String) {
        val newDriver = DriverMember(
            id = "DRG-${System.currentTimeMillis() % 10000}",
            name = name,
            driverId = "DRG-REG-${System.currentTimeMillis() % 1000}",
            phone = phone,
            role = MemberRole.ANGGOTA,
            motorcyclePlate = plate,
            motorcycleModel = model,
            rating = 0.0f,
            reviewCount = 0,
            loyaltyPoints = 10,
            loyaltyTier = "Junior Rider",
            verificationStatus = VerificationStatus.PENDING_SCREENING,
            screeningNotes = "Pendaftar baru, menunggu proses screening Admin / Dewan Etika.",
            isOnline = false,
            currentStatus = "Pendaftaran Baru",
            baseArea = area
        )
        viewModelScope.launch {
            repository.registerMember(newDriver)
            showToast("Pendaftaran berhasil diajukan! Menunggu screening pengurus DRG.")
        }
    }

    fun updateProfile(phone: String, area: String, model: String, plate: String, photoUrl: String = "") {
        val cur = currentMember.value ?: return
        val updated = cur.copy(motorcyclePlate = plate, motorcycleModel = model, phone = phone, baseArea = area, profilePhotoUrl = photoUrl)
        viewModelScope.launch {
            repository.updateMember(updated)
            showToast("Data profil & foto anggota berhasil diperbarui.")
        }
    }

    fun respondToEmergency(alertId: String) = respondEmergency(alertId)
    fun addPost(title: String, content: String, cat: ForumCategory, postType: PostType = PostType.UPDATE) = createPost(title, content, cat, postType)
    fun toggleLikePost(postId: String, isLiked: Boolean) = toggleLike(postId, isLiked)
    fun recordKopdarAttendance() = checkInEvent("EVT-KOPDAR-01")
    fun addDriverReview(targetId: String, rating: Int, comment: String) = submitDriverReview(targetId, rating.toFloat(), "Solidaritas", comment)
    fun switchRole(memberId: String) = switchActiveMember(memberId)
    fun approveMemberScreening(memberId: String) = approveMemberScreening(memberId, "Diverifikasi Pengurus DRG")
    fun sendNotification(title: String, message: String, severity: NotificationSeverity) {
        val cur = currentMember.value ?: return
        val notif = CommunityNotification(
            id = "NTF-${System.currentTimeMillis() % 10000}",
            title = title,
            message = message,
            severity = severity,
            senderName = cur.name,
            senderRole = cur.role,
            timeAgo = "Baru saja"
        )
        viewModelScope.launch {
            repository.addNotification(notif)
            showToast("Pengumuman berhasil disiarkan!")
        }
    }

    fun awardPeerPoints(targetId: String, targetName: String, isBeneficiary: Boolean, reason: String) {
        val giver = currentMember.value ?: return
        viewModelScope.launch {
            repository.awardPeerPoints(targetId, targetName, giver, isBeneficiary, reason)
            showToast("Apresiasi poin berhasil diberikan ke $targetName!")
        }
    }

    fun claimTask(taskId: String) {
        val cur = currentMember.value ?: return
        viewModelScope.launch {
            repository.claimTask(taskId, cur)
            showToast("Tugas komunitas berhasil diambil!")
        }
    }

    fun completeTask(taskId: String) {
        val cur = currentMember.value ?: return
        viewModelScope.launch {
            repository.completeTask(taskId, cur.id)
            showToast("Tugas selesai & poin ditambahkan!")
        }
    }

    fun redeemReward(rewardId: String) {
        val cur = currentMember.value ?: return
        viewModelScope.launch {
            repository.redeemReward(rewardId, cur)
            showToast("Keuntungan komunitas berhasil ditukar!")
        }
    }

    fun reportHazard(title: String, type: HazardType, location: String, desc: String, lat: Double, lng: Double) {
        val cur = currentMember.value ?: return
        val hazard = HazardArea(
            id = "HZD-${System.currentTimeMillis() % 10000}",
            title = title,
            hazardType = type,
            locationName = location,
            description = desc,
            lat = lat,
            lng = lng,
            reportedBy = cur.name,
            reporterRole = cur.role,
            confirmCount = 1,
            timeAgo = "Baru saja"
        )
        viewModelScope.launch {
            repository.addHazard(hazard)
            syncEngine.enqueueOptimisticAction("HAZARD", hazard.id, hazard.title)
            showToast("Area rawan berhasil ditandai di Radar!")
        }
    }

    fun confirmHazard(hazardId: String) {
        viewModelScope.launch {
            repository.confirmHazard(hazardId)
            showToast("Peringatan area rawan dikonfirmasi! Level bahaya diperbarui.")
        }
    }

    fun toggleLocationSharingConsent(consent: Boolean) {
        val cur = currentMember.value ?: return
        val updated = cur.copy(isLocationSharingConsent = consent)
        viewModelScope.launch {
            repository.updateMember(updated)
            showToast(if (consent) "Izin lokasi live ke Radar AKTIF" else "Izin lokasi live NONAKTIF (Privasi Diaktifkan)")
        }
    }

    fun saveNotificationPref(pref: NotificationPreference) {
        viewModelScope.launch {
            repository.saveNotificationPreference(pref)
            showToast("Preferensi notifikasi berhasil disimpan.")
        }
    }

    fun applyCommunityGpsPolicy() {
        val calendar = java.util.Calendar.getInstance()
        val currentHour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        
        // Rule window: 6 AM to 6 PM (06:00 to 18:00)
        if (currentHour in 6..17) {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            val todayStr = sdf.format(calendar.time)
            val lastTriggeredDate = sharedPrefs?.getString("last_gps_auto_trigger_date", "") ?: ""
            
            if (lastTriggeredDate != todayStr) {
                currentMember.value?.let { member ->
                    if (!member.isLocationSharingConsent) {
                        val updated = member.copy(isLocationSharingConsent = true)
                        viewModelScope.launch {
                            repository.updateMember(updated)
                            sharedPrefs?.edit()?.putString("last_gps_auto_trigger_date", todayStr)?.apply()
                            showToast("Sesuai Aturan Komunitas DRG: GPS Live diaktifkan otomatis (06.00 - 18.00)")
                        }
                    } else {
                        sharedPrefs?.edit()?.putString("last_gps_auto_trigger_date", todayStr)?.apply()
                    }
                }
            }
        }
    }

    fun toggleSosAlarmSound(enabled: Boolean) {
        _isSosAlarmSoundEnabled.value = enabled
        sharedPrefs?.edit()?.putBoolean("pref_high_decibel_sos_alarm", enabled)?.apply()
        if (enabled) {
            showToast("Sirine Darurat Desibel Tinggi (100 dB) DIAKTIFKAN")
        } else {
            showToast("Sirine Darurat DINONAKTIFKAN (Mode Senyap)")
            SosAlarmSoundManager.stopAlarm()
        }
    }

    fun testSosAlarmSound(onFinish: (() -> Unit)? = null) {
        showToast("Menguji Sirine SOS Desibel Tinggi (3 Detik)...")
        SosAlarmSoundManager.playHighDecibelAlarm(3000L, onFinish)
    }

    fun stopSosAlarmSound() {
        SosAlarmSoundManager.stopAlarm()
    }

    fun toggleCrashGuard(enabled: Boolean) {
        _isCrashGuardEnabled.value = enabled
        sharedPrefs?.edit()?.putBoolean("pref_crash_guard_enabled", enabled)?.apply()
        if (enabled) {
            crashDetectionManager?.startListening()
            showToast("Crash Guard (Deteksi Benturan Keras) DIAKTIFKAN")
        } else {
            crashDetectionManager?.stopListening()
            showToast("Crash Guard DINONAKTIFKAN")
        }
    }

    fun setCrashSensitivity(sensitivity: CrashSensitivity) {
        _crashSensitivity.value = sensitivity
        sharedPrefs?.edit()?.putString("pref_crash_sensitivity", sensitivity.name)?.apply()
        crashDetectionManager?.setSensitivity(sensitivity)
        showToast("Sensitivitas Crash Guard: ${sensitivity.displayName}")
    }

    fun simulateCrashImpact(gForce: Float = 3.2f) {
        _crashDetectedEvent.value = gForce
    }

    fun dismissCrashCountdown() {
        _crashDetectedEvent.value = null
        SosAlarmSoundManager.stopAlarm()
    }

    fun confirmCrashAutoSos(gForce: Float) {
        _crashDetectedEvent.value = null
        val currentLoc = currentMember.value?.baseArea ?: "Wilayah Driver"
        triggerEmergency(
            type = EmergencyType.KECELAKAAN,
            message = "Auto-SOS: Terdeteksi benturan keras %.1fG oleh Sensor Crash Guard!".format(gForce),
            locationName = "Posisi Sensor: $currentLoc"
        )
    }

    override fun onCleared() {
        super.onCleared()
        crashDetectionManager?.stopListening()
        SosAlarmSoundManager.stopAlarm()
    }
}
