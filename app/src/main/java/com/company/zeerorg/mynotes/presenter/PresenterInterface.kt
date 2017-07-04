package com.company.zeerorg.mynotes.presenter

import com.company.zeerorg.mynotes.model.Note

/**
 * Created by zeerorg on 6/13/17.
 */
interface PresenterInterface {
    fun addNote(data: String)

    fun getNotesList() : MutableList<Note>

    fun startLoad()

    fun updateNote(note: Note, newData: String)

    fun deleteNote(note: Note)
}