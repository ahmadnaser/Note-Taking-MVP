package com.example.zeerorg.firstkotlin.model

/**
 * Created by zeerorg on 6/13/17.
 */
interface NoteRepositoryInterface {
    fun pushNote(bareNote: Note) : Boolean

    fun createNote(data: String): Note

    fun getAll() : MutableList<Note>

    fun setUploaded(id : Int, value: Boolean)

    fun getNotUploaded() : List<Note>

    fun isPresent(note: Note) : Boolean
}