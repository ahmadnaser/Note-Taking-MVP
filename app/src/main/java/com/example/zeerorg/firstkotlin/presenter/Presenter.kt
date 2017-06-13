package com.example.zeerorg.firstkotlin.presenter

import com.example.zeerorg.firstkotlin.model.Note
import com.example.zeerorg.firstkotlin.model.NoteRepository
import com.example.zeerorg.firstkotlin.model.NoteRepositoryInterface

/**
 * Created by zeerorg on 6/12/17.
 *
 * TODO : inject view in presenter view interface :: Actually NO! because it is a dependency and should not be given in a method
 */
class Presenter(val view: PresenterViewInterface, val noteRepo: NoteRepositoryInterface = NoteRepository()) : PresenterInterface {

    private val notesList = noteRepo.getAll()

    override fun addNote(data: String){
        val note = noteRepo.createNote(data)
        noteRepo.pushNote(note)
        notesList.add(note)

        // TODO("update recycler")
        view.updateRecycler()

    }

    override fun getNotesList(): MutableList<Note> {
        return notesList
    }

}