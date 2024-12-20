package xyz.alaniz.aaron.lightsaber.motion

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.math.abs

private const val SWING_THRESHOLD = 8

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class AndroidSwingDetector(
    private val sensorManager: SensorManager,
    private val accelerometer: Accelerometer
) : SwingDetector {
    override val swings: Flow<SwingEvent>
        get() = callbackFlow {
            val sensorEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val x: Float = event.values[0]
                    val y: Float = event.values[1]

                    if (abs(x) > SWING_THRESHOLD && abs(y) > SWING_THRESHOLD) {
                        trySendBlocking(SwingEvent)
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
            }
            sensorManager.registerListener(
                sensorEventListener,
                accelerometer.sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
            awaitClose {
                sensorManager.unregisterListener(sensorEventListener)
            }
        }
}