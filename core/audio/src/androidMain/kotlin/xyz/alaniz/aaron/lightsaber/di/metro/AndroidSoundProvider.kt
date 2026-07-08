package xyz.alaniz.aaron.lightsaber.di.metro

import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import xyz.alaniz.aaron.lightsaber.di.AppContext

@ContributesTo(AppScope::class)
interface AndroidSoundProvider {
    @Provides
    fun providesAudioManager(appContext: AppContext): AudioManager =
        appContext.value.getSystemService(AUDIO_SERVICE) as AudioManager
}
