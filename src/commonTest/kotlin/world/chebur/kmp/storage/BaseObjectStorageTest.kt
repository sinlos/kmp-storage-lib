//package world.chebur.kmp.storage
//
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.test.runTest
//import kotlin.test.BeforeTest
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
//abstract class BaseObjectStorageTest2<T> {
//
//    protected lateinit var storage: KmpStorage<T>
//
//    abstract fun createStorage(): KmpStorage<T>
//
//    @BeforeTest
//    fun setUp() {
//        storage = createStorage()
//    }
//
//    @Test
//    fun `initial data is default value`() = runTest {
////        assertEquals(getDefaultValue(), storage.data.first())
//    }
//
//    @Test
//    fun `updateData changes value`() = runTest {
//        // Сначала убедимся, что значение по умолчанию
////        assertEquals(getDefaultValue(), storage.data.first())
//
////        // Обновляем
////        storage.updateData { "New Value" }
////
////        // Проверяем, что оно изменилось
////        assertEquals("New Value", storage.data.first())
//    }
//
//    @Test
//    fun `updateData uses previous value`() = runTest {
////        storage.updateData { "Initial" }
////        storage.updateData { prev -> "$prev, Appended" }
////        assertEquals("Initial, Appended", storage.data.first())
//    }
//
//}