package de.showcase.tezos_viewer.domains.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.showcase.tezos_viewer.domains.shared.Api
import de.showcase.tezos_viewer.domains.shared.services.StoreDataService
import de.showcase.tezos_viewer.environment.Key
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    val storeDataService: StoreDataService,
    val api: Api,
) : ViewModel() {
    val route = "/settings"

    val isEditing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val apiKey: MutableStateFlow<String> = MutableStateFlow(storeDataService.read(key = Key.API_KEY.name))

    fun unLockEditing() {
        isEditing.value = true
    }

    fun lockEditing() {
        isEditing.value = false
    }

    fun collectApiKey(newApiKey: String) {
        apiKey.value = newApiKey
    }

    fun writeApiKey(): Job {
        return viewModelScope.launch {
            storeDataService.write(key = Key.API_KEY.name, value = apiKey.value)
            api.environment.setApiKeyFromStorage(newApiKey=apiKey.value)
        }
    }
}