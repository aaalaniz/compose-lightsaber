package xyz.alaniz.aaron.lightsaber.audio

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.media.SoundPool.OnLoadCompleteListener
import xyz.alaniz.aaron.lightsaber.di.AppContext
import kotlinx.coroutines.suspendCancellableCoroutine
import me.tatarka.inject.annotations.Inject
import kotlin.coroutines.resume

@Inject
class AndroidSoundPlayer(
    appContext: AppContext,
    private val context: Context = appContext.value,
    private val audioManager: AudioManager
) : SoundPlayer {

    private lateinit var soundPool: SoundPool

    private data class SoundIds(val loadId: Int, val playId: Int? = null)
    private lateinit var soundResourceToStreamIdMap: MutableMap<SoundResource, SoundIds>

    override suspend fun load(sounds: Set<SoundResource>) =
        suspendCancellableCoroutine { continuation ->
            soundPool = SoundPool.Builder()
                .setMaxStreams(sounds.size)
                .build()
            var loadedCount = 0
            val onLoadListener = OnLoadCompleteListener { _, _, _ ->
                if (++loadedCount == sounds.size) {
                    continuation.resume(Unit)
                }
            }
            soundPool.setOnLoadCompleteListener(onLoadListener)

            soundResourceToStreamIdMap = sounds.associateWith { sound ->
                val resourceId = context.resources.getIdentifier(
                    sound.name, "raw", context.packageName
                )
                SoundIds(loadId = soundPool.load(context, resourceId, 1))
            }.toMutableMap()

            continuation.invokeOnCancellation { soundPool.setOnLoadCompleteListener(null) }
        }

    override fun play(soundResource: SoundResource, loop: Boolean) {
        val volume = audioManager.soundMusicVolume()
        val soundIds = requireNotNull(
            soundResourceToStreamIdMap[soundResource]
        )
        val loopInt = if (loop) -1 else 0
        val playId = soundPool.play(
            soundIds.loadId,
            volume,
            volume,
            1,
            loopInt,
            1f
        )
        soundResourceToStreamIdMap[soundResource] = soundIds.copy(playId = playId)
    }

    override fun stop(soundResource: SoundResource) {
        soundResourceToStreamIdMap[soundResource]?.playId?.let {
            soundPool.stop(it)
        }
    }

    override fun release() {
        if (::soundPool.isInitialized && ::soundResourceToStreamIdMap.isInitialized) {
            soundResourceToStreamIdMap.values.forEach { soundIds ->
                soundIds.playId?.let { soundPool.stop(it) }
                soundPool.unload(soundIds.loadId)
            }
            soundPool.release()
        }
    }

    private fun AudioManager.soundMusicVolume(): Float {
        val actualVolume = getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        val maxVolume = getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        return actualVolume / maxVolume
    }
}