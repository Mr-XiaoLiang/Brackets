package com.lollipop.brackets.impl

open class RadioProtocol : CheckableProtocol()

abstract class RadioBrackets<P : RadioProtocol>(protocol: P) : CheckableBrackets<P>(protocol) {

    private var checkedCallback: CheckedCallback? = null

    init {
        // 尝试注册选中状态的观察器
        tryRegister(protocol.isChecked) { old, new ->
            if (old != new) {
                notifyChanged()
            }
        }
    }

    internal fun setCheckedCallback(callback: CheckedCallback) {
        this.checkedCallback = callback
    }

    override fun onCheckedChanged(isChecked: Boolean) {
        val groupCallback = checkedCallback
        if (groupCallback != null) {
            groupCallback.onCheckedChanged(this, isChecked)
        } else {
            super.onCheckedChanged(isChecked)
        }
    }

    internal fun setCheckState(isChecked: Boolean) {
        protocol.onCheckedChanged(isChecked)
        notifyChanged()
    }

    internal fun isChecked(): Boolean {
        return protocol.isChecked()
    }

    internal interface CheckedCallback {

        fun onCheckedChanged(brackets: RadioBrackets<*>, isChecked: Boolean)

    }

}
