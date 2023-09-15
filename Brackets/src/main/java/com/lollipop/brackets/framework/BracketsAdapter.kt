package com.lollipop.brackets.framework

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.brackets.core.Brackets

interface BracketsAdapter {

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BracketsHolder

    fun getItemCount(): Int

    fun onBindViewHolder(holder: BracketsHolder, position: Int)

    fun getItemViewType(position: Int): Int

    fun notifyDataSetChanged()

    fun notifyItemChanged(position: Int)

}

internal class BracketsAdapterHelper {
    private val tempItemId = HashMap<Class<out Brackets>, Int>()
    private val itemCreatorSet = HashMap<Int, Brackets>()

    private fun getOrCreateTempId(brackets: Brackets): Int {
        val clazz = brackets::class.java
        val id = tempItemId[clazz]
        if (id != null) {
            return id
        }
        val newId = tempItemId.size + 65000
        tempItemId[clazz] = newId
        return newId
    }

    fun getTypeId(brackets: Brackets): Int {
        val typeId = brackets.typeId
        val finalTypeId = if (typeId == 0) {
            getOrCreateTempId(brackets)
        } else {
            typeId
        }
        itemCreatorSet[finalTypeId] = brackets
        return finalTypeId
    }

    fun createView(parent: ViewGroup, viewType: Int): View {
        return itemCreatorSet[viewType]?.createView(parent) ?: TextView(parent.context)
    }
}

class FullBracketsAdapter(
    private val viewGroup: ViewGroup,
    private val list: List<Brackets>
) : BracketsAdapter {

    private val adapterHelper = BracketsAdapterHelper()

    private val itemTypeMap = HashMap<Int, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BracketsHolder {
        return BracketsHolder(
            adapterHelper.createView(parent, viewType)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BracketsHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        return adapterHelper.getTypeId(list[position])
    }

    override fun notifyDataSetChanged() {
        viewGroup.removeAllViews()
        itemTypeMap.clear()
        for (index in list.indices) {
            val viewType = getItemViewType(index)
            itemTypeMap[index] = viewType
            val holder = onCreateViewHolder(viewGroup, viewType)
            val itemView = holder.itemView
            viewGroup.addView(itemView)
            onBindViewHolder(holder, index)
        }
    }

    override fun notifyItemChanged(position: Int) {
        if (position < 0 || position >= getItemCount()) {
            return
        }
        val oldType = itemTypeMap[position]
        val newType = getItemViewType(position)
        if (oldType == newType) {
            val child = viewGroup.getChildAt(position) ?: return
            val holder = child.tag
            if (holder is BracketsHolder) {
                onBindViewHolder(holder, position)
                return
            }
        }
        viewGroup.removeViewAt(position)
        itemTypeMap[position] = newType
        val holder = onCreateViewHolder(viewGroup, newType)
        val itemView = holder.itemView
        viewGroup.addView(itemView, position)
        onBindViewHolder(holder, position)
    }

}

class RecyclerBracketsAdapter(
    private val list: List<Brackets>
) : RecyclerView.Adapter<BracketsHolder>(), BracketsAdapter {

    private val adapterHelper = BracketsAdapterHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BracketsHolder {
        return BracketsHolder(
            adapterHelper.createView(parent, viewType)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BracketsHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        return adapterHelper.getTypeId(list[position])
    }


}