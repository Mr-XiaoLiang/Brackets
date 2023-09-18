package com.lollipop.brackets_demo

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lollipop.brackets.core.BracketsUtils
import com.lollipop.brackets.core.Scope
import com.lollipop.brackets.impl.RadioGroupBrackets
import com.lollipop.brackets.impl.RadioGroupProtocol

class DemoRadioGroup(
    protocol: DemoRadioGroupProtocol
) : RadioGroupBrackets<DemoRadioGroupProtocol>(protocol) {

    override val typeId: Int
        get() = R.layout.brackets_radio_group

    override fun createView(parent: ViewGroup): View {
        return inflateView(parent, R.layout.brackets_radio_group)
    }

    override fun bindView(view: View) {
        view.child<TextView>(R.id.titleView) {
            it.text = protocol.title()
        }
    }
}

class DemoRadioGroupProtocol : RadioGroupProtocol()

fun Scope.RadioGroup(
    builder: DemoRadioGroupProtocol.() -> Unit
) {
    BracketsUtils.createBuilder<DemoRadioGroup, DemoRadioGroupProtocol>(this, builder)
}
