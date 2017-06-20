package com.example.zeerorg.firstkotlin.model

import android.util.Log
import com.parse.ParseObject
import com.parse.ParseUser
import com.parse.ParseQuery

/**
 * Created by zeerorg on 6/20/17.
 */
class ParseNoteRepo(val localRepo : NoteRepositoryInterface = NoteRepository()) : ParseNoteRepoInterface {

    override fun pushToBackend(note: Note) {
        val noteObject = ParseObject("Note")
        noteObject.put("user", ParseUser.getCurrentUser().username)
        noteObject.put("id", note.id)
        noteObject.put("data", note.data)
        noteObject.saveInBackground{
            localRepo.setUploaded(note.id, true)
        }
    }

    override fun getLatestNoteBackground(skip: Int, latestNoteCallback: (note: Note) -> Unit, finalCallback: () -> Unit) {
        val query = ParseQuery.getQuery<ParseObject>("Note")
        query.whereEqualTo("user", ParseUser.getCurrentUser().username)
        query.skip = skip
        query.getFirstInBackground{ parseNote, e ->
            if(e == null) {
                Log.v("MyApp", "Fetched latest after " + skip)
                latestNoteCallback(toLocalNote(parseNote))
            } else {
                Log.e("MyApp", "Problem in fetching latest Note")
                finalCallback()
            }
        }
    }

    fun toLocalNote(parseNote: ParseObject) : Note {
        val note = Note()
        note.id = parseNote.getInt("id")
        note.data = parseNote.getString("data")
        note.uploaded = true
        return note
    }
}