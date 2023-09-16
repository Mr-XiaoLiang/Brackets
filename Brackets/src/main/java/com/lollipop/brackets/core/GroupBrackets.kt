package com.lollipop.brackets.core

import androidx.annotation.CallSuper

abstract class GroupBrackets<B : Brackets<*>, P : GroupProtocol<B>>(
    protocol: P
) : Brackets<P>(protocol) {

    val children: List<B> = protocol.children

    open val expand: Boolean = true

    @CallSuper
    open fun onChildrenReady() {
        protocol.onChildrenReady()
    }

}