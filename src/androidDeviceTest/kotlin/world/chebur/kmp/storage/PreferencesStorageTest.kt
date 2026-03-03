package world.chebur.kmp.storage

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.junit.After
import org.junit.runner.RunWith
import java.io.File
import kotlin.test.BeforeTest

@RunWith(AndroidJUnit4::class)
class PreferencesStorageTest: BaseKeyValueStorageTest() {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var testScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private val produceFile = File(context.filesDir, "test.preferences_pb")

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