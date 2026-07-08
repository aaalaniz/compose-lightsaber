package xyz.alaniz.aaron.lightsaber

import android.content.Context
import androidx.compose.runtime.Composable
import dev.zacsweers.metro.createGraphFactory
import xyz.alaniz.aaron.lightsaber.di.AppContext
import xyz.alaniz.aaron.lightsaber.di.DataStorePath
import xyz.alaniz.aaron.lightsaber.di.metro.AndroidApplicationGraph
import xyz.alaniz.aaron.lightsaber.di.metro.DATA_STORE_FILE_NAME
import xyz.alaniz.aaron.lightsaber.feature.lightsaber.AndroidLightsaberScreen

@Composable
fun MainView(appContext: Context) {
    val dataStorePath = appContext.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    App(initialScreen = AndroidLightsaberScreen) { scope ->
        createGraphFactory<AndroidApplicationGraph.Factory>().create(
            appScope = scope,
            dataStorePath = DataStorePath(value = dataStorePath),
            appContext = AppContext(appContext)
        )
    }
}
