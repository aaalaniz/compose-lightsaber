package xyz.alaniz.aaron.lightsaber

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecoration
import kotlinx.coroutines.CoroutineScope
import xyz.alaniz.aaron.lightsaber.di.ApplicationComponent
import xyz.alaniz.aaron.lightsaber.di.DatastoreComponent

@Composable
fun App(createAppComponent: (CoroutineScope) -> ApplicationComponent) {
    val scope = rememberCoroutineScope()
    val appComponent = remember { createAppComponent(scope) }
    val backstack = rememberSaveableBackStack { push(appComponent.initialScreen.value) }
    val navigator = rememberCircuitNavigator(backstack) {
        /**
         * TODO handle root pops
         */
    }

    CircuitCompositionLocals(appComponent.circuit) {
        NavigableCircuitContent(
            navigator = navigator, backstack = backstack, decoration = GestureNavigationDecoration(
                navigator::pop
            )
        )
    }
}