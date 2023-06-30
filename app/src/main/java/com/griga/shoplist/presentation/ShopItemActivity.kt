package com.griga.shoplist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.griga.shoplist.R
import com.griga.shoplist.databinding.ActivityShopItemBinding
import com.griga.shoplist.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopItemBinding
    private lateinit var viewModel: ShopItemViewModel
    private var shopItemId = ShopItem.UNDEFINED_ID
    private var screenMode = MODE_UNKNOWN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        when(screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
        setInputCountTextChangedListener()
        setInputNameTextChangedListener()
        observeInputCountErrors()
        observeInputNameErrors()
        checkIfFinished()
    }

    private fun checkIfFinished() {
        viewModel.readyToFinish.observe(this) {
            finish()
        }
    }

    private fun observeInputNameErrors() {
        viewModel.errorInputName.observe(this) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            binding.tilName.error = message
        }
    }

    private fun observeInputCountErrors() {
        viewModel.errorInputCount.observe(this) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            binding.tilCount.error = message
        }
    }

    private fun setInputNameTextChangedListener() {
        binding.etName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setInputCountTextChangedListener() {
        binding.etCount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(this) {
            binding.etName.setText(it.name)
            binding.etCount.setText(it.count.toString())
        }
       binding.btSave.setOnClickListener {
           viewModel.editShopItem(binding.etName.text?.toString(), binding.etCount.text?.toString())
       }
    }

    private fun launchAddMode() {
        binding.btSave.setOnClickListener {
            viewModel.addShopItem(binding.etName.text?.toString(), binding.etCount.text?.toString())
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("No param $EXTRA_SCREEN_MODE")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown param $EXTRA_SCREEN_MODE")
        }
        screenMode = mode
        if (mode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("No param $EXTRA_SHOP_ITEM_ID")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }


    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_screen_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, id: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, id)
            return intent
        }
    }
}