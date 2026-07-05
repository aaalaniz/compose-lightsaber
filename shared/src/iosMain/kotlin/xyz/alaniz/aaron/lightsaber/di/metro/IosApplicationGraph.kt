package xyz.alaniz.aaron.lightsaber.di.metro

import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.CoroutineScope
import xyz.alaniz.aaron.lightsaber.di.DataStorePath

@DependencyGraph(AppScope::class)
interface IosApplicationGraph {
    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides appScope: CoroutineScope,
            @Provides dataStorePath: DataStorePath,
        ): IosApplicationGraph
    }
}
