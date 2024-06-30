package com.rose.greeneye.data.remote.response

import com.google.gson.annotations.SerializedName

data class PlantResponse(
    val data: DataPlantResponse,
)

data class PlantsResponse(
    val data: List<DataPlantResponse>,
)

data class DataPlantResponse(
    @field:SerializedName("science_name") val scienceName: String,
    @field:SerializedName("en_name") val enName: String,
    @field:SerializedName("id_name") val idName: String,
    @field:SerializedName("image_url") val imageUrl: Array<String>,
    val index: Int,
    val benefits: String,
    val effects: String,
    val description: String,
    val tips: String,
)
