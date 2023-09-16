package com.lollipop.brackets.framework

import com.lollipop.brackets.BracketsConfig
import com.lollipop.brackets.core.Brackets
import com.lollipop.brackets.core.BracketsContentBuilder
import com.lollipop.brackets.core.BracketsFilter
import com.lollipop.brackets.core.GroupBrackets
import java.util.LinkedList

class BracketsHandler(private val adapter: BracketsAdapter) : Brackets.Callback {

    private val bracketsRootScope = BracketsRootScope()
    private val finalBracketsList = ArrayList<Brackets<*>>()

    private var bracketsFilter: BracketsFilter? = null
    private val defaultFilter: BracketsFilter? by lazy {
        BracketsConfig.createBracketsFilter()
    }

    fun setFilter(filter: BracketsFilter?) {
        this.bracketsFilter = filter
    }

    private fun getFilter(): BracketsFilter? {
        return bracketsFilter ?: defaultFilter
    }

    fun build(builder: BracketsContentBuilder<Brackets<*>>) {
        bracketsRootScope.bracketsList.clear()
        builder.buildBrackets(bracketsRootScope)
        parseBrackets()
    }

    private fun parseBrackets() {
        finalBracketsList.clear()
        val bracketsList = bracketsRootScope.bracketsList
        val pending = LinkedList<Brackets<*>>()
        pending.addAll(bracketsList)
        while (pending.isNotEmpty()) {
            val first = filter(pending.removeFirst()) ?: continue
            first.setCallback(this)
            finalBracketsList.add(first)
            if (first is GroupBrackets<*, *> && first.expand) {
                val children = first.children
                val size = children.size
                for (i in (size - 1) downTo 0) {
                    pending.addFirst(children[i])
                }
                first.onChildrenReady()
            }
        }
        onBracketsListReady(finalBracketsList)
    }

    private fun onBracketsListReady(list: List<Brackets<*>>) {
        adapter.setData(list)
    }

    private fun filter(brackets: Brackets<*>): Brackets<*>? {
        val filter = getFilter() ?: return brackets
        return filter.filter(brackets)
    }

    override fun notifyItemChanged(tag: String) {
        adapter.notifyItemChanged(tag)
    }

}