package xyz.alaniz.aaron.lightsaber.di

import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import xyz.alaniz.aaron.lightsaber.ui.common.InitialScreen
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.IosLightsaberScreen
import xyz.alaniz.aaron.lightsaber.ui.settings.IosSettingsScreen
import xyz.alaniz.aaron.lightsaber.ui.settings.SettingsScreen

@Component
abstract class IosApplicationComponent(
    @get:Provides protected val appScope: CoroutineScope,
    dataStorePath: String,
    @Component protected val iosSoundComponent: IosSoundComponent = IosSoundComponent::class.create(),
    @Component protected val iosMotionComponent: IosMotionComponent = IosMotionComponent::class.create(),
    @Component protected val dataStoreComponent: DatastoreComponent = DatastoreComponent::class.create(
        appScope,
        dataStorePath
    )
) : ApplicationComponent() {
    @Provides
    protected fun provideInitialScreen(lightsaberScreen: IosLightsaberScreen): InitialScreen =
        InitialScreen(lightsaberScreen)

    protected val IosSettingsScreen.bind: SettingsScreen
        @Provides get() = this
}