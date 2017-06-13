package com.example.zeerorg.firstkotlin.presenter

import com.example.zeerorg.firstkotlin.model.Note

/**
 * Created by zeerorg on 6/13/17.
 */
interface PresenterInterface {
    fun addNote(data: String)

    fun getNotesList() : MutableList<Note>
}