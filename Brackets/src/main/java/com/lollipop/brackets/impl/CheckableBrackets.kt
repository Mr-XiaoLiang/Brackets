package com.lollipop.brackets.impl

import com.lollipop.brackets.core.Brackets
import com.lollipop.brackets.core.Protocol
import com.lollipop.brackets.core.Stateful
import com.lollipop.brackets.core.TypedProvider
import com.lollipop.brackets.core.TypedResponse

open class CheckableProtocol : Protocol() {

    var isChecked: TypedProvider<Boolean> = Stateful(false)

    var onCheckedChanged: TypedResponse<Boolean> = TypedResponse {
        val provider = isChecked
        if (provider is Stateful) {
            provider.set(it)
        }
    }

}

abstract class CheckableBrackets<P : CheckableProtocol>(protocol: P) : Brackets<P>(protocol) {

    open fun onCheckedChanged(isChecked: Boolean) {
        protocol.onCheckedChanged(isChecked)
        notifyChanged()
    }

}
