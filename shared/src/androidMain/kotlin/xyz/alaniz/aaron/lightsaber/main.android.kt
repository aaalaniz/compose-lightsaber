package xyz.alaniz.aaron.lightsaber

import android.content.Context
import androidx.compose.runtime.Composable
import dev.zacsweers.metro.createGraphFactory
import xyz.alaniz.aaron.lightsaber.di.AppContext
import xyz.alaniz.aaron.lightsaber.di.DataStorePath
import xyz.alaniz.aaron.lightsaber.di.metro.AndroidApplicationGraph
import xyz.alaniz.aaron.lightsaber.di.metro.dataStoreFileName
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.AndroidLightsaberScreen

@Composable
fun MainView(appContext: Context) {
    val dataStorePath = appContext.filesDir.resolve(dataStoreFileName).absolutePath
    App(initialScreen = AndroidLightsaberScreen) { scope ->
        createGraphFactory<AndroidApplicationGraph.Factory>().create(
            appScope = scope,
            dataStorePath = DataStorePath(value = dataStorePath),
            appContext = AppContext(appContext)
        )
    }
}
