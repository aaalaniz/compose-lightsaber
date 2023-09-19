package xyz.alaniz.aaron.lightsaber.di

import com.slack.circuit.runtime.Navigator
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import xyz.alaniz.aaron.lightsaber.ui.settings.IosSettingsScreen
import xyz.alaniz.aaron.lightsaber.ui.settings.SettingsScreen

@Component
abstract class IosApplicationComponent(
    @get:Provides protected val navigator: Navigator,
    @get:Provides protected val appScope: CoroutineScope,
    dataStorePath: String,
    @Component protected val iosSoundComponent: IosSoundComponent = IosSoundComponent::class.create(),
    @Component protected val iosMotionComponent: IosMotionComponent = IosMotionComponent::class.create(),
    @Component protected val dataStoreComponent: DatastoreComponent = DatastoreComponent::class.create(
        appScope,
        dataStorePath
    )
) : ApplicationComponent() {
    protected val IosSettingsScreen.bind: SettingsScreen
        @Provides get() = this
}