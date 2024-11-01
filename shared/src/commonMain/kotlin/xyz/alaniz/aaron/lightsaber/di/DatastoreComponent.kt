package xyz.alaniz.aaron.lightsaber.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import me.tatarka.inject.annotations.Provides
import okio.Path.Companion.toPath
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

const val dataStoreFileName = "lightsaber.preferences_pb"

@ContributesTo(AppScope::class)
interface DatastoreComponent {
    @Provides
    fun provideDatastorePreferences(dataStorePath: DataStorePath): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath {
            dataStorePath.value.toPath()
        }
    }
}