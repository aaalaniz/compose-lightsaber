package xyz.alaniz.aaron.lightsaber

import androidx.compose.ui.window.ComposeUIViewController
import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.NSLogWriter
import dev.zacsweers.metro.createGraphFactory
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import xyz.alaniz.aaron.lightsaber.di.DataStorePath
import xyz.alaniz.aaron.lightsaber.di.metro.IosApplicationGraph
import xyz.alaniz.aaron.lightsaber.di.metro.DATA_STORE_FILE_NAME
import xyz.alaniz.aaron.lightsaber.feature.lightsaber.IosLightsaberScreen
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
fun mainViewController() = ComposeUIViewController(configure = {}) {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    val dataStorePath = requireNotNull(documentDirectory).path + "/$DATA_STORE_FILE_NAME"

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
        Logger.e(throwable = it) {
            "Unhandled exception: cause = ${it.cause} message = ${it.message}"
        }
    }
    App(initialScreen = IosLightsaberScreen) { scope ->
        createGraphFactory<IosApplicationGraph.Factory>()
            .create(appScope = scope, dataStorePath = DataStorePath(dataStorePath))
    }
}
