package de.showcase.tezos_viewer

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.showcase.tezos_viewer.domains.block.BlockScreen
import de.showcase.tezos_viewer.domains.block.BlockViewModel
import de.showcase.tezos_viewer.domains.blocks.BlocksService
import de.showcase.tezos_viewer.domains.blocks.BlocksScreen
import de.showcase.tezos_viewer.domains.blocks.BlocksViewModel
import de.showcase.tezos_viewer.domains.settings.SettingsScreen
import de.showcase.tezos_viewer.domains.settings.SettingsViewModel
import de.showcase.tezos_viewer.domains.shared.composables.BottomNavigationBar
import de.showcase.tezos_viewer.domains.shared.services.StoreService
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun TezosViewerApp(
    navController: NavHostController = rememberNavController(),
) {
    val activeScreen = MutableStateFlow("/")

    val context = LocalContext.current

    val blocksViewModel: BlocksViewModel = remember {
        ViewModelProvider(
            context as ViewModelStoreOwner,
            BlocksViewModelFactory(context.applicationContext)
        )[BlocksViewModel::class.java]
    }

    val blockViewModel: BlockViewModel = remember {
        ViewModelProvider(
            context as ViewModelStoreOwner,
            BlockViewModelFactory(context.applicationContext)
        )[BlockViewModel::class.java]
    }

    val settingsViewModel: SettingsViewModel = remember {
        ViewModelProvider(
            context as ViewModelStoreOwner,
            SettingsViewModelFactory(context.applicationContext)
        )[SettingsViewModel::class.java]
    }

    // adding the api key for pro version via a settings or more screen would be sufficient
    // see api documentation for handling bearer token authentication for a TzKT Pro access.
    // link: https://api.tzkt.io/#section/Get-Started/TzKT-Pro

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                onNavigateToBlocks = {
                    activeScreen.value = blocksViewModel.route
                    navController.navigate(blocksViewModel.route)
                },
                onNavigateToSettings = {
                    activeScreen.value = settingsViewModel.route
                    navController.navigate(settingsViewModel.route)
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = blocksViewModel.route,
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                start = innerPadding.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                end = innerPadding.calculateEndPadding(layoutDirection = LayoutDirection.Rtl)
            )
        ) {
            composable(route = blocksViewModel.route) {
                // build blocks screen
                BlocksScreen(
                    viewModel = blocksViewModel,
                    onCardTap = { hashId ->
                        val block = blocksViewModel.blocks.value.find { it?.hash == hashId }
                        if (block == null) return@BlocksScreen
                        blockViewModel.setBlock(block)

                        navController.navigate(activeScreen.value)
                    })
            }
            composable("${blockViewModel.route}/{hashId}") { backStackEntry ->
                val hashId = backStackEntry.arguments?.getString("hashId").orEmpty()
                blockViewModel.setBlockIdHashId(hashId = hashId)

                // build block screen
                BlockScreen(
                    viewModel = blockViewModel,
                    onBackTap = {
                        activeScreen.value = blocksViewModel.route
                        navController.popBackStack()
                    },
                )
            }

            composable(settingsViewModel.route) { backStackEntry ->
                SettingsScreen(
                    viewModel = settingsViewModel,
                )
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class BlocksViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlocksViewModel::class.java)) {
            val blocksService = BlocksService()
            return BlocksViewModel(
                context = context,
                blocksService = blocksService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


@Suppress("UNCHECKED_CAST")
class BlockViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlockViewModel::class.java)) {
            return BlockViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            val storeService = StoreService(context = context)
            return SettingsViewModel(storeService = storeService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}