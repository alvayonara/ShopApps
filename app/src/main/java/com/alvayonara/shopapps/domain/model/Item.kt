package com.alvayonara.shopapps.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val itemCode: String,
    val itemName: String,
    val itemStock: String,
    val itemPrice: String,
    val itemUnit: String,
    val itemStatus: String,
    val message: String
): Parcelable