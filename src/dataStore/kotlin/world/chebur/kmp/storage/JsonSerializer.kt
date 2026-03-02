package world.chebur.kmp.storage

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource
import okio.use

class JsonSerializer<T>(
    private val serializer: KSerializer<T>,
    override val defaultValue: T
) : OkioSerializer<T> {

    override suspend fun readFrom(source: BufferedSource): T {
        try {
            return Json.Default.decodeFromString(
                serializer,
                source.readUtf8()
            )
        } catch (e: SerializationException) {
            throw CorruptionException("Cannot read JSON", e)
        }
    }

    override suspend fun writeTo(t: T, sink: BufferedSink) {
        sink.use {
            it.writeUtf8(Json.Default.encodeToString(serializer, t))
        }
    }
}