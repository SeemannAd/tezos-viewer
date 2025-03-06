package de.showcase.tezos_viewer.home

import Blocks
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val status by viewModel.status.collectAsState("")
    val blocks by viewModel.blocks.collectAsState(null)
    val loading by viewModel.loading.collectAsState(false)

    var counter = 0

    LaunchedEffect(Unit) {
        viewModel.fetchBakers()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Text(text = viewModel.route)
            if (loading) {
                CircularProgressIndicator()
            }

            Text(text = "[$counter] $status > bakers found ${blocks?.hash}")

            Button(
                enabled = !loading,
                onClick = {
                    viewModel.fetchBakers()
                    counter++
                }) {
                Text(text = "fetch")
            }
        }

    }
}

class HomeViewModel : ViewModel() {
    val route = "/home"

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
    }

    val blocks: MutableStateFlow<Blocks?> = MutableStateFlow(null)
    val status: MutableStateFlow<String> = MutableStateFlow("n/a")
    val loading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun fetchBakers() {
        viewModelScope.launch {
            try {
                loading.value = true

                val response = withContext(Dispatchers.IO) {
                    client.get("https://api.tzkt.io/v1/blocks")
                }

                status.value = response.status.value.toString()

                val raw = response.body<ArrayList<Blocks>>().first()
                blocks.value = raw
            } catch (e: Exception) {
                Timber.e("Could not fetch bakers! $e")
                status.value = "Error: ${e.message}"
                blocks.value = null
            }

            loading.value = false
        }
    }
}
