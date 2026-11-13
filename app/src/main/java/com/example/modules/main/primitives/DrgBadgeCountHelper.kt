package com.example.modules.main.primitives

import androidx.compose.runtime.Composable
import com.example.shared.models.TaskStatus
import com.example.shared.utils.rememberSharedRecordBadgeState

internal data class CommunityLiveCounts(
    val kasCount: Int,
    val forumCount: Int,
    val tasksCount: Int,
    val membersCount: Int,
    val eventsCount: Int,
    val totalUnreadCommunity: Int
)

private val COMMUNITY_BADGE_KEYS = listOf("kas", "forum", "tasks", "members", "event")

@Composable
internal fun rememberCommunityLiveCounts(
    kasCount: Int,
    forumCount: Int,
    tasksCount: Int,
    membersCount: Int,
    eventsCount: Int
): CommunityLiveCounts {
    val seenState = rememberSharedRecordBadgeState(COMMUNITY_BADGE_KEYS)
    val unread = seenState.badgeFor("kas", kasCount) +
        seenState.badgeFor("forum", forumCount) +
        seenState.badgeFor("tasks", tasksCount) +
        seenState.badgeFor("members", membersCount) +
        seenState.badgeFor("event", eventsCount)
    return CommunityLiveCounts(kasCount, forumCount, tasksCount, membersCount, eventsCount, unread)
}

internal fun computeForumActivityCount(posts: List<com.example.shared.models.ForumPost>): Int =
    posts.sumOf { 1 + it.commentsCount }

internal fun computeOpenTasksCount(tasks: List<com.example.shared.models.CommunityTask>): Int =
    tasks.count { it.status == TaskStatus.OPEN || it.status == TaskStatus.IN_PROGRESS }

internal fun computeEventActivityCount(
    events: List<com.example.shared.models.CommunityEventEntity>,
    feedback: List<com.example.shared.models.EventMemberFeedbackEntity>
): Int = events.size + feedback.count { !it.isResolved }
