package com.rose.greeneye.ui.find

import androidx.lifecycle.ViewModel
import com.rose.greeneye.data.PlantRepository
import com.rose.greeneye.data.remote.response.PlantsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FindViewModel
    @Inject
    constructor(private val plantRepository: PlantRepository) :
    ViewModel() {
        suspend fun searchPlant(scienceName: String): Flow<Result<PlantsResponse>> = plantRepository.searchPlant(scienceName)
    }
