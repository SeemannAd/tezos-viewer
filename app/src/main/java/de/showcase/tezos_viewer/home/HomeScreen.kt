package de.showcase.tezos_viewer.home

import Blocks
import Content
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val blocks by viewModel.blocks.collectAsState(null)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = viewModel.route,
                    modifier = Modifier.padding(16.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp)
            ) {
                items(blocks?.size ?: 0) { index ->
                    BlockItem(blocks!![index])
                }
            }

            // Button at the bottom
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    viewModel.fetchFromAssets()
                }
            ) {
                Text(text = "Fetch")
            }
        }
    }
}

@SuppressLint("StaticFieldLeak")
class HomeViewModel(
    private val context: Context,
) : ViewModel() {
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

    private val block: MutableStateFlow<Blocks?> = MutableStateFlow(null)
    val blocks: MutableStateFlow<List<Blocks>?> = MutableStateFlow(null)

    fun fetchFromAssets() {
        viewModelScope.launch {
            try {

                blocks.value = withContext(Dispatchers.IO) {
                    val jsonString = context
                        .assets
                        .open("blocks.json")
                        .bufferedReader()
                        .use { it.readText() }

                    Json.decodeFromString<List<Blocks>>(jsonString)
                }
            } catch (e: Exception) {
                Timber.e("Could not fetch bakers! $e")
                blocks.value = null
            }
        }
    }

    fun fetchBlocksFromRemote() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    client.get("https://api.tzkt.io/v1/blocks")
                }

                val raw = response.body<ArrayList<Blocks>>().first()
                block.value = raw
            } catch (e: Exception) {
                Timber.e("Could not fetch bakers! $e")
                blocks.value = null
            }
        }
    }
}

@Composable
fun BlockItem(block: Blocks) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "Block: ${block.priority} ${block.hash} ${block.cycle} ${block.level}",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(text = "Timestamp: ${block.timestamp}")
        Text(text = "Baker: ${block.baker?.alias} (${block.baker?.address})")
        Spacer(modifier = Modifier.height(8.dp))
    }
}