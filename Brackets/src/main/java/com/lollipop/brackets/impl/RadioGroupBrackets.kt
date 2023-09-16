package com.lollipop.brackets.impl

import android.view.View
import com.lollipop.brackets.core.Brackets
import com.lollipop.brackets.core.GroupBrackets
import com.lollipop.brackets.core.GroupProtocol
import com.lollipop.brackets.core.Stateful
import com.lollipop.brackets.core.TypedProvider
import com.lollipop.brackets.core.TypedResponse

open class RadioGroupProtocol : GroupProtocol() {

    var checkedTag: TypedProvider<String> = TypedProvider { "" }

    var onCheckedChanged: TypedResponse<CheckResult> = TypedResponse {
        val provider = checkedTag
        if (provider is Stateful) {
            provider.set(
                if (it.isChecked) {
                    it.tag
                } else {
                    ""
                }
            )
        }
    }

    class CheckResult(val tag: String, val isChecked: Boolean)

}

abstract class RadioGroupBrackets<P : RadioGroupProtocol>(
    protocol: P
) : GroupBrackets<P>(protocol), RadioBrackets.CheckedCallback {

    private var checkStateLock = false

    override val expand: Boolean
        get() = true

    override fun onChildrenReady() {
        super.onChildrenReady()
        var firstChecked = protocol.checkedTag()
        forEachChildrenByType<RadioBrackets<*>> {
            it.setCheckedCallback(this)
            if (firstChecked.isEmpty()) {
                if (it.isChecked()) {
                    firstChecked = it.tag
                }
            } else {
                it.setCheckState(false)
            }
        }
    }

    override fun bindView(view: View) {
    }

    override fun onCheckedChanged(brackets: RadioBrackets<*>, isChecked: Boolean) {
        if (checkStateLock) {
            return
        }
        checkStateLock = true
        val newTag = brackets.tag
        val checkedTag = protocol.checkedTag()
        if (isChecked) {
            if (checkedTag != newTag) {
                protocol.onCheckedChanged(RadioGroupProtocol.CheckResult(newTag, true))
            }
        } else {
            if (checkedTag == newTag) {
                protocol.onCheckedChanged(RadioGroupProtocol.CheckResult(newTag, false))
            }
        }
        forEachChildrenByType<RadioBrackets<*>> {
            if (brackets !== it) {
                it.setCheckState(it.tag == newTag)
            }
        }
        checkStateLock = false
    }

}



