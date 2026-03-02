package world.chebur.kmp.storage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class JvmInMemoryObjectStorageTest : BaseObjectStorageTest() {
    override fun createStorage(defaultValue: String): KmpStorage<String> {
        return InMemoryStorage(defaultValue)
    }
}

class JvmPreferencesObjectStorageTest : BaseObjectStorageTest() {
    private val testFile = File.createTempFile("test-prefs", ".preferences_pb")

    override fun createStorage(defaultValue: String): KmpStorage<String> {
        // Перед каждым тестом стираем предыдущие данные
        testFile.delete()

        val dataStore = createPreferencesDataStore { testFile.absolutePath }
        // PreferencesStorage(dataStore, defaultValue)
        return object : KmpStorage<String> {
            override val data: Flow<String>
                get() = TODO("Not yet implemented")

            override suspend fun updateData(transform: (String) -> String) {
                TODO("Not yet implemented")
            }
        }
    }

    @Test
    fun `data is persisted between instances`() = runBlocking {
        // Создаем первое хранилище и меняем данные
        val storage1 = createStorage("Initial")
        storage1.updateData { "Persisted Value" }

        // Создаем второе, указывая на тот же файл, и проверяем
        val storage2 = createStorage("Initial")
        assertEquals("Persisted Value", storage2.data.first())
    }
}