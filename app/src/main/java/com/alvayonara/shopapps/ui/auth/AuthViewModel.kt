package com.alvayonara.shopapps.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.alvayonara.shopapps.core.base.BaseViewModel
import com.alvayonara.shopapps.domain.usecase.ShopUseCase
import com.google.gson.JsonObject
import org.json.JSONObject
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val shopUseCase: ShopUseCase) : BaseViewModel() {

    private val login = MutableLiveData<JsonObject>()
    private val register = MutableLiveData<JsonObject>()

    fun setLogin(email: String, password: String) {
        val request = JSONObject().let {
            it.put("email", email)
            it.put("password", password)
            constructRawRequest(it)
        }
        setLog("kentir ${request}")
        this.login.value = request
    }

    fun setRegister(email: String, password: String) {
        val request = JSONObject().let {
            it.put("email", email)
            it.put("password", password)
            constructRawRequest(it)
        }
        this.register.value = request
    }

    val postLogin = login.switchMap {
        liveData {
            emitSource(shopUseCase.postLogin(it).asLiveData())
        }
    }

    val postRegister = register.switchMap {
        liveData {
            emitSource(shopUseCase.postRegister(it).asLiveData())
        }
    }
}