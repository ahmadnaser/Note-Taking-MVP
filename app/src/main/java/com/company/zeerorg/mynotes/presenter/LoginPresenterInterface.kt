package com.company.zeerorg.mynotes.presenter

/**
 * Created by zeerorg on 6/14/17.
 */
interface LoginPresenterInterface {
    fun checkLogin()

    fun attemptLogin(email: String, password: String)

    fun getUserName(): String

    fun logOut()

    fun attemptSignUp(email: String, password: String)
}