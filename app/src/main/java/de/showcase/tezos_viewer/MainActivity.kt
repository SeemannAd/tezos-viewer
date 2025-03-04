package de.showcase.tezos_viewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.showcase.tezos_viewer.ui.theme.TezosviewerTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TezosviewerTheme {
                TezosViewerApp()
            }
        }
    }
}

@Composable
fun TezosViewerApp(
    viewModel: AppViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Home.name
    )

    // State to hold the fetched data
    var state by remember { mutableStateOf("No data fetched") }

    lateinit var getJob: Job

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = "Hello ${currentScreen.name}-Screen",
                modifier = Modifier.padding(innerPadding)
            )

            Text(text = state)

            Button(onClick = {
                // Launch a coroutine to call the suspend function
               getJob =  viewModel.viewModelScope.launch {
                    viewModel.getData { fetchedData ->
                        state = fetchedData
                    }
                }
            }) {
                Text(text = "get")
            }
        }
    }
}

enum class AppScreen(@StringRes val title: Int) {
    Home(title = R.string.home_screen),
}

class AppViewModel : ViewModel() {

    private val client = HttpClient(CIO)

    suspend fun getData(onResult: (String) -> Unit) {
        val response: HttpResponse = client.get("https://ktor.io/")
        delay(5000)
        onResult(response.toString())
    }
}