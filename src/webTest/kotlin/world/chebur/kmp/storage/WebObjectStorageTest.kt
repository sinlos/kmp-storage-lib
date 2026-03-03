package world.chebur.kmp.storage

import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.serializer
import world.chebur.kmp.storage.model.AnotherStructure
import world.chebur.kmp.storage.model.SomeStructure
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WebObjectStorageTest {

    private val storageKey = "test-storage-key"

    @BeforeTest
    fun setUp() {
        localStorage.removeItem(storageKey)
    }

    private fun createStorage() = WebObjectStorage(
        key = storageKey,
        encoder = JsonKmpEncoder(serializer<AnotherStructure>()),
        defaultValue = AnotherStructure()
    )

    @Test
    fun test() = runTest {
        val storage = createStorage()

        assertEquals(storage.data.first().some2, AnotherStructure().some2)

        storage.updateData {
            it.copy(some2 = SomeStructure(i = 3, b = true))
        }
        assertEquals(storage.data.first().some2, SomeStructure(i = 3, b = true))
    }

}