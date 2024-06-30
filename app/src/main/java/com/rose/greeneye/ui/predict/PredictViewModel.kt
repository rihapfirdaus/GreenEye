package com.rose.greeneye.ui.predict

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rose.greeneye.data.PlantRepository
import com.rose.greeneye.data.remote.response.PredictResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class PredictViewModel
    @Inject
    constructor(
        private val plantRepository: PlantRepository,
    ) : ViewModel() {
        private val _currentImageUri = MutableLiveData<Uri?>()
        val currentImageUri: LiveData<Uri?> get() = _currentImageUri

        fun setCurrentImageUri(uri: Uri?) {
            _currentImageUri.value = uri
        }

        suspend fun predictPlant(file: MultipartBody.Part): Flow<Result<PredictResponse>> = plantRepository.predictPlant(file)
    }
