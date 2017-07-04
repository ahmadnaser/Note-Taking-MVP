package com.company.zeerorg.mynotes.model

/**
 * Created by zeerorg on 6/20/17.
 */
interface ParseNoteRepoInterface {
    fun pushToBackend(note: Note)

    fun getLatestNoteBackground(skip : Int, latestNoteCallback: (note: Note) -> Unit, finalCallback: () -> Unit)

    fun updateBackend(note: Note)

    fun getAllNotes(callback: (listNote: MutableList<Note>) -> Unit)
}