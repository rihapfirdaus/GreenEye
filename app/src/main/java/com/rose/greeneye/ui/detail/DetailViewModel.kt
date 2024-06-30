package com.rose.greeneye.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rose.greeneye.data.PlantRepository
import com.rose.greeneye.data.local.entity.FavoriteEntity
import com.rose.greeneye.data.local.entity.HistoryEntity
import com.rose.greeneye.data.remote.response.PlantResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
    @Inject
    constructor(
        private val plantRepository: PlantRepository,
    ) : ViewModel() {
        fun getPlantById(id: Int): LiveData<Result<PlantResponse>> = plantRepository.getPlantById(id).asLiveData()

        fun isFavoritePlant(scienceName: String): LiveData<Boolean> = plantRepository.getFavoriteByName(scienceName).asLiveData()

        suspend fun removeFavoritePlant(scienceName: String): Flow<Result<Unit>> = plantRepository.deleteFavoriteByName(scienceName)

        suspend fun addToFavorite(plant: FavoriteEntity): Flow<Result<Unit>> = plantRepository.addFavorite(plant)

        suspend fun addToHistory(plant: HistoryEntity): Flow<Result<Unit>> = plantRepository.addHistory(plant)
    }
