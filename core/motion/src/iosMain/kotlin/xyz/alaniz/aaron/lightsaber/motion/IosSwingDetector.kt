package xyz.alaniz.aaron.lightsaber.motion

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CoreMotion.CMMotionManager
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.AppScope
import kotlin.math.abs

private const val SWING_THRESHOLD = 2.5

@Suppress("MagicNumber")
@OptIn(ExperimentalForeignApi::class)
@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class IosSwingDetector(
    private val motionManager: CMMotionManager,
    private val motionQueue: MotionQueue
) : SwingDetector {
    override val swings: Flow<SwingEvent> = callbackFlow {
        memScoped {
            motionManager.deviceMotionUpdateInterval = 0.1

            motionManager.startAccelerometerUpdatesToQueue(queue = motionQueue.value) { data, _ ->
                    val accelerometerData = data?.acceleration?.ptr?.pointed
                    val x = abs(accelerometerData?.x ?: 0.0)
                    val y = abs(accelerometerData?.y ?: 0.0)

                    if (x > SWING_THRESHOLD && y > SWING_THRESHOLD) {
                        trySendBlocking(SwingEvent)
                    }
                }
            awaitClose {
                motionManager.stopAccelerometerUpdates()
                motionQueue.value.cancelAllOperations()
            }
        }
    }
}
