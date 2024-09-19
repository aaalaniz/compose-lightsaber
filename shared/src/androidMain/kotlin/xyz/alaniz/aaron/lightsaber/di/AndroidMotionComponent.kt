package xyz.alaniz.aaron.lightsaber.di

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import xyz.alaniz.aaron.lightsaber.motion.Accelerometer

@ContributesTo(AppScope::class)
interface AndroidMotionComponent {
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