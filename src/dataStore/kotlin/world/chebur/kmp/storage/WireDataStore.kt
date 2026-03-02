package world.chebur.countdown.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import okio.FileSystem
import okio.Path.Companion.toPath

fun <T : Message<T, Nothing>> createWireDataStore(
    adapter: ProtoAdapter<T>,
    defaultValue: T,
    producePath: () -> String
): DataStore<T> {
    val dataStore = DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = WireSerializer(adapter, defaultValue),
            producePath = { producePath().toPath() }
        )
    )
    return dataStore
}
