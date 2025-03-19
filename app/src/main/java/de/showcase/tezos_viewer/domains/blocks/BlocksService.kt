package de.showcase.tezos_viewer.domains.blocks

import Block
import de.showcase.tezos_viewer.environment.Environment
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class Api(
    val environment: Environment,
    private val engine: HttpClientEngine = CIO.create(),
) {
    val ktorClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
}

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