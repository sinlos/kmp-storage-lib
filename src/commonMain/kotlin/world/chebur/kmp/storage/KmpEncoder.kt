package world.chebur.kmp.storage

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import com.squareup.wire.ProtoAdapter
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

interface KmpEncoder<T> {
    fun encode(value: T): String
    fun decode(string: String): T
}

class JsonKmpEncoder<T>(
    private val serializer: KSerializer<T>,
    private val json: Json = Json
) : KmpEncoder<T> {
    override fun encode(value: T): String = json.encodeToString(serializer, value)
    override fun decode(string: String): T = json.decodeFromString(serializer, string)
}

class WireKmpEncoder<T : Any>(
    private val adapter: ProtoAdapter<T>
) : KmpEncoder<T> {
    @OptIn(ExperimentalEncodingApi::class)
    override fun encode(value: T): String {
        return Base64.encode(adapter.encode(value))
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun decode(string: String): T {
        val bytes = Base64.decode(string)
        return adapter.decode(bytes)
    }
}

object StringEncoder: KmpEncoder<String> {
    override fun encode(value: String) = value

    override fun decode(string: String) = string
}