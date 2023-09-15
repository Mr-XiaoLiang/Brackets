package com.lollipop.brackets.kit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.brackets.core.Brackets
import com.lollipop.brackets.core.BracketsContentBuilder
import com.lollipop.brackets.core.Scope
import com.lollipop.brackets.framework.BracketsHandler
import com.lollipop.brackets.framework.FullBracketsAdapter
import com.lollipop.brackets.framework.RecyclerBracketsAdapter

abstract class BracketsFragment : Fragment() {

    private var adapterHandler: BracketsHandler? = null
    private val builder = BracketsContentBuilderImpl {
        buildContent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initContainer(inflater.context, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notifyContentChanged()
    }

    protected fun notifyItemChanged(tag: String) {
        adapterHandler?.notifyItemChanged(tag)
    }

    protected fun notifyContentChanged() {
        adapterHandler?.build(builder)
    }

    protected abstract fun Scope<Brackets>.buildContent()

    protected open fun initContainer(context: Context, parent: ViewGroup?): View {
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        createByRecyclerView(recyclerView)
        return recyclerView
    }

    protected fun createByViewGroup(viewGroup: ViewGroup) {
        val adapter = FullBracketsAdapter(viewGroup)
        adapterHandler = BracketsHandler(adapter)
    }

    protected fun createByRecyclerView(recyclerView: RecyclerView) {
        val adapter = RecyclerBracketsAdapter()
        adapterHandler = BracketsHandler(adapter)
        recyclerView.adapter = adapter
    }

    protected class BracketsContentBuilderImpl(
        private val callback: Scope<Brackets>.() -> Unit
    ) : BracketsContentBuilder<Brackets> {
        override fun buildBrackets(scope: Scope<Brackets>) {
            callback(scope)
        }

    }

}