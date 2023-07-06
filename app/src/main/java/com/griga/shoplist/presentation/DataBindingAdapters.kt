package com.griga.shoplist.presentation

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.griga.shoplist.R

@BindingAdapter("nameError")
fun bindNameError(textInputLayout: TextInputLayout, error: Boolean) {
    val message = if (error) {
        textInputLayout.context.getString(R.string.error_input_name)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("countError")
fun bindCountError(textInputLayout: TextInputLayout, error: Boolean) {
    val message = if (error) {
        textInputLayout.context.getString(R.string.error_input_count)
    } else {
        null
    }
    textInputLayout.error = message
}