package world.chebur.kmp.storage

import kotlinx.coroutines.flow.Flow

interface KmpStorage<T> {
    val data: Flow<T>
    suspend fun updateData(transform: (T) -> T)
}