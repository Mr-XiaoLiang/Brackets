package com.lollipop.brackets.core

import androidx.annotation.CallSuper

abstract class GroupBrackets<P : GroupProtocol>(
    protocol: P
) : Brackets<P>(protocol) {

    val children: List<Brackets<*>> = protocol.children

    open val expand: Boolean = true

    protected inline fun <reified T: Brackets<*>> forEachChildrenByType(callback: (T) -> Unit) {
        children.forEach {
            if (it is T) {
                callback(it)
            }
        }
    }

    @CallSuper
    open fun onChildrenReady() {
        protocol.onChildrenReady()
    }

}