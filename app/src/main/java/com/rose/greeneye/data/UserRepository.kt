package com.rose.greeneye.data

import com.rose.greeneye.data.local.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository
    @Inject
    constructor(
        private val userPreferences: UserPreferences,
    ) {
        fun getSession(): Flow<Boolean> = userPreferences.getSession()

        suspend fun saveSession(firstInstalled: Boolean) {
            userPreferences.saveSession(firstInstalled)
        }
    }
