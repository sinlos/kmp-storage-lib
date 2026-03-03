package world.chebur.kmp.storage

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class BaseKeyValueStorageTest {
    abstract fun createStorage(): KeyValueStorage

    @Test
    fun emptyStorageTest() = runTest {
        val storage = createStorage()
        assertEquals(storage.size(), 0)
    }

    @Test
    fun defaultValueTest() = runTest {
        val storage = createStorage()
        assertEquals(storage.getString("test_key"), "")
        assertEquals(storage.getInt("test_key"), 0)
        assertEquals(storage.getLong("test_key"), 0L)
        assertEquals(storage.getDouble("test_key"), 0.0)
        assertEquals(storage.getBoolean("test_key"), false)
        assertEquals(storage.getFloat("test_key"), 0f)
    }

    @Test
    fun dataTypesTest() = runTest {
        val storage = createStorage()
        storage.withValue("test_key", "test_value")
        assertEquals(storage.getString("test_key"), "test_value")
        storage.withValue("test_key", 1)
        assertEquals(storage.getInt("test_key"), 1)
        storage.withValue("test_key", 2L)
        assertEquals(storage.getLong("test_key"), 2L)
        storage.withValue("test_key", 3.3)
        assertEquals(storage.getDouble("test_key"), 3.3)
        storage.withValue("test_key", true)
        assertEquals(storage.getBoolean("test_key"), true)
        storage.withValue("test_key", 4f)
        assertEquals(storage.getFloat("test_key"), 4f)
        assertEquals(storage.size(), 1)
    }

    @Test
    fun dataIsPersistedBetweenInstances() = runTest {
        val storage1 = createStorage()
        storage1.withValue("test_key", "test_value")

        val storage2 = createStorage()
        assertEquals(storage2.getString("test_key"), "test_value")
    }
}