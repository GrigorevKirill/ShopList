package com.griga.shoplist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.griga.shoplist.R
import com.griga.shoplist.databinding.ItemShopDisabledBinding
import com.griga.shoplist.databinding.ItemShopEnabledBinding
import com.griga.shoplist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {
    companion object {
        const val VIEW_TYPE_ENABLED = 0
        const val VIEW_TYPE_DISABLED = 1
        const val MAX_POOL_SIZE = 15
    }

    var count = 0

    var shopList = listOf<ShopItem>()
        set(value) {
            val callback = ShopListDiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }
    var shopItemOnLongClickListener: ((ShopItem) -> Unit)? = null
    var shopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("ShopListAdapter", "onCreateViewHolder, count:${++count}")
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown ViewType $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        val binding = viewHolder.binding
        binding.root.setOnLongClickListener {
            shopItemOnLongClickListener?.invoke(shopItem)
            true
        }
        binding.root.setOnClickListener {
            shopItemClickListener?.invoke(shopItem)
        }
        when (binding) {
            is ItemShopDisabledBinding -> {
                binding.shopItem = shopItem
            }
            is ItemShopEnabledBinding -> {
                binding.shopItem = shopItem
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

//    override fun onViewRecycled(viewHolder: ShopItemViewHolder) {
//        viewHolder.tvName.setTextColor(
//            ContextCompat.getColor(
//                viewHolder.view.context,
//                android.R.color.white
//            )
//        )
//    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    class ShopItemViewHolder(
        val binding: ViewDataBinding
    ) : ViewHolder(binding.root) {
    }

}