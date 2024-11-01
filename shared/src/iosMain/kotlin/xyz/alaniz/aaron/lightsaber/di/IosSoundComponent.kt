package xyz.alaniz.aaron.lightsaber.di

import me.tatarka.inject.annotations.KmpComponentCreate
import me.tatarka.inject.annotations.Provides
import platform.AVFAudio.AVAudioEngine
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface IosSoundComponent {
    @Provides
    @SingleIn(AppScope::class)
    fun providesAvAudioEngine(): AVAudioEngine = AVAudioEngine()
}