package xyz.alaniz.aaron.lightsaber.di

import xyz.alaniz.aaron.lightsaber.audio.IosSoundPlayer
import xyz.alaniz.aaron.lightsaber.audio.SoundPlayer
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import platform.AVFAudio.AVAudioEngine

@Component
abstract class IosSoundComponent {
    internal val IosSoundPlayer.bind: SoundPlayer
        @Provides get() = this

    @Provides
    internal fun providesAvAudioEngine(): AVAudioEngine = AVAudioEngine()
}