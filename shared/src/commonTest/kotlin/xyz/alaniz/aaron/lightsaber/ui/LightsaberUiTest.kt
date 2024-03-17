package xyz.alaniz.aaron.lightsaber.ui

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberGreen
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.BladeState
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.Lightsaber
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.LightsaberState
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class LightsaberUiTest {
  private val greenDeactivatedLightsaberState = LightsaberState(
    bladeState = BladeState.Deactivated,
    bladeColor = LightsaberGreen,
    onEvent = {}
  )
  private val greenActivatedLightsaberState = LightsaberState(
    bladeState = BladeState.Activated,
    bladeColor = LightsaberGreen,
    onEvent = {}
  )

  @Test
  fun `given a deactivated lightsaber when the lightsaber is shown then the blade should not be visible`() =
    runLightsaberUiTest(initialState = greenDeactivatedLightsaberState) {
      lightsaber {
        seeHandle()
        doNotSeeBlade()
      }
    }

  @Test
  fun `given an activated lightsaber when the lightsaber is shown then the blade should be visible`() =
    runLightsaberUiTest(initialState = greenActivatedLightsaberState) {
      lightsaber {
        seeHandle()
        seeBlade()
      }
    }

  private fun runLightsaberUiTest(initialState: LightsaberState, testBody: ComposeUiTest.() -> Unit) = runComposeUiTest {
    setContent { Lightsaber(lightsaberState = initialState) }
    testBody()
  }
}