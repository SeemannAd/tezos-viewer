package de.showcase.tezos_viewer.domains.blocks

import Block
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber

@SuppressLint("StaticFieldLeak")
class BlocksViewModel(
    private val context: Context,
    private val blocksService: BlocksService,
) : ViewModel() {

    val route = "/blocks"

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

    fun fetchBlocksFromRemote() : Job {
        return viewModelScope.launch {
            blocks.value = withContext(Dispatchers.IO) {
                blocksService.getBlocks()
            }
        }
    }
}