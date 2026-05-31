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
        soundResourceToPlayerNodeMap = sounds.associateWith { soundResource ->
            val playerNode = AVAudioPlayerNode()
            val uriString = try {
                Res.getUri("${soundResource.directory}/${soundResource.name}.${soundResource.fileType}")
            } catch (e: Exception) {
                val mainBundle = platform.Foundation.NSBundle.mainBundle
                val searchPaths = listOf(
                    "compose-resources/raw",
                    "compose-resources/composeResources/lightsaber.shared.generated.resources/files/raw",
                    "compose-resources/lightsaber.shared.generated.resources/files/raw"
                )
                
                var fallbackPath: String? = null
                for (searchPath in searchPaths) {
                    val bundlePath = mainBundle.pathForResource(
                        name = soundResource.name,
                        ofType = soundResource.fileType,
                        inDirectory = searchPath
                    )
                    if (bundlePath != null) {
                        fallbackPath = "file://$bundlePath"
                        break
                    }
                }
                
                fallbackPath ?: throw IllegalStateException("Failed to find resource ${soundResource.name}.${soundResource.fileType}", e)
            }
            
            // Res.getUri may return a string starting with "file://".
            // If the path contains spaces (like in CI's "iOS 26.4.simruntime"), NSURL.URLWithString returns nil.
            // Passing a string starting with "file://" to fileURLWithPath creates an invalid "file:///file://..." URL.
            // The safest approach is to strip the prefix and use fileURLWithPath.
            val path = uriString.removePrefix("file://")
            val url = NSURL(fileURLWithPath = path)
            val audioFile = AVAudioFile(forReading = url, error = null)
            
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