package com.rose.greeneye.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences
    @Inject
    constructor(private val dataStore: DataStore<Preferences>) {
        fun getSession(): Flow<Boolean> {
            return dataStore.data.map { preferences ->
                preferences[SESSION_KEY] ?: false
            }
        }

        suspend fun saveSession(firstInstalled: Boolean) {
            dataStore.edit { preferences ->
                preferences[SESSION_KEY] = firstInstalled
            }
        }

        companion object {
            private val SESSION_KEY = booleanPreferencesKey("first_installed")
        }
    }
