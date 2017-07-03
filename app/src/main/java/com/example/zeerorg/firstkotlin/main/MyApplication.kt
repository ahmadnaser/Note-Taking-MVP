package com.example.zeerorg.firstkotlin.main

import android.app.Application
import com.example.zeerorg.firstkotlin.model.DaoMaster
import com.example.zeerorg.firstkotlin.model.DaoSession
import com.parse.Parse

/**
 * Created by zeerorg on 6/8/17.
 */
class MyApplication : Application() {

    companion object {
        lateinit var daoSession : DaoSession
    }

    override fun onCreate() {
        super.onCreate()
        Parse.initialize(this)
        val helper = DaoMaster.DevOpenHelper(this, "note-db")
        val db = helper.writableDb
        daoSession = DaoMaster(db).newSession()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}