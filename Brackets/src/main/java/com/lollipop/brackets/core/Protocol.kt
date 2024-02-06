package com.lollipop.brackets.core

@BracketsScope
open class Protocol {

    var title: TypedProvider<String> = TypedProvider { "" }

    var tag: Stateless<String> = Stateless("")

}

open class GroupProtocol : Protocol(), Scope {

    private val childrenList = ArrayList<Brackets<*>>()

    protected var childrenLock = false

    val children: List<Brackets<*>>
        get() {
            return childrenList
        }

    override fun add(item: Brackets<*>) {
        if (childrenLock) {
            return
        }
        childrenList.add(item)
    }

    open fun onChildrenReady() {
        childrenLock = true
    }

}