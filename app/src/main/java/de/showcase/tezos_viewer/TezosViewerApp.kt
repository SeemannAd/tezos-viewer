package de.showcase.tezos_viewer

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun TezosViewerApp(
    navController: NavHostController = rememberNavController(),
) {
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

    // adding the api key for pro version via a settings or more screen would be sufficient
    // see api documentation for handling bearer token authentication for a TzKT Pro access.
    // link: https://api.tzkt.io/#section/Get-Started/TzKT-Pro

    NavHost(navController = navController, startDestination = blocksViewModel.route) {
        composable(route = blocksViewModel.route) {

            // build blocks screen
            BlocksScreen(
                viewModel = blocksViewModel,
                onCardTap = { hashId ->
                    val block = blocksViewModel.blocks.value.find { it?.hash == hashId }

                    if (block == null) return@BlocksScreen

                    blockViewModel.setBlock(block)
                    navController.navigate("${blockViewModel.route}/$hashId")
                })
        }
        composable("${blockViewModel.route}/{hashId}") { backStackEntry ->
            val hashId = backStackEntry.arguments?.getString("hashId").orEmpty()
            blockViewModel.setBlockIdHashId(hashId = hashId)

            // build block screen
            BlockScreen(
                viewModel = blockViewModel,
                onBackTap = { navController.popBackStack() },
            )
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