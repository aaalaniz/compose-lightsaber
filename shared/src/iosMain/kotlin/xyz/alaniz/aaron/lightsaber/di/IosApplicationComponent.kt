package xyz.alaniz.aaron.lightsaber.di

import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import xyz.alaniz.aaron.lightsaber.ui.common.InitialScreen
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.IosLightsaberScreen

@Component
abstract class IosApplicationComponent(
    @Component protected val iosSoundComponent: IosSoundComponent = IosSoundComponent::class.create(),
    @Component protected val iosMotionComponent: IosMotionComponent = IosMotionComponent::class.create()
) : ApplicationComponent() {
    @Provides
    protected fun provideInitialScreen(lightsaberScreen: IosLightsaberScreen): InitialScreen =
        InitialScreen(lightsaberScreen)
}