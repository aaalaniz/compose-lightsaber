package xyz.alaniz.aaron.lightsaber

import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import xyz.alaniz.aaron.lightsaber.di.IosApplicationComponent
import xyz.alaniz.aaron.lightsaber.di.create
import xyz.alaniz.aaron.lightsaber.di.dataStoreFileName
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.IosLightsaberScreen

@OptIn(ExperimentalForeignApi::class)
fun MainViewController() = ComposeUIViewController {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    val dataStorePath = requireNotNull(documentDirectory).path + "/$dataStoreFileName"
    App(initialScreen = IosLightsaberScreen) { scope, navigator ->
        IosApplicationComponent::class.create(
            navigator = navigator,
            appScope = scope,
            dataStorePath = dataStorePath
        )
    }
}