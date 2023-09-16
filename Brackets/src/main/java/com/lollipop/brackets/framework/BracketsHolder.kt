package com.lollipop.brackets.framework

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.brackets.core.Brackets

class BracketsHolder(
    view: View,
    private val onClickListener: OnItemViewClickListener
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private val lastClickViewIdList = ArrayList<Int>()

    var positionInLayout = -1
        internal set

    private val itemPosition: Int
        get() {
            val position = adapterPosition
            if (position == RecyclerView.NO_POSITION) {
                return positionInLayout
            }
            return position
        }

    fun bind(brackets: Brackets<*>) {
        lastClickViewIdList.forEach { id ->
            itemView.findViewById<View>(id)?.setOnClickListener(null)
        }
        bindListener(brackets.clickableViewIdArray)
        brackets.bindView(itemView)
    }

    private fun onItemClick(view: View) {
        val viewId = if (view == itemView) {
            Brackets.ID_ITEM_VIEW
        } else {
            view.id
        }
        onClickListener.onItemViewClick(itemView, viewId, itemPosition)
    }

    private fun bindListener(ids: IntArray) {
        lastClickViewIdList.forEach { id ->
            findView(id)?.setOnClickListener(null)
        }
        lastClickViewIdList.clear()
        ids.forEach { id ->
            findView(id)?.setOnClickListener(this)
            lastClickViewIdList.add(id)
        }
    }

    private fun findView(id: Int): View? {
        return if (id == Brackets.ID_ITEM_VIEW) {
            itemView
        } else {
            itemView.findViewById(id)
        }
    }

    override fun onClick(v: View?) {
        v ?: return
        onItemClick(v)
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(itemView: View, viewId: Int, position: Int)
    }

}