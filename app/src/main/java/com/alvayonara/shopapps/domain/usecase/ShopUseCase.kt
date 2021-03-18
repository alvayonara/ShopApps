package com.alvayonara.shopapps.domain.usecase

import com.alvayonara.shopapps.core.data.source.remote.network.Result
import com.alvayonara.shopapps.core.data.source.remote.response.ItemResponse
import com.alvayonara.shopapps.core.data.source.remote.response.LoginResponse
import com.alvayonara.shopapps.core.data.source.remote.response.RegisterResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow

interface ShopUseCase {
    suspend fun postRegister(params: JsonObject): Flow<Result<RegisterResponse>>

    suspend fun postLogin(params: JsonObject): Flow<Result<LoginResponse>>

    suspend fun postAddItem(token: String, params: JsonObject): Flow<Result<ItemResponse>>

    suspend fun postUpdateItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>>

    suspend fun postDeleteItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>>

    suspend fun getListItem(token: String): Flow<Result<List<ItemResponse>>>

    suspend fun getSearchListItem(
        token: String,
        params: JsonObject
    ): Flow<Result<ItemResponse>>
}