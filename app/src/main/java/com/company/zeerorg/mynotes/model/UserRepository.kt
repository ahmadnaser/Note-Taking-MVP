package com.company.zeerorg.mynotes.model

import android.util.Log
import com.parse.ParseQuery
import com.parse.ParseUser

/**
 * Created by zeerorg on 6/14/17.
 */
class UserRepository : UserRepositoryInterface {
    override fun getUser(): ParseUser {
        return ParseUser.getCurrentUser()
    }

    override fun isLoggedIn(): Boolean {
        return ParseUser.getCurrentUser() != null
    }

    override fun exists(email: String, login: () -> Unit, signUp: () -> Unit) {
        val userQuery: ParseQuery<ParseUser> = ParseUser.getQuery()
        userQuery.whereEqualTo("username", email)
        userQuery.countInBackground{ count, err ->
            if (err == null)
                if (count == 0)
                    signUp()
                else
                    login()
        }
    }

    override fun login(email: String, password: String, successLogin: () -> Unit, failLogin: () -> Unit) {
        ParseUser.logInInBackground(email, password, { user, e ->
            if(user != null) {
                Log.e("MyApp", "Login success")
                successLogin()
            } else {
                Log.e("MyApp", "Login fail " + e.toString())
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
            if(e == null) {
                Log.e("Signing Up", "Sign Up success")
                successSignUp()
            }
            else {
                Log.e("Signing Up", "Sign Up fail " + e.toString())
                failSignUp()
            }
        }
    }
}