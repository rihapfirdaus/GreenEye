package com.rose.greeneye.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rose.greeneye.data.PlantRepository
import com.rose.greeneye.data.local.entity.HistoryEntity
import com.rose.greeneye.data.remote.response.PlantsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel
    @Inject
    constructor(
        private val plantRepository: PlantRepository,
    ) : ViewModel() {
        fun getPlants(): LiveData<Result<PlantsResponse>> = plantRepository.getPlants().asLiveData()

        fun getHistories(): LiveData<Result<List<HistoryEntity>>> = plantRepository.getAllHistory().asLiveData()
    }
