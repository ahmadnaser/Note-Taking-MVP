package com.company.zeerorg.mynotes.model

import com.parse.ParseUser

/**
 * Created by zeerorg on 6/14/17.
 */
interface UserRepositoryInterface {
    fun isLoggedIn(): Boolean

    fun exists(email: String, login: () -> Unit, signUp: () -> Unit)

    fun login(email: String, password: String, successLogin: () -> Unit, failLogin: (code: Int) -> Unit)

    fun signUp(email: String, password: String, successSignUp: () -> Unit, failSignUp: (code: Int) -> Unit)

    fun getUser(): ParseUser

    fun logOut()

    fun getUsername(): String
}