package xyz.alaniz.aaron.lightsaber

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import xyz.alaniz.aaron.lightsaber.di.ApplicationComponent

@Composable
fun App(applicationComponent: ApplicationComponent) {
    val appComponent = remember { applicationComponent }

    CircuitCompositionLocals(appComponent.circuit) {
        CircuitContent(appComponent.initialScreen.value)
    }
}