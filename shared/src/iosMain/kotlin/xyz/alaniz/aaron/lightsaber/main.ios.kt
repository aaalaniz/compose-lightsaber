package xyz.alaniz.aaron.lightsaber

import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.platform.AccessibilitySyncOptions
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

@OptIn(ExperimentalForeignApi::class, ExperimentalComposeApi::class)
fun MainViewController() = ComposeUIViewController(configure = {
    /**
     * TODO Update this to only sync the accessibility tree for debug builds
     */
    accessibilitySyncOptions = AccessibilitySyncOptions.Always(debugLogger = null)

    /**
     * TODO Understand why this needs to be false for Maestro tests to pass
     *
     * The default value for this is true, but then the accessibility tree mappings include an
     * offset for the status bar
     */
    platformLayers = false
}) {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    val dataStorePath = requireNotNull(documentDirectory).path + "/$dataStoreFileName"
    App(initialScreen = IosLightsaberScreen) { scope, navigator ->
        IosApplicationComponent.create(
            navigator = navigator,
            appScope = scope,
            dataStorePath = dataStorePath
        )
    }
}