package xyz.alaniz.aaron.lightsaber.di

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import xyz.alaniz.aaron.lightsaber.ui.common.InitialScreen
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.LightsaberPresenterFactory
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.LightsaberUiFactory
import xyz.alaniz.aaron.lightsaber.ui.settings.SettingsPresenterFactory
import xyz.alaniz.aaron.lightsaber.ui.settings.SettingsUiFactory

@AppScope
abstract class ApplicationComponent {
    abstract val initialScreen: InitialScreen
    abstract val circuit: Circuit
    protected abstract val presenterFactories: Set<Presenter.Factory>
    protected abstract val uiFactories: Set<Ui.Factory>

    protected val LightsaberPresenterFactory.bind: Presenter.Factory
        @IntoSet @Provides get() = this

    protected val LightsaberUiFactory.bind: Ui.Factory
        @IntoSet @Provides get() = this

    protected val SettingsPresenterFactory.bind: Presenter.Factory
        @IntoSet @Provides get() = this

    protected val SettingsUiFactory.bind: Ui.Factory
        @IntoSet @Provides get() = this

    @Provides
    protected fun provideCircuit(): Circuit = Circuit.Builder()
        .addUiFactories(uiFactories)
        .addPresenterFactories(presenterFactories)
        .build()
}