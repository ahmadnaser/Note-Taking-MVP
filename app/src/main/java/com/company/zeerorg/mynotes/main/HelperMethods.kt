package com.company.zeerorg.mynotes.main

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Created by zeerorg on 7/3/17.
 */
class HelperMethods : HelperMethodsInterface {
    fun isOnline() : Boolean {
        try{
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr = InetSocketAddress("8.8.8.8", 53)

            sock.connect(sockaddr, timeoutMs)
            sock.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    override fun checkOnline(successFun: () -> Unit, failFun: () -> Unit) {
//        val runnable = Runnable {
//            if(isOnline()) {
//                successFun()
//            } else {
//                failFun()
//            }
//
//        }
//        Thread(runnable).start()
//        val th = Thread().run {
//            runnable.run()
//
//        }
        val task = MyAsync(this::isOnline, successFun, failFun)
        task.execute()
    }

    class MyAsync(val backgroundFun: () -> Boolean, val successFun: () -> Unit, val failFun: () -> Unit) : AsyncTask<Unit, Unit, Boolean>() {

        override fun doInBackground(vararg params: Unit?) : Boolean {
            Log.e("Helper", "Background Execute")
            return backgroundFun()
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            if(result) {
                successFun()
            } else {
                failFun()
            }
        }

    }
}