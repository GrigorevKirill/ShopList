package com.griga.shoplist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.griga.shoplist.R
import com.griga.shoplist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.shopList = it
        }
        binding.buttonAddShopItem.setOnClickListener {
            if (isPortraitOrientation()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                val fragment = ShopItemFragment.newIntentAddItem()
                launchFragment(fragment)
            }
        }
    }

    override fun onEditFinished() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun isPortraitOrientation(): Boolean {
        return binding.fragmentContainerView == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container_view, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() {
        with(binding.rvShopList) {
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
        setupSwipeListener(binding.rvShopList)
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
            if (isPortraitOrientation()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                val fragment = ShopItemFragment.newIntentEditItem(it.id)
                launchFragment(fragment)
            }
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.shopItemOnLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }


}