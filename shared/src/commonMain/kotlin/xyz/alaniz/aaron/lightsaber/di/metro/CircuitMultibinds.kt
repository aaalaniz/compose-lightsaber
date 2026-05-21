package xyz.alaniz.aaron.lightsaber.di.metro

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.ElementsIntoSet
import xyz.alaniz.aaron.lightsaber.feature.lightsaber.LightsaberPresenterFactory
import xyz.alaniz.aaron.lightsaber.feature.lightsaber.LightsaberFactory
import xyz.alaniz.aaron.lightsaber.feature.settings.SettingsPresenterFactory
import xyz.alaniz.aaron.lightsaber.feature.settings.SettingsFactory

@ContributesTo(AppScope::class)
interface CircuitMultibinds {
    @Provides
    @ElementsIntoSet
    fun providePresenterFactories(
        lightsaber: LightsaberPresenterFactory,
        settings: SettingsPresenterFactory
    ): Set<Presenter.Factory> = setOf(lightsaber, settings)

    @Provides
    @ElementsIntoSet
    fun provideUiFactories(
        lightsaber: LightsaberFactory,
        settings: SettingsFactory
    ): Set<Ui.Factory> = setOf(lightsaber, settings)
}
