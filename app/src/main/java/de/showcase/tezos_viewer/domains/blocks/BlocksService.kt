package de.showcase.tezos_viewer.domains.blocks

import Block
import de.showcase.tezos_viewer.domains.shared.Api
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url

class BlocksService(private val api: Api) {

    fun checkForProAccess() : Boolean {
        return api.environment.apiKey.isNotEmpty()
    }

    suspend fun getBlocks(): List<Block> {
        val url = Url(api.environment.endPointBlocks)
        return try {
            val response = api.ktorClient.get(url)
            val body = response.body<List<Block>>()

            body
        } catch (e: Exception) {
            // https://api.tzkt.io/#section/Get-Started/Free-TzKT-API
            // if status code is 429 than rate limit has been exceeded
            emptyList()
        }
    }
}