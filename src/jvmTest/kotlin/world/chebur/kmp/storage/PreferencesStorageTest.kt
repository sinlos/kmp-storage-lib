package world.chebur.kmp.storage

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.junit.After
import java.io.File
import kotlin.test.BeforeTest

class PreferencesStorageTest: BaseKeyValueStorageTest() {
    private var testScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private val produceFile = File.createTempFile("datastore", ".preferences_pb")

    private lateinit var _storage: PreferencesStorage

    @BeforeTest
    fun setUp() {
        produceFile.delete()
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

    override fun createStorage(): PreferencesStorage {
        if (!this::_storage.isInitialized) {
            _storage = PreferencesStorage(createPreferencesDataStore({ produceFile.absolutePath }, testScope))
        }
        return _storage
    }

}