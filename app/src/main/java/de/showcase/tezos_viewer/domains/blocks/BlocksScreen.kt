package de.showcase.tezos_viewer.domains.blocks

import Block
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BlocksScreen(viewModel: BlocksViewModel) {
    val blocks by viewModel.blocks.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchBlocksFromRemote()
    }

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
                text = "Timestamp: ${block.timestamp}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Cycle: ${block.cycle}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = "Level: ${block.level}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "Producer: ${block.producer?.alias}",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )

            Text(
                text = "Proposer: ${block.proposer?.alias}",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )

            Text(
                text = "Transactions: ${block.transactions?.size}",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )

            Text(
                text = "Baker: ${block.baker?.alias}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "Reward: ${block.reward}ꜩ Fee: ${block.fees}ꜩ",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}