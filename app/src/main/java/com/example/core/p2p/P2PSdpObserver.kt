package com.example.core.p2p

import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

internal class SimpleSdpObserver(
    private val onCreateSuccessCallback: (SessionDescription) -> Unit = {},
    private val onSetSuccessCallback: () -> Unit = {},
    private val onFailureCallback: (String) -> Unit = {}
) : SdpObserver {
    override fun onCreateSuccess(sdp: SessionDescription?) {
        if (sdp != null) onCreateSuccessCallback(sdp)
    }

    override fun onSetSuccess() {
        onSetSuccessCallback()
    }

    override fun onCreateFailure(error: String?) {
        onFailureCallback(error ?: "SDP creation failed")
    }

    override fun onSetFailure(error: String?) {
        onFailureCallback(error ?: "SDP apply failed")
    }
}
