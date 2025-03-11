package de.showcase.tezos_viewer.home

import Block
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val blocks by viewModel.blocks.collectAsState(emptyList())

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                if (blocks.isNotEmpty()) {
                    items(blocks.size) { index ->
                        BlockCard(block = blocks[index]!!)
                    }
                }
            }

            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
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
        /*
        install(JsonFeature) {
            serializer = KotlinxSerializer() // Use kotlinx.serialization for JSON
        }
         */

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
    }

    private val block: MutableStateFlow<Block?> = MutableStateFlow(null)
    val blocks: MutableStateFlow<List<Block?>> = MutableStateFlow(emptyList())

    fun fetchFromAssets() {
        viewModelScope.launch {
            try {

                blocks.value = withContext(Dispatchers.IO) {
                    val jsonString = context
                        .assets
                        .open("blocks.json")
                        .bufferedReader()
                        .use { it.readText() }

                    Json.decodeFromString<List<Block>>(jsonString)
                }
            } catch (e: Exception) {
                //Todo @Dev add error handling
                Timber.e("Could not fetch bakers! $e")
            }
        }
    }

    fun fetchBlocksFromRemote() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    client.get("https://api.tzkt.io/v1/blocks")
                }

                val raw = response.body<ArrayList<Block>>().first()
                block.value = raw
            } catch (e: Exception) {
                //Todo @Dev add error handling
                Timber.e("Could not fetch bakers! $e")
            }
        }
    }
}

@Composable
fun BlockCard(block: Block) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),

        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = "Baker: ${block.baker?.alias}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "Hash: ${block.hash}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Timestamp: ${block.timestamp}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Reward: ${block.reward} ꜩ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}