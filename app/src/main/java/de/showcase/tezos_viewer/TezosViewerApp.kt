package de.showcase.tezos_viewer

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.showcase.tezos_viewer.home.HomeScreen
import de.showcase.tezos_viewer.home.HomeViewModel
import de.showcase.tezos_viewer.splash.SplashScreen
import de.showcase.tezos_viewer.splash.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun TezosViewerApp(
    navController: NavHostController = rememberNavController(),
) {
    // Retrieve application context
    val context = LocalContext.current

    val splashViewModel: SplashViewModel = viewModel()

    // Create HomeViewModel with the application context
    val homeViewModel: HomeViewModel = remember {
        ViewModelProvider(
            context as ViewModelStoreOwner,
            HomeViewModelFactory(context.applicationContext)
        )[HomeViewModel::class.java]
    }

    LaunchedEffect(Unit) {
        delay(200)
        navController.navigate(homeViewModel.route) {
            popUpTo(splashViewModel.route) { inclusive = true }
        }
    }



    NavHost(navController = navController, startDestination = splashViewModel.route) {
        composable(route = splashViewModel.route) { SplashScreen(viewModel = splashViewModel) }
        composable(route = homeViewModel.route) { HomeScreen(viewModel = homeViewModel) }
    }
}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}