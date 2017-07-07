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
                val fileLog: FileLogInterface = FileLog(view.getFileDirectory()),
                val userRepo: UserRepositoryInterface = UserRepository()) : PresenterInterface {

    private var notesList : MutableList<Note> = mutableListOf()

    override fun updateNote(note: Note, newData: String, newTitle: String) {
        if(newData.trim() != note.data || newTitle.trim() != note.title) {
            note.data = newData
            note.title = newTitle
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

    override fun addNote(data: String, title: String){
        if(data.trim() == "" && title.trim() == "") {
            return
        }
        val note = noteRepo.createNote(data.trim(), title.trim())
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
        notesList.clear()
        notesList.addAll(noteRepo.getAll())
        view.updateRecycler()
        helpers.checkOnline(
                {
                    executeLoggedData()
                    noteOnlineRepo.getAllNotes { listNote ->
                        Log.e("Presenter", "Updating from Backend")
                        notesList.clear()
                        notesList.addAll(listNote)
                        Log.e("Presenter", listNote.toString())
                        view.updateRecycler()
                        noteRepo.clearAll()
                        noteRepo.setAll(listNote)
                    }
                },
                {
                    Log.e("presenter", "Continue with offline database")
                }
        )
    }

    override fun deleteNote(note: Note) {
        noteRepo.deleteNote(note)
        notesList.remove(note)
        helpers.checkOnline(
                {
                    noteOnlineRepo.pullNote(note)
                    executeLoggedData()
                    view.updateRecycler()
                },
                {
                    fileLog.logDeleteNote(note.id)
                    view.updateRecycler()
                }
        )
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
                { id ->
                    noteOnlineRepo.pullNote(id)   // Since note is deleted from local repository I will use the ID
                }
        )
    }

    fun addLink(text: String) : String {
        val fir = "<a href=\""
        val last = "</a>"
        val mid = "\">"
        val newText = text
        for(x in newText.split(" ")) {
            if(x.startsWith("http")) {
                val link = x
                x.replace(link, fir+link+mid+link+last)
            }
        }
        return newText
    }

    override fun logOut() {
        userRepo.logOut()
        noteRepo.clearAll()
        view.finishActivity()
    }

    override fun getUsername(): String {
        return userRepo.getUsername()
    }
}