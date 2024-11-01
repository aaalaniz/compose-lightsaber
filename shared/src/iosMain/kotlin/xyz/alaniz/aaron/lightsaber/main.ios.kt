package xyz.alaniz.aaron.lightsaber

import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.platform.AccessibilitySyncOptions
import androidx.compose.ui.window.ComposeUIViewController
import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.NSLogWriter
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import xyz.alaniz.aaron.lightsaber.di.DataStorePath
import xyz.alaniz.aaron.lightsaber.di.createApplicationComponent
import xyz.alaniz.aaron.lightsaber.di.dataStoreFileName
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.IosLightsaberScreen
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalForeignApi::class, ExperimentalComposeApi::class, ExperimentalNativeApi::class)
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

    /**
     * Use NSLogWriter so that logs are available to pull off the simulator in CI.
     *
     * TODO Inject log writers based on debug or release builds.
     */
    Logger.setLogWriters(listOf(NSLogWriter(messageStringFormatter = DefaultFormatter)))

    /**
     * Log unhandled exceptions.
     */
    setUnhandledExceptionHook {
        Logger.e(throwable = it) { "Unhandled exception: cause = ${it.cause} message = ${it.message}" }
    }
    App(initialScreen = IosLightsaberScreen) { scope ->
        createApplicationComponent(
            appScope = scope,
            dataStorePath = DataStorePath(dataStorePath)
        )
    }
}