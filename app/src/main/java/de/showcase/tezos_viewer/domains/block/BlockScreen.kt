package de.showcase.tezos_viewer.domains.block

import Block
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.showcase.tezos_viewer.domains.shared.composables.TransactionComposable
import de.showcase.tezos_viewer.domains.shared.composables.Chip

@Composable
fun BlockScreen(viewModel: BlockViewModel, onBackTap: () -> Unit) {

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
                        onClick = onBackTap
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Chip(label = "Hash", value = "${block.hash}".take(12) + "...")
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
                            text = "${block.proposer?.address}",
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
                            text = "${block.producer?.address}",
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    LazyColumn(
                        modifier = Modifier
                            .padding(vertical = 24.dp)
                    ) {
                        items(block.transactions?.size ?: 0) { index ->
                            val transaction = block.transactions!![index]
                            val transactionData = TransactionData(
                                id = transaction.id,
                                block = transaction.block,
                                level = transaction.level,
                                status = transaction.status,
                                amount = transaction.amount
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.height(100.dp)
                            ) {
                                Box(
                                    Modifier
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    MaterialTheme.colorScheme.secondary,
                                                    MaterialTheme.colorScheme.primary,
                                                    MaterialTheme.colorScheme.primary
                                                )
                                            ),
                                        )
                                        .fillMaxSize()
                                        .weight(0.3f),
                                    contentAlignment = Alignment.Center

                                ) {
                                    TransactionComposable(
                                        dimension = 65.dp,
                                        lineColor = MaterialTheme.colorScheme.inversePrimary,
                                        textColor = MaterialTheme.colorScheme.outlineVariant
                                    )
                                }

                                Box(
                                    Modifier
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    MaterialTheme.colorScheme.primary,
                                                    MaterialTheme.colorScheme.primary,
                                                    MaterialTheme.colorScheme.secondary
                                                )
                                            ),
                                        )
                                        .fillMaxSize()
                                        .weight(0.7f)
                                ) {
                                    TransactionEntry(data = transactionData)
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

data class TransactionData(
    val id: Int? = null,
    val block: String? = null,
    val level: Int? = null,
    val amount: Int? = null,
    val status: String? = null
)

@Composable
fun TransactionEntry(
    data: TransactionData,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "name ${data.block}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.surfaceBright,
            )
            Text(
                text = "id ${data.id}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.surfaceBright,
            )
        }
        Column {
            Text(
                text = "status ${data.status}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.surfaceBright,
            )
            Text(
                text = "level ${data.level}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.surfaceBright,
            )
            Text(
                text = "amount ${data.amount}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.surfaceBright,
            )
        }
    }
}
