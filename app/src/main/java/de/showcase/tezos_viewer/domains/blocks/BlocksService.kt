package de.showcase.tezos_viewer.domains.blocks

import Block
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class BlocksService() {
    private val ktorClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun getBlocks(): List<Block> {
        val url = Url("https://api.tzkt.io/v1/blocks")
        return try {
            val response = ktorClient.get(url)
            response.body<List<Block>>()
        } catch (e: Exception) {
            emptyList()
        }
    }
}


