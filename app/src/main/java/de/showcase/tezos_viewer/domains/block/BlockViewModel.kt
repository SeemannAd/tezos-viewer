package de.showcase.tezos_viewer.domains.block

import Block
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow


class BlockViewModel : ViewModel() {
    private var _route = "/block/"
    val route = _route

    val block: MutableStateFlow<Block> = MutableStateFlow(Block())

    fun setBlockIdHashId(hashId: String) {
        _route += hashId
    }

    fun setBlock(block: Block) {
        this.block.value = block
    }
}