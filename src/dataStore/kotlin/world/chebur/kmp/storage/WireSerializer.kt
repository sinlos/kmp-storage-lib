package world.chebur.countdown.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.okio.OkioSerializer
import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import okio.BufferedSink
import okio.BufferedSource
import java.io.IOException

class WireSerializer<T : Message<T, Nothing>>(
    private val adapter: ProtoAdapter<T>,
    override val defaultValue: T
) : OkioSerializer<T> {

    override suspend fun readFrom(source: BufferedSource): T {
        try {
            return adapter.decode(source)
        } catch (e: IOException) {
            throw CorruptionException("Cannot read proto", e)
        }
    }

    override suspend fun writeTo(t: T, sink: BufferedSink) {
        adapter.encode(sink, t)
    }
}