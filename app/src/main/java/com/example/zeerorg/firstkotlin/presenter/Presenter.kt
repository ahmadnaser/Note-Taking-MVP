package com.example.zeerorg.firstkotlin.presenter

import android.util.Log
import com.example.zeerorg.firstkotlin.model.*
import com.example.zeerorg.firstkotlin.view.NoteDependencyInterface

/**
 * Created by zeerorg on 6/12/17.
 *
 * TODO : inject view in presenter view interface :: Actually NO! because it is a dependency and should not be given in a method
 */
class Presenter(val view: NoteDependencyInterface,
                val noteRepo: NoteRepositoryInterface = NoteRepository(),
                val noteOnlineRepo: ParseNoteRepoInterface = ParseNoteRepo()) : PresenterInterface {

    private var notesList = noteRepo.getAll()

    override fun addNote(data: String){
        if(data.trim() == "") {
            return
        }
        val note = noteRepo.createNote(data)
        noteRepo.pushNote(note)
        notesList.add(note)
        noteOnlineRepo.pushToBackend(note)

        view.updateRecycler()
    }

    override fun getNotesList(): MutableList<Note> {
        return notesList
    }

    /**
     * First I upload the notes which haven't been uploaded.
     * Then I download the notes from parse
     */
    override fun startLoad() {
        val notesNotUploaded : List<Note> = noteRepo.getNotUploaded()
        for(note in notesNotUploaded) {
            noteOnlineRepo.pushToBackend(note)
        }
        updateLocalNotes(mutableListOf(), 0)
    }

    /**
     * Recursive asynchronous function
     */
    fun updateLocalNotes(latestNoteList: MutableList<Note>, skip: Int) {
        noteOnlineRepo.getLatestNoteBackground(skip, { latestNote ->
            if (!noteRepo.isPresent(latestNote)) {
                Log.e("MyApp", "Note is not present locally")
                latestNoteList.add(latestNote)
                updateLocalNotes(latestNoteList, skip+1)
            } else {
                Log.e("MyApp", "Notes are present Locally :-)")
                for(note in latestNoteList) {
                    noteRepo.pushNote(note)
                }
                updateNoteList()
            }
        }, {
            Log.e("MyApp", "Notes are present Locally :-)")     // same as in above
            for(note in latestNoteList) {
                noteRepo.pushNote(note)
            }
            updateNoteList()
        })
    }

    fun updateNoteList() {
        notesList.clear()
        notesList.addAll(noteRepo.getAll())
        view.updateRecycler()
    }
}