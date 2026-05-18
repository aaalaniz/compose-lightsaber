package xyz.alaniz.aaron.lightsaber.di.metro

import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.AppScope
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSOperationQueue
import xyz.alaniz.aaron.lightsaber.motion.MotionQueue

@ContributesTo(AppScope::class)
interface IosMotionProvider {
    @Provides
    @SingleIn(AppScope::class)
    fun providesMotionManager(): CMMotionManager = CMMotionManager()

    @Provides
    @SingleIn(AppScope::class)
    fun providesMotionQueue(): MotionQueue = MotionQueue(NSOperationQueue())
}