package xyz.alaniz.aaron.lightsaber

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.gesturenavigation.GestureNavigationDecoration
import kotlinx.coroutines.CoroutineScope
import xyz.alaniz.aaron.lightsaber.di.ApplicationComponent

@Composable
fun App(initialScreen: Screen, createAppComponent: (CoroutineScope) -> ApplicationComponent) {
    val scope = rememberCoroutineScope()
    val backstack = rememberSaveableBackStack(root = initialScreen)
    val navigator = rememberCircuitNavigator(backstack) {
        /**
         * TODO handle root pops
         */
    }
    val appComponent = remember { createAppComponent(scope) }

    CircuitCompositionLocals(appComponent.circuit) {
        NavigableCircuitContent(
            navigator = navigator, backStack = backstack, decoration = GestureNavigationDecoration {
                navigator::pop
            }
        )
    }
}