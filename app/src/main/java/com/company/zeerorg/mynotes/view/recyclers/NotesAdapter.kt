package com.company.zeerorg.mynotes.view.recyclers

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.company.zeerorg.mynotes.R
import com.company.zeerorg.mynotes.model.Note

/**
 * Created by zeerorg on 6/9/17.
 *
 * TODO : here I am using Notes as raw in onBindViewHolder find a way to abstract this so that view and model are not bound
 */
class NotesAdapter(val notesList: MutableList<Note>,
                   val editNote: (org: Note) -> Unit,
                   val delNote: (org: Note) -> Unit) : RecyclerView.Adapter<NoteViewHolder>() {

    var selected: Int = -1

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.data.text = note.data
        if(selected == position)
            holder.extraFunction.visibility = View.VISIBLE
        else
            holder.extraFunction.visibility = View.GONE
        holder.dataContainer.setOnClickListener {
            Log.e("Adapter", position.toString())
            if(selected == position)
                selected = -1
            else
                selected = position
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(view: ViewGroup, position: Int): NoteViewHolder {

        val noteViewHolder = NoteViewHolder(LayoutInflater.from(view.context).inflate(R.layout.note_item, view, false))
        return noteViewHolder
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun onDismiss(position: Int) {
        delNote(notesList[position])
    }
}