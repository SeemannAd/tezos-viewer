package de.showcase.tezos_viewer.domains

import Block
import de.showcase.tezos_viewer.domains.blocks.Api
import de.showcase.tezos_viewer.domains.blocks.BlocksService
import de.showcase.tezos_viewer.environment.Environment
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class BlocksServiceTest {
    private val expectedBlocks: List<Block> = listOf(
        Block(
            cycle = 1,
            level = 1,
        )
    )

    private val testEnvironment = Environment()

    private val engine = MockEngine { request ->
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

    private val api = Api(
        environment = testEnvironment,
        engine = engine
    )

    private val blocksService = BlocksService(api = api)

    @Test
    fun `GIVEN the BlocksService WHEN a getBlocks request is made THEN return a list of blocks`() =
        runTest {
            val blocks = blocksService.getBlocks()

            assertEquals(expectedBlocks, blocks)
        }
}