package de.showcase.tezos_viewer.domains.blocks

import Block
import de.showcase.tezos_viewer.domains.shared.Api
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import io.ktor.http.headers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
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
            withContext(Dispatchers.Default.limitedParallelism(1)) {
                withTimeout(api.timeout) {
                    api.ktorClient.get(request.url) {
                        headers {
                            if (checkForProAccess()) {
                                append("Authorization", "Bearer ${request.apiKey}")
                            }
                        }
                    }
                }
            }.let { response ->
                Timber.d("getBlocks():\nrequest=$request\nresponse=$response")

                when (response.status.value) {
                    200 -> {
                        return response.body<List<Block>>()
                    }

                    429 -> {
                        throw Exception("${response.status} Rate limit exceeded!")
                    }

                    else -> {
                        throw Exception("${response.status} Could not fetch blocks!")
                    }
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