package xyz.alaniz.aaron.lightsaber.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import lightsaber.shared.generated.resources.Res
import lightsaber.shared.generated.resources.color_picker_dialog_button_cancel
import lightsaber.shared.generated.resources.color_picker_dialog_button_ok
import lightsaber.shared.generated.resources.color_picker_dialog_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ColorPickerDialog(
    onDismissRequest: () -> Unit,
    onColorSelected: (Color) -> Unit
) {
    val controller = rememberColorPickerController()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(Res.string.color_picker_dialog_title)) },
        text = {
            Column {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(10.dp),
                    controller = controller
                )
                Spacer(modifier = Modifier.height(16.dp))
                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                        .padding(10.dp),
                    controller = controller
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onColorSelected(controller.selectedColor.value)
                    onDismissRequest()
                }
            ) {
                Text(stringResource(Res.string.color_picker_dialog_button_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.color_picker_dialog_button_cancel))
            }
        }
    )
}
