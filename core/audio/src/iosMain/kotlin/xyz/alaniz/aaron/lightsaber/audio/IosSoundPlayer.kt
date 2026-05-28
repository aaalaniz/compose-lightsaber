package xyz.alaniz.aaron.lightsaber.audio

import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.AppScope
import kotlinx.cinterop.ExperimentalForeignApi
import xyz.alaniz.aaron.lightsaber.core.audio.resources.Res
import platform.Foundation.NSURL
import platform.AVFAudio.AVAudioEngine
import platform.AVFAudio.AVAudioFile
import platform.AVFAudio.AVAudioPCMBuffer
import platform.AVFAudio.AVAudioPlayerNode
import platform.AVFAudio.AVAudioPlayerNodeBufferOptions

@Inject
@OptIn(ExperimentalForeignApi::class)
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class IosSoundPlayer(private val audioEngine: AVAudioEngine) : SoundPlayer {
    private lateinit var soundResourceToPlayerNodeMap: MutableMap<SoundResource,
            Pair<AVAudioPCMBuffer, AVAudioPlayerNode>>
    private val playingSounds: MutableSet<SoundResource> = mutableSetOf()

    override suspend fun load(sounds: Set<SoundResource>) {
        soundResourceToPlayerNodeMap = sounds.mapNotNull { soundResource ->
            try {
                val playerNode = AVAudioPlayerNode()
                val uriStr = Res.getUri("${soundResource.directory}/${soundResource.name}.${soundResource.fileType}")
                val url = NSURL.URLWithString(uriStr) ?: NSURL(fileURLWithPath = uriStr.removePrefix("file://"))
                val audioFile = AVAudioFile(forReading = url, error = null)
                
                if (audioFile == null) return@mapNotNull null
                
                val buffer = AVAudioPCMBuffer(
                    pCMFormat = audioFile.processingFormat,
                    frameCapacity = audioFile.length.toUInt()
                )
                
                if (buffer == null) return@mapNotNull null
                
                audioFile.readIntoBuffer(
                    buffer = buffer,
                    frameCount = buffer.frameCapacity,
                    error = null
                )

                audioEngine.attachNode(playerNode)
                audioEngine.connect(
                    playerNode,
                    audioEngine.mainMixerNode,
                    audioFile.processingFormat
                )
                soundResource to (buffer to playerNode)
            } catch (e: Exception) {
                // If Res.getUri throws MissingResourceException or anything else fails, skip this sound
                null
            }
        }.toMap().toMutableMap()

        audioEngine.prepare()
        audioEngine.startAndReturnError(outError = null)
    }

    override fun play(soundResource: SoundResource, loop: Boolean) {
        if (!::soundResourceToPlayerNodeMap.isInitialized) return
        if (playingSounds.contains(soundResource)) return
        val playerNodeData = soundResourceToPlayerNodeMap[soundResource] ?: return
        val (buffer, playerNode) = playerNodeData
        val options: AVAudioPlayerNodeBufferOptions = if (loop) 1u else 0u

        playingSounds.add(soundResource)
        playerNode.scheduleBuffer(
            buffer = buffer,
            atTime = null,
            options = options
        ) {
            playingSounds.remove(soundResource)
        }
        playerNode.play()
    }

    override fun stop(soundResource: SoundResource) {
        if (!::soundResourceToPlayerNodeMap.isInitialized) return
        val playerNodeData = soundResourceToPlayerNodeMap[soundResource] ?: return
        val (_, playerNode) = playerNodeData

        playerNode.stop()
    }

    override fun release() {
        if (::soundResourceToPlayerNodeMap.isInitialized) {
            soundResourceToPlayerNodeMap.values.forEach {
                val playerNode = it.second
                playerNode.stop()
                audioEngine.disconnectNodeInput(playerNode)
                audioEngine.detachNode(playerNode)
            }
            soundResourceToPlayerNodeMap.clear()
        }
        playingSounds.clear()

        audioEngine.stop()
    }
}