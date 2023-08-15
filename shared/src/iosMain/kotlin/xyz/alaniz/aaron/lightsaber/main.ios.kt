package xyz.alaniz.aaron.lightsaber

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import xyz.alaniz.aaron.lightsaber.di.ApplicationComponent
import xyz.alaniz.aaron.lightsaber.di.IosApplicationComponent
import xyz.alaniz.aaron.lightsaber.di.create

fun MainViewController() = ComposeUIViewController {
    val component: ApplicationComponent = remember { IosApplicationComponent::class.create() }
    App(applicationComponent = component)
}