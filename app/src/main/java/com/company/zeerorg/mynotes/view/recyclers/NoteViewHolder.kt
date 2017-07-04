package com.company.zeerorg.mynotes.view.recyclers

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.company.zeerorg.mynotes.R

/**
 * Created by zeerorg on 6/9/17.
 */
class NoteViewHolder(val mainView: View) : RecyclerView.ViewHolder(mainView) {
    val data = mainView.findViewById(R.id.note_data) as TextView
}