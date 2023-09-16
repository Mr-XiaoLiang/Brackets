package com.lollipop.brackets.framework

import com.lollipop.brackets.core.Brackets
import com.lollipop.brackets.core.Scope

class BracketsRootScope : Scope {

    val bracketsList = ArrayList<Brackets<*>>()

    override fun add(item: Brackets<*>) {
        bracketsList.add(item)
    }
}