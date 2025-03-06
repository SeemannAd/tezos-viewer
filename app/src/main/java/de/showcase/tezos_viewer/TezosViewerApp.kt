package de.showcase.tezos_viewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    splashViewModel: SplashViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(),
) {
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

