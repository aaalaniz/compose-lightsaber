package xyz.alaniz.aaron.lightsaber.di

import com.slack.circuit.runtime.presenter.Presenter
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import xyz.alaniz.aaron.lightsaber.ui.common.InitialScreen
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.AndroidLightsaberScreen
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.LightsaberPresenterFactory
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.LightsaberScreen
import xyz.alaniz.aaron.lightsaber.di.ApplicationComponent

@Component
abstract class AndroidApplicationComponent(
    @get:Provides protected val appContext: AppContext,
    @Component protected val androidSoundComponent: AndroidSoundComponent = AndroidSoundComponent::class.create(
        appContext
    ),
    @Component protected val androidMotionComponent: AndroidMotionComponent = AndroidMotionComponent::class.create(
        appContext
    )
) :
    ApplicationComponent() {

    @Provides
    protected fun provideInitialScreen(lightsaberScreen: AndroidLightsaberScreen): InitialScreen =
        InitialScreen(lightsaberScreen)
}