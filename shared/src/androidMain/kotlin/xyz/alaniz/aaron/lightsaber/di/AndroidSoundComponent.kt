package xyz.alaniz.aaron.lightsaber.di

import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import xyz.alaniz.aaron.lightsaber.audio.AndroidSoundPlayer
import xyz.alaniz.aaron.lightsaber.audio.SoundPlayer
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class AndroidSoundComponent(@get:Provides internal val appContext: AppContext) {
    @Provides
    internal fun providesAudioManager(appContext: AppContext): AudioManager =
        appContext.value.getSystemService(AUDIO_SERVICE) as AudioManager

    internal val AndroidSoundPlayer.bind: SoundPlayer
        @Provides get() = this
}