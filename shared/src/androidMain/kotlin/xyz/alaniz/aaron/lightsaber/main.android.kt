package xyz.alaniz.aaron.lightsaber

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import xyz.alaniz.aaron.lightsaber.di.AndroidApplicationComponent
import xyz.alaniz.aaron.lightsaber.di.ApplicationComponent
import xyz.alaniz.aaron.lightsaber.di.AppContext
import xyz.alaniz.aaron.lightsaber.di.create
import xyz.alaniz.aaron.lightsaber.di.dataStoreFileName

@Composable
fun MainView(appContext: Context) {
    val dataStorePath = appContext.filesDir.resolve(dataStoreFileName).absolutePath
    App { scope ->
        AndroidApplicationComponent::class.create(
            appScope = scope,
            dataStorePath = dataStorePath,
            appContext = AppContext(appContext)
        )
    }
}
