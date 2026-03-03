package world.chebur.kmp.storage

import kotlinx.coroutines.flow.first

interface KeyValueStorage: KmpStorage<KmpPrefsMap> {
    suspend fun size() = data.first().size

    suspend fun withValue(key: String, value: String) = updateData { it.withValue(key, value) }
    suspend fun withValue(key: String, value: Long) = updateData { it.withValue(key, value) }
    suspend fun withValue(key: String, value: Double) = updateData { it.withValue(key, value) }
    suspend fun withValue(key: String, value: Boolean) = updateData { it.withValue(key, value) }
    suspend fun withValue(key: String, value: Float) = updateData { it.withValue(key, value) }
    suspend fun withValue(key: String, value: Int) = updateData { it.withValue(key, value) }

    suspend fun getString(key: String, defaultValue: String = "") = data.first().getString(key, defaultValue)
    suspend fun getInt(key: String, defaultValue: Int = 0) = data.first().getInt(key, defaultValue)
    suspend fun getLong(key: String, defaultValue: Long = 0L) = data.first().getLong(key, defaultValue)
    suspend fun getDouble(key: String, defaultValue: Double = 0.0) = data.first().getDouble(key, defaultValue)
    suspend fun getBoolean(key: String, defaultValue: Boolean = false) = data.first().getBoolean(key, defaultValue)
    suspend fun getFloat(key: String, defaultValue: Float = 0f) = data.first().getFloat(key, defaultValue)

}