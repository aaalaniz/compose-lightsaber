package xyz.alaniz.aaron.lightsaber.audio

interface SoundPlayer {
    suspend fun load(sounds: Set<SoundResource>)

    fun play(soundResource: SoundResource, loop: Boolean)

    fun stop(soundResource: SoundResource)

    fun release()
}