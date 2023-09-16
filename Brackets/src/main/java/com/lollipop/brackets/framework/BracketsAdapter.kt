package com.lollipop.brackets.framework

import android.annotation.SuppressLint
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

    fun notifyItemChanged(tag: String)

    fun setData(list: List<Brackets<*>>)

}

internal class BracketsAdapterHelper {
    val list: List<Brackets<*>>
        get() {
            return dataList
        }
    private val dataList = ArrayList<Brackets<*>>()
    private val tempItemId = HashMap<Class<out Brackets<*>>, Int>()
    private val itemCreatorSet = HashMap<Int, Brackets<*>>()

    fun setData(data: List<Brackets<*>>) {
        dataList.clear()
        dataList.addAll(data)
    }

    private fun getOrCreateTempId(brackets: Brackets<*>): Int {
        val clazz = brackets::class.java
        val id = tempItemId[clazz]
        if (id != null) {
            return id
        }
        val newId = tempItemId.size + 65000
        tempItemId[clazz] = newId
        return newId
    }

    fun getTypeId(brackets: Brackets<*>): Int {
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

    fun findPositionByTag(tag: String): Int {
        for (i in dataList.indices) {
            if (dataList[i].tag == tag) {
                return i
            }
        }
        return -1
    }

    fun findPositionByTag(tag: String, callback: (Int) -> Unit) {
        val position = findPositionByTag(tag)
        if (position >= 0 && position < dataList.size) {
            callback(position)
        }
    }

}

class FullBracketsAdapter(
    private val viewGroup: ViewGroup,
) : BracketsAdapter, BracketsHolder.OnItemViewClickListener {

    private val adapterHelper = BracketsAdapterHelper()

    private val itemTypeMap = HashMap<Int, Int>()

    private val pendingUpdateSet = HashSet<String>()

    private val pendingChangeCallback = Runnable {
        updateByPending()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BracketsHolder {
        return BracketsHolder(
            adapterHelper.createView(parent, viewType), this
        )
    }

    override fun getItemCount(): Int {
        return adapterHelper.list.size
    }

    override fun onBindViewHolder(holder: BracketsHolder, position: Int) {
        holder.bind(adapterHelper.list[position])
    }

    override fun getItemViewType(position: Int): Int {
        return adapterHelper.getTypeId(adapterHelper.list[position])
    }

    override fun notifyDataSetChanged() {
        viewGroup.removeAllViews()
        itemTypeMap.clear()
        for (index in adapterHelper.list.indices) {
            val viewType = getItemViewType(index)
            itemTypeMap[index] = viewType
            val holder = onCreateViewHolder(viewGroup, viewType)
            holder.positionInLayout = index
            val itemView = holder.itemView
            viewGroup.addView(itemView)
            onBindViewHolder(holder, index)
        }
    }

    override fun notifyItemChanged(tag: String) {
        pendingUpdateSet.add(tag)
        viewGroup.removeCallbacks(pendingChangeCallback)
        viewGroup.post(pendingChangeCallback)
    }

    private fun updateByPending() {
        if (pendingUpdateSet.isEmpty()) {
            return
        }
        val tagList = ArrayList<String>(pendingUpdateSet)
        pendingUpdateSet.clear()
        tagList.forEach { tag ->
            adapterHelper.findPositionByTag(tag) { position ->
                notifyItemChanged(position)
            }
        }
    }

    private fun notifyItemChanged(position: Int) {
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

    override fun setData(list: List<Brackets<*>>) {
        adapterHelper.setData(list)
        notifyDataSetChanged()
    }

    override fun onItemViewClick(itemView: View, viewId: Int, position: Int) {
        if (position < 0 || position >= getItemCount()) {
            return
        }
        adapterHelper.list[position].onViewClick(itemView, viewId)
    }

}

class RecyclerBracketsAdapter(
) : RecyclerView.Adapter<BracketsHolder>(), BracketsAdapter,
    BracketsHolder.OnItemViewClickListener {

    private val adapterHelper = BracketsAdapterHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BracketsHolder {
        return BracketsHolder(
            adapterHelper.createView(parent, viewType), this
        )
    }

    override fun getItemCount(): Int {
        return adapterHelper.list.size
    }

    override fun onBindViewHolder(holder: BracketsHolder, position: Int) {
        holder.bind(adapterHelper.list[position])
    }

    override fun getItemViewType(position: Int): Int {
        return adapterHelper.getTypeId(adapterHelper.list[position])
    }

    override fun notifyItemChanged(tag: String) {
        adapterHelper.findPositionByTag(tag) {
            notifyItemChanged(it)
        }
    }

    override fun onItemViewClick(itemView: View, viewId: Int, position: Int) {
        if (position < 0 || position >= itemCount) {
            return
        }
        adapterHelper.list[position].onViewClick(itemView, viewId)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setData(list: List<Brackets<*>>) {
        adapterHelper.setData(list)
        notifyDataSetChanged()
    }

}