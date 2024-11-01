package xyz.alaniz.aaron.lightsaber.motion

import kotlinx.coroutines.flow.Flow

interface SwingDetector {
    val swings: Flow<SwingEvent>
}