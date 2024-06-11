package xyz.alaniz.aaron.lightsaber.di

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.KmpComponentCreate
import me.tatarka.inject.annotations.Provides
import xyz.alaniz.aaron.lightsaber.motion.IosSwingDetector
import xyz.alaniz.aaron.lightsaber.motion.SwingEvent
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSOperationQueue
import xyz.alaniz.aaron.lightsaber.motion.MotionQueue

@Component
abstract class IosMotionComponent {
    private val motionManager: CMMotionManager = CMMotionManager()

    @Provides
    internal fun providesMotionManager(): CMMotionManager = motionManager

    @Provides
    internal fun providesMotionQueue(): MotionQueue = MotionQueue(NSOperationQueue())

    internal val IosSwingDetector.bind: Flow<SwingEvent>
        @Provides get() = this.swings

    companion object
}

@KmpComponentCreate
expect fun IosMotionComponent.Companion.create(): IosMotionComponent