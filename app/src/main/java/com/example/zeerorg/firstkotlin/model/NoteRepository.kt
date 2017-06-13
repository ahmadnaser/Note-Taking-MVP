package com.example.zeerorg.firstkotlin.model

import android.util.Log
import com.example.zeerorg.firstkotlin.NotesItem
import com.raizlabs.android.dbflow.sql.language.Select

/**
 * Created by zeerorg on 6/9/17.
 */
class NoteRepository : NoteRepositoryInterface {

    override fun getAll(): MutableList<Note> {
        val savedNotes = Select().from(Note::class.java).where().queryList()
        Log.e("Access note", savedNotes.size.toString())
        return savedNotes
    }

    override fun pushNote(data: Note) : Boolean {
        data.save()
        return true
    }

    override fun createNote(data: String): Note {
        val note =  Note()
        note.data = data
        return note
    }

    fun getListNotesItem(): MutableList<NotesItem> {
        return getAll().map { NotesItem(it.data) }.toMutableList()
    }

    fun addNote(note: Note) {
        // TODO : function to add a note
    }
}