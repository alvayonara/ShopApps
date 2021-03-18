package com.alvayonara.shopapps.core.data.source.remote

import com.alvayonara.shopapps.core.base.BaseDataSource
import com.alvayonara.shopapps.core.data.source.remote.network.ApiService
import com.alvayonara.shopapps.core.data.source.remote.network.Result
import com.alvayonara.shopapps.core.data.source.remote.response.ItemResponse
import com.alvayonara.shopapps.core.data.source.remote.response.LoginResponse
import com.alvayonara.shopapps.core.data.source.remote.response.RegisterResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) : BaseDataSource() {

    suspend fun postRegister(params: JsonObject): Flow<Result<RegisterResponse>> = flow {
        emit(Result.loading(null))
        emit(safeApiCall { apiService.postRegister(params) })
    }.flowOn(Dispatchers.IO)

    suspend fun postLogin(params: JsonObject): Flow<Result<LoginResponse>> = flow {
        emit(Result.loading(null))
        emit(safeApiCall { apiService.postLogin(params) })
    }.flowOn(Dispatchers.IO)

    suspend fun postAddItem(token: String, params: JsonObject): Flow<Result<ItemResponse>> =
        flow {
            emit(Result.loading(null))
            emit(safeApiCall { apiService.postAddItem(token, params) })
        }.flowOn(Dispatchers.IO)

    suspend fun postUpdateItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>> =
        flow {
            emit(Result.loading(null))
            emit(safeApiCall { apiService.postUpdateItem(token, params) })
        }.flowOn(Dispatchers.IO)

    suspend fun postDeleteItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>> =
        flow {
            emit(Result.loading(null))
            emit(safeApiCall { apiService.postDeleteItem(token, params) })
        }.flowOn(Dispatchers.IO)

    suspend fun getListItem(token: String): Flow<Result<List<ItemResponse>>> = flow {
        emit(Result.loading(null))
        emit(safeApiCall { apiService.getListItem(token) })
    }.flowOn(Dispatchers.IO)

    suspend fun getSearchListItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>> = flow {
        emit(Result.loading(null))
        emit(safeApiCall { apiService.getSearchListItem(token, params) })
    }.flowOn(Dispatchers.IO)
}