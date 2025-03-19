package de.showcase.tezos_viewer.domains

import Block
import android.content.Context
import android.content.SharedPreferences
import de.showcase.tezos_viewer.domains.blocks.Api
import de.showcase.tezos_viewer.domains.blocks.BlocksService
import de.showcase.tezos_viewer.domains.shared.extensions.read
import de.showcase.tezos_viewer.domains.shared.services.StoreDataService
import de.showcase.tezos_viewer.environment.Environment
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BlocksServiceTest {
    lateinit var testEnvironment : Environment
    lateinit var  engine:MockEngine
    lateinit var  api: Api
    lateinit var blocksService: BlocksService

    private val expectedBlocks: List<Block> = listOf(
        Block(
            cycle = 1,
            level = 1,
        )
    )

    @Before
    fun before() {
        val storeDataService = mockk<StoreDataService>()
        every { storeDataService.read(any()) } returns ""

        testEnvironment = Environment(storeDataService = storeDataService)

        engine = MockEngine { request ->
            val responseBody = Json.encodeToString<List<Block>>(expectedBlocks)

            when (request.url.toString()) {
                testEnvironment.endPointBlocks -> {
                    respond(
                        content = ByteReadChannel(content = responseBody.toByteArray()),
                        status = HttpStatusCode.OK,
                        headers = Headers.build {
                            append("Content-Type", "application/json")
                        },
                    )
                }

                else -> {
                    respond(
                        content = ByteReadChannel.Empty,
                        status = HttpStatusCode.BadRequest,
                    )
                }
            }
        }

        api = Api(
            environment = testEnvironment,
            engine = engine
        )
        blocksService = BlocksService(api = api)
    }

    @Test
    fun `GIVEN the BlocksService WHEN a getBlocks request is made THEN return a list of blocks`() =
        runTest {
            val blocks = blocksService.getBlocks()
            assertEquals(expectedBlocks, blocks)
        }
}