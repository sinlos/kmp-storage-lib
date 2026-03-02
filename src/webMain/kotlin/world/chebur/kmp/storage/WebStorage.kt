package world.chebur.kmp.storage

import kotlinx.browser.window
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class WebStorage<T>(
    private val key: String,
    private val encoder: KmpEncoder<T>,
    private val defaultValue: T
) : KmpStorage<T> {

    private val _state = MutableStateFlow(load())
    override val data: Flow<T> = _state

    private fun load(): T {
        val saved = window.localStorage.getItem(key) ?: return defaultValue
        return try {
            encoder.decode(saved)
        } catch (_: Exception) {
            defaultValue
        }
    }

    override suspend fun updateData(transform: (T) -> T) {
        val nextValue = transform(_state.value)
        _state.value = nextValue
        window.localStorage.setItem(key, encoder.encode(nextValue))
    }
}