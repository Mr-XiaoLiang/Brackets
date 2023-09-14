package com.lollipop.brackets.impl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lollipop.brackets.core.Brackets

abstract class XmlBrackets : Brackets() {

    abstract val layoutId: Int

    override val typeId: Int
        get() = layoutId

    override fun createView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

}