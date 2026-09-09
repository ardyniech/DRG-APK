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
        val actorName = actor?.name ?: "Pengurus DRG"
        val actorRole = actor?.role?.shortName ?: "Admin"
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
