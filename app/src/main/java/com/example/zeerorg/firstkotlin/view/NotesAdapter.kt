package com.example.zeerorg.firstkotlin.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.zeerorg.firstkotlin.R
import com.example.zeerorg.firstkotlin.model.Note

/**
 * Created by zeerorg on 6/9/17.
 *
 * TODO : here I am using Notes as raw in onBindViewHolder find a way to abstract this so that view and model are not bound
 */
class NotesAdapter(val notesList: MutableList<Note>) : RecyclerView.Adapter<NoteViewHolder>() {

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.data.text = note.data
    }

    override fun onCreateViewHolder(view: ViewGroup, position: Int): NoteViewHolder {
        return NoteViewHolder(
                LayoutInflater.from(view.context).inflate(R.layout.note_item, view, false)
        )
    }

    override fun getItemCount(): Int {
        return notesList.size
    }
}