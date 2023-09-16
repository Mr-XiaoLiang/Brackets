package com.lollipop.brackets.core

fun interface BracketsFilter {
    fun filter(brackets: Brackets<*>): Brackets<*>?
}