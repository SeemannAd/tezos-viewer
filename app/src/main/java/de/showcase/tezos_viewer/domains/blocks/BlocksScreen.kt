package de.showcase.tezos_viewer.domains.blocks

import Block
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun BlocksScreen(viewModel: BlocksViewModel) {
    val blocks by viewModel.blocks.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        // viewModel.fetchBlocksFromRemote()
        viewModel.fetchFromAssets()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (blocks.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column {
                    BlocksHeader(
                        header = Header(
                            netName = "Mainnet",
                            blocksCount = blocks.size,
                            cycle = blocks.first()?.cycle!!
                        )
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp)
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
                text = "Priority: ${block.priority}",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )

            Text(
                text = "Producer: ${block.producer?.address?.take(24)}...",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
            Text(
                text = "Proposer: ${block.proposer?.address?.take(24)}...",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
            Text(
                text = "Reward: ${block.reward}ꜩ Bonus: ${block.bonus}, Fee: ${block.fees}ꜩ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}

data class Header(
    val netName: String,
    val blocksCount: Int,
    val cycle: Int
)

@Composable
fun BlocksHeader(header: Header) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        BlocksCycle(blocksCount = header.blocksCount)

        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .background(color = Color.Blue)
        ) {
            Text(
                text = header.netName,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        CycleChip(cycle = header.cycle.toString())
    }
}

@Composable
fun CycleChip(cycle: String) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.secondary,
    ) {
        Text(
            text = "cycle $cycle",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun BlocksCycle(blocksCount: Int) {
    var countdown by remember { mutableIntStateOf(60) }

    LaunchedEffect(Unit) {
        while (true) {
            while (countdown > 0) {
                delay(1000)
                countdown--

            }
            countdown = 60
        }
    }

    // adjust progress for clockwise indication
    val progress = 1f - (countdown / 60f)

    Box(
        contentAlignment = Alignment.Center
    ) {
        val outerCircleSize = 44.dp
        val innerCircleSize = 36.dp

        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(outerCircleSize),
            strokeCap = StrokeCap.Square,
            color = Color.Green,
            gapSize = 0.dp,
            trackColor = ProgressIndicatorDefaults.circularDeterminateTrackColor
        )
        Box(
            modifier = Modifier
                .size(innerCircleSize)
                .background(MaterialTheme.colorScheme.secondary,
                    shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = blocksCount.toString(),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}