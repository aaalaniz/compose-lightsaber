package xyz.alaniz.aaron.lightsaber.ui.settings


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import xyz.alaniz.aaron.lightsaber.data.SettingsRepository
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberBlue
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberGreen
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberPurple
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberRed
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberYellow

sealed interface SettingsEvent : CircuitUiEvent {
    data object SettingsExited : SettingsEvent

    data class BladeColorUpdate(val newBladeColor: Color) : SettingsEvent
}

data class SettingsState(
    val bladeColor: Color,
    val bladeColorOptions: List<Color>,
    val onEvent: (SettingsEvent) -> Unit
) :
    CircuitUiState

@CircuitInject(SettingsScreen::class, AppScope::class)
@Inject
class SettingsPresenter(
    private val settingsRepository: SettingsRepository,
    @Assisted private val navigator: Navigator
) :
    Presenter<SettingsState> {

    private val bladeColorOptions = listOf(
        LightsaberGreen, LightsaberRed,
        LightsaberYellow, LightsaberBlue,
        LightsaberPurple
    )

    @Composable
    override fun present(): SettingsState {
        val scope = rememberCoroutineScope()
        val settings = settingsRepository.lightsaberSettings.collectAsState()

        return SettingsState(
            bladeColor = settings.value.bladeColor,
            bladeColorOptions = bladeColorOptions
        ) {
            when (it) {
                SettingsEvent.SettingsExited -> navigator.pop()
                is SettingsEvent.BladeColorUpdate -> scope.launch {
                    settingsRepository.saveSettings(
                        settings.value.copy(bladeColor = it.newBladeColor)
                    )
                }
            }
        }
    }
}