package de.showcase.tezos_viewer.domains.blocks

import Block
import android.health.connect.datatypes.PlannedExerciseBlock
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import de.showcase.tezos_viewer.R
import de.showcase.tezos_viewer.domains.composables.Chip
import kotlinx.coroutines.delay


@Composable
fun BlocksScreen(viewModel: BlocksViewModel, onCardTap: (String) -> Unit) {
    val blocks by viewModel.blocks.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        // viewModel.fetchBlocksFromRemote()
        viewModel.fetchFromAssets()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
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
                AnimatedBackground(
                    enabled = false
                ) {
                    BlocksHeader(
                        enabled = false,
                        data = BlocksHeaderData(
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
                        items(blocks.size) { index ->
                            BlockCard(block = blocks[index]!!, onTap = onCardTap)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedBackground(
    label: String = "Background Animation [Gradient]",
    enabled: Boolean = true,
    duration: Int = 5000,
    colors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.surfaceBright
    ),
    content: @Composable () -> Unit,
) {
    if(!enabled) return Column{ content() }

    val transition = rememberInfiniteTransition(label)

    val animatedValue by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = label
    )

    val gradientColors = listOf(
        colors[0].copy(alpha = 1f - animatedValue),
        colors[1].copy(alpha = animatedValue),
        colors[2].copy(alpha = animatedValue)
    )

    val brush = Brush.linearGradient(gradientColors)

    Column(
        modifier = Modifier.background(brush)
    ) {
        content()
    }
}

@Composable
fun BlockCard(block: Block, onTap: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                if(block.hash == null) return@clickable
                onTap(block.hash)
            }
        ,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.surface),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .height(100.dp)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 6.dp, vertical = 4.dp)
                    .weight(0.30f)
            ) {

                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxSize(),
                )
                {
                    Priority(priority = block.priority ?: 0)

                    Column {
                        Text(
                            text = block.date,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.surfaceBright,
                        )

                        Text(
                            text = block.time,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.surfaceBright,
                        )
                    }
                }
            }


            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(0.70f)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(horizontal = 6.dp, vertical = 4.dp)
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


                Text(
                    text = "Reward: ${block.reward}ꜩ, Fee: ${block.fees}ꜩ, Bonus: ${block.bonus}ꜩ",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.surfaceBright
                )
            }
        }


    }
}

@Composable
fun Priority(priority: Int) {
    Row {
        if (priority == 0) {
            Icon(
                painter = painterResource(id = R.drawable.star),
                contentDescription = "Zero Priority",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(14.dp)
            )
        }

        val convertedPriority = if (priority > 5) 5 else priority
        for (i in 1..(convertedPriority)) {
            Icon(
                painter = painterResource(id = R.drawable.star),
                contentDescription = "[$priority] Priority",
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
        }
    }
}

data class BlocksHeaderData(
    val netName: String,
    val blocksCount: Int,
    val cycle: Int
)

@Composable
fun BlocksHeader(
    enabled: Boolean = true,
    data: BlocksHeaderData
) {

    var countdown by remember { mutableIntStateOf(60) }

    if(enabled) {
        LaunchedEffect(Unit) {
            while (true) {
                while (countdown > 0) {
                    delay(1000)
                    countdown--
                }
                countdown = 60
            }
        }
    }

    // adjust progress for clockwise indication
    val progress = 1f - (countdown / 60f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        BlocksCycle(label = "ꜩ", progress = progress)

        Row(
            modifier = Modifier
                .weight(1f),
        )
        {
            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "Tezos ${data.netName}",
                    color = MaterialTheme.colorScheme.inversePrimary,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                Chip(label = "next block in", value = "${countdown}sec")
                Spacer(modifier = Modifier.size(4.dp, 4.dp))
                Chip(label = "cycle", value = "${data.cycle}")
            }
        }
    }
}

@Composable
fun BlocksCycle(
    label: String,
    progress: Float
) {

    Box(
        contentAlignment = Alignment.Center
    ) {
        val outerCircleSize = 44.dp
        val innerCircleSize = 36.dp

        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(outerCircleSize),
            strokeCap = StrokeCap.Square,
            gapSize = 0.dp,
            color = MaterialTheme.colorScheme.outline,
            trackColor = MaterialTheme.colorScheme.surface
        )
        Box(
            modifier = Modifier
                .size(innerCircleSize)
                .background(
                    MaterialTheme.colorScheme.secondary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.surfaceBright,
                textAlign = TextAlign.Center
            )
        }
    }
}