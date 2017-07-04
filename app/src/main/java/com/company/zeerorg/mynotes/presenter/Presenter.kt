package com.company.zeerorg.mynotes.presenter

import android.util.Log
import com.company.zeerorg.mynotes.main.HelperMethods
import com.company.zeerorg.mynotes.main.HelperMethodsInterface
import com.company.zeerorg.mynotes.model.*
import com.company.zeerorg.mynotes.view.NoteDependencyInterface

/**
 * Created by zeerorg on 6/12/17.
 *
 * TODO : inject view in presenter view interface :: Actually NO! because it is a dependency and should not be given in a method
 */
class Presenter(val view: NoteDependencyInterface,
                val noteRepo: NoteRepositoryInterface = NoteRepository(),
                val noteOnlineRepo: ParseNoteRepoInterface = ParseNoteRepo(),
                val helpers: HelperMethodsInterface = HelperMethods(),
                val fileLog: FileLogInterface = FileLog(view.getFileDirectory())) : PresenterInterface {

    lateinit private var notesList : MutableList<Note>

    override fun updateNote(note: Note, newData: String) {
        if(newData.trim() != note.data) {
            note.data = newData
            note.timestamp = System.currentTimeMillis()
            noteRepo.updateNote(note)
            helpers.checkOnline(
                    {
                        noteOnlineRepo.updateBackend(note)
                        executeLoggedData()
                        Log.e("presenter", "updated online")
                    },
                    {
                        fileLog.logUpdateNote(note.id)
                        Log.e("presenter", "Not online")
                    }
            )
            view.updateRecycler()
        }
    }

    override fun addNote(data: String){
        if(data.trim() == "") {
            return
        }
        val note = noteRepo.createNote(data)
        noteRepo.pushNote(note)
        notesList.add(note)
        helpers.checkOnline(
                {
                    noteOnlineRepo.pushToBackend(note)
                    executeLoggedData()
                    Log.e("presenter", "pushed online")
                },
                {
                    fileLog.logCreateNote(note.id)
                    Log.e("presenter", "NOt online")
                }
        )
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
        // Removed uploading of not uploaded notes
        notesList = mutableListOf()
        helpers.checkOnline(
                {
                    executeLoggedData()
                    noteOnlineRepo.getAllNotes { listNote ->
                        notesList.clear()
                        notesList.addAll(listNote)
                        view.updateRecycler()
                    }
                },
                {
                    notesList.clear()
                    notesList.addAll(noteRepo.getAll())
                    view.updateRecycler()
                    Log.e("presenter", "Continue with offline database")
                }
        )
    }

    /**
     * Recursive asynchronous function
     *
     * It fetches the latest note in background
     * Checks if that note is present in local repo
     *      if not : It adds the note to a list which will be updated afterwards
     *
     *      if yes : It stops and adds all the notes present in latestNoteList to local repo
     */
    fun updateLocalNotes(latestNoteList: MutableList<Note>, skip: Int) {
//        noteOnlineRepo.getLatestNoteBackground(skip, { latestNote ->
//            if (!noteRepo.isPresent(latestNote)) {
//                Log.e("MyApp", "Note is not present locally")
//                latestNoteList.add(latestNote)
//                updateLocalNotes(latestNoteList, skip+1)
//            } else {
//                Log.e("MyApp", "Notes are present Locally :-)")
//                for(note in latestNoteList) {
//                    noteRepo.pushNote(note)
//                }
//                updateNoteList()
//            }
//        }, {
//            Log.e("MyApp", "Notes are present Locally :-)")     // same as in above
//            for(note in latestNoteList) {
//                noteRepo.pushNote(note)
//            }
//            updateNoteList()
//        })
        executeLoggedData()
        noteOnlineRepo.getAllNotes { listNote ->
            notesList.clear()
            notesList.addAll(listNote)
            view.updateRecycler()
        }
    }

    fun updateNoteList() {
        notesList.clear()
        notesList.addAll(noteRepo.getAll())
        view.updateRecycler()
    }

    fun executeLoggedData(){
        Log.e("Presenter","Executing Log")
        fileLog.executeLog(
                { id ->
                    noteOnlineRepo.pushToBackend(noteRepo.getNote(id))
                },
                { id ->
                    noteOnlineRepo.updateBackend(noteRepo.getNote(id))
                },
                {

                })
    }
}