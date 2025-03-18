package de.showcase.tezos_viewer.domains.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.showcase.tezos_viewer.domains.shared.services.StoreData
import de.showcase.tezos_viewer.domains.shared.services.StoreService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val apiKey by viewModel.apiKey.collectAsState("")

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->

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
                    Text("API Key: $apiKey")
                }
            }
        }
    }
}

@SuppressLint("StaticFieldLeak")
class SettingsViewModel(
    private val storeService: StoreService,
) : ViewModel() {
    val route = "/settings"

    val apiKey: MutableStateFlow<String> = MutableStateFlow("")

    fun write(): Job {
        return viewModelScope.launch {
            storeService.write(
                storeData = StoreData(
                    fileName = "api_key.text",
                    data = "API-KEY"
                )
            )
        }
    }

    fun read(): Job {
        return viewModelScope.launch {
            storeService.read(fromFileName = "api_key.text",)
        }
    }
}