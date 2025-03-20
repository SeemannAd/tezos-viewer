package de.showcase.tezos_viewer

import android.content.Context
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
import de.showcase.tezos_viewer.domains.shared.Api
import de.showcase.tezos_viewer.domains.shared.composables.BottomNavigationBar
import de.showcase.tezos_viewer.domains.shared.services.StoreDataService
import de.showcase.tezos_viewer.environment.Environment
import kotlinx.coroutines.flow.MutableStateFlow

data class BlocksDependencies(
    val api: Api
)

data class SettingsDependencies(
    val api: Api
)

@Composable
fun TezosViewerApp(
    navController: NavHostController = rememberNavController(),
) {
    val activeScreen = MutableStateFlow("/")

    val context = LocalContext.current

    val environment = Environment()
    val api = Api(environment = environment)

    val blocksViewModel: BlocksViewModel = remember {
        ViewModelProvider(
            context as ViewModelStoreOwner,
            BlocksViewModelFactory(
                context = context.applicationContext,
                dependencies = BlocksDependencies(api = api),
            )
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
            SettingsViewModelFactory(
                context = context.applicationContext,
                dependencies = SettingsDependencies(api = api)
            )
        )[SettingsViewModel::class.java]
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                onNavigateToBlocks = {
                    activeScreen.value = blocksViewModel.route
                    navController.navigate(activeScreen.value)
                },
                onNavigateToSettings = {
                    activeScreen.value = settingsViewModel.route
                    navController.navigate(activeScreen.value)
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
                BlocksScreen(
                    blocksViewModel = blocksViewModel,
                    onCardTap = { hashId ->
                        val block = blocksViewModel.blocks.value.find { it?.hash == hashId }
                        if (block == null) return@BlocksScreen
                        blockViewModel.setBlock(block)

                        navController.navigate("${blockViewModel.route}/${block.hash}")
                    })
            }
            composable("${blockViewModel.route}/{hashId}") { backStackEntry ->
                val hashId = backStackEntry.arguments?.getString("hashId").orEmpty()
                blockViewModel.setBlockIdHashId(hashId = hashId)

                BlockScreen(
                    viewModel = blockViewModel,
                    onBackTap = {
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
class BlocksViewModelFactory(
    private val context: Context,
    private val dependencies: BlocksDependencies
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlocksViewModel::class.java)) {
            return BlocksViewModel(
                context = context,
                blocksService = BlocksService(api = dependencies.api),
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
class SettingsViewModelFactory(
    private val context: Context,
    private val dependencies: SettingsDependencies
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(
                storeDataService = StoreDataService(context),
                api = dependencies.api
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}