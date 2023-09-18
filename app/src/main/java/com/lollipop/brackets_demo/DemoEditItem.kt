package com.lollipop.brackets_demo

import android.text.Editable
import android.view.View
import com.lollipop.brackets.core.BracketsUtils
import com.lollipop.brackets.core.Protocol
import com.lollipop.brackets.core.Scope
import com.lollipop.brackets.core.TypedProvider
import com.lollipop.brackets.core.TypedResponse
import com.lollipop.brackets.impl.XmlBrackets
import com.lollipop.brackets.kit.BracketsEditView

class DemoEditProtocol : Protocol() {

    var value: TypedProvider<String> = TypedProvider { "" }

    var onInputChanged: TypedResponse<String> = TypedResponse { }

}

class DemoEditItem(
    protocol: DemoEditProtocol
) : XmlBrackets<DemoEditProtocol>(protocol), BracketsEditView.AfterTextChangedCallback {
    override val layoutId: Int
        get() = R.layout.brackets_edit

    override fun bindView(view: View) {

        view.child<BracketsEditView>(R.id.editView) {
            it.afterTextChanged(null)
            it.setText(protocol.value())
            it.afterTextChanged(this)
        }

    }

    override fun afterTextChanged(s: Editable?) {
        protocol.onInputChanged(s?.toString() ?: "")
    }
}

fun Scope.Edit(builder: DemoEditProtocol.() -> Unit) {
    BracketsUtils.createBuilder<DemoEditItem, DemoEditProtocol>(this, builder)
}