package xyz.alaniz.aaron.lightsaber.di

import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class IosApplicationComponent(
    @get:Provides protected val appScope: CoroutineScope,
    @get:Provides protected val dataStorePath: DataStorePath
) : ApplicationComponent


@MergeComponent.CreateComponent
expect fun createApplicationComponent(
    appScope: CoroutineScope,
    dataStorePath: DataStorePath
): IosApplicationComponent

