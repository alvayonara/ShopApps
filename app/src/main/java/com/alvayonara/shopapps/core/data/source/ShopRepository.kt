package com.alvayonara.shopapps.core.data.source

import com.alvayonara.shopapps.core.data.source.remote.RemoteDataSource
import com.alvayonara.shopapps.core.data.source.remote.network.Result
import com.alvayonara.shopapps.core.data.source.remote.response.ItemResponse
import com.alvayonara.shopapps.core.data.source.remote.response.LoginResponse
import com.alvayonara.shopapps.core.data.source.remote.response.RegisterResponse
import com.alvayonara.shopapps.domain.model.Item
import com.alvayonara.shopapps.domain.repository.IShopRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : IShopRepository {

    override suspend fun postRegister(params: JsonObject): Flow<Result<RegisterResponse>> =
        remoteDataSource.postRegister(params)

    override suspend fun postLogin(params: JsonObject): Flow<Result<LoginResponse>> =
        remoteDataSource.postLogin(params)

    override suspend fun postAddItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>> = remoteDataSource.postAddItem(token, params)

    override suspend fun postUpdateItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>> = remoteDataSource.postUpdateItem(token, params)

    override suspend fun postDeleteItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>> = remoteDataSource.postDeleteItem(token, params)

    override suspend fun getListItem(token: String): Flow<Result<List<ItemResponse>>> =
        remoteDataSource.getListItem(token)

    override suspend fun getSearchListItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>> = remoteDataSource.getSearchListItem(token, params)
}