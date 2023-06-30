package com.griga.shoplist.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("shop_item")
data class ShopItem(
    val name: String,
    val count: Int,
    val enabled: Boolean = true,
    @PrimaryKey(autoGenerate = true)
    var id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}