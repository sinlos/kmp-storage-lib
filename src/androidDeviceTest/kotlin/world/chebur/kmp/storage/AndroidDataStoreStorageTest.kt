package world.chebur.kmp.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class AndroidDataStoreStorageTest : BaseStorageTest() {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    override fun createStorage(defaultValue: String): KmpStorage<String> {
        // Используем контекст для создания DataStore
        val dataStore: DataStore<Preferences> = createPreferencesDataStore {
            File(context.filesDir, "test_datastore.preferences_pb").absolutePath
        }
        return PreferencesStorage(dataStore, defaultValue)
    }
}