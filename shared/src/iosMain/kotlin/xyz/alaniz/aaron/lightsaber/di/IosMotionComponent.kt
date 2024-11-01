package xyz.alaniz.aaron.lightsaber.di

import me.tatarka.inject.annotations.Provides
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSOperationQueue
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import xyz.alaniz.aaron.lightsaber.motion.MotionQueue

@ContributesTo(AppScope::class)
interface IosMotionComponent {
    @Provides
    @SingleIn(AppScope::class)
    fun providesMotionManager(): CMMotionManager = CMMotionManager()

    @Provides
    @SingleIn(AppScope::class)
    fun providesMotionQueue(): MotionQueue = MotionQueue(NSOperationQueue())
}