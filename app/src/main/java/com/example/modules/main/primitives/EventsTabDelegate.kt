package com.example.modules.main.primitives

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.viewmodel.*
import com.example.modules.events.CommunityEventsScreen
import com.example.shared.models.AppVisualTheme
import com.example.shared.models.DriverMember
import com.example.shared.utils.rememberSharedRecordBadgeState

@Composable
fun EventsTabDelegate(
    viewModel: DRGViewModel,
    currentMember: DriverMember?,
    visualTheme: AppVisualTheme,
    scaffoldPadding: PaddingValues
) {
    val events by viewModel.communityEvents.collectAsStateWithLifecycle()
    val tasks by viewModel.committeeTasks.collectAsStateWithLifecycle()
    val feedback by viewModel.eventFeedback.collectAsStateWithLifecycle()
    val seenState = rememberSharedRecordBadgeState(listOf("event"))
    val activityCount = computeEventActivityCount(events, feedback)

    LaunchedEffect(activityCount) { seenState.markSeen("event", activityCount) }

    CommunityEventsScreen(
        events = events,
        tasks = tasks,
        feedbackList = feedback,
        currentMember = currentMember,
        visualTheme = visualTheme,
        contentPadding = scaffoldPadding,
        onBack = { viewModel.selectTab(MainNavTab.DASHBOARD) },
        onCreateEvent = { t, cat, desc, dt, ts, loc, cid, cname, rab, fnd, att, notes, draft ->
            viewModel.createCommunityEvent(t, cat, desc, dt, ts, loc, cid, cname, rab, fnd, att, notes, draft)
        },
        onUpdateEventStage = { evId, stage, notes -> viewModel.updateCommunityEventStage(evId, stage, notes) },
        onAddTask = { evId, div, title, pic, dl, bud -> viewModel.addCommitteeTask(evId, div, title, pic, dl, bud) },
        onUpdateTask = { tId, prog, st, note -> viewModel.updateCommitteeTask(tId, prog, st, note) },
        onSubmitFeedback = { evId, cat, sev, msg -> viewModel.submitEventFeedback(evId, cat, sev, msg) },
        onResolveFeedback = { fbId, resp -> viewModel.resolveEventFeedback(fbId, resp) }
    )
}
