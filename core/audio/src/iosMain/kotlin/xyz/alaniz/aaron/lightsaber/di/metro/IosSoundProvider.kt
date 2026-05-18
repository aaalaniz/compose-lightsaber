package xyz.alaniz.aaron.lightsaber.di.metro

import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.AppScope
import platform.AVFAudio.AVAudioEngine

@ContributesTo(AppScope::class)
interface IosSoundProvider {
    @Provides
    @SingleIn(AppScope::class)
    fun providesAvAudioEngine(): AVAudioEngine = AVAudioEngine()
}