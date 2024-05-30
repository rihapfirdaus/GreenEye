package com.rose.greeneye.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rose.greeneye.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _currentPage = MutableLiveData<Int>()
        val currentPage: LiveData<Int> = _currentPage

        fun setCurrentPage(page: Int) {
            _currentPage.value = page
        }

        init {
            _currentPage.value = 1
        }

        fun getSession(): Flow<Boolean> = userRepository.getSession()
    }
