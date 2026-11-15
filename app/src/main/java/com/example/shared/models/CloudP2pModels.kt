package com.example.shared.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class P2pPeerDoc(
    @DocumentId val id: String = "",
    val userId: String = "",
    val driverName: String = "",
    val channelId: String = "",
    val localIp: String = "",
    val port: Long = 8080L,
    val sdpOffer: String? = null,
    val status: String = "ONLINE",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class P2pSignalDoc(
    @DocumentId val id: String = "",
    val senderUserId: String = "",
    val targetUserId: String = "",
    val channelId: String = "",
    val signalType: String = "OFFER",
    val payload: String = "",
    val consumed: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CloudPttTransmissionDoc(
    @DocumentId val id: String = "",
    val userId: String = "",
    val senderName: String = "",
    val channelId: String = "",
    val audioBase64: String = "",
    val codec: String = "ULAW_8K",
    val sampleRateHz: Long = 8000L,
    val durationMs: Long = 0L,
    val status: String = "LIVE",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

enum class PttDeliveryState {
    IDLE,
    RECORDING,
    TRANSMITTING,
    SENT_CLOUD_P2P,
    QUEUED_OFFLINE,
    FAILED
}
