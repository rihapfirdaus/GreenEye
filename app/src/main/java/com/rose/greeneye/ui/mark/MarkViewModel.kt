package com.rose.greeneye.ui.mark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rose.greeneye.data.PlantRepository
import com.rose.greeneye.data.local.entity.FavoriteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarkViewModel
    @Inject
    constructor(private val plantRepository: PlantRepository) : ViewModel() {
        fun getFavorites(): LiveData<Result<List<FavoriteEntity>>> = plantRepository.getAllFavorite().asLiveData()
    }
