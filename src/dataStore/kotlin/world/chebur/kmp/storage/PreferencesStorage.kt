package world.chebur.kmp.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesStorage(
    private val dataStore: DataStore<Preferences>
) : KmpStorage<KmpPrefsMap>, KeyValueStorage {

    override val data: Flow<KmpPrefsMap> = dataStore.data.map { preferences ->
        preferences.asMap().mapNotNull { (key, value) ->
            val prefValue = when (value) {
                is String -> PreferenceValue.StringValue(value)
                is Int -> PreferenceValue.IntValue(value)
                is Long -> PreferenceValue.LongValue(value)
                is Double -> PreferenceValue.DoubleValue(value)
                is Boolean -> PreferenceValue.BooleanValue(value)
                is Float -> PreferenceValue.FloatValue(value)
                else -> null
            }
            prefValue?.let { key.name to it }
        }.toMap()
    }

    override suspend fun updateData(transform: (KmpPrefsMap) -> KmpPrefsMap) {
        dataStore.edit { preferences ->
            val currentMap = preferences.asMap().mapNotNull { (key, value) ->
                val prefValue = when (value) {
                    is String -> PreferenceValue.StringValue(value)
                    is Int -> PreferenceValue.IntValue(value)
                    is Long -> PreferenceValue.LongValue(value)
                    is Double -> PreferenceValue.DoubleValue(value)
                    is Boolean -> PreferenceValue.BooleanValue(value)
                    is Float -> PreferenceValue.FloatValue(value)
                    else -> null
                }
                prefValue?.let { key.name to it }
            }.toMap()

            val nextMap = transform(currentMap)

            // Удаляем старые ключи, которых нет в новом мапе
            val currentKeys = preferences.asMap().keys.toSet()
            currentKeys.forEach { key ->
                if (!nextMap.containsKey(key.name)) {
                    preferences.remove(key)
                }
            }

            // Добавляем/обновляем значения
            nextMap.forEach { (key, value) ->
                when (value) {
                    is PreferenceValue.StringValue -> preferences[stringPreferencesKey(key)] = value.value
                    is PreferenceValue.IntValue -> preferences[intPreferencesKey(key)] = value.value
                    is PreferenceValue.LongValue -> preferences[longPreferencesKey(key)] = value.value
                    is PreferenceValue.DoubleValue -> preferences[doublePreferencesKey(key)] = value.value
                    is PreferenceValue.BooleanValue -> preferences[booleanPreferencesKey(key)] = value.value
                    is PreferenceValue.FloatValue -> preferences[floatPreferencesKey(key)] = value.value
                }
            }
        }
    }
}

fun DataStore<Preferences>.asKmpStorage(): KmpStorage<KmpPrefsMap> = PreferencesStorage(this)

