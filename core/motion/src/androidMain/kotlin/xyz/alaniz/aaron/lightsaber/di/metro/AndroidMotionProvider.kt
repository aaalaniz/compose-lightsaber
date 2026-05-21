package xyz.alaniz.aaron.lightsaber.di.metro

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import xyz.alaniz.aaron.lightsaber.di.AppContext
import xyz.alaniz.aaron.lightsaber.motion.Accelerometer

@ContributesTo(AppScope::class)
interface AndroidMotionProvider {
    @Provides
    @SingleIn(AppScope::class)
    fun providesSensorManager(appContext: AppContext): SensorManager =
        appContext.value.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @Provides
    @SingleIn(AppScope::class)
    fun providesAccelerometer(sensorManager: SensorManager): Accelerometer = Accelerometer(
        sensor = requireNotNull(
            sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
            )
        )
    )
}