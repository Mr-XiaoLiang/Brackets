package com.lollipop.brackets.impl

import android.view.View
import android.view.ViewGroup
import com.lollipop.brackets.core.Brackets
import com.lollipop.brackets.core.Protocol

abstract class XmlBrackets<P : Protocol>(protocol: P) : Brackets<P>(protocol) {

    abstract val layoutId: Int

    override val typeId: Int
        get() = layoutId

    override fun createView(parent: ViewGroup): View {
        return inflateView(parent, layoutId)
    }

}