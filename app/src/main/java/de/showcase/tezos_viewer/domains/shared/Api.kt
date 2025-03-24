@file:Suppress("FunctionName")

package de.showcase.tezos_viewer.domains.shared

import de.showcase.tezos_viewer.environment.Environment
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.Url
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.StringValues
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

data class Response<T>(
    val statusCode: Int,
    val headers: StringValues,
    val body: T?,
)

data class Request(
    val url: Url,
    val apiKey: String,
)

class Api(
    val timeout: Long = 4000,
    val environment: Environment,
    private val engine: HttpClientEngine = CIO.create(),
) {
    val ktorClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend inline fun <reified T> GET(request: Request): Response<T> {
        val requestHeaders = headers {
            if (environment.apiKey.isNotEmpty()) {
                append("Authorization", "Bearer ${request.apiKey}")
            }
        }

        return try {
            withTimeout(timeout) {
                val httpResponse = ktorClient.get(request.url) {
                    headers.appendAll(requestHeaders)
                }
                Response(
                    statusCode = httpResponse.status.value,
                    headers = httpResponse.headers,
                    body = httpResponse.body<T>(),
                )
            }
        } catch (e: TimeoutCancellationException) {
            Response(
                statusCode = 429,
                headers = requestHeaders,
                body = null,
            )
        }
    }
}