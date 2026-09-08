package com.example.core.viewmodel.coordinators

import com.example.shared.models.AdminLog
import com.example.shared.models.DriverMember
import com.example.shared.utils.DateTimeUtils
import java.util.UUID

object AdminLogUtils {
    fun createLog(
        actor: DriverMember?,
        action: String,
        targetName: String
    ): AdminLog {
        val actorName = actor?.name ?: "Slamet Rahardjo"
        val actorRole = actor?.role?.shortName ?: "Ketua"
        return AdminLog(
            id = UUID.randomUUID().toString(),
            actorName = actorName,
            actorRole = actorRole,
            action = action,
            targetName = targetName,
            timestamp = DateTimeUtils.formatCurrentTimeHHmm()
        )
    }
}
