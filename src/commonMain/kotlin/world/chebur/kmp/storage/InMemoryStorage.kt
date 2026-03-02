package world.chebur.kmp.storage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class InMemoryStorage<T>(defaultValue: T) : KmpStorage<T> {
    private val _state = MutableStateFlow(defaultValue)
    override val data: Flow<T> = _state
    override suspend fun updateData(transform: (T) -> T) {
        _state.value = transform(_state.value)
    }
}