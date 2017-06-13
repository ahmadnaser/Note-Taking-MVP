package com.example.zeerorg.firstkotlin.model

/**
 * Created by zeerorg on 6/13/17.
 */
interface NoteRepositoryInterface {
    fun pushNote(data: Note) : Boolean

    fun createNote(data: String): Note

    fun getAll() : MutableList<Note>
}