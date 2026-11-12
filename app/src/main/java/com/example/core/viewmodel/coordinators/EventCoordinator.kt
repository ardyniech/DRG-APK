package com.example.core.viewmodel.coordinators

import com.example.core.repository.DRGRepository
import com.example.shared.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

class EventCoordinator(
    private val repository: DRGRepository,
    private val scope: CoroutineScope,
    private val showToast: (String) -> Unit
) {
    private fun id(prefix: String) = "$prefix-${UUID.randomUUID().toString().take(8).uppercase()}"

    fun saveEvent(cur: DriverMember?, event: CommunityEvent, isNew: Boolean) {
        if (cur == null) return
        val now = System.currentTimeMillis()
        val e = event.copy(createdBy = if (isNew) cur.name else event.createdBy, updatedAt = now)
        scope.launch {
            runCatching { repository.upsertCommunityEvent(e) }
                .onSuccess { showToast(if (isNew) "Event dibuat." else "Event diperbarui.") }
                .onFailure { showToast("Gagal: ${it.localizedMessage}") }
        }
    }

    fun deleteEvent(event: CommunityEvent) = scope.launch {
        runCatching { repository.deleteCommunityEvent(event) }
            .onSuccess { showToast("Event dihapus.") }
            .onFailure { showToast("Gagal: ${it.localizedMessage}") }
    }

    fun saveCommittee(eventId: String, name: String, role: String, phone: String, responsibility: String, existingId: String? = null) {
        val m = EventCommitteeMember(existingId ?: id("PAN"), eventId, "", name, role, phone, responsibility)
        scope.launch { runCatching { repository.upsertEventCommittee(m) }.onSuccess { showToast("Panitia disimpan.") } }
    }

    fun deleteCommittee(m: EventCommitteeMember) = scope.launch { repository.deleteEventCommittee(m); showToast("Panitia dihapus.") }

    fun saveFunding(eventId: String, type: EventFundingType, amount: Long, category: String, src: String, desc: String, date: String, proof: String, by: String, existingId: String? = null) {
        val f = EventFunding(existingId ?: id("DANA"), eventId, type, amount, category, src, desc, date, proof, by)
        scope.launch { runCatching { repository.upsertEventFunding(f) }.onSuccess { showToast("Dana dicatat.") } }
    }

    fun deleteFunding(f: EventFunding) = scope.launch { repository.deleteEventFunding(f); showToast("Catatan dana dihapus.") }

    fun saveLpj(eventId: String, status: EventLpjStatus, summary: String, income: Long, expense: Long, notes: String, by: String, existingId: String? = null) {
        val r = EventLpjReport(existingId ?: id("LPJ"), eventId, status, summary, income, expense, income - expense, notes, by, System.currentTimeMillis())
        scope.launch { runCatching { repository.upsertEventLpj(r) }.onSuccess { showToast("LPJ disimpan.") } }
    }

    fun saveStakeholder(eventId: String, name: String, role: String, org: String, contact: String, contribution: String, existingId: String? = null) {
        val s = EventStakeholder(existingId ?: id("STK"), eventId, name, role, org, contact, contribution)
        scope.launch { runCatching { repository.upsertEventStakeholder(s) }.onSuccess { showToast("Pihak terkait disimpan.") } }
    }

    fun deleteStakeholder(s: EventStakeholder) = scope.launch { repository.deleteEventStakeholder(s); showToast("Pihak terkait dihapus.") }
}
