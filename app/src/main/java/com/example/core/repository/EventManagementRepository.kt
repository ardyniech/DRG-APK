package com.example.core.repository

import com.example.core.database.AppDatabase
import com.example.shared.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EventManagementRepository(private val db: AppDatabase) {
    private val dao = db.communityEventDao()
    val allEvents: Flow<List<CommunityEvent>> = dao.getAllEvents()

    fun observeEvent(eventId: String): Flow<CommunityEvent?> = dao.observeEvent(eventId)
    fun committee(eventId: String): Flow<List<EventCommitteeMember>> = dao.getCommittee(eventId)
    fun funding(eventId: String): Flow<List<EventFunding>> = dao.getFunding(eventId)
    fun lpj(eventId: String): Flow<EventLpjReport?> = dao.observeLpj(eventId)
    fun stakeholders(eventId: String): Flow<List<EventStakeholder>> = dao.getStakeholders(eventId)

    suspend fun upsertEvent(event: CommunityEvent) = withContext(Dispatchers.IO) { dao.upsertEvent(event) }
    suspend fun deleteEvent(event: CommunityEvent) = withContext(Dispatchers.IO) { dao.deleteEvent(event) }
    suspend fun upsertCommittee(m: EventCommitteeMember) = withContext(Dispatchers.IO) { dao.upsertCommitteeMember(m) }
    suspend fun deleteCommittee(m: EventCommitteeMember) = withContext(Dispatchers.IO) { dao.deleteCommitteeMember(m) }
    suspend fun upsertFunding(f: EventFunding) = withContext(Dispatchers.IO) { dao.upsertFunding(f) }
    suspend fun deleteFunding(f: EventFunding) = withContext(Dispatchers.IO) { dao.deleteFunding(f) }
    suspend fun upsertLpj(r: EventLpjReport) = withContext(Dispatchers.IO) { dao.upsertLpj(r) }
    suspend fun upsertStakeholder(s: EventStakeholder) = withContext(Dispatchers.IO) { dao.upsertStakeholder(s) }
    suspend fun deleteStakeholder(s: EventStakeholder) = withContext(Dispatchers.IO) { dao.deleteStakeholder(s) }
}
