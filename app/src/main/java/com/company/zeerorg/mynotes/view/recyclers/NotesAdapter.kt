package com.company.zeerorg.mynotes.view.recyclers

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.company.zeerorg.mynotes.R
import com.company.zeerorg.mynotes.model.Note
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


/**
 * Created by zeerorg on 6/9/17.
 *
 * TODO : (Fucking Important) here I am using Notes as raw in onBindViewHolder find a way to abstract this so that view and model are not bound
 */
class NotesAdapter(val notesList: MutableList<Note>,
                   val context: Context,
                   val editNote: (org: Note) -> Unit,
                   val delNote: (org: Note) -> Unit) : RecyclerView.Adapter<NoteViewHolder>() {

    var selected: Int = -1

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        if(note.data != "" && note.data != null) {
            holder.data.visibility = View.VISIBLE
            var data = note.data

            holder.data.text = data
            holder.urlBtnCnt.visibility = View.GONE

            if(data.contains("http")) {
                var link = ""
                for(x in data.split(" ")){
                    if(x.contains("http")) {
                        link = x
                        holder.urlBtnCnt.visibility = View.VISIBLE
                        holder.urlBtn.setOnClickListener{
                            val url = Uri.parse(link)
                            val intent = Intent(Intent.ACTION_VIEW, url)
                            startActivity(context, intent, null)
                        }
                    }
                }
                Log.e("Adapter", "Found Url " + link)
            }
            Log.e("Adapter", note.data)
        } else {
            holder.data.visibility = View.GONE
            Log.e("Adapter", "Bye bye title: " + position.toString())
        }
        if(note.title != "" && note.title != null) {
            holder.title.visibility = View.VISIBLE
            holder.title.text = note.title
            Log.e("Adapter", note.title)
        } else {
            holder.title.visibility = View.GONE
            Log.e("Adapter", "Bye bye title: " + position.toString())
        }

        if(selected == position)
            holder.extraFunction.visibility = View.VISIBLE
        else
            holder.extraFunction.visibility = View.GONE
        holder.dataContainer.setOnClickListener {
            Log.e("Adapter", position.toString())
            val prev = selected
            if(selected == position)
                selected = -1
            else
                selected = position
            notifyItemChanged(prev)
            notifyItemChanged(selected)
        }

        holder.editBtn.setOnClickListener{
            editNote(notesList[position])
        }
        holder.delBtn.setOnClickListener{
            delNote(notesList[position])
            notifyItemRemoved(position)
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