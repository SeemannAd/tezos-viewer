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

    NavHost(navController = navController, startDestination = blocksViewModel.route) {
        composable(route = blocksViewModel.route) { BlocksScreen(viewModel = blocksViewModel) }
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