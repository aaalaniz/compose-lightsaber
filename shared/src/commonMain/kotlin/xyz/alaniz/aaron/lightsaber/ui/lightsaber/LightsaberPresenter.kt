package xyz.alaniz.aaron.lightsaber.ui.lightsaber

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import xyz.alaniz.aaron.lightsaber.audio.SoundPlayer
import xyz.alaniz.aaron.lightsaber.audio.SoundResource
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import xyz.alaniz.aaron.lightsaber.motion.SwingEvent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import xyz.alaniz.aaron.lightsaber.util.noop


sealed interface LightsaberEvent : CircuitUiEvent {
    data object LightsaberActivating : LightsaberEvent
    data object LightsaberActivated : LightsaberEvent
    data object LightsaberDeactivating : LightsaberEvent
    data object LightsaberDeactivated : LightsaberEvent
}

enum class BladeState {
    Initializing, Deactivated, Activating, Activated, Deactivating
}

data class LightsaberState(
    val bladeState: BladeState,
    val onEvent: (LightsaberEvent) -> Unit
) : CircuitUiState

@Inject
class LightsaberPresenter(
    private val swingEvents: Flow<SwingEvent>,
    private val soundPlayer: SoundPlayer
) :
    Presenter<LightsaberState> {
    private val lightsaberActivateSound =
        SoundResource(name = "lightsaber_activating", fileType = "m4a")
    private val lightsaberDeactivateSound =
        SoundResource(name = "lightsaber_deactivating", fileType = "m4a")
    private val lightsaberIdleSound = SoundResource(name = "lightsaber_idle", fileType = "m4a")
    private val swingSounds = listOf(
        SoundResource(name = "lightsaber_hum1", fileType = "m4a"),
        SoundResource(name = "lightsaber_hum2", fileType = "m4a"),
        SoundResource(name = "lightsaber_hum3", fileType = "m4a"),
        SoundResource(name = "lightsaber_clash1", fileType = "m4a"),
        SoundResource(name = "lightsaber_clash2", fileType = "m4a"),
        SoundResource(name = "lightsaber_clash3", fileType = "m4a"),
    )
    private val lightsaberSoundEffects = setOf(
        lightsaberActivateSound,
        lightsaberDeactivateSound,
        lightsaberIdleSound
    ) + swingSounds

    @Composable
    override fun present(): LightsaberState {
        val bladeState = remember {
            mutableStateOf(BladeState.Initializing)
        }
        val scope = rememberCoroutineScope()
        var playIdleSoundJob: Job? = remember { null }

        DisposableEffect(Unit) {
            scope.launch {
                soundPlayer.load(sounds = lightsaberSoundEffects)
                bladeState.value = BladeState.Deactivated
            }

            onDispose {
                soundPlayer.release()
            }
        }

        LaunchedEffect(bladeState.value) {
            when (bladeState.value) {
                BladeState.Initializing, BladeState.Deactivated -> noop()
                BladeState.Activating -> {
                    soundPlayer.play(soundResource = lightsaberActivateSound, loop = false)
                }
                BladeState.Activated -> {
                    soundPlayer.play(soundResource = lightsaberIdleSound, loop = true)
                    swingEvents.collect {

                        /**
                         * Stop the idling sound, play a random swing sound but then, schedule the
                         * idling sound to resume after the swing sound completes. Swing events may
                         * arrive frequently, so reschedule the idling sound job on each swing event
                         * to ensure that the idling continues only after the last swing event.
                         */
                        soundPlayer.stop(soundResource = lightsaberIdleSound)
                        soundPlayer.play(soundResource = swingSounds.random(), loop = false)
                        playIdleSoundJob?.cancel()
                        playIdleSoundJob = launch {
                            delay(500)
                            if (isActive) {
                                soundPlayer.play(soundResource = lightsaberIdleSound, loop = true)
                            }
                        }
                    }
                }

                BladeState.Deactivating -> {
                    soundPlayer.stop(soundResource = lightsaberIdleSound)
                    soundPlayer.play(soundResource = lightsaberDeactivateSound, loop = false)
                }
            }
        }

        return LightsaberState(
            bladeState = bladeState.value,
        ) { event ->
            when (event) {
                LightsaberEvent.LightsaberActivated -> bladeState.value = BladeState.Activated
                LightsaberEvent.LightsaberActivating -> bladeState.value = BladeState.Activating
                LightsaberEvent.LightsaberDeactivated -> bladeState.value =
                    BladeState.Deactivated

                LightsaberEvent.LightsaberDeactivating -> bladeState.value =
                    BladeState.Deactivating
            }
        }
    }
}

@Inject
class LightsaberPresenterFactory(private val lightsaberPresenter: LightsaberPresenter) :
    Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is LightsaberScreen -> presenterOf { lightsaberPresenter.present() }
            else -> null
        }
    }
}
