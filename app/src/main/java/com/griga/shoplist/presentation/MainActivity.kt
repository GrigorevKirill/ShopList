package com.griga.shoplist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.griga.shoplist.R

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var llShopList: LinearLayout
    private lateinit var shopListAdapter: ShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.shopList = it
        }
        val addButton = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        addButton.setOnClickListener {
            val intent = ShopItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        val rvShop = findViewById<RecyclerView>(R.id.rv_shop_list)
        with(rvShop) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter

            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }

        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShop)

    }

    private fun setupSwipeListener(rvShop: RecyclerView) {
        val callBack = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.shopList[viewHolder.adapterPosition]
                viewModel.removeShopItem(item)
            }

        }
        val itemTouchHelper = ItemTouchHelper(callBack)
        itemTouchHelper.attachToRecyclerView(rvShop)
    }

    private fun setupClickListener() {
        shopListAdapter.shopItemClickListener = {
           val intent = ShopItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.shopItemOnLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }


}