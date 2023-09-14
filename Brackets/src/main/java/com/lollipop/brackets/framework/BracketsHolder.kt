package com.lollipop.brackets.framework

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.brackets.core.Brackets

class BracketsHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(brackets: Brackets) {
        brackets.bindView(itemView)
    }

}