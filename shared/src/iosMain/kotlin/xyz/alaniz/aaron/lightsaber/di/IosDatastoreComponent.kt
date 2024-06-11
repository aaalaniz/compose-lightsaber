package xyz.alaniz.aaron.lightsaber.di

import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.KmpComponentCreate


@KmpComponentCreate
expect fun DatastoreComponent.Companion.create(scope: CoroutineScope, dataStorePath: String): DatastoreComponent
