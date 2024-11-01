package xyz.alaniz.aaron.lightsaber.audio

import kotlinx.cinterop.ExperimentalForeignApi
import me.tatarka.inject.annotations.Inject
import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.AVFAudio.AVAudioEngine
import platform.AVFAudio.AVAudioFile
import platform.AVFAudio.AVAudioPCMBuffer
import platform.AVFAudio.AVAudioPlayerNode
import platform.AVFAudio.AVAudioPlayerNodeBufferOptions
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn


@Inject
@OptIn(ExperimentalForeignApi::class)
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class IosSoundPlayer(private val audioEngine: AVAudioEngine) : SoundPlayer {
    private lateinit var soundResourceToPlayerNodeMap: MutableMap<SoundResource,
            Pair<AVAudioPCMBuffer, AVAudioPlayerNode>>
    private val playingSounds: MutableSet<SoundResource> = mutableSetOf()

    override suspend fun load(sounds: Set<SoundResource>) {
        soundResourceToPlayerNodeMap = sounds.associateWith { soundResource ->
            val playerNode = AVAudioPlayerNode()
            val filePath = requireNotNull(
                NSBundle.mainBundle.pathForResource(
                    name = soundResource.name,
                    ofType = soundResource.fileType,
                    inDirectory = "compose-resources/composeResources/lightsaber.shared.generated.resources/${soundResource.directory}"
                )
            )
            val audioFile =
                AVAudioFile(forReading = NSURL(fileURLWithPath = filePath), error = null)
            val buffer = AVAudioPCMBuffer(
                pCMFormat = audioFile.processingFormat,
                frameCapacity = audioFile.length.toUInt()
            )
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
            buffer to playerNode
        }.toMutableMap()

        audioEngine.prepare()
        audioEngine.startAndReturnError(outError = null)
    }

    override fun play(soundResource: SoundResource, loop: Boolean) {
        check(::soundResourceToPlayerNodeMap.isInitialized) {
            "Cannot play without loading sound resources."
        }
        if (playingSounds.contains(soundResource)) return
        val (buffer, playerNode) = requireNotNull(soundResourceToPlayerNodeMap[soundResource])
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
        val (_, playerNode) = requireNotNull(soundResourceToPlayerNodeMap[soundResource])

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