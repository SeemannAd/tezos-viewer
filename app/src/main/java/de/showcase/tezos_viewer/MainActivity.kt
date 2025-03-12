package de.showcase.tezos_viewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import de.showcase.tezos_viewer.theme.TezosViewerTheme
import timber.log.Timber

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        enableEdgeToEdge()
        setContent {
            TezosViewerTheme {
                TezosViewerApp()
            }
        }
    }
}