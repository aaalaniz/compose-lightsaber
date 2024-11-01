package xyz.alaniz.aaron.lightsaber.di

import kotlinx.coroutines.CoroutineScope

expect fun createApplicationComponent(
    appScope: CoroutineScope,
    dataStorePath: DataStorePath
): ApplicationComponent