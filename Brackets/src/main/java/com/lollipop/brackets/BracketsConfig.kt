package com.lollipop.brackets

import com.lollipop.brackets.core.BracketsFilter

object BracketsConfig {

    var defaultBracketsFilter: Class<out BracketsFilter>? = null

    fun createBracketsFilter(): BracketsFilter? {
        return defaultBracketsFilter?.newInstance()
    }

}