package com.lollipop.brackets.core

import android.view.View
import android.view.ViewGroup

/**
 * DSL Item 的最基础单元
 */
abstract class Brackets {

    /**
     * item的类型，它主要用于在可以复用的场景下，用于区分并且复用Item
     */
    open val typeId: Int = 0

    protected val defaultTag by lazy {
        System.identityHashCode(this).toString(16)
    }

    open val tag: String
        get() {
            return defaultTag
        }

    private var callback: Callback? = null

    internal fun setCallback(callback: Callback) {
        this.callback = callback
    }

    abstract fun createView(parent: ViewGroup): View

    abstract fun bindView(view: View)

    protected fun notifyChanged() {
        this.callback?.notifyItemChanged(tag)
    }

    protected inline fun <reified T : View> View.child(id: Int): T? {
        val viewById = findViewById<View>(id) ?: return null
        if (viewById is T) {
            return viewById
        }
        return null
    }

    protected inline fun <reified T : View> View.child(id: Int, callback: (T) -> Unit) {
        val childView = child<T>(id)
        if (childView != null) {
            callback(childView)
        }
    }

    internal interface Callback {

        fun notifyItemChanged(tag: String)

    }

}

