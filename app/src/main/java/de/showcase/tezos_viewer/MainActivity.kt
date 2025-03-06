package de.showcase.tezos_viewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import de.showcase.tezos_viewer.ui.theme.TezosviewerTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        enableEdgeToEdge()
        setContent {
            TezosviewerTheme {
                TezosViewerApp()
            }
        }
    }
}