package com.example.zeerorg.firstkotlin

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.mikepenz.fastadapter.items.AbstractItem

/**
 * Created by zeerorg on 6/8/17.
 */

// Bad idea for MVP because view is implemented inside and no model is taken so no change this
class NotesItem(val data: String = "") : AbstractItem<NotesItem, NotesItem.ViewHolder>() {

//    constructor(data: String) {
//
//    }

    @IdRes
    override fun getType(): Int {
        return R.id.note_item
    }

    override fun getLayoutRes(): Int {
        return R.layout.note_item
    }

    override fun getViewHolder(v: View): ViewHolder { //removed null test to see : looks good for now
        return ViewHolder(v)
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) { // removed null test to see
        super.bindView(holder, payloads)
        holder.data?.text = data
    }

    override fun unbindView(holder: ViewHolder?) {
        super.unbindView(holder)
        holder?.data?.text = null
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val data: TextView? = view.findViewById(R.id.note_data) as TextView
    }
}