package xyz.alaniz.aaron.lightsaber.di.metro

import android.content.Context
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import kotlinx.coroutines.CoroutineScope
import xyz.alaniz.aaron.lightsaber.di.AppContext
import xyz.alaniz.aaron.lightsaber.di.DataStorePath

@DependencyGraph(AppScope::class)
interface AndroidApplicationGraph {
    @Provides
    @SingleIn(AppScope::class)
    fun provideAppContext(appContext: AppContext): Context = appContext.value

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides appScope: CoroutineScope,
            @Provides dataStorePath: DataStorePath,
            @Provides appContext: AppContext
        ): AndroidApplicationGraph
    }
}