package com.lollipop.brackets.framework

import com.lollipop.brackets.core.Brackets
import com.lollipop.brackets.core.ExpandGroupScope
import com.lollipop.brackets.core.Scope
import java.util.LinkedList

sealed class BracketsHandler : Brackets.Callback {

    private val bracketsRootScope = BracketsRootScope()
    private val finalBracketsList = ArrayList<Brackets>()

    fun build(builder: BracketsBuilder) {
        bracketsRootScope.bracketsList.clear()
        builder.buildBrackets(bracketsRootScope)
        parseBrackets()
    }

    private fun parseBrackets() {
        finalBracketsList.clear()
        val bracketsList = bracketsRootScope.bracketsList
        val pending = LinkedList<Brackets>()
        pending.addAll(bracketsList)
        while (pending.isNotEmpty()) {
            val first = filter(pending.removeFirst()) ?: continue
            first.setCallback(this)
            finalBracketsList.add(first)
            if (first is ExpandGroupScope<*>) {
                val children = first.children
                val size = children.size
                for (i in (size - 1) downTo 0) {
                    pending.addFirst(children[i])
                }
            }
        }
        onBracketsListReady(finalBracketsList)
    }

    protected abstract fun onBracketsListReady(list: List<Brackets>)

    protected open fun filter(brackets: Brackets): Brackets? {
        return brackets
    }

    override fun notifyItemChanged(tag: String) {
        for (i in finalBracketsList.indices) {
            if (finalBracketsList[i].tag == tag) {
                notifyItemChanged(i)
            }
        }
    }

    protected abstract fun notifyItemChanged(position: Int)

    class Linear: BracketsHandler() {
        override fun onBracketsListReady(list: List<Brackets>) {
            TODO("Not yet implemented")
        }

        override fun notifyItemChanged(position: Int) {
            TODO("Not yet implemented")
        }

    }

    fun interface BracketsBuilder {

        fun buildBrackets(scope: Scope<Brackets>)

    }

}