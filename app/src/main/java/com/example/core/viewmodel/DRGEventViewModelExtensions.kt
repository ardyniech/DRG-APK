package com.example.core.viewmodel

import com.example.shared.models.*

fun DRGViewModel.saveCommunityEvent(event: CommunityEvent, isNew: Boolean) =
    eventCoord.saveEvent(currentMember.value, event, isNew)

fun DRGViewModel.deleteCommunityEvent(event: CommunityEvent) = eventCoord.deleteEvent(event)

fun DRGViewModel.saveEventCommittee(eventId: String, name: String, role: String, phone: String, responsibility: String, existingId: String? = null) =
    eventCoord.saveCommittee(eventId, name, role, phone, responsibility, existingId)

fun DRGViewModel.deleteEventCommittee(m: EventCommitteeMember) = eventCoord.deleteCommittee(m)

fun DRGViewModel.saveEventFunding(eventId: String, type: EventFundingType, amount: Long, category: String, src: String, desc: String, date: String, proof: String, existingId: String? = null) =
    eventCoord.saveFunding(eventId, type, amount, category, src, desc, date, proof, currentMember.value?.name ?: "", existingId)

fun DRGViewModel.deleteEventFunding(f: EventFunding) = eventCoord.deleteFunding(f)

fun DRGViewModel.saveEventLpj(eventId: String, status: EventLpjStatus, summary: String, income: Long, expense: Long, notes: String, existingId: String? = null) =
    eventCoord.saveLpj(eventId, status, summary, income, expense, notes, currentMember.value?.name ?: "", existingId)

fun DRGViewModel.saveEventStakeholder(eventId: String, name: String, role: String, org: String, contact: String, contribution: String, existingId: String? = null) =
    eventCoord.saveStakeholder(eventId, name, role, org, contact, contribution, existingId)

fun DRGViewModel.deleteEventStakeholder(s: EventStakeholder) = eventCoord.deleteStakeholder(s)

fun DRGViewModel.observeCommunityEvent(eventId: String) = repository.observeCommunityEvent(eventId)
fun DRGViewModel.eventCommittee(eventId: String) = repository.eventCommittee(eventId)
fun DRGViewModel.eventFunding(eventId: String) = repository.eventFunding(eventId)
fun DRGViewModel.eventLpj(eventId: String) = repository.eventLpj(eventId)
fun DRGViewModel.eventStakeholders(eventId: String) = repository.eventStakeholders(eventId)
