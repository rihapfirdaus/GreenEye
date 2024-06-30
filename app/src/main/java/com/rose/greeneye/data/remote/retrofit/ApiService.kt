package com.rose.greeneye.data.remote.retrofit

import com.rose.greeneye.data.remote.response.PlantResponse
import com.rose.greeneye.data.remote.response.PlantsResponse
import com.rose.greeneye.data.remote.response.PredictResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("predict")
    suspend fun predict(
        @Part file: MultipartBody.Part,
    ): PredictResponse

    @POST("search")
    suspend fun search(
        @Query("keyword") keyword: String,
    ): PlantsResponse

    @GET("plants/{plant_id}")
    suspend fun getPlantById(
        @Path("plant_id") plantId: Int,
    ): PlantResponse

    @GET("plants")
    suspend fun getPlants(): PlantsResponse
}
