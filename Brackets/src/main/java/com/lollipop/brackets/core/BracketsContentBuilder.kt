package com.lollipop.brackets.core

fun interface BracketsContentBuilder<T : Brackets> {

    fun buildBrackets(scope: Scope<T>)

}