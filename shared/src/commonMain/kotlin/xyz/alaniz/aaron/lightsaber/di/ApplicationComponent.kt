package xyz.alaniz.aaron.lightsaber.di

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface ApplicationComponent {
    val circuit: Circuit

    @Provides
    fun provideCircuit(
        presenterFactories: Set<Presenter.Factory>,
        uiFactories: Set<Ui.Factory>
    ) = Circuit.Builder()
        .addUiFactories(uiFactories)
        .addPresenterFactories(presenterFactories)
        .build()
}