package com.example.zeerorg.firstkotlin.main

import android.app.Application
import com.parse.Parse
import com.parse.ParseFacebookUtils
import io.realm.Realm

/**
 * Created by zeerorg on 6/8/17.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Parse.initialize(this)
        ParseFacebookUtils.initialize(this)
        Realm.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}