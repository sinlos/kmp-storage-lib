package world.chebur.kmp.storage

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import world.chebur.kmp.storage.model.ServerStatistics
import world.chebur.kmp.storage.model.UserStatistics
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class WireDataStoreStorageTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val produceFile = File(context.filesDir, "datastore.pb")

    @BeforeTest
    fun setUp() {
        produceFile.delete()
    }

    private fun createStorage() = createWireDataStore(
        adapter = UserStatistics.ADAPTER,
        defaultValue = UserStatistics(),
        producePath = { produceFile.absolutePath }

    )

    @Test
    fun test() = runTest {
        val storage = createStorage()

        assertEquals(storage.data.first(), UserStatistics())

        storage.updateData {
            it.copy(last_server_stats = ServerStatistics(name = "server"))
        }
        assertEquals(storage.data.first().last_server_stats, ServerStatistics(name = "server"))
    }

}