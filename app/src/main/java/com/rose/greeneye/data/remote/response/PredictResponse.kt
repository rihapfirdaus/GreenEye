package com.rose.greeneye.data.remote.response

data class PredictResponse(
    val data: DataPredictResponse,
)

data class DataPredictResponse(
    val organs: String,
    val classname: String,
    val plants: DataPlantResponse,
    val index: Int,
    val accuracy: Float,
)
