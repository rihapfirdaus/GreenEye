package com.rose.greeneye.data

import com.rose.greeneye.data.local.entity.FavoriteEntity
import com.rose.greeneye.data.local.entity.HistoryEntity
import com.rose.greeneye.data.local.room.AppDatabase
import com.rose.greeneye.data.remote.response.PlantResponse
import com.rose.greeneye.data.remote.response.PlantsResponse
import com.rose.greeneye.data.remote.response.PredictResponse
import com.rose.greeneye.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

class PlantRepository
    @Inject
    constructor(
        private val apiService: ApiService,
        private val appDatabase: AppDatabase,
    ) {
        suspend fun predictPlant(file: MultipartBody.Part): Flow<Result<PredictResponse>> =
            flow {
                emit(
                    Result.success(
                        apiService.predict(file),
                    ),
                )
            }.catch { e ->
                emit(Result.failure(e))
            }.flowOn(Dispatchers.IO)

        suspend fun searchPlant(keyword: String): Flow<Result<PlantsResponse>> =
            flow {
                emit(
                    Result.success(
                        apiService.search(keyword),
                    ),
                )
            }.catch { e ->
                emit(Result.failure(e))
            }.flowOn(Dispatchers.IO)

        fun getPlantById(id: Int): Flow<Result<PlantResponse>> =
            flow {
                emit(
                    Result.success(
                        apiService.getPlantById(id),
                    ),
                )
            }.catch { e ->
                emit(Result.failure(e))
            }.flowOn(Dispatchers.IO)

        fun getPlants(): Flow<Result<PlantsResponse>> =
            flow {
                emit(
                    Result.success(
                        apiService.getPlants(),
                    ),
                )
            }.catch { e ->
                emit(Result.failure(e))
            }.flowOn(Dispatchers.IO)

        suspend fun addFavorite(favoriteEntity: FavoriteEntity): Flow<Result<Unit>> =
            flow {
                appDatabase.favoriteDao().addToFavorite(favoriteEntity)
                emit(Result.success(Unit))
            }.catch { e ->
                emit(Result.failure(e))
            }.flowOn(Dispatchers.IO)

        fun getAllFavorite(): Flow<Result<List<FavoriteEntity>>> =
            flow {
                emit(
                    Result.success(
                        appDatabase.favoriteDao().getAllFavorite(),
                    ),
                )
            }.catch { e ->
                emit(Result.failure(e))
            }.flowOn(Dispatchers.IO)

        fun getFavoriteByName(scienceName: String): Flow<Boolean> =
            flow {
                emit(
                    appDatabase.favoriteDao().isFavorite(scienceName),
                )
            }.flowOn(Dispatchers.IO)

        suspend fun deleteFavoriteByName(scienceName: String): Flow<Result<Unit>> =
            flow {
                emit(
                    Result.success(
                        appDatabase.favoriteDao().deleteFavorite(scienceName),
                    ),
                )
            }.catch { e ->
                emit(Result.failure(e))
            }.flowOn(Dispatchers.IO)

        suspend fun addHistory(historyEntity: HistoryEntity): Flow<Result<Unit>> =
            flow {
                appDatabase.historyDao().addToHistory(historyEntity)
                emit(Result.success(Unit))
            }.catch { e ->
                emit(Result.failure(e))
            }.flowOn(Dispatchers.IO)

        fun getAllHistory(): Flow<Result<List<HistoryEntity>>> =
            flow {
                emit(
                    Result.success(
                        appDatabase.historyDao().getAllHistory(),
                    ),
                )
            }.catch { e ->
                emit(Result.failure(e))
            }.flowOn(Dispatchers.IO)

        fun deleteAllHistory(): Flow<Result<Unit>> =
            flow {
                emit(
                    Result.success(
                        appDatabase.historyDao().deleteAllHistory(),
                    ),
                )
            }.catch { e ->
                emit(Result.failure(e))
            }.flowOn(Dispatchers.IO)
    }
