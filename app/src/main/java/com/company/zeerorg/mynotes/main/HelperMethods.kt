package com.company.zeerorg.mynotes.main

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
        val runnable = Runnable {
            if(isOnline()) {
                successFun()
            } else {
                failFun()
            }
        }
        Thread(runnable).start()
    }
}