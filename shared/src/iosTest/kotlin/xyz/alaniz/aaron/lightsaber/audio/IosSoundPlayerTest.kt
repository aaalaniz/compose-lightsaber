package xyz.alaniz.aaron.lightsaber.audio

import platform.AVFAudio.AVAudioEngine
import kotlin.test.Test
import kotlin.test.assertFailsWith

/**
 * TODO Implement the rest of the test suite
 */
class IosSoundPlayerTest {
    private val iosSoundPlayer = IosSoundPlayer(audioEngine = AVAudioEngine())
    private val soundResource = SoundResource(name = "test", "m4a")

    @Test
    fun `play without loading throws exception`() {
        assertFailsWith<IllegalArgumentException> {
            iosSoundPlayer.play(soundResource = soundResource, loop = false)
        }
    }
}