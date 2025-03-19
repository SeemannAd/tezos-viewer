package de.showcase.tezos_viewer.environment

enum class Key {
    API_KEY
}

class Environment(
    var apiKey: String = "",
    val endPointBlocks: String = "https://api.tzkt.io/v1/blocks"
) {
    fun setApiKeyFromStorage(newApiKey: String) {
        apiKey = newApiKey
    }
}