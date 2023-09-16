package com.lollipop.brackets_demo

import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.TextView
import com.lollipop.brackets.impl.RadioBrackets
import com.lollipop.brackets.impl.RadioProtocol


class DemoRadioItem(
    protocol: RadioProtocol
) : RadioBrackets<RadioProtocol>(protocol),
    CompoundButton.OnCheckedChangeListener {

    override val typeId: Int
        get() = R.layout.brackets_radio_item

    override fun createView(parent: ViewGroup): View {
        return inflateView(parent, R.layout.brackets_radio_item)
    }

    override fun bindView(view: View) {
        view.child<TextView>(R.id.titleView) {
            it.text = protocol.title()
        }
        view.child<RadioButton>(R.id.radioButton) {
            it.setOnCheckedChangeListener(this)
            it.isChecked = protocol.isChecked()
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked != protocol.isChecked()) {
            onCheckedChanged(isChecked)
        }
    }
}

inline fun DemoRadioGroupProtocol.Radio(
    builder: RadioProtocol.() -> Unit
) {
    val protocol = RadioProtocol()
    builder(protocol)
    add(DemoRadioItem(protocol))
}
