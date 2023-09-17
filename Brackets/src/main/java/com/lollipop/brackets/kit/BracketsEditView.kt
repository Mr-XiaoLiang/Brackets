package com.lollipop.brackets.kit

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class BracketsEditView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : TextInputEditText(context, attributeSet) {

    private val singleTextChangedListener = SingleTextChangedListener()

    init {
        addTextChangedListener(singleTextChangedListener)
    }

    fun beforeTextChanged(callback: BeforeTextChangedCallback?) {
        singleTextChangedListener.beforeTextChangedCallback = callback
    }

    fun onTextChanged(callback: OnTextChangedCallback?) {
        singleTextChangedListener.onTextChangedCallback = callback
    }

    fun afterTextChanged(callback: AfterTextChangedCallback?) {
        singleTextChangedListener.afterTextChangedCallback = callback
    }

    private class SingleTextChangedListener : TextWatcher {

        var beforeTextChangedCallback: BeforeTextChangedCallback? = null
        var onTextChangedCallback: OnTextChangedCallback? = null
        var afterTextChangedCallback: AfterTextChangedCallback? = null

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChangedCallback?.beforeTextChanged(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChangedCallback?.onTextChanged(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {
            afterTextChangedCallback?.afterTextChanged(s)
        }

    }

    fun interface BeforeTextChangedCallback {
        fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
    }

    fun interface OnTextChangedCallback {
        fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)

    }

    fun interface AfterTextChangedCallback {
        fun afterTextChanged(s: Editable?)

    }


}