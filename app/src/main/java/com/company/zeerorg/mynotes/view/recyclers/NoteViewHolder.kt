package com.company.zeerorg.mynotes.view.recyclers

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.company.zeerorg.mynotes.R

/**
 * Created by zeerorg on 6/9/17.
 */
class NoteViewHolder(val mainView: View) : RecyclerView.ViewHolder(mainView) {
    val data = mainView.findViewById(R.id.note_data) as TextView

    val dataContainer = mainView.findViewById(R.id.contain_data_view) as View

    val extraFunction = mainView.findViewById(R.id.extra_function) as View

    val title = mainView.findViewById(R.id.note_title) as TextView

    val editBtn = mainView.findViewById(R.id.note_edit) as Button

    val delBtn = mainView.findViewById(R.id.note_delete) as Button

    val urlBtn = mainView.findViewById(R.id.note_url) as Button

    val urlBtnCnt = mainView.findViewById(R.id.note_url_container) as View

    val shareBtn = mainView.findViewById(R.id.note_share) as Button
}