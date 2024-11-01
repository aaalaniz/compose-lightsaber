package xyz.alaniz.aaron.lightsaber.ui.lightsaber

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import lightsaber.shared.generated.resources.Res
import lightsaber.shared.generated.resources.lightsaber_handle
import lightsaber.shared.generated.resources.lightsaber_screen_lightsaber_blade
import lightsaber.shared.generated.resources.lightsaber_screen_lightsaber_handle
import lightsaber.shared.generated.resources.lightsaber_screen_settings_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberTheme


@CircuitInject(LightsaberScreen::class, AppScope::class)
@Composable
fun Lightsaber(lightsaberState: LightsaberState, modifier: Modifier = Modifier) {
    LightsaberTheme {
        Scaffold {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {
                SettingsIcon(modifier = modifier.align(Alignment.TopEnd).padding(16.dp)) {
                    lightsaberState.onEvent(LightsaberEvent.SettingsSelected)
                }
                val lightSaberHandlePainter =
                    painterResource(resource = Res.drawable.lightsaber_handle)
                val lightSaberHandleWidth = 110.dp
                val lightSaberHeightDp = 480.dp
                val lightSaberHeightPx = with(LocalDensity.current) { lightSaberHeightDp.toPx() }
                val bladeState = lightsaberState.bladeState
                val height = remember { Animatable(0f) }
                val targetHeight =
                    if (bladeState == BladeState.Activating) lightSaberHeightPx else 0f

                if (bladeState == BladeState.Activating ||
                    bladeState == BladeState.Deactivating
                ) {
                    LaunchedEffect(key1 = bladeState, block = {
                        height.animateTo(targetHeight, animationSpec = tween(500)) {
                            when {
                                this.value == targetHeight && this.value == 0f -> {
                                    lightsaberState.onEvent(LightsaberEvent.LightsaberDeactivated)
                                }

                                this.value == targetHeight && this.value == lightSaberHeightPx -> {
                                    lightsaberState.onEvent(LightsaberEvent.LightsaberActivated)
                                }
                            }
                        }
                    })
                }
                Column(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .width(width = lightSaberHandleWidth)
                        .padding(all = 16.dp)
                ) {
                    if (bladeState != BladeState.Initializing &&
                        bladeState != BladeState.Deactivated
                    ) {
                        LightsaberBlade(
                            modifier = Modifier
                                .align(alignment = Alignment.CenterHorizontally)
                                .offset(x = 0.dp, y = 16.dp),
                            lightsaberBladeCurrentHeight = height.value,
                            lightsaberBladeTotalHeight = lightSaberHeightDp,
                            bladeColor = lightsaberState.bladeColor
                        )
                    }

                    Image(
                        painter = lightSaberHandlePainter,
                        contentDescription = stringResource(Res.string.lightsaber_screen_lightsaber_handle),
                        Modifier
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                enabled = bladeState != BladeState.Initializing
                            ) {
                                when (bladeState) {
                                    BladeState.Deactivated -> lightsaberState.onEvent(
                                        LightsaberEvent.LightsaberActivating
                                    )

                                    BladeState.Activating, BladeState.Activated,
                                    BladeState.Deactivating -> lightsaberState.onEvent(
                                        LightsaberEvent.LightsaberDeactivating
                                    )

                                    BladeState.Initializing -> {
                                        error("Should not allow clicking before lightsaber is initialized")
                                    }
                                }
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsIcon(modifier: Modifier = Modifier, onSettingsClicked: () -> Unit) {
    IconButton(onClick = {
        onSettingsClicked()
    }, modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = stringResource(Res.string.lightsaber_screen_settings_icon)
        )
    }
}

@Composable
private fun LightsaberBlade(
    modifier: Modifier = Modifier,
    lightsaberBladeCurrentHeight: Float,
    lightsaberBladeTotalHeight: Dp,
    bladeColor: Color
) {
    val lightsaberBladeWidth = 20.dp
    val blurSize = 4.dp
    val lightsaberBladeDescription =
        stringResource(Res.string.lightsaber_screen_lightsaber_blade)
    Box(
        modifier = modifier
            .semantics {
                contentDescription = lightsaberBladeDescription
            }
            .width(width = lightsaberBladeWidth + blurSize)
            .height(height = lightsaberBladeTotalHeight + blurSize)
            .blur(radius = blurSize)
            .drawWithCache {
                val lightSaberBladeSize = Size(
                    width = (lightsaberBladeWidth - blurSize).toPx(),
                    height = lightsaberBladeCurrentHeight
                )
                val lightSaberBladeRadius = CornerRadius(30.dp.toPx(), 30.dp.toPx())
                val roundRect = RoundRect(
                    rect = Rect(
                        offset = Offset(
                            x = blurSize.toPx(),
                            y = -(lightSaberBladeSize.height)
                        ),
                        size = lightSaberBladeSize,
                    ).translate(0f, this.size.height),
                    topLeft = lightSaberBladeRadius,
                    topRight = lightSaberBladeRadius
                )
                val path = Path().apply {
                    addRoundRect(
                        roundRect
                    )
                }
                onDrawWithContent {
                    drawPath(path, color = Color.White)
                    drawOutline(
                        outline = Outline.Rounded(roundRect),
                        style = Stroke(width = 8f),
                        color = bladeColor
                    )
                }
            }
    ) {
    }
}