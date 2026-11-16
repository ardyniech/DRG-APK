package com.example.core.sync

/**
 * Elemen CRDT Last-Write-Wins (LWW-Element-Set) untuk rekonsiliasi data multi-device
 * tanpa database locking. Jika timestamp sama, nodeId leksikografis menjadi pemutus seri deterministik.
 */
data class CrdtRegister<T>(
    val value: T,
    val updatedAtMillis: Long,
    val nodeId: String,
    val deleted: Boolean = false
)

object LwwCrdtReconciler {

    fun <T> merge(local: CrdtRegister<T>, remote: CrdtRegister<T>): CrdtRegister<T> {
        return when {
            remote.updatedAtMillis > local.updatedAtMillis -> remote
            remote.updatedAtMillis < local.updatedAtMillis -> local
            remote.nodeId > local.nodeId -> remote
            else -> local
        }
    }

    fun <T> mergeMap(
        local: Map<String, CrdtRegister<T>>,
        remote: Map<String, CrdtRegister<T>>
    ): Map<String, CrdtRegister<T>> {
        val merged = LinkedHashMap<String, CrdtRegister<T>>(local.size + remote.size)
        for ((key, reg) in local) merged[key] = reg
        for ((key, remoteReg) in remote) {
            val current = merged[key]
            merged[key] = if (current == null) remoteReg else merge(current, remoteReg)
        }
        return merged
    }

    fun <T> activeValues(state: Map<String, CrdtRegister<T>>): Map<String, T> =
        state.filterValues { !it.deleted }.mapValues { it.value.value }
}
