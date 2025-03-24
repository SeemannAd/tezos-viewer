package de.showcase.tezos_viewer.domains.blocks

import Block
import de.showcase.tezos_viewer.domains.shared.Api
import de.showcase.tezos_viewer.domains.shared.Request
import io.ktor.http.Url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BlocksService(private val api: Api) {

    fun checkForProAccess(): Boolean = api.environment.apiKey.isNotEmpty()

    suspend fun getBlocks(): List<Block>? {
        val request = Request(
            url = Url(api.environment.endPointBlocks),
            apiKey = api.environment.apiKey
        )

        val response = withContext(Dispatchers.Default.limitedParallelism(1)) {
            api.GET<List<Block>>(request)
        }

        return when (response.statusCode) {
            200 -> {
                response.body
            }
            429 -> {
                null // Timeout
            }
            else -> {
                emptyList()
            }
        }
    }
}