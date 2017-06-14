package com.example.zeerorg.firstkotlin.presenter

/**
 * Created by zeerorg on 6/14/17.
 */
interface LoginPresenterInterface {
    fun checkLogin() : Boolean

    fun attemptLogin(email: String, password: String)
}