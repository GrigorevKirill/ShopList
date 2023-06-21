package com.griga.shoplist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.griga.shoplist.R
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
        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        val status = if (shopItem.enabled) {
            "Active"
        } else {
            "Not active"
        }
        viewHolder.tvName.text = "${shopItem.name} $status"
        viewHolder.tvCount.text = shopItem.count.toString()
        viewHolder.view.setOnLongClickListener {
            shopItemOnLongClickListener?.invoke(shopItem)
            true
        }
        viewHolder.view.setOnClickListener {
            shopItemClickListener?.invoke(shopItem)
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

    class ShopItemViewHolder(val view: View) : ViewHolder(view) {
        val tvName: TextView = view.findViewById<TextView>(R.id.tv_name)
        val tvCount: TextView = view.findViewById<TextView>(R.id.tv_count)
    }

    interface ShopItemOnLongClickListener {
        fun shopItemOnClick(shopItem: ShopItem)
    }

    interface ShopItemClickListener {
        fun shopItemOnClick(shopItem: ShopItem)
    }
}