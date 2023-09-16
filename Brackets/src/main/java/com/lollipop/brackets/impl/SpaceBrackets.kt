package com.lollipop.brackets.impl

import android.view.View
import android.view.ViewGroup
import android.widget.Space
import com.lollipop.brackets.core.Brackets
import com.lollipop.brackets.core.Protocol
import com.lollipop.brackets.core.Scope
import com.lollipop.brackets.core.TypedProvider

class SpaceProtocol : Protocol() {
    var size: TypedProvider<Float> = TypedProvider { 0F }

    var horizontal: TypedProvider<Float> = TypedProvider { size() }
    var vertical: TypedProvider<Float> = TypedProvider { size() }

}

class SpaceBrackets(protocol: SpaceProtocol) : Brackets<SpaceProtocol>(protocol) {
    override fun createView(parent: ViewGroup): View {
        return Space(parent.context)
    }

    override fun bindView(view: View) {
        val layoutParams = view.layoutParams
        if (layoutParams != null) {
            layoutParams.width = protocol.horizontal().toInt()
            layoutParams.height = protocol.vertical().toInt()
            view.layoutParams = layoutParams
            return
        }
        view.layoutParams = ViewGroup.LayoutParams(
            protocol.horizontal().toInt(),
            protocol.vertical().toInt()
        )
    }
}

inline fun Scope.Space(
    builder: SpaceProtocol.() -> Unit
) {
    val protocol = SpaceProtocol()
    builder(protocol)
    add(SpaceBrackets(protocol))
}