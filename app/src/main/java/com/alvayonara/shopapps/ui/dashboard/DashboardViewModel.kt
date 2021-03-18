package com.alvayonara.shopapps.ui.dashboard

import androidx.lifecycle.*
import com.alvayonara.shopapps.core.base.BaseViewModel
import com.alvayonara.shopapps.core.data.source.remote.network.Result
import com.alvayonara.shopapps.core.data.source.remote.response.ItemResponse
import com.alvayonara.shopapps.core.utils.ConstPreferences.PREF_TOKEN
import com.alvayonara.shopapps.core.utils.getDataPreferenceString
import com.alvayonara.shopapps.domain.model.Item
import com.alvayonara.shopapps.domain.usecase.ShopUseCase
import com.google.gson.JsonObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import org.json.JSONObject
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DashboardViewModel @Inject constructor(private val shopUseCase: ShopUseCase) :
    BaseViewModel() {

    private val token = MutableLiveData<String>()
    fun setToken(token: String) {
        this.token.value = token
    }

    private val item = MutableLiveData<JsonObject>()
    fun setSelectedItem(item: Item) {
        val request = JSONObject().let {
            it.put("kode_barang", item.itemCode)
            it.put("nama_barang", item.itemName)
            it.put("jumlah_barang", item.itemStock)
            it.put("harga_barang", item.itemPrice)
            it.put("satuan_barang", item.itemUnit)
            it.put("status_barang", item.itemStatus)
            constructRawRequest(it)
        }
        this.item.value = request
    }

    private val itemCode = MutableLiveData<JsonObject>()
    fun setItemCode(itemCode: String) {
        val request = JSONObject().let {
            it.put("kode_barang", itemCode)
            constructRawRequest(it)
        }
        this.itemCode.value = request
    }

    private val queryChannelSearch = ConflatedBroadcastChannel<String>()
    fun setSelectedSearch(search: String) {
        queryChannelSearch.offer(search)
    }

    fun getListItem(): LiveData<Result<List<ItemResponse>>> = liveData {
        emitSource(shopUseCase.getListItem(token.value.orEmpty()).asLiveData())
    }

    val postAddItem = item.switchMap {
        liveData {
            emitSource(shopUseCase.postAddItem(token.value.orEmpty(), it).asLiveData())
        }
    }

    val postUpdateItem = item.switchMap {
        liveData {
            emitSource(shopUseCase.postUpdateItem(token.value.orEmpty(), it).asLiveData())
        }
    }

    val postDeleteItem = itemCode.switchMap {
        liveData {
            emitSource(shopUseCase.postDeleteItem(token.value.orEmpty(), it).asLiveData())
        }
    }

    @FlowPreview
    val searchBarang = queryChannelSearch.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .flatMapLatest {
            val request = JSONObject().let { obj ->
                obj.put("kode_barang", it)
                constructRawRequest(obj)
            }
            shopUseCase.getSearchListItem(token.value.orEmpty(), request)
        }
        .asLiveData()
}