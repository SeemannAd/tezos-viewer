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
    val environment: Environment = Environment(),
    private val engine: HttpClientEngine = CIO.create()) {
    val ktorClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    /*
    val ktorClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
     */
}

class BlocksService(private val api: Api = Api()) {

    /*
    private val ktorClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
     */

    suspend fun getBlocks(): List<Block> {
        val url = Url(api.environment.endPointBlocks)
        return try {
            // val response = ktorClient.get(url)
            val response = api.ktorClient.get(url)
            val body = response.body<List<Block>>()

            body
        } catch (e: Exception) {
            emptyList()
        }
    }
}