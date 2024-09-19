package xyz.alaniz.aaron.lightsaber.di

import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface AndroidSoundComponent {
    @Provides
    fun providesAudioManager(appContext: AppContext): AudioManager =
        appContext.value.getSystemService(AUDIO_SERVICE) as AudioManager
}