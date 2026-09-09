package com.example.core.viewmodel

import android.content.Context
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.shared.models.*

fun DRGViewModel.login(memberId: String) = profileCoord.login(memberId)
fun DRGViewModel.logout() = profileCoord.logout()
fun DRGViewModel.switchActiveMember(memberId: String) = profileCoord.switchActiveMember(memberId)
fun DRGViewModel.switchRole(memberId: String) = switchActiveMember(memberId)
fun DRGViewModel.registerNewDriver(name: String, phone: String, plate: String, motorcycle: String, area: String, pin: String) =
    profileCoord.registerNewDriver(name, phone, plate, motorcycle, area, pin)
fun DRGViewModel.updateProfile(phone: String, area: String, motorcycle: String, plate: String, photoUrl: String = "") =
    profileCoord.updateProfile(currentMember.value, phone, area, motorcycle, plate, photoUrl)

fun DRGViewModel.triggerEmergency(type: EmergencyType, message: String, location: String) =
    emergencyCoord.triggerEmergency(currentMember.value, type, message, location)
fun DRGViewModel.resolveEmergency(alertId: String) = emergencyCoord.resolveEmergency(alertId, currentMember.value)
fun DRGViewModel.respondEmergency(alertId: String) = emergencyCoord.respondEmergency(alertId)
fun DRGViewModel.respondToEmergency(alertId: String) = respondEmergency(alertId)
fun DRGViewModel.toggleSosAlarmSound(enabled: Boolean) = emergencyCoord.toggleSosAlarmSound(enabled)
fun DRGViewModel.testSosAlarmSound(onFinish: (() -> Unit)? = null) = emergencyCoord.testSosAlarmSound(onFinish)
fun DRGViewModel.stopSosAlarmSound() = emergencyCoord.stopSosAlarmSound()
fun DRGViewModel.toggleCrashGuard(enabled: Boolean) = emergencyCoord.toggleCrashGuard(enabled)
fun DRGViewModel.setCrashSensitivity(sensitivity: CrashSensitivity) = emergencyCoord.setCrashSensitivity(sensitivity)
fun DRGViewModel.simulateCrashImpact(gForce: Float = 3.2f) = emergencyCoord.simulateCrashImpact(gForce)
fun DRGViewModel.dismissCrashCountdown() = emergencyCoord.dismissCrashCountdown()
fun DRGViewModel.confirmCrashAutoSos(gForce: Float) {
    dismissCrashCountdown()
    triggerEmergency(EmergencyType.KECELAKAAN, "Auto-SOS: Crash Guard %.1fG".format(gForce), "Posisi: ${currentMember.value?.baseArea ?: "Malang"}")
}

fun DRGViewModel.approveMemberScreening(memberId: String, note: String = "Diverifikasi") = governanceCoord.approveMemberScreening(memberId, note)
fun DRGViewModel.rejectMemberScreening(memberId: String, reason: String) = governanceCoord.rejectMemberScreening(memberId, reason)
fun DRGViewModel.updateMemberRole(memberId: String, role: MemberRole) = governanceCoord.updateMemberRole(memberId, role, currentMember.value)
fun DRGViewModel.updateMemberVerification(memberId: String, status: VerificationStatus) = governanceCoord.updateMemberVerification(memberId, status, currentMember.value)
fun DRGViewModel.createPost(title: String, content: String, category: ForumCategory, postType: PostType = PostType.UPDATE) =
    communityCoord.createPost(currentMember.value, title, content, category, postType)
fun DRGViewModel.addPost(title: String, content: String, category: ForumCategory, postType: PostType = PostType.UPDATE) =
    createPost(title, content, category, postType)
fun DRGViewModel.deletePost(postId: String) = communityCoord.deletePost(postId)
fun DRGViewModel.toggleLike(postId: String, isLiked: Boolean) = communityCoord.toggleLike(postId, isLiked)
fun DRGViewModel.toggleLikePost(postId: String, isLiked: Boolean) = toggleLike(postId, isLiked)
fun DRGViewModel.checkInEvent(eventId: String) = communityCoord.checkInEvent(currentMember.value, eventId)
fun DRGViewModel.recordKopdarAttendance() = checkInEvent("EVT-KOPDAR-01")
fun DRGViewModel.submitDriverReview(targetDriverId: String, rating: Float, tag: String, comment: String) =
    communityCoord.submitDriverReview(currentMember.value, targetDriverId, rating, tag, comment)
fun DRGViewModel.addDriverReview(targetDriverId: String, rating: Int, comment: String) = submitDriverReview(targetDriverId, rating.toFloat(), "Solidaritas", comment)
fun DRGViewModel.sendNotification(title: String, message: String, severity: NotificationSeverity) =
    communityCoord.sendNotification(currentMember.value, title, message, severity)
fun DRGViewModel.awardPeerPoints(targetId: String, targetName: String, isBeneficiary: Boolean, reason: String) =
    communityCoord.awardPeerPoints(currentMember.value, targetId, targetName, isBeneficiary, reason)
fun DRGViewModel.claimTask(taskId: String) = communityCoord.claimTask(currentMember.value, taskId)
fun DRGViewModel.completeTask(taskId: String) = communityCoord.completeTask(currentMember.value, taskId)
fun DRGViewModel.redeemReward(rewardId: String) = communityCoord.redeemReward(currentMember.value, rewardId)

fun DRGViewModel.reportHazard(title: String, type: HazardType, location: String, description: String, lat: Double, lng: Double) =
    radarCoord.reportHazard(currentMember.value, title, type, location, description, lat, lng)
fun DRGViewModel.confirmHazard(hazardId: String) = radarCoord.confirmHazard(hazardId)
fun DRGViewModel.toggleLocationSharingConsent(consent: Boolean) = radarCoord.toggleLocationSharingConsent(currentMember.value, consent)
fun DRGViewModel.saveNotificationPref(preference: NotificationPreference) = radarCoord.saveNotificationPref(preference)
fun DRGViewModel.applyCommunityGpsPolicy() = radarCoord.applyCommunityGpsPolicy(currentMember.value)
fun DRGViewModel.setMapCachePolicy(policy: MapCachePolicy) = radarCoord.setMapCachePolicy(policy)
fun DRGViewModel.setLocationSyncProfile(profile: LocationSyncPowerProfile) = radarCoord.setLocationSyncProfile(profile)
fun DRGViewModel.setPowerSaverMode(enabled: Boolean) = radarCoord.setPowerSaverMode(enabled)
fun DRGViewModel.setDataSaverMode(enabled: Boolean) = radarCoord.setDataSaverMode(enabled)
fun DRGViewModel.precacheMap(context: Context) = radarCoord.precacheMap(context, isPowerSaverMode.value)
fun DRGViewModel.clearMapCache(context: Context) = radarCoord.clearMapCache(context)
fun DRGViewModel.getCacheSizeDesc(context: Context) = radarCoord.getCacheSizeDesc(context)
