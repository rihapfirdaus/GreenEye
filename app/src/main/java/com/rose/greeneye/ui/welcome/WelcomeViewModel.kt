package com.rose.greeneye.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rose.greeneye.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel
    @Inject
    constructor(private val userRepository: UserRepository) : ViewModel() {
        fun getSession(): LiveData<Boolean> = userRepository.getSession().asLiveData()

        suspend fun saveSession(firstInstalled: Boolean) {
            viewModelScope.launch {
                userRepository.saveSession(firstInstalled)
            }
        }
    }
