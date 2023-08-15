package xyz.alaniz.aaron.lightsaber

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import xyz.alaniz.aaron.lightsaber.di.AndroidApplicationComponent
import xyz.alaniz.aaron.lightsaber.di.ApplicationComponent
import xyz.alaniz.aaron.lightsaber.di.AppContext
import xyz.alaniz.aaron.lightsaber.di.create

@Composable
fun MainView(appContext: Context) {
    val component: ApplicationComponent =
        remember { AndroidApplicationComponent::class.create(AppContext(appContext)) }
    App(applicationComponent = component)
}
