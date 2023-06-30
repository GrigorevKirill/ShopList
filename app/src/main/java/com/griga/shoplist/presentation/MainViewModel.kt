package com.griga.shoplist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.griga.shoplist.data.ShopListRepositoryImpl
import com.griga.shoplist.domain.EditShopItemUseCase
import com.griga.shoplist.domain.GetShopListUseCase
import com.griga.shoplist.domain.RemoveShopItemUseCase
import com.griga.shoplist.domain.ShopItem

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val removeShopItemUseCase = RemoveShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()


    fun removeShopItem(shopItem: ShopItem) {
        removeShopItemUseCase.removeShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }
}