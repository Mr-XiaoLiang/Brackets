package com.lollipop.brackets.kit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.brackets.core.Brackets
import com.lollipop.brackets.core.BracketsContentBuilder
import com.lollipop.brackets.core.Scope
import com.lollipop.brackets.framework.BracketsHandler
import com.lollipop.brackets.framework.FullBracketsAdapter
import com.lollipop.brackets.framework.RecyclerBracketsAdapter

open class BracketsFragment : Fragment() {

    private var adapterHandler: BracketsHandler? = null
    private val builder = BracketsContentBuilderImpl {
        buildContent()
    }
    private var contentBuilder: ContentBuilder? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contentBuilder = findCallback(context)
    }

    override fun onDetach() {
        super.onDetach()
        contentBuilder = null
    }

    protected inline fun <reified T> findCallback(c: Context): T? {
        var parent: Fragment? = parentFragment
        while (parent != null) {
            if (parent is T) {
                return parent
            }
            parent = parent.parentFragment
        }
        if (c is T) {
            return c
        }
        val act = activity
        if (act is T) {
            return act
        }
        val con = context
        if (con is T) {
            return con
        }
        return null
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

    fun notifyItemChanged(tag: String) {
        adapterHandler?.notifyItemChanged(tag)
    }

    fun notifyContentChanged() {
        adapterHandler?.build(builder)
    }

    protected fun Scope.buildContent() {
        val builder = contentBuilder ?: return
        builder.buildContent(this@BracketsFragment, this)
    }

    protected open fun initContainer(context: Context, parent: ViewGroup?): View {
        return initRecyclerContainer(context)
    }

    protected fun initLinearContainer(context: Context): View {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        createByViewGroup(linearLayout)
        return linearLayout
    }

    protected fun initRecyclerContainer(context: Context): View {
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
        private val callback: Scope.() -> Unit
    ) : BracketsContentBuilder {
        override fun buildBrackets(scope: Scope) {
            callback(scope)
        }

    }

    fun interface ContentBuilder {

        fun buildContent(fragment: BracketsFragment, scope: Scope)

    }

}