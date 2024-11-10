package xyz.alaniz.aaron.lightsaber.di

import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.reflect.KClass

@Component
@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class IosApplicationComponent(
    @get:Provides protected val appScope: CoroutineScope,
    @get:Provides protected val dataStorePath: DataStorePath
) : IosApplicationComponentMerged


@MergeComponent.CreateComponent
expect fun KClass<IosApplicationComponent>.create(
    appScope: CoroutineScope,
    dataStorePath: DataStorePath
): IosApplicationComponent

