package xyz.alaniz.aaron.lightsaber.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import okio.Path.Companion.toPath

const val dataStoreFileName = "lightsaber.preferences_pb"

@Component
@DataScope
abstract class DatastoreComponent(
    @get:Provides internal val scope: CoroutineScope,
    private val dataStorePath: String) {
    @Provides
    @DataScope
    internal fun provideDatastorePreferences(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath {
            dataStorePath.toPath()
        }
    }
    companion object
}