package world.chebur.kmp.storage.model

import kotlinx.serialization.Serializable

@Serializable
data class AnotherStructure(
    val some1: SomeStructure = SomeStructure(i = 1, b = true),
    val some2: SomeStructure = SomeStructure(i = 2, b = false)
)