package com.example.modules.members

import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import com.example.shared.utils.DateTimeUtils
import java.util.*

object ReportGenerator {
    fun generateMembersMarkdown(
        filteredMembers: List<DriverMember>,
        filterRoleByPengurusOnly: Boolean,
        filterStatusSelected: VerificationStatus?,
        searchQuery: String
    ): String {
        val timestamp = DateTimeUtils.formatCurrentDateTimeReadable()
        return buildString {
            appendLine("# LAPORAN ANGGOTA DRG MALANG RAYA")
            appendLine("Dibuat pada: $timestamp")
            appendLine("Filter: ${if (filterRoleByPengurusOnly) "Hanya Pengurus" else "Semua"}, Status: ${filterStatusSelected?.name ?: "Semua"}, Kueri: \"$searchQuery\"")
            appendLine("Total Data: ${filteredMembers.size} Driver")
            appendLine()
            appendLine("| No | ID Anggota | Nama Driver | Pelat Nomor | Peran/Jabatan | Status | Rating |")
            appendLine("|---|---|---|---|---|---|---|")
            filteredMembers.forEachIndexed { idx, m ->
                appendLine("| ${idx + 1} | ${m.id} | ${m.name} | ${m.motorcyclePlate} | ${m.role.title} | ${m.verificationStatus.name} | ${m.rating} |")
            }
            appendLine()
            appendLine("## Ringkasan Eksekutif")
            appendLine("- Total Driver Terdaftar: ${filteredMembers.size}")
            appendLine("- Pengurus Aktif: ${filteredMembers.count { it.role != MemberRole.ANGGOTA }}")
            appendLine("- Rata-rata Rating Komunitas: ${String.format(Locale.getDefault(), "%.2f", if (filteredMembers.isEmpty()) 0.0 else filteredMembers.map { it.rating }.average())} ⭐")
            appendLine()
            appendLine("*Laporan ini sah dikeluarkan oleh sistem Otoritas DRG Malang Raya*")
        }
    }
}
