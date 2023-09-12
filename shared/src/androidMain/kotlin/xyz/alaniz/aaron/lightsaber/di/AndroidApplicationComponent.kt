package xyz.alaniz.aaron.lightsaber.di

import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import xyz.alaniz.aaron.lightsaber.ui.common.InitialScreen
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.AndroidLightsaberScreen
import xyz.alaniz.aaron.lightsaber.ui.settings.AndroidSettingsScreen
import xyz.alaniz.aaron.lightsaber.ui.settings.SettingsScreen

@Component
abstract class AndroidApplicationComponent(
    @get:Provides protected val appScope: CoroutineScope,
    dataStorePath: String,
    @get:Provides protected val appContext: AppContext,
    @Component protected val androidSoundComponent: AndroidSoundComponent = AndroidSoundComponent::class.create(
        appContext
    ),
    @Component protected val androidMotionComponent: AndroidMotionComponent = AndroidMotionComponent::class.create(
        appContext
    ),
    @Component protected val dataStoreComponent: DatastoreComponent = DatastoreComponent::class.create(
        appScope,
        dataStorePath
    )
) :
    ApplicationComponent() {

    @Provides
    protected fun provideInitialScreen(lightsaberScreen: AndroidLightsaberScreen): InitialScreen =
        InitialScreen(lightsaberScreen)

    protected val AndroidSettingsScreen.bind: SettingsScreen
        @Provides get() = this
}