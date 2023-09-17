package xyz.alaniz.aaron.lightsaber.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberTheme

@Composable
fun Settings(settingsState: SettingsState, modifier: Modifier = Modifier) {
    LightsaberTheme {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = {
                        Text("Settings")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            settingsState.onEvent(SettingsEvent.SettingsExited)
                        }) {
                            Icon(Icons.Filled.ArrowBack, "exit settings")
                        }
                    })
            }
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(8.dp)
            ) {
                SettingsGroup(name = "Lightsaber") {
                    LightsaberBladeColorDropdownMenu(
                        currentColor = settingsState.bladeColor,
                        colors = settingsState.bladeColorOptions
                    ) { item ->
                        settingsState.onEvent(SettingsEvent.BladeColorUpdate(newBladeColor = item))
                    }
                }
                SettingsGroup(name = "About") {
                    /**
                     * TODO Populate the version information from the platforms
                     */
                    TextOnlySetting("Version", "1.0.0")
                }
            }
        }
    }
}

@Composable
private fun SettingsGroup(
    name: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = name)
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

@Composable
fun TextOnlySetting(
    name: String,
    value: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.weight(1.0f))
            Text(
                text = value,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun LightsaberBladeColorDropdownMenu(
    currentColor: Color,
    colors: List<Color>,
    onColorSelected: (Color) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Blade Color",
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.weight(1.0f))
            var expanded = remember { mutableStateOf(false) }
            var selectedColor = remember { mutableStateOf(currentColor) }
            Box {
                LightsaberCircle(color = selectedColor.value, modifier = Modifier.clickable {
                    expanded.value = true
                })
                DropdownMenu(
                    expanded = expanded.value,
                    modifier = Modifier.width(64.dp),
                    onDismissRequest = { expanded.value = false }) {
                    colors.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                selectedColor.value = item
                                expanded.value = false
                                onColorSelected(item)
                            },
                        ) {
                            LightsaberCircle(color = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LightsaberCircle(color: Color, modifier: Modifier = Modifier) {
    Surface(
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(4.dp, color),
        modifier = modifier.size(32.dp).blur(4.dp).clip(CircleShape)
    ) {
    }
}

@Inject
class SettingsUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is SettingsScreen -> ui<SettingsState> { state, modifier ->
                Settings(
                    state,
                    modifier
                )
            }

            else -> null
        }
    }
}
