package com.griga.shoplist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.griga.shoplist.databinding.ShopItemFragmentBinding
import com.griga.shoplist.domain.ShopItem

class ShopItemFragment : Fragment() {

    private lateinit var viewModel: ShopItemViewModel

    private var _binding: ShopItemFragmentBinding? = null
    private val binding: ShopItemFragmentBinding
        get() = _binding ?: throw RuntimeException("ShopItemFragmentBinding == null")

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var shopItemId: Int = ShopItem.UNDEFINED_ID
    private var screenMode: String = MODE_UNKNOWN

    override fun onAttach(context: Context) {
        Log.d("ShopItemFragment", "onAttach")
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement onEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ShopItemFragment", "onCreate")
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ShopItemFragment", "onCreateView")
        _binding = ShopItemFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("ShopItemFragment", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setInputCountTextChangedListener()
        setInputNameTextChangedListener()
        checkIfFinished()
    }

    override fun onStart() {
        Log.d("ShopItemFragment", "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d("ShopItemFragment", "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("ShopItemFragment", "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("ShopItemFragment", "onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d("ShopItemFragment", "onDestroyView")
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d("ShopItemFragment", "onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d("ShopItemFragment", "onDestroy")
        super.onDetach()
    }

    private fun checkIfFinished() {
        viewModel.readyToFinish.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditFinished()
        }
    }

    private fun setInputNameTextChangedListener() {
        binding.etName.addTextChangedListener(object : TextWatcher {
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
        binding.etCount.addTextChangedListener(object : TextWatcher {
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
        viewModel.shopItem.observe(viewLifecycleOwner) {
            binding.etName.setText(it.name)
            binding.etCount.setText(it.count.toString())
        }
        binding.btSave.setOnClickListener {
            viewModel.editShopItem(
                binding.etName.text?.toString(),
                binding.etCount.text?.toString()
            )
        }
    }

    private fun launchAddMode() {
        binding.btSave.setOnClickListener {
            viewModel.addShopItem(binding.etName.text?.toString(), binding.etCount.text?.toString())
        }
    }

    private fun parseArguments() {
        val arguments = requireArguments()
        if (!arguments.containsKey(SCREEN_MODE)) {
            throw RuntimeException("No param screenMode")
        }
        val mode = arguments.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown param $mode")
        }
        screenMode = mode
        if (mode == MODE_EDIT && !arguments.containsKey(SHOP_ITEM_ID)) {
            throw RuntimeException("No param $shopItemId")
        }
        shopItemId = arguments.getInt(SHOP_ITEM_ID)

    }

    companion object {
        private const val SCREEN_MODE = "extra_screen_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newIntentEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }

    interface OnEditingFinishedListener {
        fun onEditFinished()
    }


}