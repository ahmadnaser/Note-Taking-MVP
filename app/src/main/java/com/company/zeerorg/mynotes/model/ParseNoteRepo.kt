package com.company.zeerorg.mynotes.model

import android.util.Log
import com.parse.ParseObject
import com.parse.ParseUser
import com.parse.ParseQuery

/**
 * Created by zeerorg on 6/20/17.
 */
class ParseNoteRepo(val localRepo : NoteRepositoryInterface = NoteRepository()) : ParseNoteRepoInterface {

    override fun updateBackend(note: Note) {
        val query = ParseQuery.getQuery<ParseObject>("Note")
        query.getInBackground(note.objectId,
                { obj, e ->
                    if(e == null) {
                        obj.put("data", note.data)
                        obj.put("timestamp", note.timestamp)
                        obj.saveInBackground {
                            localRepo.hasBeenUpdated(note)
                        }
                    } else {
                        pushToBackend(note)
                    }
                }
        )
    }

    override fun pushToBackend(note: Note) {
        val noteObject = ParseObject("Note")
        noteObject.put("user", ParseUser.getCurrentUser().username)
        noteObject.put("id", note.id)
        noteObject.put("data", note.data)
        noteObject.put("timestamp", note.timestamp)
        noteObject.saveInBackground{
            localRepo.setUploaded(note.id, true)
            localRepo.setObjectId(note.id, noteObject.objectId)
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

    override fun getAllNotes(callback: (listNote: MutableList<Note>) -> Unit) {
        val query = ParseQuery.getQuery<ParseObject>("Note")
        query.whereEqualTo("user", ParseUser.getCurrentUser().username)
        query.orderByAscending("id")
        query.findInBackground{ listObj, e ->
            if(e == null) {
                val listNote = mutableListOf<Note>()
                listObj.mapTo(listNote) { toLocalNote(it) }
                callback(listNote)
            } else {
                e.printStackTrace()
            }
        }
    }

    override fun pullNote(note: Note) {
        val query = ParseQuery.getQuery<ParseObject>("Note")
        query.getInBackground(note.objectId, { obj, e ->
            if(e == null)
                obj.deleteInBackground()
        })
    }

    override fun pullNote(id: Long) {
        val query = ParseQuery.getQuery<ParseObject>("Note")
        query.whereEqualTo("user", ParseUser.getCurrentUser().username)
        query.whereEqualTo("id", id)
        query.getFirstInBackground{ obj, e ->
            if(e == null)
                obj.deleteInBackground()
        }
    }

    fun toLocalNote(parseNote: ParseObject) : Note {
        val note = Note()
        note.objectId = parseNote.objectId
        note.id = parseNote.getLong("id")
        note.data = parseNote.getString("data")
        note.uploaded = true
        note.timestamp = parseNote.getLong("timestamp")
        return note
    }
}