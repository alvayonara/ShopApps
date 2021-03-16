package com.alvayonara.shopapps.core.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.alvayonara.newsapps.core.base.BaseViewHolder
import timber.log.Timber

abstract class BaseAdapter<T, VH : BaseViewHolder<T>>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffCallback) {
    protected fun setLog(message: String) {
        Timber.e(message)
    }
}