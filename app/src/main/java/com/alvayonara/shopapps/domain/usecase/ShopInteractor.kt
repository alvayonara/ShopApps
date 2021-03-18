package com.alvayonara.shopapps.domain.usecase

import com.alvayonara.shopapps.core.data.source.remote.network.Result
import com.alvayonara.shopapps.core.data.source.remote.response.ItemResponse
import com.alvayonara.shopapps.core.data.source.remote.response.LoginResponse
import com.alvayonara.shopapps.core.data.source.remote.response.RegisterResponse
import com.alvayonara.shopapps.domain.repository.IShopRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShopInteractor @Inject constructor(private val shopRepository: IShopRepository) : ShopUseCase {

    override suspend fun postRegister(params: JsonObject): Flow<Result<RegisterResponse>> =
        shopRepository.postRegister(params)

    override suspend fun postLogin(params: JsonObject): Flow<Result<LoginResponse>> =
        shopRepository.postLogin(params)

    override suspend fun postAddItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>> = shopRepository.postAddItem(token, params)

    override suspend fun postUpdateItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>> = shopRepository.postUpdateItem(token, params)

    override suspend fun postDeleteItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>> = shopRepository.postDeleteItem(token, params)

    override suspend fun getListItem(token: String): Flow<Result<List<ItemResponse>>> =
        shopRepository.getListItem(token)

    override suspend fun getSearchListItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>> = shopRepository.getSearchListItem(token, params)
}