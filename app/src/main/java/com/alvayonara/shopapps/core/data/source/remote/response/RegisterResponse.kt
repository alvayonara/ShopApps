package com.alvayonara.shopapps.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("message") val message: String? = "",
    @SerializedName("email") val email: List<String>? = null
)