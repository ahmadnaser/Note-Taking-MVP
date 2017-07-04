package com.company.zeerorg.mynotes.model

import android.util.Log
import com.company.zeerorg.mynotes.main.MyApplication

/**
 * Created by zeerorg on 6/9/17.
 */
class NoteRepository(daoSession: DaoSession = MyApplication.daoSession) : NoteRepositoryInterface {

    override fun hasBeenUpdated(note: Note) {
        val toUpdate = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(note.id)).unique()
        toUpdate.isUpdated = false
        noteDao.update(toUpdate)
    }

    override fun updateNote(note: Note) {
        val toUpdate = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(note.id)).unique()
        toUpdate.data = note.data
        toUpdate.timestamp = note.timestamp
        toUpdate.isUpdated = true
        noteDao.update(toUpdate)
    }

    override fun setObjectId(id: Long, value: String) {
        val note = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(id)).unique()
        note.objectId = value
        noteDao.update(note)
    }

    val noteDao : NoteDao = daoSession.noteDao

    override fun getAll(): MutableList<Note> {
        return noteDao.queryBuilder().list()
    }

    override fun pushNote(bareNote: Note) : Boolean {
        noteDao.insert(bareNote)
        return true
    }

    override fun createNote(data: String): Note {
        val note =  Note()
        note.data = data
        note.id = System.currentTimeMillis()
        note.isUploaded = false
        note.isUpdated = false
        note.timestamp = System.currentTimeMillis()
        return note
    }

    override fun setUploaded(id: Long, value: Boolean) {
        val note = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(id)).unique()
        note.isUploaded = value
        noteDao.update(note)
    }

    override fun getNotUploaded() : List<Note> {
        val noteQuery = noteDao.queryBuilder().where(NoteDao.Properties.Uploaded.eq(false))
        return noteQuery.list()
    }

    override fun isPresent(note: Note): Boolean {
        val noteQuery = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(note.id))
        if(noteQuery.count() > 0) {
            return true
        }
        return false
    }

    override fun getNote(id: Long) : Note {
        return noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(id)).unique()
    }

    override fun deleteNote(note: Note) {
        Log.e("Local repo", note.id.toString())
        val toDelete = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(note.id)).unique()
        noteDao.delete(toDelete)
    }

    override fun setAll(noteList: MutableList<Note>) {
        noteDao.insertInTx(noteList)
    }

    override fun clearAll() {
        noteDao.deleteAll()
    }
}