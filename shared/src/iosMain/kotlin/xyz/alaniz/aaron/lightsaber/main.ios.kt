package xyz.alaniz.aaron.lightsaber

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.MainScope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import xyz.alaniz.aaron.lightsaber.di.ApplicationComponent
import xyz.alaniz.aaron.lightsaber.di.IosApplicationComponent
import xyz.alaniz.aaron.lightsaber.di.create
import xyz.alaniz.aaron.lightsaber.di.dataStoreFileName

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
    App { scope ->
        IosApplicationComponent::class.create(appScope = scope, dataStorePath = dataStorePath)
    }
}