package world.chebur.kmp.storage

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import world.chebur.kmp.storage.model.AnotherStructure
import world.chebur.kmp.storage.model.SomeStructure
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class JsonDataStoreStorageTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val produceFile = File(context.filesDir, "datastore.json")

    @BeforeTest
    fun setUp() {
        produceFile.delete()
    }

    private fun createStorage() = createJsonDataStore(
        serializer = AnotherStructure.serializer(),
        defaultValue = AnotherStructure(),
        producePath = { produceFile.absolutePath }
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