package world.chebur.kmp.storage

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow

class DataStoreStorage<T>(
    private val dataStore: DataStore<T>
) : KmpStorage<T> {
    override val data: Flow<T> = dataStore.data

    override suspend fun updateData(transform: (T) -> T) {
        dataStore.updateData { transform(it) }
    }
}