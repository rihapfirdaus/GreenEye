package com.rose.greeneye.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rose.greeneye.data.local.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "application")

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.dataStore

    @Provides
    fun providesAuthPreferences(dataStore: DataStore<Preferences>): UserPreferences = UserPreferences(dataStore)
}
