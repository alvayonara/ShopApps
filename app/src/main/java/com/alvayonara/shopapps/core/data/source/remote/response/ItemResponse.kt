package com.alvayonara.shopapps.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ItemResponse(
    @SerializedName("kode_barang") val kode_barang: String? = "",
    @SerializedName("nama_barang") val nama_barang: String? = "",
    @SerializedName("jumlah_barang") val jumlah_barang: String? = "",
    @SerializedName("harga_barang") val harga_barang: String? = "",
    @SerializedName("satuan_barang") val satuan_barang: String? = "",
    @SerializedName("status_barang") val status_barang: String? = "",
    @SerializedName("message") val message: String? = ""
)