package world.chebur.kmp.storage.model

import kotlinx.serialization.Serializable

@Serializable
data class SomeStructure(
    val i: Int = 0,
    val b: Boolean = false
)