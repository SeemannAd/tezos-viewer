package de.showcase.tezos_viewer.domains.block

import Block
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import de.showcase.tezos_viewer.R
import de.showcase.tezos_viewer.domains.composables.Chip
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun BlockScreen(viewModel: BlockViewModel, onBackTap: () -> Unit){

    val block by viewModel.block.collectAsState(Block())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    )
    { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(horizontal = 12.dp, vertical = 14.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        modifier = Modifier.padding(0.dp),
                        onClick =  onBackTap) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Chip(label = "Hash", value = "${block.hash}")
                    Spacer(modifier = Modifier.size(12.dp, 12.dp))
                    Chip(label = "Cycle", value = "${block.cycle}")
                }

                Spacer(modifier = Modifier.size(12.dp, 12.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    )
                    {
                        Text(
                            text = "Proposer:",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "${block.proposer?.address?.take(24)}...",
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    )
                    {
                        Text(
                            text = "Producer:",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "${block.producer?.address?.take(36)}...",
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}

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