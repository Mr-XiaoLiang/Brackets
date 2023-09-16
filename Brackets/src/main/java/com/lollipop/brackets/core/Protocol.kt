package com.lollipop.brackets.core

open class Protocol {

    var title: TypedProvider<String> = TypedProvider { "" }

    var tag: Stateless<String> = Stateless("")

}

open class GroupProtocol<T : Brackets<*>> : Protocol(), Scope<T> {

    private val childrenList = ArrayList<T>()

    protected var childrenLock = false

    val children: List<T>
        get() {
            return childrenList
        }

    override fun add(item: T) {
        if (childrenLock) {
            return
        }
        childrenList.add(item)
    }

    open fun onChildrenReady() {
        childrenLock = true
    }

}