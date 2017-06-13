package com.example.zeerorg.firstkotlin.main

import com.raizlabs.android.dbflow.annotation.Database

/**
 * Created by zeerorg on 6/8/17.
 */
@com.raizlabs.android.dbflow.annotation.Database(name = com.example.zeerorg.firstkotlin.main.MyDatabase.NAME, version = com.example.zeerorg.firstkotlin.main.MyDatabase.VERSION, generatedClassSeparator = "_")
object MyDatabase {
    const val NAME: String = "NotesDatabase"
    const val VERSION = 1
}