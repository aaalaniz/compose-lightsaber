package xyz.alaniz.aaron.lightsaber.di.metro

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Multibinds

@ContributesTo(AppScope::class)
interface CircuitMultibinds {
    @Multibinds(allowEmpty = true)
    fun presenterFactories(): Set<Presenter.Factory>

    @Multibinds(allowEmpty = true)
    fun uiFactories(): Set<Ui.Factory>
}
