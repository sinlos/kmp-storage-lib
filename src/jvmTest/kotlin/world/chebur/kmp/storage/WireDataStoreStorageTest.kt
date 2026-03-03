package world.chebur.kmp.storage

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import world.chebur.kmp.storage.model.ServerStatistics
import world.chebur.kmp.storage.model.UserStatistics
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WireDataStoreStorageTest {

    private val produceFile = File.createTempFile("datastore", ".pb")

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