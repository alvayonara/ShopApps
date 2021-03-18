package com.alvayonara.shopapps.core.utils

import com.alvayonara.shopapps.core.data.source.remote.response.ItemResponse
import com.alvayonara.shopapps.domain.model.Item

object DataMapper {

    /**
     * Get barang
     */
    fun mapItemResponsesToDomain(input: ItemResponse): Item = input.let {
        Item(
            itemCode = it.kode_barang.orEmpty(),
            itemName = it.nama_barang.orEmpty(),
            itemStock = it.jumlah_barang.orEmpty(),
            itemPrice = it.harga_barang.orEmpty(),
            itemUnit = it.satuan_barang.orEmpty(),
            itemStatus = it.status_barang.orEmpty(),
            message = it.message ?: ""
        )
    }

    /**
     * Get list barang
     */
    fun mapListItemResponsesToDomain(input: List<ItemResponse>): List<Item> = input.map {
        Item(
            itemCode = it.kode_barang.orEmpty(),
            itemName = it.nama_barang.orEmpty(),
            itemStock = it.jumlah_barang.orEmpty(),
            itemPrice = it.harga_barang.orEmpty(),
            itemUnit = it.satuan_barang.orEmpty(),
            itemStatus = it.status_barang.orEmpty(),
            message = it.message ?: ""
        )
    }
}