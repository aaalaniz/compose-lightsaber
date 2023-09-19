package xyz.alaniz.aaron.lightsaber

import android.content.Context
import androidx.compose.runtime.Composable
import xyz.alaniz.aaron.lightsaber.di.AndroidApplicationComponent
import xyz.alaniz.aaron.lightsaber.di.AppContext
import xyz.alaniz.aaron.lightsaber.di.create
import xyz.alaniz.aaron.lightsaber.di.dataStoreFileName
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.AndroidLightsaberScreen

@Composable
fun MainView(appContext: Context) {
    val dataStorePath = appContext.filesDir.resolve(dataStoreFileName).absolutePath
    App(initialScreen = AndroidLightsaberScreen) { scope, navigator ->
        AndroidApplicationComponent::class.create(
            navigator = navigator,
            appScope = scope,
            dataStorePath = dataStorePath,
            appContext = AppContext(appContext)
        )
    }
}
