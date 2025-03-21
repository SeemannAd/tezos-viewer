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

@SuppressLint("StaticFieldLeak")
class BlocksViewModel(
    private val context: Context,
    private val blocksService: BlocksService,
) : ViewModel() {

    val route = "/blocks"

    val blocks: MutableStateFlow<List<Block>?> = MutableStateFlow(emptyList())

    val isPro: MutableStateFlow<Boolean> = MutableStateFlow(blocksService.checkForProAccess())

    fun checkForProAccess() {
        isPro.value = blocksService.checkForProAccess()
    }

    fun fetchFromAssets(): Job {
        return viewModelScope.launch {
            blocks.value = withContext(Dispatchers.IO) {
                val jsonString = context
                    .assets
                    .open("blocks.json")
                    .bufferedReader()
                    .use { it.readText() }

                Json.decodeFromString<List<Block>>(jsonString)
            }
        }
    }

    fun fetchBlocksFromRemote(): Job {
        return viewModelScope.launch {
            blocks.value = withContext(Dispatchers.IO) {
                blocksService.getBlocks()
                    ?.filter { it.producer != null && it.proposer != null }
                    ?.sortedByDescending { it.timestamp }
            }
        }
    }
}
