package com.alvayonara.shopapps.core.data.source.remote.network

import com.alvayonara.shopapps.core.data.source.remote.response.ItemResponse
import com.alvayonara.shopapps.core.data.source.remote.response.LoginResponse
import com.alvayonara.shopapps.core.data.source.remote.response.RegisterResponse
import com.alvayonara.shopapps.core.utils.ApiUrl
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST(ApiUrl.REGISTER)
    suspend fun postRegister(
        @Body params: JsonObject
    ): Response<RegisterResponse>

    @POST(ApiUrl.LOGIN)
    suspend fun postLogin(
        @Body params: JsonObject
    ): Response<LoginResponse>

    @POST(ApiUrl.ADD_ITEM)
    suspend fun postAddItem(
        @Header("Authorization") token: String,
        @Body params: JsonObject
    ): Response<ItemResponse>

    @POST(ApiUrl.UPDATE_ITEM)
    suspend fun postUpdateItem(
        @Header("Authorization") token: String,
        @Body params: JsonObject
    ): Response<ItemResponse>

    @POST(ApiUrl.DELETE_ITEM)
    suspend fun postDeleteItem(
        @Header("Authorization") token: String,
        @Body params: JsonObject
    ): Response<ItemResponse>

    @GET(ApiUrl.LIST_ITEM)
    suspend fun getListItem(
        @Header("Authorization") token: String
    ): Response<List<ItemResponse>>

    @POST(ApiUrl.SEARCH_ITEM)
    suspend fun getSearchListItem(
        @Header("Authorization") token: String,
        @Body params: JsonObject
    ): Response<ItemResponse>
}