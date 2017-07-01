package com.example.zeerorg.firstkotlin.model

import android.util.Log
import io.realm.Realm
import io.realm.RealmResults
import java.util.*

/**
 * Created by zeerorg on 6/9/17.
 */
class NoteRepository : NoteRepositoryInterface {

    override fun getAll(): MutableList<Note> {
        val realm : Realm = Realm.getDefaultInstance()
        val notes : RealmResults<Note> = realm.where(Note::class.java).findAll()
        return realm.copyFromRealm(notes)
    }

    override fun pushNote(bareNote: Note) : Boolean {
        val realm : Realm = Realm.getDefaultInstance()
        realm.executeTransaction({
            val note = realm.createObject(Note::class.java, bareNote.id)
            note.data = bareNote.data
            note.uploaded = bareNote.uploaded
        })
        return true
    }

    override fun createNote(data: String): Note {
        val note =  Note()
        note.data = data
        note.id = UUID.randomUUID().hashCode()
        note.uploaded = false
        return note
    }

    override fun setUploaded(id: Int, value: Boolean) {
        val realm : Realm = Realm.getDefaultInstance()
        val note = realm.where(Note::class.java).equalTo("id", id).findFirst()
        realm.executeTransaction{
            note.uploaded = true
        }
    }

    override fun getNotUploaded() : List<Note> {
        val realm : Realm = Realm.getDefaultInstance()
        val results : RealmResults<Note> = realm.where(Note::class.java).equalTo("uploaded", false).findAll()
        Log.v("MyApp", results.size.toString() + " not uploaded")
        return realm.copyToRealm(results)
    }

    override fun isPresent(note: Note): Boolean {
        val realm : Realm = Realm.getDefaultInstance()
        val checkNote : Note? = realm.where(Note::class.java).equalTo("id", note.id).findFirst()
        if(checkNote != null) {
            return true
        }
        return false
    }
}