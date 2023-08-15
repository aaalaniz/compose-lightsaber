package xyz.alaniz.aaron.lightsaber.di

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import xyz.alaniz.aaron.lightsaber.motion.Accelerometer
import xyz.alaniz.aaron.lightsaber.motion.AndroidSwingDetector
import xyz.alaniz.aaron.lightsaber.motion.SwingEvent

@Component
abstract class AndroidMotionComponent(@get:Provides internal val appContext: AppContext) {
    @Provides
    internal fun providesSensorManager(appContext: AppContext): SensorManager =
        appContext.value.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @Provides
    internal fun providesAccelerometer(sensorManager: SensorManager): Accelerometer = Accelerometer(
        sensor = requireNotNull(
            sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
            )
        )
    )

    internal val AndroidSwingDetector.bind: Flow<SwingEvent>
        @Provides get() = this.swings
}