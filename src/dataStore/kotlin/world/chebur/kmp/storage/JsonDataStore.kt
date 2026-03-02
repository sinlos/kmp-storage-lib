package world.chebur.kmp.storage

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import kotlinx.serialization.KSerializer
import okio.FileSystem
import okio.Path.Companion.toPath

fun <T> createJsonDataStore(
    serializer: KSerializer<T>,
    defaultValue: T,
    producePath: () -> String
): DataStore<T> =
    DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = JsonSerializer(serializer, defaultValue),
            producePath = { producePath().toPath() }
        )
    )
