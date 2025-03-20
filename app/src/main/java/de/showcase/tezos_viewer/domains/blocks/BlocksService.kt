package de.showcase.tezos_viewer.domains.blocks

import Block
import de.showcase.tezos_viewer.domains.shared.Api
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import io.ktor.http.headers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.util.concurrent.TimeoutException

data class Request(
    val url: Url,
    val apiKey: String,
)

class BlocksService(private val api: Api) {

    fun checkForProAccess(): Boolean {
        return api.environment.apiKey.isNotEmpty()
    }

    suspend fun getBlocks(): List<Block>? {
        val request = Request(
            url = Url(api.environment.endPointBlocks),
            apiKey = api.environment.apiKey
        )

        return try {
            val response = withTimeout(5000) {
                api.ktorClient.get(request.url) {
                    headers {
                        if (checkForProAccess()) {
                            append("Authorization", "Bearer ${request.apiKey}")
                        }
                    }
                }
            }

            Timber.d("getBlocks():\nrequest=$request\nresponse=$response")

            when (response.status.value) {
                200 -> {
                    return response.body<List<Block>>()
                }

                429 -> {
                    // https://api.tzkt.io/#section/Get-Started/Free-TzKT-API
                    // if status code is 429 than rate limit has been exceeded
                    throw Exception("${response.status} Rate limit exceeded!")
                }

                else -> {
                    throw Exception("${response.status} Could not fetch blocks!")
                }
            }
        } catch (e: TimeoutCancellationException) {
            Timber.e("getBlocks(): Timeout Error $e")
            null
        } catch (e: Exception) {
            Timber.e("getBlocks(): Error $e")
            emptyList()
        }
    }
}