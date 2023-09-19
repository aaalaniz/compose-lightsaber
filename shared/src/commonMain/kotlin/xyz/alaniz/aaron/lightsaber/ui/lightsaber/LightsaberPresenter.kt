package xyz.alaniz.aaron.lightsaber.ui.lightsaber

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import xyz.alaniz.aaron.lightsaber.motion.SwingEvent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import xyz.alaniz.aaron.lightsaber.data.SettingsRepository
import xyz.alaniz.aaron.lightsaber.ui.settings.SettingsEvent
import xyz.alaniz.aaron.lightsaber.ui.settings.SettingsScreen
import xyz.alaniz.aaron.lightsaber.util.noop

sealed interface LightsaberEvent : CircuitUiEvent {
    data object LightsaberActivating : LightsaberEvent
    data object LightsaberActivated : LightsaberEvent
    data object LightsaberDeactivating : LightsaberEvent
    data object LightsaberDeactivated : LightsaberEvent
    data object SettingsSelected : LightsaberEvent
}

enum class BladeState {
    Initializing, Deactivated, Activating, Activated, Deactivating
}

data class LightsaberState(
    val bladeState: BladeState,
    val bladeColor: Color,
    val onEvent: (LightsaberEvent) -> Unit
) : CircuitUiState

@Inject
class LightsaberPresenter(
    private val swingEvents: Flow<SwingEvent>,
    private val soundPlayer: SoundPlayer,
    private val settingsScreen: SettingsScreen,
    private val settingsRepository: SettingsRepository,
    @Assisted private val navigator: Navigator
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
        var playIdleSoundJob: Job? = remember { null }
        val settings = settingsRepository.lightsaberSettings.collectAsState()

        LaunchedEffect(bladeState.value) {
            when (bladeState.value) {
                BladeState.Initializing -> {
                    soundPlayer.load(sounds = lightsaberSoundEffects)
                    bladeState.value = BladeState.Deactivated
                }
                BladeState.Deactivated -> noop()
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
            bladeColor = settings.value.bladeColor
        ) { event ->
            when (event) {
                LightsaberEvent.LightsaberActivated -> bladeState.value = BladeState.Activated
                LightsaberEvent.LightsaberActivating -> bladeState.value = BladeState.Activating
                LightsaberEvent.LightsaberDeactivated -> bladeState.value =
                    BladeState.Deactivated

                LightsaberEvent.LightsaberDeactivating -> bladeState.value =
                    BladeState.Deactivating

                LightsaberEvent.SettingsSelected -> {
                    playIdleSoundJob?.cancel()
                    soundPlayer.release()
                    navigator.goTo(settingsScreen)
                }
            }
        }
    }
}

@Inject
class LightsaberPresenterFactory(private val createPresenter: (Navigator) -> LightsaberPresenter) :
    Presenter.Factory {

    private lateinit var lightsaberPresenter: LightsaberPresenter
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return when (screen) {
            is LightsaberScreen -> {
                if (::lightsaberPresenter.isInitialized.not()) {
                    lightsaberPresenter = createPresenter(navigator)
                }
                presenterOf { lightsaberPresenter.present() }
            }

            else -> null
        }
    }
}
