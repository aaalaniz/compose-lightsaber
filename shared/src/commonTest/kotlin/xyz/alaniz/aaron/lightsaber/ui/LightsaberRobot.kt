package xyz.alaniz.aaron.lightsaber.ui

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription

fun SemanticsNodeInteractionsProvider.lightsaber(block: LightsaberRobot.() -> Unit) =
  LightsaberRobot(nodeInteractionsProvider = this).apply { block() }

class LightsaberRobot(private val nodeInteractionsProvider: SemanticsNodeInteractionsProvider) {

  fun seeHandle() = apply {
    nodeInteractionsProvider.onNodeWithContentDescription(label = "lightsaber handle")
      .assertIsDisplayed()
      .assertIsEnabled()
  }

  fun seeBlade() = apply {
    nodeInteractionsProvider.onNodeWithContentDescription(label = "lightsaber blade")
      .assertIsDisplayed()
  }

  fun doNotSeeBlade() = apply {
    nodeInteractionsProvider.onNode(hasContentDescription(value = "lightsaber blade")).assertDoesNotExist()
  }
}

