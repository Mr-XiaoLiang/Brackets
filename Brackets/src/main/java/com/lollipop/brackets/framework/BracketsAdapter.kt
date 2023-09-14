package com.lollipop.brackets.framework

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.brackets.core.Brackets

class BracketsAdapter(
    private val list: List<Brackets>
) : RecyclerView.Adapter<BracketsHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BracketsHolder {
        return BracketsHolder(
            itemCreatorSet[viewType]?.createView(parent) ?: TextView(parent.context)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BracketsHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        return getTypeId(list[position])
    }

    private fun getTypeId(brackets: Brackets): Int {
        val typeId = brackets.typeId
        val finalTypeId = if (typeId == 0) {
            getOrCreateTempId(brackets)
        } else {
            typeId
        }
        itemCreatorSet[finalTypeId] = brackets
        return finalTypeId
    }

}