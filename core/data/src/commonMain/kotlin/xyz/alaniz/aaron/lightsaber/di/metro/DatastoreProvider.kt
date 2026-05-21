package xyz.alaniz.aaron.lightsaber.di.metro

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import okio.Path.Companion.toPath
import xyz.alaniz.aaron.lightsaber.di.DataStorePath

const val dataStoreFileName = "lightsaber.preferences_pb"

@ContributesTo(AppScope::class)
interface DatastoreProvider {
    @Provides
    fun provideDatastorePreferences(dataStorePath: DataStorePath): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath {
            dataStorePath.value.toPath()
        }
    }
}