package de.showcase.tezos_viewer.domains.shared.services

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.showcase.tezos_viewer.tester.isRunningOnEmulator
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

import org.junit.runner.RunWith


/**
 * This test must run on real device, since the path to the storage is not accessible
 */
@RunWith(AndroidJUnit4::class)
class StoreServiceTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val storeService = StoreService(
        context = context
    )

    @Test
    fun `GIVEN the StoreService WHEN something is stored THEN return a successfully StoreResult must be returned`() =
        runTest {
            if (isRunningOnEmulator()) return@runTest

            val expected = StoreResult(
                success = true,
            )

            val storeData = StoreData(
                fileName = "test_file.txt",
                data = "some test data"
            )

            val actual = storeService.write(storeData)

            assertEquals("Message: ${actual.message}", expected.success, actual.success)

            storeService.clear()
        }

    @Test
    fun `GIVEN the StoreService WHEN no file is available THEN !success must be returned`() =
        runTest {
            if (isRunningOnEmulator()) return@runTest

            val fileName = "test_file.txt"

            val actual = storeService.read(fileName)

            assertEquals("Message: ${actual.message}", false, actual.success)
        }
}