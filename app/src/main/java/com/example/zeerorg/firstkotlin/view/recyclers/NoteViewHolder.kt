package com.example.zeerorg.firstkotlin.view.recyclers

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.zeerorg.firstkotlin.R

/**
 * Created by zeerorg on 6/9/17.
 */
class NoteViewHolder(mainView: View, onClick: (orgText: String) -> Unit) : RecyclerView.ViewHolder(mainView) {
    val data = mainView.findViewById(R.id.note_data) as TextView

    init {
        mainView.setOnClickListener {
            onClick(data.text.toString())
        }
    }
}