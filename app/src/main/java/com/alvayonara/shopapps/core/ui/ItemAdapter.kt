package com.alvayonara.shopapps.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.alvayonara.shopapps.core.base.BaseAdapter
import com.alvayonara.shopapps.core.base.BaseViewHolder
import com.alvayonara.shopapps.core.utils.*
import com.alvayonara.shopapps.core.utils.ConstPreferences.PREF_IS_LOGIN
import com.alvayonara.shopapps.databinding.ItemRowBinding
import com.alvayonara.shopapps.domain.model.Item

class ItemAdapter : BaseAdapter<Item, ItemAdapter.ItemViewHolder>(diffCallBack) {

    var onEditClick: ((Item) -> Unit)? = null
    var onDeleteClick: ((String) -> Unit)? = null

    companion object {
        val diffCallBack = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean {
                return oldItem.itemCode == newItem.itemCode
            }

            override fun areContentsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemRowBinding
            .inflate(inflater, parent, false)
        return ItemViewHolder(view.root, view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null)
            holder.bindView(item)

        holder.binding.btnEditItem.setOnClickListener {
            onEditClick?.invoke(item)
        }

        holder.binding.btnDeleteItem.setOnClickListener {
            onDeleteClick?.invoke(item.itemCode)
        }
    }

    inner class ItemViewHolder(
        root: View,
        val binding: ItemRowBinding
    ) : BaseViewHolder<Item>(root) {
        @SuppressLint("SetTextI18n")
        override fun bindView(element: Item) {
            binding.apply {
                tvItemName.text = element.itemName
                tvItemCode.text = "Kode Barang: ${element.itemCode}"
                tvItemPrice.text = "Rp. ${element.itemPrice}"
                tvItemStatus.text = "Status: ${element.itemStatus}"
                tvItemStock.text = "Stok: ${element.itemStock} ${element.itemUnit}"

                if (getDataPreferenceBoolean(itemView.context, PREF_IS_LOGIN)) {
                    btnEditItem.visible()
                    btnDeleteItem.visible()
                } else {
                    btnEditItem.invisible()
                    btnDeleteItem.invisible()
                }
            }
        }
    }
}