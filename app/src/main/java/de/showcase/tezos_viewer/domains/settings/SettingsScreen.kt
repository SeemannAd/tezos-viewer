package de.showcase.tezos_viewer.domains.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.showcase.tezos_viewer.domains.shared.composables.AnimatedBackground
import de.showcase.tezos_viewer.domains.shared.services.StoreData
import de.showcase.tezos_viewer.domains.shared.services.StoreService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val isEditing by viewModel.isEditing.collectAsState(false)
    val apiKey by viewModel.apiKey.collectAsState("")

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.readApiKey()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->

        AnimatedBackground(
            enabled = false
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                ) {
                    items(1) { index ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)){
                            Column {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Column {
                                        Text(
                                            text = "API-KEY",
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.tertiary,
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .fillMaxHeight()
                                        )
                                        if(apiKey.isNotEmpty()) {
                                            Text(
                                                text = apiKey,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.tertiary,
                                                modifier = Modifier
                                                    .padding(horizontal = 12.dp)
                                            )
                                        }
                                    }

                                    if(isEditing) {
                                        IconButton(
                                            onClick = {
                                                viewModel.writeApiKey(apiKey)
                                                viewModel.lockEditing()
                                            }) {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = "Edit",
                                                tint = MaterialTheme.colorScheme.tertiary,
                                            )
                                        }
                                    } else {
                                        IconButton(
                                            onClick = {
                                                viewModel.unLockEditing()
                                            }) {
                                            Icon(
                                                imageVector = Icons.Filled.Edit,
                                                contentDescription = "Edit",
                                                tint = MaterialTheme.colorScheme.tertiary,
                                            )
                                        }
                                    }

                                }

                                if(isEditing) {
                                    TextField(
                                        value = apiKey,
                                        onValueChange = { newApiKey -> viewModel.writeApiKey(newApiKey) },
                                        label = {
                                            if(apiKey.isEmpty()){
                                                Text(
                                                    text = "enter your API-KEY",
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 14.sp,
                                                    color = MaterialTheme.colorScheme.tertiary
                                                )
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                keyboardController?.hide()
                                            }
                                        ),
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            imeAction = ImeAction.Done
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class SettingsViewModel(
    private val storeService: StoreService,
) : ViewModel() {
    val route = "/settings"

    val isEditing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val apiKey: MutableStateFlow<String> = MutableStateFlow("")

    fun unLockEditing() {
        isEditing.value = true
    }

    fun lockEditing() {
        isEditing.value = false
    }

    fun writeApiKey(newApiKey: String): Job {
        return viewModelScope.launch {
            storeService.write(
                storeData = StoreData(
                    fileName = "api_key.text",
                    data = newApiKey
                )
            )
            apiKey.value = newApiKey
        }
    }

    fun readApiKey(): Job {
        return viewModelScope.launch {
            val result = storeService.read(fromFileName = "api_key.text")
            apiKey.value = result.data ?: ""
        }
    }
}