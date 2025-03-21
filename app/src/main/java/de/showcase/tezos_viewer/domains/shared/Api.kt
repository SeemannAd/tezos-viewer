package de.showcase.tezos_viewer.domains.shared

import de.showcase.tezos_viewer.environment.Environment
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

class Api(
    val timeout: Long = 5000,
    val environment: Environment,
    private val engine: HttpClientEngine = CIO.create(),
) {
    val ktorClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json()
        }
    }
}