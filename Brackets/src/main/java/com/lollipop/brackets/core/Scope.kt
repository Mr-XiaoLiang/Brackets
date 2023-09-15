package com.lollipop.brackets.core

interface Scope<T : Brackets> {

    fun add(item: T)

}

interface ExpandGroupScope<T : Brackets> : Scope<T> {

    val children: List<Brackets>

}