package xyz.alaniz.aaron.lightsaber.di

import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

/**
 * TODO Remove this workaround when kotlin-anvil-inject 0.0.6 is released
 */
@Component
@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class IosSimArm64AppComponent(
    @get:Provides protected val appScope: CoroutineScope,
    @get:Provides protected val dataStorePath: DataStorePath
) : IosSimArm64AppComponentMerged

actual fun createApplicationComponent(
    appScope: CoroutineScope,
    dataStorePath: DataStorePath
): ApplicationComponent = IosSimArm64AppComponent::class.create(
    appScope = appScope,
    dataStorePath = dataStorePath
)