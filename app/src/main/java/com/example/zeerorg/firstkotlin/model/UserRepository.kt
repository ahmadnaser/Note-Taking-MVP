package com.example.zeerorg.firstkotlin.model

import android.util.Log
import com.parse.ParseQuery
import com.parse.ParseUser

/**
 * Created by zeerorg on 6/14/17.
 */
class UserRepository : UserRepositoryInterface {

    override fun isLoggedIn(): Boolean {
        return ParseUser.getCurrentUser() != null
    }

    override fun exists(email: String, login: () -> Unit, signUp: () -> Unit) {
        val userQuery: ParseQuery<ParseUser> = ParseUser.getQuery()
        userQuery.whereEqualTo("username", email)
        userQuery.countInBackground{ _, err ->
            if (err == null)  // No error, user exists
                login()
            else  // Error user needs to be created
                signUp()
        }
    }

    override fun login(email: String, password: String, successLogin: () -> Unit, failLogin: () -> Unit) {
        ParseUser.logInInBackground(email, password, { user, _ ->
            if(user != null) {
                successLogin()
            } else {
                failLogin()
            }
        })
    }

    override fun signUp(email: String, password: String, successSignUp: () -> Unit, failSignUp: () -> Unit) {
        val user = ParseUser()
        user.username = email
        user.setPassword(password)
        user.email = email
        Log.e("MyApp", "Trying sign up")
        user.signUpInBackground { e ->
            if(e == null)
                successSignUp()
            else
                failSignUp()
        }
    }
}