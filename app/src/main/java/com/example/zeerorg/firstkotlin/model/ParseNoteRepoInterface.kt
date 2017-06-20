package com.example.zeerorg.firstkotlin.model

/**
 * Created by zeerorg on 6/20/17.
 */
interface ParseNoteRepoInterface {
    fun pushToBackend(note: Note)

    fun getLatestNoteBackground(skip : Int, latestNoteCallback: (note: Note) -> Unit, finalCallback: () -> Unit)
}