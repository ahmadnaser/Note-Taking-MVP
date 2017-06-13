package com.example.zeerorg.firstkotlin.main

import android.app.Application
import com.parse.Parse
import com.parse.ParseFacebookUtils
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager

/**
 * Created by zeerorg on 6/8/17.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FlowManager.init(FlowConfig.Builder(this).build())
        Parse.initialize(this)
        ParseFacebookUtils.initialize(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        FlowManager.destroy()
    }
}