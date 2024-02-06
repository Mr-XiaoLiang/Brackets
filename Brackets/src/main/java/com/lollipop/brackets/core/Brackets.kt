package com.lollipop.brackets.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

/**
 * DSL Item 的最基础单元
 */
abstract class Brackets<P : Protocol>(protected val protocol: P) {

    companion object {
        const val ID_ITEM_VIEW = 0
    }

    /**
     * item的类型，它主要用于在可以复用的场景下，用于区分并且复用Item
     */
    open val typeId: Int = 0

    open val clickableViewIdArray: IntArray = intArrayOf(ID_ITEM_VIEW)

    protected val defaultTag by lazy {
        System.identityHashCode(this).toString(16)
    }

    open val tag: String
        get() {
            return protocol.tag().ifBlank { defaultTag }
        }

    private var callback: Callback? = null

    open fun onViewClick(itemView: View, id: Int) {}

    internal fun setCallback(callback: Callback) {
        this.callback = callback
    }

    abstract fun createView(parent: ViewGroup): View

    abstract fun bindView(view: View)

    protected fun notifyChanged() {
        this.callback?.notifyItemChanged(tag)
    }

    protected fun inflateView(parent: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

    protected inline fun <reified T> tryRegister(
        provider: TypedProvider<T>,
        noinline onValueChanged: (last: T, new: T) -> Unit
    ) {
        if (provider is Stateful<T>) {
            provider.live.observeForever(StatefulObserver(provider.invoke(), onValueChanged))
        }
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

    protected class StatefulObserver<T>(
        var lastValue: T,
        private val onChanged: (last: T, new: T) -> Unit
    ) : Observer<T> {

        override fun onChanged(value: T) {
            onChanged(lastValue, value)
            lastValue = value
        }

    }

}

